import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        String ip = "http://localhost/";
        Thread myThread;
        long startTime;
        long endTime;

        startTime = System.currentTimeMillis();
        myThread = new HttpRequestThread(ip + "smallfile.txt");
        myThread.start();
        myThread.join();
        endTime = System.currentTimeMillis();

        System.out.println("Small file test completed in: " + (endTime - startTime) + " milliseconds");

        String[] dirFiles = {
            "directorytest/HttpParser.java", 
            "directorytest/HttpRequest.java", 
            "directorytest/HttpResponse.java", 
            "directorytest/HttpStatus.java"
        };

        startTime = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<Thread>();
        for (String file : dirFiles) {
            myThread = new HttpRequestThread(ip + file);
            threads.add(myThread);
            myThread.start();
        }
        for(Thread thread : threads) {
            thread.join();
        }
        endTime = System.currentTimeMillis();

        System.out.println("Directory file test completed in: " + (endTime - startTime) + " milliseconds");

        startTime = System.currentTimeMillis();
        myThread = new HttpRequestThread(ip + "bigfile.jar");
        myThread.start();
        myThread.join();
        endTime = System.currentTimeMillis();

        System.out.println("Large file test completed in: " + (endTime - startTime)  + " milliseconds");
    }

    static class HttpRequestThread extends Thread {
        private final String url;
        
        public HttpRequestThread(String url) {
            this.url = url;
        }
        
        @Override
        public void run() {
            try {
                URL url = new URL(this.url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                
                BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                System.out.println("Message recieved: " + this.url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
