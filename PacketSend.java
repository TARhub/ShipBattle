import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Sends and recieves packets to/from the server.
 *
 * @version %I%
 * @since 1.2
 */
public class PacketSend {
    // CONSTANTS \\
    private static final int PLAYER_ONE = 1;
    private static final int PLAYER_TWO = 2;

    private final String LOCAL_HOST = "localhost";
    private final int    PORT;
    private final int    player = -1;

    private ServerConnection sC;
    private LinkedBlockingQueue<Packet> packets;
    private Socket client;

    private Packet storedPacket = null;

    /**
     * Constructs a <code>PacketSend</code> object using the player #,
     * and the port number specified by the host of the Battleship server.
     *
     * @param player The player number of the player that is sending the packet.
     * @param port   The port # of ther server that the player is connected to.
     */
    public PacketSend(int player, int port) throws IOException {
        this.PORT = port;
        client = new Socket(LOCAL_HOST,PORT);
        client.setSoTimeout(100000);

        packets = new LinkedBlockingQueue<Packet>();
        sC = new ServerConnection(client);

        switch (player) {
            case PLAYER_ONE: player = 1; break;
            case PLAYER_TWO: player = 2; break;
        }

        Thread packetHandling = new Thread() {
            public void run() {
                while (true) {
                    try {
                        System.out.println("Yeet! PacketHandling.");
                        storedPacket = packets.take();
                        System.out.println(storedPacket.packet()+"\nYours truly, PacketHandling(client).");
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

    public String getHead() {
        return storedPacket.packet();
    }

    private class ServerConnection extends PacketRunnable {

        ServerConnection(Socket server) throws IOException {
            super(server);

            Thread read = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            System.out.println("Yeet! Reading.");
                            System.out.println("1");
                            Packet packet = (Packet) in.readObject();
                            System.out.println("2");
                            System.out.println(packet.packet());
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

    public void send(Packet packet) {
        sC.write(packet);
    }
}
