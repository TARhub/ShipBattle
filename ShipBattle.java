import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.JFrame;

/**
 * Plays a game of battleship (not capitalized, jokes on you Hasbro)
 *
 * @version %I%
 * @since 0.0
 */
public class ShipBattle { // AKA "Overly Complex Board Game"
    // CONSTANTS \\
    private static final int PLAYER_ONE = 1;
    private static final int PLAYER_TWO = 2;

    private final  String LOCAL_HOST = "localhost";
    private final  int    PORT;
    private static int    player = -1;

    private ServerConnection sC;
    private LinkedBlockingQueue<String> packets;
    private Socket client;

    private static String storedPacket = null;

    public ShipBattle(int player, int port) throws IOException {
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
                        storedPacket = packets.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        packetHandling.setDaemon(true);
        packetHandling.start();
    }

    public static void main(String[] args) throws IOException {
        Scanner kb = new Scanner(System.in);
        int port = -420;

        String mode = args[0];

        if (mode.equals("start")) {
            player = 1;
            port = new Random().nextInt(16383) + 49152;
            System.out.println("Game hosted on port "+port);
        }
        else if (mode.equals("join")) {
            player = 2;
            System.out.println("Enter port: ");
            port = kb.nextInt();
        }
        else {
            System.err.println("Wutrudoing?");
            System.exit(0);
        }

        PacketServer server = null;
        ShipBattle   packet = null;

        if ( player == 1 ) {
            try {
                server = new PacketServer(port);
                packet = new ShipBattle(player,port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if ( player == 2 ) {
            try {
                packet = new ShipBattle(player,port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if ( player == 1 ) {
            while (server.size() != 2) {
                System.out.println("Waiting for second player on port "+port+".");
            }
        }

        Board tB = new Board(player);

        JFrame win = new JFrame("ShipBattle");
        win.setSize(1280,660);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.add( tB );
		win.setVisible(true);
        win.setResizable(false);

        int turn = player;
        ExecutorService exec = Executors.newFixedThreadPool(1);

        while (true) {
            if (turn == 1) {
                System.out.print("Where would you like to hit? ");
                String hit = kb.next();
                packet.send(hit);
                turn++;
            }
            else if (turn == 2) {
                Callable<String> task = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        while(storedPacket == null) {System.out.println("It's null!");}
                        return storedPacket;
                    }
                };

                Future<String> future = exec.submit(task);

                String toHit = null;

                try {
                    toHit = future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                tB.hit(tB.thisCoord(toHit));
                turn--;
            }
        }

        //exec.shutdownNow();
    }

    public static int oppPlayer() {
        int nEw = -1;
        switch(player) {
            case 1:  nEw = 2;  break;
            case 2:  nEw = 1;  break;
            default: nEw = 0;  break;
        }

        return nEw;
    }

    private class ServerConnection extends PacketRunnable {

        ServerConnection(Socket server) throws IOException {
            super(server);

            Thread read = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            System.out.println(in.ready());
                            String packet = in.readLine();
                            System.out.println(packet);
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
    }

    public void send(String packet) {
        sC.write(packet);
    }
}
