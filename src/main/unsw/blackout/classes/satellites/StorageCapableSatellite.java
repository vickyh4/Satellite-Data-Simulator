package unsw.blackout.classes.satellites;

import unsw.utils.Angle;

/**
 * A subclass of satellite which represents all satellites which are capable of storing and directly sending files.
 * @author Vicky Hu
 */
public class StorageCapableSatellite extends Satellite{
    protected int downloadSpeed;
    protected int uploadSpeed;
    protected int maxStorageCapacityBytes;
    protected int maxStorageCapacityFiles;
    protected int numberOfDownloading;
    protected int numberOfUploading;

    public StorageCapableSatellite(String satelliteID, Angle position, double height, int range, int speed, int upload, int download, int maxStorageCapacityBytes, int maxStorageCapacityFiles) {
        super(satelliteID, position, height, range, speed);
        this.uploadSpeed = upload;
        this.downloadSpeed = download;
        this.maxStorageCapacityBytes = maxStorageCapacityBytes;
        this.maxStorageCapacityFiles = maxStorageCapacityFiles;
    }

    protected void setMaxStorageCapacityFiles(int maxStorageCapacityFiles) {
        this.maxStorageCapacityFiles = maxStorageCapacityFiles;
    }

    protected void setDownloadSpeed(int downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    protected void setUploadSpeed(int uploadSpeed) {
        this.uploadSpeed = uploadSpeed;
    }

    protected void setMaxStorageCapacityBytes(int maxStorageCapacityBytes) {
        this.maxStorageCapacityBytes = maxStorageCapacityBytes;
    }

    public int getDownloadSpeed() {
        return downloadSpeed;
    }

    public int getUploadSpeed() {
        return uploadSpeed;
    }

    public int getMaxStorageCapacityBytes() {
        return maxStorageCapacityBytes;
    }

    public int getMaxStorageCapacityFiles() {
        return maxStorageCapacityFiles;
    }

    public int getNumberOfDownloading() {
        return numberOfDownloading;
    }

    public void addNumberOfDownloading(int numberOfDownloading) {
        this.numberOfDownloading += numberOfDownloading;
    }

    public int getNumberOfUploading() {
        return numberOfUploading;
    }

    public void addNumberOfUploading(int numberOfUploading) {
        this.numberOfUploading += numberOfUploading;
    }

    public int getUploadBandwidthPerFile() {
        return calcUploadBandwidthPerFile();
    }

    public int getDownloadBandwidthPerFile() {
        return calcDownloadBandwidthPerFile();
    }

    private int calcDownloadBandwidthPerFile() {
        int bandwidth;
        if (numberOfDownloading > 0) {
            bandwidth = downloadSpeed/numberOfDownloading;
        } else {
            bandwidth = downloadSpeed;
        }
        return bandwidth;
    }

    private int calcUploadBandwidthPerFile() {
        int bandwidth;
        if (numberOfUploading > 0) {
            bandwidth = uploadSpeed/numberOfUploading;
        } else {
            bandwidth = uploadSpeed;
        }
        return bandwidth;
    }

    public boolean hasFileStorageCapacity() {
        if (getTotalFileBytes() > maxStorageCapacityBytes) {
            return false;
        } 
        return true;
    }

    public boolean checkIfDownloadBandwidth() {
        if (numberOfDownloading <= downloadSpeed) {
            return true;
        }
        return false;
    }
    public boolean checkIfUploadBandwidth() {
        if (numberOfUploading <= uploadSpeed) {
            return true;
        }
        return false;
    }

    // public void subtractDownloadBandwidth(int x) {
    //     this.downloadBandwidthPerFile -= x;
    // }

    // public void addDownloadBandwidth(int x) {
    //     this.downloadBandwidthPerFile += x;
    // }

    // public void subtractUploadBandwidth(int x) {
    //     this.downloadBandwidthPerFile -= x;
    // }

    // public void addUpBandwidth(int x) {
    //     this.downloadBandwidthPerFile += x;
    // }

}
