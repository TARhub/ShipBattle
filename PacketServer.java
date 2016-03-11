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
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread accept = new Thread() {
            public void run() {
                while (true) {
                    try {
                        System.out.println("Yeet! Accept.");
                        runnables.add(new PacketRunnable(serverSocket.accept()));
                        System.out.println("Yaw! Accept.");
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
                while (true) {
                    try {
                        System.out.println("Yeet! Packet Handling.");
                        String packet = packets.take();
                        System.out.println(packet);
                        System.out.println("Yaw! PacketHandling.");
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
                            System.out.println("Yeet! Reading.");
                            System.out.println(in.ready());
                            String packet = in.readLine();
                            System.out.println(packet);
                            packets.put(packet);
                            System.out.println("Yaw! Reading.");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            read.setDaemon(true);
            read.start();
        }

        @Override
        public void write(String packet) {
            try {
                out.writeUTF(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int size() {
        return runnables.size();
    }

    public void send(int player, String packet) {
        runnables.get(player).write(packet);
    }
}
