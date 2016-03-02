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

        if (Integer.parseInt(args[0]) == 1) {
            try {
                System.out.println("foo");
                server = new PacketServer(port);
                System.out.println("bar");
                server.start();
                System.out.println("foo");
                packet = new PacketSend(player,port);
                System.out.println("bar");
                packet.starts();
            } catch (java.net.ConnectException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Board tB = new Board();


        JFrame win = new JFrame("ShipBattle");
        win.setSize(1280,660);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.add( tB );
		win.setVisible(true);
        win.setResizable(false);

        while (true) {
            System.out.print("Where would you like to hit? ");
            String hit = kb.next();

            String hit2 = packet.send(hit);

            tB.hit(tB.thisCoord(hit2));
        }
    }
}
