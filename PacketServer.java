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
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(15000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i=0;i<2;i++) {
            try {
                System.out.println("foo!");
                pR = new PacketRunnable(serverSocket.accept());
                Thread t = new Thread(pR);
                t.start();
                System.out.println("bar!");
                new Thread(new PacketServer(pR.getPort())).start();
                System.out.println("bar!1");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class PacketRunnable implements Runnable {
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
                System.out.println("foo!1");
                inFromClient = client.getInputStream();
                System.out.println("foo!2");
                in = new BufferedReader(new InputStreamReader(inFromClient));
                System.out.println("foo!3");
                toClient = new DataOutputStream(client.getOutputStream());
                System.out.println("foo!4");
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("foo!5");
                String txt = in.readLine();
                System.out.println("foo!6");
                System.out.println(txt);
                System.out.println("foo!7");
                toClient.writeUTF(txt);
                System.out.println("foo!8");
                toClient.flush();
                System.out.println("foo!9");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
