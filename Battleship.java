import java.net.ConnectException;
import java.util.Scanner;
import java.io.IOException;
import javax.swing.JFrame;

/**
 * Plays a game of battleship (not capitalized, jokes on you Hasbro)
 *
 * @version %I%, %G%
 * @since 0.0
 */
public class Battleship { // AKA "Overly Complex Board Game"
    public static void main(String[] args) throws IOException {
        Scanner kb = new Scanner(System.in);

        int player = Integer.parseInt(args[0]);
        int port   = Integer.parseInt(args[1]);

        PacketServer server = null;
        PacketSend   packet = null;

        if ( player == 1 ) {
            try {
                server = new PacketServer(port);
                packet = new PacketSend(player,port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if ( player == 2 ) {
            try {
                packet = new PacketSend(player,port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if ( player == 1 ) {
            while (server.size() != 2) {
                System.out.println("Waiting for second player on port "+port+".");
            }
        }

        Board tB = new Board();

        JFrame win = new JFrame("ShipBattle");
        win.setSize(1280,660);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.add( tB );
		win.setVisible(true);
        win.setResizable(false);

        int turn = player;

        while (true) {
            if (turn == 1) {
                System.out.print("Where would you like to hit? ");
                String hit = kb.next();
                packet.send(hit);
                turn++;
            }
            else if (turn == 2) {
                tB.hit(tB.thisCoord(packet.getHead()));
                turn--;
            }
        }
    }
}
