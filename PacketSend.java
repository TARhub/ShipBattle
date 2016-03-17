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
    private LinkedBlockingQueue<String> packets;
    private Socket client;

    private String storedPacket = null;

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

        packets = new LinkedBlockingQueue<String>();
        sC      = new ServerConnection(client);

        switch (player) {
            case PLAYER_ONE: player = 1; break;
            case PLAYER_TWO: player = 2; break;
        }

        Thread packetHandling = new Thread() {
            public void run() {
                while (true) {
                    try {
                        System.out.println("Yeet! Packet Handling.");
                        storedPacket = packets.take();
                        System.out.println(storedPacket+"\nYours truly, PacketHandling(client).");
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

    public Callable<String> getHead() {
        Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                while(storedPacket == null) {System.out.println("It's null!");}
                return storedPacket;
            }
        };

        return task;
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
                            System.out.println(in.ready());
                            System.out.println("1");
                            String packet = in.readLine();
                            System.out.println("2");
                            System.out.println(packet);
                            System.out.println("3");
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

    }

    public void send(String packet) {
        sC.write(packet);
    }
}
