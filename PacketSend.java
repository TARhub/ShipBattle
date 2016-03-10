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

        packets = new LinkedBlockingQueue<String>();
        sC = new ServerConnection(client);

        switch (player) {
            case PLAYER_ONE: player = 1; break;
            case PLAYER_TWO: player = 2; break;
        }

    }

    public Socket getSocket() {
        return client;
    }

    private class ServerConnection extends PacketRunnable {

        ServerConnection(Socket server) {
            super(server);

            Thread read = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            String packet = in.readLine();
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

        @Override
        public void write(String packet) {
            try {
                out.writeUTF(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String packet) {
        sC.write(packet);
    }
}
