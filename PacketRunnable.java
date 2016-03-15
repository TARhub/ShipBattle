import java.io.*;
import java.net.*;
import java.util.*;

public class PacketRunnable {
    private Socket client;

    protected InputStream inFromClient  = null;
    protected BufferedReader in         = null;
    protected BufferedWriter out        = null;

    public PacketRunnable(Socket socket) throws IOException {
        this.client = socket;

        inFromClient = client.getInputStream();
        in  = new BufferedReader(new InputStreamReader(inFromClient));
        out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
    }

    public int getPort() {
        return client.getPort();
    }

    public Socket getSocket() {
        return client;
    }

    public void write(String packet) {
        try {
            out.write(packet,0,packet.length());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
