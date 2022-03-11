package unsw.blackout.classes.devices;
import unsw.utils.Angle;
/**
 * Device of type Desktop Device
 * @author Vicky Hu
 */
public class DesktopDevice extends Device {
    /**
     * Desktop Constructor
     * @param deviceID
     * @param devicePosition
     */
    public DesktopDevice(String deviceID, Angle devicePosition) {
        super(deviceID, devicePosition, 200000);
    }
}
