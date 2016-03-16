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
    private LinkedBlockingQueue<Packet> packets;
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
            packets   = new LinkedBlockingQueue<Packet>();
            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(100000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread accept = new Thread() {
            public void run() {
                while (true) {
                    try {
                        System.out.println("Yeet! Accept.");
                        runnables.add(new ClientConnection(serverSocket.accept()));
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
                        System.out.println("Yeet! PacketHandling.");
                        Packet packet = packets.take();
                        System.out.println(packet.packet()+"\nYours truly, PacketHandling(server).");
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
                            System.out.println("1");
                            Packet packet = (Packet) in.readObject();
                            System.out.println("2");
                            System.out.println(packet);
                            System.out.println("3");
                            packets.put(packet);
                            System.out.println("Yaw! Reading.");
                        } catch (IOException | InterruptedException
                               | ClassNotFoundException e) {
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

    public void send(Packet packet) {
        runnables.get(packet.player()).write(packet);
    }
}
