package unsw.blackout.classes.satellites;
import unsw.utils.Angle;
/**
 * Standard Satellites extends StorageCapable Satellites.
 * @author Vicky Hu
 */
public class StandardSatellite extends StorageCapableSatellite {
    public static int DOWNLOAD_SPEED = 1;
    public static int UPLOAD_SPEED = 1;
    public static int MAX_CAPACITY_BYTES = 150;
    public static int MAX_CAPACITY_FILES = 3;

    /**
     * Standard Satellite Constructor
     * @param satelliteID
     * @param position
     * @param height
     */
    public StandardSatellite(String satelliteID, Angle position, double height) {
        super(satelliteID, position, height, 200000, 1000, UPLOAD_SPEED, DOWNLOAD_SPEED, MAX_CAPACITY_BYTES, MAX_CAPACITY_FILES);
    }
}