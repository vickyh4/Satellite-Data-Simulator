package unsw.blackout.classes.satellites;

import unsw.utils.Angle;
/**
 * A subclass of satellite which represents a relay satellite which cannot store files but acts as an extension to other devices or satellites
 * @author Vicky Hu
 */
public class RelaySatellite extends Satellite {
    private static int MAX_SIGNAL_RANGE = 300000;
    private static int MAX_TRAVEL_SPEED = 1500;
    private int travelDirection;

    /**
     * Relay Satellite Constructor
     * @param satelliteID
     * @param position
     * @param height
     */
    public RelaySatellite(String satelliteID, Angle position, double height) {
        super(satelliteID, position, height, MAX_SIGNAL_RANGE, MAX_TRAVEL_SPEED);
        this.travelDirection = 1;
    }

    public int getTravelDirection() {
        return travelDirection;
    }

    public void setTravelDirection() {
        this.travelDirection = travelDirection * -1;
    }

}