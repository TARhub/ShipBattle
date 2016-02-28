/**
 * Generates coordinates for use within the <code>Board</code>.
 *
 * @author Thomas A. Rodriguez
 * @version %I%
 * @since 1.0
 */
public class Coord {
    // CONSTANTS \\
    private final String COORD;
    private final int X_;
    private final int Y_;
    private final int GX;
    private final int GY;

    private static final char A_='A',B_='B',C_='C',D_='D',E_='E',
                              F_='F',G_='G',H_='H',I_='I',J_='J';

    // INSTANCE VARIABLES \\
    private boolean hit;
    private boolean hasShip;
    private boolean attHit;

    /**
     * Default constructor that creates a <code>Coord</code> object,
     * with a x and y coordinate.
     *
     * @param x The x-coordinate of the <code>Coord</code>.
     * @param y The x-coordinate of the <code>Coord</code>.
     */
    public Coord(int x,int y) {
        this.X_ = x;
        this.Y_ = y;
        GX = x*64;
        GY = y*64;
        hasShip = false;
        attHit = false;

        char c='x';
        Integer d = (Integer) Y_ + 1;

        switch(x) {
            case 0: c = A_; break;
            case 1: c = B_; break;
            case 2: c = C_; break;
            case 3: c = D_; break;
            case 4: c = E_; break;
            case 5: c = F_; break;
            case 6: c = G_; break;
            case 7: c = H_; break;
            case 8: c = I_; break;
            case 9: c = J_; break;
        }

        COORD = c+d.toString();

    }

    // ACCESSOR METHODS \\

    /**
     * @returns the coordinate name of the <code>Coord</code>.
     */
    public String getCoord() {
        return COORD;
    }
    /**
     * @returns the x coordinate of the <code>Coord</code>.
     */
    public int x() {
        return X_;
    }
    /**
     * @returns the y coordinate of the <code>Coord</code>.
     */
    public int y() {
        return Y_;
    }
    /**
     * @returns the graphical x coordinate of the <code>Coord</code>.
     */
    public int gx() {
        return GX;
    }
    /**
     * @returns the graphical y coordinate of the <code>Coord</code>.
     */
    public int gy() {
        return GY;
    }
    /**
     * @return if the <code>Coord</code> has been it or not.
     */
    public boolean isHit() {
        return hit;
    }
    /**
     * @return if the <code>Coord</code> contains a <code>Ship</code> or not.
     */
    public boolean hasShip() {
        return hasShip;
    }

    public boolean attHit() {
        return attHit;
    }

    // MODIFIER METHODS \\

    /**
     * Sets the coord as hit
     */
    public void setHit() {
        hit = true;
    }

    public void setMiss() {
        hit = false;
        attHit = true;
    }

    public void setShip() {
        hasShip = true;
    }

    public void refreshShip() {
        hasShip = false;
    }

}
