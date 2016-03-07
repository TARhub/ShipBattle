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
            serverSocket.setSoTimeout(15000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i=0;i<2;i++) {
            try {
                pR = new PacketRunnable(serverSocket.accept());
                Thread t = new Thread(pR);
                t.start();
                new Thread(new PacketServer(pR.getPort())).start();
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
