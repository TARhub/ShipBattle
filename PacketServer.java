import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Redirects packets to clients via a LAN server.
 *
 * @version %I%
 * @since 1.3
 */
public class PacketServer {
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
            runnables = new ArrayList<PacketRunnable>();
            packets   = new LinkedBlockingQueue<String>();
            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(100000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread accept = new Thread() {
            public void run() {
                for (int i=0;i<2;i++) {
                    try {
                        runnables.add(new ClientConnection(serverSocket.accept()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        accept.setDaemon(true);
        accept.start();

        Thread packetHandling = new Thread() {
            public void run() {
                int i=1;
                while (true) {
                    try {
                        String packet = packets.take();

                        send(i,packet);
                        if (i == 0) {i++;}
                        else if (i == 1) {i--;}

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        packetHandling.setDaemon(true);
        packetHandling.start();
    }

    private class ClientConnection extends PacketRunnable {

        ClientConnection(Socket client) throws IOException {
            super(client);

            Thread read = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            System.out.println(in.ready());
                            String packet = in.readLine();
                            System.out.println(packet);
                            packets.put(packet);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            read.setDaemon(true);
            read.start();
        }
    }

    public int size() {
        return runnables.size();
    }

    public void send(int player, String packet) {
        runnables.get(player).write(packet);
    }
}
