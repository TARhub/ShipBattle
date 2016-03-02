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
    private Socket socket = null;
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
                socket = serverSocket.accept();
                new Thread(new PacketServer(socket.getPort())).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        InputStream inFromClient  = null;
        BufferedReader in         = null;
        DataOutputStream toClient = null;

        try {
            inFromClient = socket.getInputStream();
            in = new BufferedReader(new InputStreamReader(inFromClient));
            toClient = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String txt = in.readLine();
            System.out.println(txt);
            toClient.writeUTF(txt);
            toClient.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
