import java.io.IOException;
import java.net.InetAddress;

import com.supernova.server.NIOServer;
import com.supernova.server.Server;
import com.supernova.server.SerialServer;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        InetAddress localhost = InetAddress.getLocalHost();
        System.out.println("System IP Address is: " + localhost.getHostAddress());

        boolean isMultiThreaded = false;
        for(String arg : args) {
            if(arg.equals("--parallel")) {
                isMultiThreaded = true;
                break;
            }
        }

        Server server = (isMultiThreaded) ? new NIOServer(80) : new SerialServer(80);

        System.out.println("Server of type " + server.getClass().getName() + " has been launched");

        Thread myThread = new Thread(server);
        myThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                myThread.interrupt();     
            }
        });

        Thread.sleep(20000);
    }
}
