# Explanation of Blackout asssignment design decisions for Vicky Hu z5255592 COMP2511 

# Inheritance Structure

My implementation contained multiple tiers of inheritance. When considering when to group up certain classes by inheritance, the main consideration was how many similar properties did the classes have. For example in the case of 'StorageCapableSatellites' I realised that Shrinking and Standard satellites had many similar properties that I could take advantage of e.g. transfer speeds, storage capacity, etc. By structuring our inheritance structure the way that is depicted in design.pdf, I was able to greatly simplify the implementation of Blackout, effectively reducing duplicate code and code run time. 

# How file transfer works

When a file is sent from one Entity to another Entity, after necessary checks for exceptions is complete a new File is created with the same details as the original, aside from having an empty 'Content' string. This includes the original 'entityID' of the sender of the file which will be updated upon completion of the transfer.

When 'simulate()' is called, the file will be found via its 'transferCompleted' property shows it is not yet a complete file. Using the entityID property, the program will look for the original file, and after checking if the sender is still in range, will append the next few bytes of content to the file according to the calculated bandwidth of the sneder/ receiver. 

This process will continue until 1) the sender/receiver go out of range of eachother or 2) the file is completed, at which point the file's transfer status is set to complete and the entityID is updated to the new owner of the file. 

# Class Explanations

## File
Files contain their name, content, size, entityID (denoting who owns the file), and transferCompleted (denoting whether or not a file has finished transferring).
Files contain entityID so that when simulate is called, files content that have not finished transferring can be loaded from the original file owner's file list. 

## Entity

Entity is a parent class that exists to encapsulate devices and satellites. I have chosen to structure inheritance this way because all
'entities' such as satellites and devices ahve some similar properties including ability to storeFiles (exception of RelaySatellite), a communicable 'range',
a height, and a position. Id's also having to be globally unique.

### Satellite
 
Satellites encapsulate all satellite types (Shrinking, Standard, Relay). This class adds Travel speed to entities to denote the speed they orbit Jupiter.

#### StorageCapableSatellite

Storage  capable satellites are satellites that have the ability to  store and hence send files to other satellites and devices. I decided creating another layer of inheritance due to the similarities between Shrinking and Standard satellites (having transfer speed, max capacities, etc.). Creating this class made implementation in the controller much simpler, requiring far less type checking.

### Device

To encapsulate the different types of devices in the system. All devices are virtually the same other than a defined constant 'Max_signal_range' showing the maximum range at which they can transfer files. Having different classes for each made type checking simpler and code more readable.
