package unsw.blackout.classes.satellites;
import unsw.utils.Angle;
/**
 * Shrinking Satellites extends StorageCapable Satellites and is able of shrinking the byte size of the file stored if it contains "quantum"
 * @author Vicky Hu
 */
public class ShrinkingSatellite extends StorageCapableSatellite {
    public static int DOWNLOAD_SPEED = 15;
    public static int UPLOAD_SPEED = 10;
    public static int MAX_CAPACITY_BYTES = 150;
    public static int MAX_CAPACITY_FILES = -1;

    /**
     * Shrinking Satellite Constructor
     * @param satelliteID
     * @param position
     * @param height
     */
    public ShrinkingSatellite(String satelliteID, Angle position, double height) {
        super(satelliteID, position, height, 150000, 2500, UPLOAD_SPEED, DOWNLOAD_SPEED, MAX_CAPACITY_BYTES, MAX_CAPACITY_FILES);
    }
}
