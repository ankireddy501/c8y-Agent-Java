# Repository Contents
This repository contains an example Java Agent with support for Raspberry Pi, Linux, Windows and Mac systems. 
It consists of the following modules: 

 * jv-driver: Interface classes for writing hardware drivers and implementing new functionality.
 * jv-agent: The main executable agent including basic device management, should work on all Java platforms.
 * rpi-driver: Hardware driver for the Raspberry Pi.
 # parking-pi-driver : driver for c8y parkingpi.
 * kontron-driver: Hardware driver for 
 * mac-driver: Hardware driver for Mac OS X systems.
 * generic-linux-driver: Hardware driver for linux systems lacking /proc/cpuinfo. It uses the MAC address to register the device.
 * win-driver: Hardware driver for Windows systems.
 * piface-driver: A simple Piface integration.
 * tinkerforge-driver: Support for Tinkerforge bricks. 
 * assembly: Base packaging for all environments.
 * packages: Environment specific packaging.
 
# Parking-pi-driver
  *ParkingPiDriver.Java*
   To Run this Driver Apart from the above files You have add Google Simple Json dependency jar to tha POM.XML
   The main Java class is parking-pi-driver\src\main\java\com\softwareag\parkingpi\ParkingPiDriver.java which updates the parking pi's name, position and parking pi's Status which is not set by the c8y raspberry-pi agent(sets only for the first time of running from the Parking pi.json File which has been at the location /home/pi/Desktop/c8y/) 
   It also creates the ChildDevices(Basically the sensors) based on the Array size of sensors key in same Json file and copy the sensors array with updated with the SystemID(from c8y) and generate sendmeasure.Json file in same directory.
   The location of parkingpi.json be anywhere in pi but you have to update the file location in The class com\softwareag\parkingpi\PiProperties.java
