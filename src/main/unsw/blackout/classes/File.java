package unsw.blackout.classes;
/**
 * Represents a file class which stores the details of a specific file
 * @author Vicky Hu
 */
public class File {
    private String fileName;
    private String fileContent;
    private int fileSizeBytes;
    private boolean transferCompleted;
    private String entityID;

    /**
     * File constructor - for files created to a device
     * @param fileName
     * @param fileContent
     * @param entityID
     */
    public File(String fileName, String fileContent, String entityID) {
        this.fileName = fileName;
        this.fileContent = fileContent;
        setFileSizeBytes();
        this.transferCompleted = true;
        this.entityID = entityID;
    }

    /**
     * File constructor - for files which are sent from an existing entity to another
     * @param file
     */
    public File(File file) {
        this.fileName = file.fileName;
        this.fileContent = "";
        this.fileSizeBytes = file.fileSizeBytes;
        this.transferCompleted = false;
        this.entityID = file.entityID;
    }

    /**
     * Entity ID getter for which the full file is held 
     * @return String
     */
    public String getEntityID() {
        return entityID;
    }

    /**
     * Entity ID setter
     * @param entityID
     */
    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    /**
     * Transfer completed flag getter
     * @return boolean
     */
    public boolean getTransferCompleted() {
        return transferCompleted;
    }
    /**
     * Transfer completed flag setter
     * @params bool
     */
    public void setTransferCompleted(boolean bool) {
        transferCompleted = bool;
    }

    /**
     * File name getter
     * @return String
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * File content getter
     * @return String
     */
    public String getFileContent() {
        return fileContent;
    }

    /**
     * Add a section of the file content to the end of existing file content
     * @param fileContent
     */
    public void appendToFileContent(String fileContent) {
        this.fileContent += fileContent;
    }

    /**
     * File size getter in bytes
     * @return int
     */
    public int getFileSizeBytes() {
        return fileSizeBytes;
    }

    /**
     * File size setter in bytes
     */
    public void setFileSizeBytes() {
        int fileSize = this.fileContent.length();
        this.fileSizeBytes = fileSize;
    }

    /**
     * Get the length of file content
     * @return int
     */
    public int getContentLength() {
        return this.fileContent.length();
    }
}
