#!/bin/bash

cd /opt/concord4pi

java -Dtinylog.configuration=config/log.config -cp lib/jSerialComm-2.4.0.jar:concord4pi.jar:lib/org.eclipse.paho.client.mqttv3-1.1.1.jar:lib/tinylog-api-2.0.0-M2.1.jar:lib/tinylog-impl-2.0.0-M2.1.jar concord4pi.concord4pi
