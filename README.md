# Table of Contents  
[Software License Agreement](#software-license-agreement)
<a name="Software License Agreement"/>

[Home Monitor IOT Project](#homemonitoriot-project)
<a name="HomeMonitorIOT Project"/>

[1.1 Embedded Hardware](#11-embedded-hardware)
<a name="1.1 Embedded Hardware"/>

[1.2 Cloud Data Service](#12-cloud-data-service)
<a name="1.2 Cloud Data Service"/>

[1.3 Android Application](#13-android-application)
<a name="1.3 Android Application"/>

[HomeMonitorIOT Android Application](#homemonitoriot-android-application)
<a name="HomeMonitorIOT Android Application"/>

[Changelog](#changelog)
<a name="Changelog"/>

[ToDo list](#todo-list)
<a name="ToDo list"/>

# Software License Agreement
The present software is open-source and it is owned by this project contributers. Feel free to use it on your own and to improve it for your needs.
You may not combine this software with "viral" open-source software in order to form a larger program.
This software is being done as an hobby and a DIY project. It is provided as is and with all possible faults associated. 
The software contributers shall not, under any circumstances, be liable for special, incidental or consequential damages for any reason whatsoever.

# HomeMonitorIOT Project
The goal of this project is to create a home monitor system (HomeMonitorIOT) so that the user can easely have access to sensorial data of its home, directly on a mobile device or through a webpage.
HomeMonitorIOT is then based in three components:
- Embedded Hardware from Texas Instruments (Tiva C Series EK-TM4C1294XL)
- Cloud Data Service (https://ti.exosite.com/views/3304282916/3059609439)
- Android Application (this github page)

Despite the usage of a Tiva C Series EK-TM4C1294XL, this is a completly decoupled system. It means that you can use any hardware you want, in the way you want to get the sensor data. The only constraint here is that your data must be published on Exosite platform, but not necessarly on the Texas Instruments flavor, as the Android application will fetch data from there (as explained later).

## 1.1 Embedded Hardware
Today's hardware has two components: a base board Tiva C Series EK-TM4C1294XL and a sensor board TI Sensor Hub BoosterPack (BOOSTXL-SENSHUB). The Tiva C Series EK-TM4C1294XL board, is used for low level tasks: acquire and process sensor data, and for a middleware task used to feed the cloud service with the processed sensor data.
For more details refer to HomeMonitorIOT Hardware page on github: PAGE.

## 1.2 Cloud Data Service
The Cloud Data Service used is the Exosite platform, developed for Texas Instruments flavor (https://ti.exosite.com). This platform already has the layers to easely attach the embedded hardware, and create data ports for the sensor data.
For more details refer to HomeMonitorIOT Cloud Data Service page on github: PAGE.

## 1.3 Android Application
The Android Application developed here can be seen as a viewer/client in all this home monitor system. The applciation holds all the connection points to fetch sensor data from Cloud Data Service. Data is fetched by using JSON and HTTP requests on specific Exosite data ports.

# HomeMonitorIOT Android Application
1. System Overview

Global picture of this system...

2. Features/ Data Ports supported

Exosite available data ports...

3. Android Activities

Actions that can be done with the application

4. Appplication Configuration

How to setup application: defaults fetch my own exosite, how user can set their values...

5. Screenshots

Some pictures of the activities...

# Changelog
ToDo...

# ToDo list
- Add refresh option on graphs
