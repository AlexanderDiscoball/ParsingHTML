package query;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ResourceReader {
    private static String lines;

    public static String readFromResource(String resourceName) throws IOException {

        InputStream inputStream = ResourceReader.class.getClassLoader().getResourceAsStream(resourceName);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines += line +'\n';
            }
        }
        catch(IOException e){
            e.getMessage();
        }

        return lines;
    }
    public static StringBuilder read(String path) throws IOException {
        StringBuilder messages = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                messages.append(line+'\n');
            }
        }
        return messages;
    }
}