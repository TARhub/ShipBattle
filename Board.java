import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * This is the virtual board that the player will use to interact with the
 * Battleship game. Once generated, it will randomly place ships in a 10x10 grid
 * which the opposing player can hit. The board consists of actually two boards, the
 * "target board", with the radar, and the "ship board", with the water.
 * <p>
 * The target board is where the player will attempt to hit the opposing player's
 * ships. After the attempt, the board will display either a red X, a hit,
 * or a white <>, a miss.
 * The ship board doesn't have any player interaction in it, but it is a
 * very useful panel for the player to see the status of his ships.
 *
 * @version %I%
 * @since 1.0
 */
public class Board extends Canvas implements MouseListener {
	private Image radar;
    private Image water;
	private Image hit;
	private Image miss;

    private Coord[][] coords;
    private String[][] boardnames;
	private ArrayList<Ship> ships = new ArrayList<Ship>();
	private boolean placingShips = true;

	/**
	 * Default constructor for the Board class.
	 *
	 * It first instantiates the 2d array <code>Coord</code>s,which are the basis of most
	 * of the game, as it gives vital information about the game, like if there is a ship
	 * on the coordinate, if it has been hit, has been attempted to be hit, and most
	 * importantly, it's location.
	 * <p>
	 * It will then instantiate the images of the game, the radar, the sea, the hit symbol,
	 * and the miss symbol. Lastly, it will then instantiate the {@link #buildBattlefield()}
	 * method, which will randomly place ships on the board.
	 */
    public Board() {
        coords = new Coord[10][10];
        boardnames = new String[10][10];

        try {
			radar = ImageIO.read(new File("textures/radar.png"));
			water = ImageIO.read(new File("textures/water.png"));
			hit   = ImageIO.read(new File("textures/hit.png"));
			miss  = ImageIO.read(new File("textures/miss.png"));
        } catch (IOException e) {
            System.err.println("Well, this happened: "+e);
        }

        for (int a=0;a<10;a++)
            for (int b=0;b<10;b++) {
                Coord c = new Coord(a,b);
                coords[a][b] = c;
            }

        int e=0;
        for (char c='A';c<'K';c++) {
            for (int d=1;d<11;d++)
                boardnames[e][d-1] = String.valueOf(c)+String.valueOf(d);
            e++;
        }

		buildBattlefield();
    }

	/**
	 * {@inheritDoc}
	 * @see #placeShip(Ship s,Graphics g)
	 */
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(radar,0,0,this);

        for (int i=0;i<=576;i+=64)
            for (int j=0;j<=576;j+=64)
                g.drawImage(water,i+640,j,this);

		for (int i=0;i<ships.size();i++) {
			Ship s = ships.get(i);

			if (! s.isSunk() == true) {
				try {
					placeShip(s,g);
				} catch (IOException e) {
					System.err.println("Well, this happened: "+e);
				} catch (IndexOutOfBoundsException d) {
					System.err.println("Well, this happened: "+d);
				}
			}
		}

		for (int a=0;a<10;a++)
			for (int b=0;b<10;b++) {
				Coord c = coords[a][b];
				if ( c.hasShip() == true && c.isHit() == true ) {
					g.drawImage(hit,c.gx()    ,c.gy(),this);
					g.drawImage(hit,c.gx()+640,c.gy(),this);
				}
				else if ( c.attHit() == true && c.isHit() == false ) {
					g.drawImage(miss,c.gx(),c.gy(),this);
				}
			}
	}

	/**
	 * @param   c a String that refers to a coord in the String + int format.
	 * @return a <code>Coord</code> from the board from a string.
	 */
    public Coord thisCoord(String c) {
		Coord co = new Coord(0,0);
        int a=0,b=0;
        for (a=0;a<10;a++) {
            for (b=0;b<10;b++) {
				if (coords[a][b].getCoord().equals(c))
					co = coords[a][b];
            }
		}
		return co;
    }

	/**
	 * @param   c another <code>Coord</code>
	 * @return a <code>Coord</code> from the board from a another coord.
	 */
	public Coord thisCoord(Coord c) {
		return coords[c.x()][c.y()];
	}

	/**
	 * Sets a coordinate on the board to another coord.
	 * @param that another Coord.
	 */
	public void thatCoord(Coord that) {
		coords[that.x()][that.y()] = that;
	}

	/**
	 * Wipes all data from the <code>Coord</code> array.
	 */
	public void refreshStatus() {
		for (int i=0;i<10;i++)
			for (int j=0;j<10;j++)
				coords[i][j].refreshShip();
	}

	/**
	 * Randomly places ships on the Board.
	 */
	public void buildBattlefield() {
		boolean legal = false;
		Ship s;


		for (int i=5;i>=1;i--) {
				do {
					s = new Ship(i,this);
					if (! s.shipType().equals(ShipType.ERROR)) {
						ships.add(s);
						for (int x=0;x<i;x++) {
							thatCoord(s.locations(x));
						}
						break;
					}
				} while (legal == false);
		}

    }

	/**
	 * Graphically places the ships on the board.
	 * @param s The ship that will be placed.
	 * @param g Graphics inherited from the {@link #paint(Graphics g)} method.
	 */
    public void placeShip(Ship s,Graphics g) throws IOException {
        BufferedImage shipDisplay = null;

        switch(s.orientation()) {
            case NORTH_SOUTH:
                switch(s.shipType()) {
                    case AIRCRAFT_CARRIER:
                        shipDisplay = (BufferedImage) s.shipDisplay(ShipType.AIRCRAFT_CARRIER,Orientation.NORTH_SOUTH);
                        break;
                    case BATTLESHIP:
                        shipDisplay = (BufferedImage) s.shipDisplay(ShipType.BATTLESHIP,Orientation.NORTH_SOUTH);
                        break;
                    case SUBMARINE:
                        shipDisplay = (BufferedImage) s.shipDisplay(ShipType.SUBMARINE,Orientation.NORTH_SOUTH);
                        break;
                    case DESTROYER:
                        shipDisplay = (BufferedImage) s.shipDisplay(ShipType.DESTROYER,Orientation.NORTH_SOUTH);
                        break;
                    case PATROL_BOAT:
                        shipDisplay = (BufferedImage) s.shipDisplay(ShipType.PATROL_BOAT,Orientation.NORTH_SOUTH);
                        break;
                }
            break;
            case EAST_WEST:
                switch(s.shipType()) {
                    case AIRCRAFT_CARRIER:
                        shipDisplay = (BufferedImage) s.shipDisplay(ShipType.AIRCRAFT_CARRIER,Orientation.EAST_WEST);
                        break;
                    case BATTLESHIP:
                        shipDisplay = (BufferedImage) s.shipDisplay(ShipType.BATTLESHIP,Orientation.EAST_WEST);
                        break;
                    case SUBMARINE:
                        shipDisplay = (BufferedImage) s.shipDisplay(ShipType.SUBMARINE,Orientation.EAST_WEST);
                        break;
                    case DESTROYER:
                        shipDisplay = (BufferedImage) s.shipDisplay(ShipType.DESTROYER,Orientation.EAST_WEST);
                        break;
                    case PATROL_BOAT:
                        shipDisplay = (BufferedImage) s.shipDisplay(ShipType.PATROL_BOAT,Orientation.EAST_WEST);
                        break;
                }
            break;
        }

        g.drawImage(shipDisplay, s.locations(0).gx()+640,
                    s.locations(0).gy(), this);
    }

	/**
	 * Determines if the move by the player is a hit or a miss.
	 * @param c The coord that the player is attempting to hit.
	 */
	public void hit(Coord c) {
		if (thisCoord(c).hasShip() == true) {
			thisCoord(c).setHit();
			System.out.println("Hit!");
		}
		else if (thisCoord(c).hasShip() == false) {
			thisCoord(c).setMiss();
			System.out.println("Miss!");
		}
		repaint();
	}

    public void mouseClicked( MouseEvent evt ) {}
	public void mousePressed( MouseEvent evt ) {}
	public void mouseReleased( MouseEvent evt ) {}
	public void mouseEntered( MouseEvent evt ) {}
	public void mouseExited( MouseEvent evt ) {}
}
