package unsw.blackout.classes.devices;
import unsw.utils.Angle;
/**
 * Device of type Handheld Device
 * @author Vicky Hu
 */
public class HandheldDevice extends Device {
    /**
     * Handheld constructor
     * @param deviceID
     * @param devicePosition
     */
    public HandheldDevice(String deviceID, Angle devicePosition) {
        super(deviceID, devicePosition, 50000);
    }
}
