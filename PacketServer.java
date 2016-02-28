import java.io.*;
import java.net.*;

public class PacketServer extends Thread {
    private ServerSocket serverSocket;
    private final int    PORT;

    public PacketServer(int port) {
        this.PORT = port;
        serverSocket = new ServerSocket(PORT);
        serverSocket.setSoTimeout(15000);
    }

    public void run() throws IOException {
        while(true) {
            Socket acc = serverSocket.accept();
        }
    }
}
