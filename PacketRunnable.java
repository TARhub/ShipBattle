import java.io.*;
import java.net.*;
import java.util.*;

public class PacketRunnable {
    private Socket client;

    protected InputStream inFromClient  = null;
    protected ObjectInputStream in      = null;
    protected ObjectOutputStream out    = null;

    public PacketRunnable(Socket socket) throws IOException {
        this.client = socket;

        in  = new ObjectInputStream(client.getInputStream());
        out = new ObjectOutputStream(client.getOutputStream());
    }

    public int getPort() {
        return client.getPort();
    }

    public Socket getSocket() {
        return client;
    }

    public void write(Packet packet) {
        try {
            out.writeObject((Object)packet);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
