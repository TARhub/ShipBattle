import java.io.*;
import java.net.*;
import java.util.*;

public class PacketRunnable implements Runnable {
    private Socket client;

    public PacketRunnable(Socket client) {
        this.client = client;
    }

    public int getPort() {
        return client.getPort();
    }

    public Socket getSocket() {
        return client;
    }

    @Override
    public void run() {
        InputStream inFromClient  = null;
        BufferedReader in         = null;
        DataOutputStream toClient = null;

        try {
            inFromClient = client.getInputStream();
            in = new BufferedReader(new InputStreamReader(inFromClient));
            toClient = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String txt = in.readLine();
            toClient.writeUTF(txt);
            toClient.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
