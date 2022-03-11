package unsw.blackout.classes.devices;
import unsw.utils.Angle;
/**
 * Device of type Laptop Device
 * @author Vicky Hu
 */
public class LaptopDevice extends Device {
    /**
     * Laptop constructor
     * @param deviceID
     * @param devicePosition
     */
    public LaptopDevice(String deviceID, Angle devicePosition) {
        super(deviceID, devicePosition, 100000);
    }
}
