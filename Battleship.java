import java.util.Scanner;
import javax.swing.JFrame;

/**
 * Plays a game of battleship (not capitalized, jokes on you Hasbro)
 *
<<<<<<< HEAD
 * @version %I%
 * @since 0.0 
=======
 * @author Thomas A. Rodriguez
 * @version %I%, %G%
 * @since 0.0
>>>>>>> parent of 53f11cd... Merge pull request #6 from TARhub/client-server-branch
 */
public class Battleship { // AKA "Overly Complex Board Game"
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);

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
            System.out.println(tB.thisCoord(hit).hasShip());
            tB.hit(tB.thisCoord(hit));
        }
    }
}
