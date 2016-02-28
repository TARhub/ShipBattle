import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * This is a ship that can be placed on the Board. Ships are the defining factor of the game,
 * as the player's actions' success depends on where the ships are located.
 * <p>
 * Ships are pretty intelligent. They know their location, if they are hit, what kind of
 * ship they are, what orientation they are facing, and if they have sunk or not.
 *
 * @version %I%
 * @since   1.0
 */
public class Ship {
    private static final int AC = 5;
    private static final int B_ = 4;
    private static final int D_ = 3;
    private static final int SU = 2;
    private static final int PB = 1;

    private static final int NS = 0;
    private static final int EW = 1;

    private Coord[] locations;
    private boolean[] isHit;
    private ShipType type;
    private Orientation o;
    private int shipLength;

    private int x, y, oo;

    private boolean isSunk;

    /**
     * Default constructor that constructs a <code>Ship</code> object,
     * using a ship type and a player board to determine if the placement
     * of the ship is legal or not.
     *
     * @param t the type of ship that is constructed (1-5).
     * @param b the player board used to determine legal placement.
     */
    public Ship(int t,Board b) {
        Random rnd = new Random();
        oo = rnd.nextInt(2);

        if (oo == 0) {
            x = rnd.nextInt(10);
            y = rnd.nextInt(10-t);
        }
        else if (oo == 1) {
            x = rnd.nextInt(10-t);
            y = rnd.nextInt(10);
        }

        isSunk = false;

        switch(t) {
            case AC:
                this.type = ShipType.AIRCRAFT_CARRIER; break;
            case B_:
                this.type = ShipType.BATTLESHIP; break;
            case D_:
                this.type = ShipType.DESTROYER; break;
            case SU:
                this.type = ShipType.SUBMARINE; break;
            case PB:
                this.type = ShipType.PATROL_BOAT; break;
        }
        switch(oo) {
            case NS:
                this.o = Orientation.NORTH_SOUTH; break;
            case EW:
                this.o = Orientation.EAST_WEST; break;
        }

        switch(type) {
            case AIRCRAFT_CARRIER:
                isHit = new boolean[5];
                shipLength = 5;
                locations = new Coord[5];
                break;
            case BATTLESHIP:
                isHit = new boolean[4];
                shipLength = 4;
                locations = new Coord[4];
                break;
            case SUBMARINE:
            case DESTROYER:
                isHit = new boolean[3];
                shipLength = 3;
                locations = new Coord[3];
                break;
            case PATROL_BOAT:
                isHit = new boolean[2];
                shipLength = 2;
                locations = new Coord[2];
                break;
        }
        boolean c = false;


        for (int i=0;i<shipLength;i++) {
            if (o.equals(Orientation.NORTH_SOUTH))
                locations[i] = new Coord(x,y+i);
            else
                locations[i] = new Coord(x+i,y);

        }
        for (int d=0;d<locations.length;d++) {
            c = b.thisCoord(locations[d]).hasShip();
            if (c == true) {
                type = ShipType.ERROR;
            }
        }
        if (c == false) {
            for (int e=0;e<locations.length;e++)
                locations[e].setShip();
        }

    }

    /**
     * @returns the first coord of the ship's locations.
     * @deprecated use the {@link #locations(int i)} instead.
     */
    @Deprecated
    public Coord firstCoord() {
        return locations[0];
    }

    /**
     * @param n coordinate n of the ship.
     * @returns the nth coordinate of the ship.
     */
    public Coord locations(int n) {
        return locations[n];
    }

    /**
     * @returns the orientation of the ship.
     */
    public Orientation orientation() {
    	return o;
    }

    /**
     * @returns the type of ship the ship is.
     */
    public ShipType shipType() {
    	return type;
    }

    /**
     * @returns the length of the ship.
     */
    public int shipLength() {
        return shipLength;
    }

    /**
     * @returns if the ship has sunk.
     */
    public boolean isSunk() {
        for (int i=0;i<isHit.length;i++) {
            if (isHit[i] == true )
                isSunk = true;
            else if (isHit[i] == false)
                isSunk = false;
        }

        return isSunk;
    }
    /**
     * Gives the ship an image to use when being painted.
     * This method is implemented in {@link Board#placeShip(Ship s,Graphics g)}.
     *
     * @param type the type of ship the ship is.
     * @param o    the orientation of the ship.
     * @returns an image of the ship.
     * @see Board#placeShip(Ship s,Graphics g)
     */
    public Image shipDisplay(ShipType type, Orientation o) throws IOException {
        Image shipDisplay = null;

        switch (o) {
            case NORTH_SOUTH:
                switch (type) {
                    case AIRCRAFT_CARRIER:
                        shipDisplay = ImageIO.read(new File ("textures/aircraft_carrier_ns.png")); break;
                    case BATTLESHIP:
                        shipDisplay = ImageIO.read(new File ("textures/battleship_ns.png"));       break;
                    case SUBMARINE:
                        shipDisplay = ImageIO.read(new File ("textures/submarine_ns.png"));        break;
                    case DESTROYER:
                        shipDisplay = ImageIO.read(new File ("textures/destroyer_ns.png"));        break;
                    case PATROL_BOAT:
                        shipDisplay = ImageIO.read(new File ("textures/patrol_boat_ns.png"));      break;
                    default:
                        shipDisplay = null; break;
                }
                break;
            case EAST_WEST:
                switch (type) {
                    case AIRCRAFT_CARRIER:
                        shipDisplay = ImageIO.read(new File ("textures/aircraft_carrier_ew.png")); break;
                    case BATTLESHIP:
                        shipDisplay = ImageIO.read(new File ("textures/battleship_ew.png"));       break;
                    case SUBMARINE:
                        shipDisplay = ImageIO.read(new File ("textures/submarine_ew.png"));        break;
                    case DESTROYER:
                        shipDisplay = ImageIO.read(new File ("textures/destroyer_ew.png"));        break;
                    case PATROL_BOAT:
                        shipDisplay = ImageIO.read(new File ("textures/patrol_boat_ew.png"));      break;
                    default:
                        shipDisplay = null; break;
                }
                break;
        }

        return shipDisplay;
    }

    /*
    AIRCRAFT CARRIERS: 96 possible locations.
    BATTLESHIPS: 116 possible locations.
    SUBMARINES & DESTROYERS: 132 possible locations.
    PATROL BOATS: 150 possible locations.
    220492800 possible board configurations

    355687428096000 possible moves/player
    4.2344414721530856114850495158116 x 10^332 possible games of Battleship
    */
}
