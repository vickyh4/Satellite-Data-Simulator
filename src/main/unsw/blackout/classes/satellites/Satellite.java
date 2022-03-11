package unsw.blackout.classes.satellites;

import unsw.blackout.classes.Entity;
import unsw.utils.Angle;
/**
 * Satellite is a super class which houses all types of satellites
 * @author Vicky Hu
 */
public class Satellite extends Entity {
    private int travelSpeed;

    /**
     * Satellite constructor which sets the height and travel speed of the satellite
     * @param satelliteID
     * @param position
     * @param height
     * @param range
     * @param speed
     */
    public Satellite (String satelliteID, Angle position, double height, int range, int speed) {
        super(satelliteID, position, range);
        this.travelSpeed = speed;
        setHeight(height);
    }

    /**
     * @return travelSpeed
     */
    public int getTravelSpeed() {
        return travelSpeed;
    }

    /**
    *  This functions sets the new satellite position after a simulate run. 
    *  It uses the satellites travel speed to calculate its new position.
    */
    public void incrementSatellitePosition() {
        double velocity = (double)this.travelSpeed;
        int direction = 1;
        Angle zero = Angle.fromDegrees(360);
        Angle newAngle;
        if (this instanceof RelaySatellite) {
            // Condition for relay satellite movement
            Angle threshold = Angle.fromDegrees(345);
            Angle smallRange = Angle.fromDegrees(140);
            Angle largeRange = Angle.fromDegrees(190);

            RelaySatellite relaySatellite = (RelaySatellite) this;
            direction = relaySatellite.getTravelDirection();
            newAngle = this.getPosition().add(Angle.fromRadians(direction*velocity/this.getHeight()));
            if (this.getPosition().toDegrees() >= smallRange.toDegrees() && largeRange.toDegrees() >= this.getPosition().toDegrees()) {
                // If Relay satellite is inside the correct range of 190 - 140 degrees. 
                // Within this range, if the next incremented angle is outside of the range then change the direction of the satellite

                if (newAngle.toDegrees() <= smallRange.toDegrees() || newAngle.toDegrees() >= largeRange.toDegrees()) {
                    relaySatellite.setTravelDirection();
                }
                direction = relaySatellite.getTravelDirection();
            } else {
                // If the Relay satellite is between 190 and 345 degrees then switch direction to -1.
                if (this.getPosition().toDegrees() <= threshold.toDegrees() && this.getPosition().toDegrees() > largeRange.toDegrees()) {
                    direction = -1;
                }
            }
            
        }
        newAngle = this.getPosition().add(Angle.fromRadians(direction*velocity/this.getHeight()));
        if (newAngle.toDegrees() >= zero.toDegrees()) {
            // Check if the new angle is above 360 degrees
            newAngle = Angle.fromDegrees(newAngle.toDegrees()-360);
        }
        // Set new Angle position
        this.setPosition(newAngle);
    }
    
}