# concord4pi
A java-based integration for interacting with a Concord Alarm System (SuperBus 2000 Automation Module)

This is the basic working code with a lot of work to add for usability (e.g. easy start, automated builds, etc.)  but for now, it will be good enough to share the code.

If you do the following, you should be able to run the code:

1) Make a JAR out of everything in the "src" directory.
2) Download the jSerialComm.jar file from http://fazecast.github.io/jSerialComm/ or the lib directory in GITHUB
3) Download the Jetty JAR file see lib directory)
4) Download the PAHO Client JAR file (see lib directory)
5) Edit the config.properties to have the right settings for your set up. 
6) Start the program via: 
      java -Djava.util.logging.SimpleFormatter.format="%1\$tY-%1\$tm-%1\$td %1\$tH:%1\$tM:%1\$tS %4\$-6s %5\$s%6\$s%n" -cp lib/jSerialComm-1.3.11.jar:lib/jetty-all-9.4.5.v20170502-uber.jar:concord4pi.jar:lib/org.eclipse.paho.client.mqttv3-1.1.1.jar concord4pi.concord4pi
