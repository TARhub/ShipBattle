import java.io.*;
import java.net.*;
import java.util.*;

public class PacketRunnable {
    private Socket client;

    protected InputStream inFromClient  = null;
    protected BufferedReader in         = null;
    protected DataOutputStream out      = null;

    public PacketRunnable(Socket client) throws IOException {
        this.client = client;

        inFromClient = client.getInputStream();
        in  = new BufferedReader(new InputStreamReader(inFromClient));
        out = new DataOutputStream(client.getOutputStream());
    }

    public int getPort() {
        return client.getPort();
    }

    public Socket getSocket() {
        return client;
    }

    public void write(String packet) {}
}
