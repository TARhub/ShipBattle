import java.io.*;
import java.net.*;

/**
 * Sends and recieves packets to/from the server.
 *
 * @author Thomas A. Rodriguez
 * @version %I%, %G%
 * @since 1.2
 */
public class PacketSend {
    // CONSTANTS \\
    private static final int PLAYER_ONE = 1;
    private static final int PLAYER_TWO = 2;

    private final String LOCAL_HOST = "localhost";
    private final int    PORT;
    private final int    player = -1;

    /**
     * Constructs a <code>PacketSend</code> object using the player #,
     * and the port number specified by the host of the Battleship server.
     *
     * @param player The player number of the player that is sending the packet.
     * @param port   The port # of ther server that the player is connected to.
     */
    public PacketSend(int player, int port) {
        this.PORT = port;

        switch (player) {
            case PLAYER_ONE: player = 1; break;
            case PLAYER_TWO: player = 2; break;
        }
    }

}
