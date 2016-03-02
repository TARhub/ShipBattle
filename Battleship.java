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
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);

        Board tB = new Board();
        PacketSend packet = new PacketSend(Integer.parseInt(args[0]),Integer.parseInt(args[1]));

        if (Integer.parseInt(args[0]) == 1) {
            try {
                new PacketServer(Integer.parseInt(args[1]));
            } catch (IOException e) {
                System.err.println("Well, this happened: "+e);
            }
        }

        JFrame win = new JFrame("ShipBattle");
        win.setSize(1280,660);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.add( tB );
		win.setVisible(true);
        win.setResizable(false);

        while (true) {
            System.out.print("Where would you like to hit? ");
            String hit = kb.next();
            System.out.println(tB.thisCoord(hit).hasShip());
            tB.hit(tB.thisCoord(hit));
        }
    }
}
