package ru.atom.chat;
import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class ResoursWritter {
   private static final String filePath = "C:\\Users\\MC\\Desktop\\project\\httpchat\\src\\main\\resources\\buffer.txt";
    public static void write(Queue<String> messages) {

        String text = messages.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));

        try {
            FileWriter writer = new FileWriter(filePath, true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(text);
            bufferWriter.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    public static Queue<String> read() throws IOException {
        Queue<String> messages = new ConcurrentLinkedQueue<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                messages.add(line);
            }
        }
        return messages;
    }
    public static void writePass(){
        Map<String, String> usersOnline = ChatController.usersOnline;
        Collection<String> collection= usersOnline.keySet();

        for (String key : collection) {
            String namePlusKey = "\r\n" + usersOnline.get(key)+ "\r\n" + key;
            try {
                FileWriter writer = new FileWriter("C:\\Users\\MC\\Desktop\\project\\httpchat\\src\\main\\resources\\loggins.txt", true);
                BufferedWriter bufferWriter = new BufferedWriter(writer);
                bufferWriter.write(namePlusKey);
                bufferWriter.close();
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }
    }
    public static  Map<String, String> getPass()throws IOException{
        Map<String, String> usersOnline = new ConcurrentHashMap<>();
        int counter = 0;
        String pass = "";
        String name = "";
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\MC\\Desktop\\project\\httpchat\\src\\main\\resources\\loggins.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(counter%2==0)name = line;
                else pass = line;
                if(!(counter%2==0))
                usersOnline.put(pass,name);
                counter++;
            }
        }
        return usersOnline;
    }
}
