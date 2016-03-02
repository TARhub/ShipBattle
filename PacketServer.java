import java.io.*;
import java.net.*;

/**
 * Redirects packets to clients via a LAN server.
 *
 * @version %I%
 * @since 1.3
 */
public class PacketServer {
    private ServerSocket serverSocket;
    private Socket socket = null;
    private final int    PORT;

    private EchoThread echoThread;

    /**
     * Constructs a <code>PacketServer</code> object using a port.
     * This constructor allows for multiple clients, using an infinite
     * while loop to do so.
     *
     * @param port The port # of the server.
     */
    public PacketServer(int port) throws IOException {
        this.PORT = port;
        serverSocket = new ServerSocket(PORT);
        serverSocket.setSoTimeout(15000);


        while(true) {
            socket = serverSocket.accept();
            echoThread = new EchoThread(socket);
            echoThread.start();
        }
    }

    /**
     * Thread for the PacketServer, which recieves packets and immediately
     * sends it to the other client.
     *
     * @version %I%
     * @since 1.3
     */
    public class EchoThread extends Thread {
        private Socket clientSocket;

        /**
         * Constructor for the <code>EchoThread</code>.
         *
         * @param clientSocket Socket from a client that will send and
         * recieve packets
         */
        public EchoThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            InputStream inFromClient  = null;
            BufferedReader in         = null;
            DataOutputStream toClient = null;

            try {
                inFromClient = clientSocket.getInputStream();
                in = new BufferedReader(new InputStreamReader(inFromClient));
                toClient = new DataOutputStream(clientSocket.getOutputStream());
            } catch (IOException e) {
                System.err.println("Well, this happened: "+e);
            }

            try {
                String txt = in.readLine();
                toClient.writeUTF(txt);
                toClient.flush();
            } catch (IOException e) {
                System.err.println("Well, this happened: "+e);
            }
        }
    }
}
