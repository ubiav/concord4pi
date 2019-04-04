# concord4pi
A java-based integration for interacting with a Concord Alarm System (SuperBus 2000 Automation Module)

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
