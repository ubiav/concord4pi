# concord4pi
A java-based integration for interacting with a Concord Alarm System (SuperBus 2000 Automation Module)

Version: 0.3

Code Changes

The following changes were made:
 - Disabled MQTT client persistence.  This caused slowness and re-transmission that isn't necessary.  A new message from the SB2000 Controller will come quickly enough and will overwrite the existing value.  Guaranteeing delivery to the MQTT service provides more issues than it is worth.
 - Moved MQTT file persistence caching directory to /tmp/concord4pi.  As /tmp is generally a memdisk, this also speeds up the communication.
 - ISSUE #8 High CPU Utilization: Added sleep statements to allow the thread loops to pass control back to any other processes that need it.
 - ISSUE #7 Text Order Problem: Changed references of "areapartition" or "AreaPartition" to "partitionarea" or "PartitionArea" (respectively) to align with the order of the numerical value in the MQTT topics
 - Updated JAR file with updates and included library files in JAR.  This now does not require a long classpath listing to start the service.  See [start.sh](startup/start.sh) for an example of these changes.
    - Note: The JAVA_HOME and PATH statements in the start.sh file were also removed as these are highly system dependent.  In most cases, a properly installed Java JRE will already have these variables set, so it is not essential to reset them.

----

Version: 0.2

Code Changes

This is a complete re-write of the concord4pi interface bridge from the SuperBus2000 Automation Module to an IP Network.  Key changes from previous versions include:
 - Event-based actions for handling data from the serial port and sending messages through MQTT
 - Simplified / cleaned data structure for in-memory alarm state; state is built dynamically as messages are received from the automation module.
 - Simplified message processing for incoming serial data
 - Extensible interface to support additional outgoing message updates (e.g. can re-add the REST API easily, if needed)
 - MQTT topic structure simplified and allows improved discoverability

Requirements

This software requires an computer attached to the SB2000 via serial interface with network connectivity to an IPv4 network.  This has been tested on:
 - Raspberry Pi Zero W (should function on all other Raspberry Pi devices)
 - RS232 Serial to TTL Converter Board (hardwired to RPI Zero W GPIO serial pins)
