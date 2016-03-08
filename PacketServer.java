import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * Redirects packets to clients via a LAN server.
 *
 * @version %I%
 * @since 1.3
 */
public class PacketServer extends Thread {
    private ServerSocket serverSocket;
    private ArrayList<PacketRunnable> runnables = new ArrayList<PacketRunnable>();
    private PacketRunnable pR = null;
    private final int PORT;

    /**
     * Constructs a <code>PacketServer</code> object using a port.
     * This constructor allows for multiple clients, using an infinite
     * while loop to do so.
     *
     * @param port The port # of the server.
     */
    public PacketServer(int port) throws IOException {
        this.PORT = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(1500000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i=0;i<2;i++) {
            try {
                runnables.add(new PacketRunnable(serverSocket.accept()));
                pR = runnables.get(i);
                Thread t = new Thread(pR);
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class PacketRunnable implements Runnable {
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
}
