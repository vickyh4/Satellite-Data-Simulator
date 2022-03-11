package unsw.blackout.classes.devices;

import unsw.blackout.classes.Entity;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;
/**
 * Device super class to contain all devices
 * @author Vicky Hu
 */
public class Device extends Entity {
    /**
     * Device constructor
     * @param deviceID
     * @param position
     * @param range
     */
    public Device (String deviceID, Angle position, int range) {
        super(deviceID, position, range);
        setHeight(MathsHelper.RADIUS_OF_JUPITER);
    }
}
