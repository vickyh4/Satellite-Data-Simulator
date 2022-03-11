package unsw.blackout.classes;

import java.util.HashMap;

import unsw.blackout.classes.satellites.ShrinkingSatellite;
import unsw.utils.Angle;
/**
 * Entity is the highest level super class which stores information on all devices and satellites.
 * @author Vicky Hu
 */
public class Entity {
    private String gUID;
    private HashMap <String, File> storedFiles;
    private double height;
    private Angle position;
    private int range;

    /**
     * Entity Constructor
     * @param guid
     * @param position
     * @param range
     */
    public Entity (String guid, Angle position, int range) {
        this.storedFiles = new HashMap<String, File>();
        this.gUID = guid;
        this.position = position;
        this.range = range;
    }
    /**
     * Range getter
     * @return range
     */
    public int getRange() {
        return range;
    }
    /**
     * Range setter
     * @param range
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * File details getter
     * @param fileDetails
     */
    public void addFileToStoredFiles(File fileDetails) {
        storedFiles.put(fileDetails.getFileName(), fileDetails);
    }
    
    /**
     * Delete a stored file from entity  
     * @param fileName
     */
    public void deleteFileFromStoredFiles(String fileName) {
        storedFiles.remove(fileName);
    }
    
    /**
     * Returns a hashmap of an entity's stored files
     * @return HashMap <String, File>
     */
    public HashMap <String, File> getStoredFiles () {
        return this.storedFiles;
    }

    /**
     * Entity ID getter
     * @return gUID
     */
    public String getgUID() {
        return gUID;
    }

    /**
     * Gets the total number of file bytes currently stored inside entity 
     * @return sum
     */
    public int getTotalFileBytes() {
        int sum = 0;

        for (File file : storedFiles.values()) {
            int fileBytes = file.getFileSizeBytes();
            if (this instanceof ShrinkingSatellite && file.getFileContent().regionMatches(true, 0, "quantum", 0, 6)) {
                fileBytes = fileBytes *(2/3);
            }
            sum += fileBytes;
        }
        return sum;
    }

    /**
     * File count getter
     * @return storedFiles.size()
     */
    public int getTotalFileCount() {
        return storedFiles.size();
    }

    /**
     * Height getter
     * @return Height
     */
    public double getHeight() {
        return height;
    }
    /**
     * Height setter
     * @params Height
     */
    public void setHeight(double height) {
        this.height = height;
    }
    /**
     * Position getter
     * @return Position
     */
    public Angle getPosition() {
        return position;
    }
    /**
     * Position Setter
     * @params Position
     */
    public void setPosition(Angle position) {
        this.position = position;
    }
}
