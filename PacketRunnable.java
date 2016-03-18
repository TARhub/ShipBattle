import java.io.*;
import java.net.*;
import java.util.*;

public class PacketRunnable {
    private Socket client;

    protected BufferedReader in  = null;
    protected PrintWriter    out = null;

    public PacketRunnable(Socket socket) {
        this.client = socket;

        try {
            in  = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return client.getPort();
    }

    public Socket getSocket() {
        return client;
    }

    public void write(String packet) {
        out.println(packet);
        out.flush();
    }
}
