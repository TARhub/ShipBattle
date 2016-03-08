import java.io.*;
import java.net.*;

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

        switch (player) {
            case PLAYER_ONE: player = 1; break;
            case PLAYER_TWO: player = 2; break;
        }

    }

    public void starts() {
        try {
            client = new Socket(LOCAL_HOST,PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return client;
    }

    public String packet(String s) throws IOException {
        OutputStream outToServer = client.getOutputStream();
        DataOutputStream out     = new DataOutputStream(outToServer);

        out.writeUTF(s);

        InputStream inFromServer = client.getInputStream();
        DataInputStream in       = new DataInputStream(inFromServer);

        String line = in.readUTF();
        client.close();

        System.out.println(line);

        return line;
    }
}
