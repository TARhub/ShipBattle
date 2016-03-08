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
    private ArrayList<PacketRunnable> runnables;
    private LinkedBlockingQueue<String> packets;
    private ServerSocket serverSocket;
    private final int PORT;

    /**
     * Constructs a <code>PacketServer</code> object using a port.
     * This constructor allows for multiple clients, using an infinite
     * while loop to do so.
     *
     * @param port The port # of the server.
     */
    public PacketServer(int port) {
        // http://stackoverflow.com/questions/13115784/sending-a-message-to-all-clients-client-server-communication

        this.PORT = port;

        try {
            runnables = new ArrayList<Runnables>();
            packets   = new LinkedBlockingQueue<String>();
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread accept = new Thread() {
            public void run() {
                try {
                    runnables.add(new PacketRunnable(serverSocket.accept()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        accept.setDaemon(true);
        accept.start();
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
