package ru.atom.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.swing.text.html.HTML;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Controller
@RequestMapping("chat")
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private Queue<String> messages = new ConcurrentLinkedQueue<>();
    static Map<String, String> usersOnline = new ConcurrentHashMap<>();
    private Map<String, String> usersNowOnline = new ConcurrentHashMap<>();

    /**
     * curl -X POST -i localhost:8080/chat/login -d "name=rybalkin_is_STUPID"
     */
    @RequestMapping(
            path = "login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestParam("name") String name, @RequestParam("password") String password) {
        if(usersNowOnline.containsValue(name))return ResponseEntity.badRequest().body("You are already logged");
        try {
           usersOnline = ResoursWritter.getPass();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (name.length() < 1) {
            return ResponseEntity.badRequest().body("Too short name, sorry :(");
        }
        if (name.length() > 20) {
            return ResponseEntity.badRequest().body("Too long name, sorry :(");
        }
        if (usersOnline.containsValue(name)&& !(findKey(name).equals(password))) {
                    return ResponseEntity.badRequest().body("incorrect password");

        }
        usersOnline.put(password, name);
        usersNowOnline.put(password, name);
        ResoursWritter.writePass();
        messages.add("[" + name + "] logged in");
        return new ResponseEntity<>(HttpStatus.OK);
    }

        public static String findKey(String name) {
            Collection<String> collection = usersOnline.keySet();
            for (String key : collection) {
                String listName = usersOnline.get(key);
                if (key != null) {
                    if (listName.equals(name)) {
                        return key;// нашли наше значение и возвращаем  ключ
                    }
                }
            }
            return null;
        }
    /**
     * curl -i localhost:8080/chat/chat
     */
    @RequestMapping(
            path = "chat",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> chat() throws IOException {
        return new ResponseEntity<>(messages.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n")),
                HttpStatus.OK);
    }

    @RequestMapping(
            path = "chatnew",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> chatnew() throws IOException {
        messages = ResoursWritter.read();
            return new ResponseEntity<>(messages.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("\n")),
                    HttpStatus.OK);
    }

    /**
     * curl -i localhost:8080/chat/online
     */
    @RequestMapping(
            path = "online",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity online() {
        String responseBody = String.join("\n", usersNowOnline.values());
        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=rybalkin_is_STUPID"
     */
    @RequestMapping(
            path = "logout",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity logout(@RequestParam("name") String name,@RequestParam("password") String password) {
        if (usersOnline.containsValue(name)&&(findKey(name).equals(password))) {

            usersOnline.remove(password, name);
            messages.add("[" + name + "] logged out");
        }
        else{
            return ResponseEntity.badRequest().body("don't have this user");
        }
        return ResponseEntity.ok().build();

    }

    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=RYBALKIN_is_STUPID&msg=Hello everyone in this chat"
     */
    @RequestMapping(
            path = "say",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity say(@RequestParam("name") String name, @RequestParam("msg") String msg) {
        LocalTime time = LocalTime.now();
        DateTimeFormatter dt = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
        if (!usersOnline.containsValue(name)) {
            return ResponseEntity.badRequest().body("please log in");
        }
        if (msg.contains("<")||msg.contains(">")) {
            return ResponseEntity.badRequest().body("you wrote forbidden symbols");
        }
        if(msg.length()>=9&&(msg.substring(0,8).equals("https://")||msg.substring(0,7).equals("http://")) ){
            msg= "<a href="+"'"+msg+"'>"+msg+"</a>";
            messages.add("<b style='color:#1E90FF'>" + dt.format(time)+". "+"</b>"+" " +"<b style='color:#CD5C5C'>"+
                    name+" :"+"</b>" +msg);
            return ResponseEntity.ok().build();
        }
        messages.add("<b style='color:#1E90FF'>" + dt.format(time)+". "+"</b>"+" " +"<b style='color:#CD5C5C'>"+
                name+" :"+"</b>" +"<b style='color:#FF4500'>"+msg+"</b>");
        ResoursWritter.write(messages);
       return ResponseEntity.ok().build();
    }
}
