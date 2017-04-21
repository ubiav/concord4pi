# concord4pi
A java-based integration for interacting with a Concord Alarm System (SuperBus 2000 Automation Module)

This is the basic working code with a lot of work to add for usability (e.g. easy start, automated builds, etc.)  but for now, it will be good enough to share the code.

If you do the following, you should be able to run the code:

1) Make a JAR out of everything in the "src" directory.
2) Download the jSerialComm.jar file from http://fazecast.github.io/jSerialComm/
3) Edit the JAR to include the correct Serial Port (/dev/ttyAMA0 by default).  This file must be in the directory where you are executing the command.  My recommendation is:

      ..../concord4pi
      
          -> concord4pi.jar
          -> jSerialComm.jar
          -> config.properties
4) Start the program via: java -cp jSerialComm.jar:concord4pi.jar concord4pi.concord4pi
