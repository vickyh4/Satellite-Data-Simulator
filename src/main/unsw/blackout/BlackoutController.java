package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unsw.blackout.classes.devices.*;
import unsw.blackout.classes.satellites.*;
import unsw.blackout.FileTransferException.*;
import unsw.blackout.classes.Entity;
import unsw.blackout.classes.File;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

/**
 * Functions which run the functionality of the software
 * @author Vicky Hu
 */
public class BlackoutController {
    // HashMap containing all the registered entities
    private HashMap<String, Entity> entities = new HashMap<String, Entity>();

    /**
     * Create a new device
     * @param deviceId
     * @param type
     * @param position
     */
    public void createDevice(String deviceId, String type, Angle position) {
        Device device;
        if (type.equals("HandheldDevice")) {
            device = new HandheldDevice(deviceId, position);
        } else if (type.equals("LaptopDevice")) {
            device = new LaptopDevice(deviceId, position);
        } else { // DesktopDevice
            device = new DesktopDevice(deviceId, position);
        }
        entities.put(deviceId, device);
    }

    /**
     * Removes a device from entities HashMap
     */
    public void removeDevice(String deviceId) {
        entities.remove(deviceId);
    }

    /**
     * Create a new satellite
     * @param satelliteId
     * @param type
     * @param height
     * @param position
     */
    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        Satellite satellite;
        if (type.equals("StandardSatellite")) {
            satellite = new StandardSatellite(satelliteId, position, height);
        } else if (type.equals("ShrinkingSatellite")) {
            satellite = new ShrinkingSatellite(satelliteId, position, height);
        } else { // RelaySatellite
            satellite = new RelaySatellite(satelliteId, position, height);
        }
        entities.put(satelliteId, satellite);
    }
    
    /**
     * Remove a satellite from Entities
     * @param String 
     */
    public void removeSatellite(String satelliteId) {
        entities.remove(satelliteId);
    }

    /**
     * Get a list of all device entity ids
     * @return List<String>
     */
    public List<String> listDeviceIds() {
        List<String> deviceIds = new ArrayList<>();
        for (String key : entities.keySet()) {
            if (entities.get(key) instanceof Device) {
                deviceIds.add(key);
            }
        }
        return deviceIds;
    }

    /**
     * List of all satellite Ids
     * @return List<String>
     */
    public List<String> listSatelliteIds() {
        List<String> satelliteIds = new ArrayList<>();
        for (String key : entities.keySet()) {
            if (entities.get(key) instanceof Satellite) {
                satelliteIds.add(key);
            }
        }
        return satelliteIds;
    }

    /**
     * Adds a new file to a device
     * @param deviceId
     * @param filename
     * @param content
     */
    public void addFileToDevice(String deviceId, String filename, String content) {
        File newFile = new File(filename, content, deviceId);
        entities.get(deviceId).addFileToStoredFiles(newFile);
    }

    /**
     * Get the information about a certain entity
     * @param id
     * @return EntityInfoResponse
     */
    public EntityInfoResponse getInfo(String id) {
        if (entities.get(id) != null) {
            Entity entity = entities.get(id);
            Map<String, FileInfoResponse> files = new HashMap<String, FileInfoResponse>();
            for (File f : entity.getStoredFiles().values()) {
                FileInfoResponse fIResponse = new FileInfoResponse (f.getFileName(), f.getFileContent(), f.getFileSizeBytes(), f.getTransferCompleted());
                files.put(f.getFileName(), fIResponse);
            }
            EntityInfoResponse eIResponse = new EntityInfoResponse(entity.getgUID(), entity.getPosition(), entity.getHeight(), entity.getClass().getSimpleName(), files);
            return eIResponse;
        }
        return null;
    }

    /**
     * Helper function for simulate to get the correct upload chunk for file uploading
     * @param file
     * @param fromEntity
     * @param toEntity
     */
    public void simulateFileTransferTick(File file, Entity fromEntity, Entity toEntity) {
        File senderFile = fromEntity.getStoredFiles().get(file.getFileName());
        int fromIndex = file.getContentLength();
        
        if (fromEntity instanceof StorageCapableSatellite) {
            StorageCapableSatellite fromSatellite = (StorageCapableSatellite)fromEntity;
            int toIndex = file.getContentLength() + fromSatellite.getUploadBandwidthPerFile();
            if (toIndex >= senderFile.getContentLength()) {
                toIndex = senderFile.getContentLength();
            }
            file.appendToFileContent(senderFile.getFileContent().substring(fromIndex, toIndex));
        } else if (fromEntity instanceof Device) {
            StorageCapableSatellite fromSatellite = (StorageCapableSatellite)toEntity;
            int toIndex = file.getContentLength() + fromSatellite.getDownloadBandwidthPerFile();
            if (toIndex >= senderFile.getContentLength()) {
                toIndex = senderFile.getContentLength();
            }
            file.appendToFileContent(senderFile.getFileContent().substring(fromIndex, toIndex));
        }
            // check if file is complete
        if (file.getFileContent().length() >= file.getFileSizeBytes()) {
            file.setTransferCompleted(true);
            file.setEntityID(toEntity.getgUID());
        }
    }

    /**
     * Simulate is a function which makes the software tick for one minute and plays out the correct actions
     */
    public void simulate() {
        for (Entity entity : entities.values()) {
            
            if (entity instanceof Satellite) {
                // If the entity is a satellite then increment the position
                Satellite satellite = (Satellite)entity;
                satellite.incrementSatellitePosition();
            }

            for (File file : entity.getStoredFiles().values()) {
                // Find all files which are partially uploaded

                if (file.getTransferCompleted() == false) {
                    // If the file has not been completely transfered then find a sender using the file entity id
                    Entity sender = entities.get(file.getEntityID());
                    List<String> visibleEntities = communicableEntitiesInRange(entity.getgUID());
                    if (visibleEntities.contains(sender.getgUID())) {
                        // Send file is the sender is still within range
                        simulateFileTransferTick(file, sender, entity);
                    } else {
                        // If the sender is no longer in range try to find a relay satellite which is in range
                        int simulateSent = 0;
                        for (String entityID : visibleEntities) {
                            if (entities.get(entityID) instanceof RelaySatellite) {
                                List<String> visibleViaRelay = communicableEntitiesInRange(entityID);
                                if (visibleViaRelay.contains(sender.getgUID())) {
                                    simulateSent = 1;
                                    // Send file as if it was sending from the sender to receiver directly
                                    simulateFileTransferTick(file, sender, entity);
                                }
                            }
                        }
                        if (simulateSent == 0) {
                            // No segment was sent because the sender and receiver are not in range + not in range of relay
                            entity.deleteFileFromStoredFiles(file.getFileName());
                        }
                    }
                    // check if max file bytes is now above limit for entity
                }
            }
        } 
    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }
    /**
     * Finds all entities other than itself which is in communicable range
     * @param id
     * @return List<String>
     */
    public List<String> communicableEntitiesInRange(String id) {
        List<String> communicableEntities = new ArrayList<String>();
        if (entities.get(id) != null){
            // Check entity id exists
            Entity entity = entities.get(id);
            for (Entity e : entities.values()) {
                // Loop through all entities
                if (e instanceof Device || entity instanceof Device) {
                    // Instance where one of the two are a device and the other is a satellite
                    double temp = MathsHelper.getDistance(entity.getHeight(), entity.getPosition(), e.getPosition());
                    if (e instanceof Device && MathsHelper.isVisible(entity.getHeight(), entity.getPosition(), e.getPosition())) {
                        // Checks that the two are visible to eachother
                        if (temp <= entity.getRange() && temp <= e.getRange()) {
                            // Checks that the two are within range
                            communicableEntities.add(e.getgUID());
                        } 
                    } else if (MathsHelper.isVisible(e.getHeight(), e.getPosition(), entity.getPosition())) {
                        // Checks that the two are visible to eachother
                        if (temp <= entity.getRange() && temp <= e.getRange()) {
                            // Checks that the two are within range
                            communicableEntities.add(e.getgUID());
                        } 
                    }
                } else if (e.getgUID() != id && MathsHelper.isVisible(entity.getHeight(), entity.getPosition(), e.getHeight(), e.getPosition())) {
                    // For the case of a satellite to satellite comparison
                    double temp = MathsHelper.getDistance(entity.getHeight(), entity.getPosition(), e.getHeight(), e.getPosition());
                    if (temp <= entity.getRange() && temp <= e.getRange()) {
                        // Check if they are both visible and within range
                        communicableEntities.add(e.getgUID());
                    }
                }
            }
        }
        return communicableEntities;
    }

    /**
     * Send file from one entity to another
     * @param fileName
     * @param fromId
     * @param toId
     * @throws FileTransferException
     */
    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        // If the fromId is a device
        if (entities.get(fromId) != null) {
            Entity fromEntity = entities.get(fromId);
            File fileToSend = fromEntity.getStoredFiles().get(fileName);

            // Throw exception if fileName is not found in fromDevice
            if (fileToSend == null) {
                throw new VirtualFileNotFoundException("File not found");
            } 
            if (entities.get(toId) != null) {
                // Check if the receiving device or satellite id is valid
                Entity toEntity = entities.get(toId);
                if (toEntity.getStoredFiles().get(fileName) != null) {
                    // Check if the file already exists inside the toEntity
                    throw new VirtualFileAlreadyExistsException("File already exists");
                } else if (toEntity instanceof RelaySatellite) {
                    // Check if the file is trying to send to relay
                    throw new FileTransferException("Cannot send file to relay satellite");
                }
                File newFile = new File(fileToSend);
                if (fromEntity instanceof StorageCapableSatellite) {
                    // increment the upload bandwidth of satellite
                    // if (file:: satellite -~file~-> device/satellite)

                    StorageCapableSatellite fromSatellite = (StorageCapableSatellite)fromEntity;

                    if (!fromSatellite.checkIfUploadBandwidth()) {
                        // Throw exception if no upload bandwidth in satellite
                        throw new VirtualFileNoBandwidthException(fromEntity.getgUID() + " has insufficient upload bandwidth\n");
                    }
                    if (toEntity instanceof Device || toEntity instanceof StorageCapableSatellite) {
                        // If desination is standard satellite, use upload speed of standard
                        // satellite -> if destination entity is a device, use upload bandwidth from satellite
                        if (toEntity instanceof StorageCapableSatellite) {
                            // If the file destination is a storage capable satellite
                            StorageCapableSatellite toSatellite = (StorageCapableSatellite)toEntity;
                            // throw exception if not enough download bandwidth in satellite
                            if (!toSatellite.checkIfDownloadBandwidth()) {
                                // Check download bandwidth
                                throw new VirtualFileNoBandwidthException(toEntity.getgUID() + " has insufficient download bandwidth\n"); 
                            }
                            toSatellite.addFileToStoredFiles(newFile);
                            fromSatellite.addNumberOfUploading(1);
                            toSatellite.addNumberOfDownloading(1);
                            
                        } else { // If the file destination is a device
                            Device toDevice = (Device)toEntity;
                            toDevice.addFileToStoredFiles(newFile);
                            fromSatellite.addNumberOfUploading(1);
                        }
                    }
                } else if (fromEntity instanceof Device) {
                    if (toEntity instanceof StorageCapableSatellite) {
                        // device -> if desination entity is a satellite, use download bandwidth form satellite
                        StorageCapableSatellite sCSatellite = (StorageCapableSatellite)toEntity;
                        if (!sCSatellite.checkIfDownloadBandwidth()) {
                            throw new VirtualFileNoBandwidthException(toEntity.getgUID() + " has insufficient download bandwidth\n"); 
                        }
                        sCSatellite.addFileToStoredFiles(newFile);
                        sCSatellite.addNumberOfDownloading(1);
                    }
                }
            }
        }
    }
}
