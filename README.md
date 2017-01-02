# Table of Contents
&nbsp;&nbsp;[1. Software License Agreement](#1-software-license-agreement)
<a name="1. Software License Agreement"/>  
&nbsp;&nbsp;[2. Home Monitor IOT Project](#2-homemonitoriot-project)
<a name="2. HomeMonitorIOT Project"/>  
&nbsp;&nbsp;&nbsp;&nbsp;[2.1 Embedded Hardware](#21-embedded-hardware)
<a name="2.1 Embedded Hardware"/>  
&nbsp;&nbsp;&nbsp;&nbsp;[2.2 Cloud Data Service](#22-cloud-data-service)
<a name="2.2 Cloud Data Service"/>  
&nbsp;&nbsp;&nbsp;&nbsp;[2.3 Android Application](#23-android-application)
<a name="2.3 Android Application"/>  
&nbsp;&nbsp;[3. HomeMonitorIOT Android Application](#3-homemonitoriot-android-application)
<a name="3. HomeMonitorIOT Android Application"/>  
&nbsp;&nbsp;[4. Changelog](#4-changelog)
<a name="4. Changelog"/>  
&nbsp;&nbsp;[5. ToDo list](#5-todo-list)
<a name="5. ToDo list"/>

# 1. Software License Agreement
The present software is open-source and it is owned by this project contributers. Feel free to use it on your own and to improve it for your needs.
You may not combine this software with "viral" open-source software in order to form a larger program.
This software is being done as an hobby and a DIY project. It is provided as is and with all possible faults associated. 
The software contributers shall not, under any circumstances, be liable for special, incidental or consequential damages for any reason whatsoever.

# 2. HomeMonitorIOT Project
The goal of this project is to create a home monitor system (HomeMonitorIOT) so that the user can easely have access to sensorial data of its home, directly on a mobile device or through a webpage.
HomeMonitorIOT is then based in three components:
- Embedded Hardware from Texas Instruments (Tiva C Series EK-TM4C1294XL)
- Cloud Data Service (https://ti.exosite.com/views/3304282916/3059609439)
- Android Application (this github page)

Despite the usage of a Tiva C Series EK-TM4C1294XL, this is a completly decoupled system. It means that you can use any hardware you want, in the way you want to get the sensor data. The only constraint here is that your data must be published on Exosite platform, but not necessarly on the Texas Instruments flavor, as the Android application will fetch data from there (as explained later).

## 2.1 Embedded Hardware
Today's hardware has two components: a base board Tiva C Series EK-TM4C1294XL and a sensor board TI Sensor Hub BoosterPack (BOOSTXL-SENSHUB). The Tiva C Series EK-TM4C1294XL board, is used for low level tasks: acquire and process sensor data, and for a middleware task used to feed the cloud service with the processed sensor data.
For more details refer to HomeMonitorIOT Hardware page on github: PAGE.

## 2.2 Cloud Data Service
The Cloud Data Service used is the Exosite platform, developed for Texas Instruments flavor (https://ti.exosite.com). This platform already has the layers to easely attach the embedded hardware, and create data ports for the sensor data.
For more details refer to HomeMonitorIOT Cloud Data Service page on github: PAGE.

## 2.3 Android Application
The Android Application developed here can be seen as a viewer/client in all this home monitor system. The applciation holds all the connection points to fetch sensor data from Cloud Data Service. Data is fetched by using JSON and HTTP requests on specific Exosite data ports.

# 3. HomeMonitorIOT Android Application
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

# 4. Changelog
ToDo...

# 5. ToDo list
- Handle errors
- Add refresh option on ActiobBar for graphs
- Add homescreen notifications for: low temperature, high temperature, weather degradation based on pressure measurements
- Clean and refactor code as needed
