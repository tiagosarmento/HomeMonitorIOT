# HomeMonitorIOT
## Disclaimer
The present software is open source, based on Android. This development was done as an hobby and a DIY project. Feel free to use on your own and improve it. Performance issues and unexpected behaviors may occur as development process is still ongoing.

## HomeMonitorIOT Project
The goal of this project is to create a home monitor system (HomeMonitorIOT) so that the user can easely have accesst to sensorial data of its home, directly on a mobile device or through a webpage.
HomeMonitorIOT is then based in three components:
- Embedded Hardware from Texas Instruments (Tiva C Series EK-TM4C1294XL)
- Cloud Data Service (https://ti.exosite.com/views/3304282916/3059609439)
- Android Application (this github page)

### 1.1 Embedded Hardware
Today's hardware has two components: a base board Tiva C Series EK-TM4C1294XL and a sensor board TI Sensor Hub BoosterPack (BOOSTXL-SENSHUB). The Tiva C Series EK-TM4C1294XL board, is used for low level tasks: acquire and process sensor data, and for a middleware task used to feed the cloud service with the processed sensor data.
For more details refer to HomeMonitorIOT Hardware page on github: PAGE.

### 1.2 Cloud Data Service
The Cloud Data Service used is the Exosite platform, developed for Texas Instruments flavor (https://ti.exosite.com). This platform already has the layers to easely attach the embedded hardware, and create data ports for the sensor data.
For more details refer to HomeMonitorIOT Cloud Data Service page on github: PAGE.

### 1.3 Android Application
The Android Application developed here can be seen as a viewer/client in all this home monitor system. The applciation holds all the connection points to fetch sensor data from Cloud Data Service. Data is fetched by using JSON and HTTP requests on specific Exosite data ports.

## HomeMonitorIOT Android Application
Describe details of this application...

## Change log
ToDo...

## Todo list:
- Add refresh option on graphs
