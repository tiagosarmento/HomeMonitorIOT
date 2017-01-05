/**
 * Author: Tiago Sarmento Santos
 * Github: https://github.com/tiagosarmento/HomeMonitorIOT
 *
 * Software License Agreement
 * The present software is open-source and it is owned by this project contributors. Feel free to
 * use it on your own and to improve it for your needs. You may not combine this software with
 * "viral" open-source software in order to form a larger program. This software is being done as
 * an hobby and a DIY project. It is provided as is and with all possible faults associated.
 * The software contributors shall not, under any circumstances, be liable for special, incidental
 * or consequential damages for any reason whatsoever.
 */

package com.tiasan.homemonitoriot;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author Tiago Sarmento Santos
 * @class AlarmService
 * @desc This class extends IntentService, and it does the actual Alarm work.
 */
public class AlarmService extends IntentService {

    // Set Global data
    private static final String gTag    = "DBG - AlarmService";
    private String sTemperatureData     = null;
    private String sHumidityData        = null;
    private String sPressureData        = null;
    private String sAltitudeData        = null;
    private String sVisibleLightData    = null;
    private String sInfraredLightData   = null;
    private SettingsHandler gshSettings = null;

    /**
     * @author Tiago Sarmento Santos
     * @desc This is the AlarmService class constructor
     * @constructor AlarmService
     */
    public AlarmService() {
        // Set a string name, to be used to identify the Alarm in the system
        super("UpdateDataAlarmService");
    }

    /**
     * @author Tiago Sarmento Santos
     * @func onHandleIntent
     * @desc This function is called when the service is triggered, it does the Service work
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(gTag, "The onHandleIntent() event");
        // Create hook on application settings, private settings are needed to hold sensor data
        gshSettings = new SettingsHandler(this);
        // Get Sensor Data from Exosite Platform
        sTemperatureData   = getExoInstData(
                gshSettings.getSettingStringValue(getString(R.string.keyTemperaturePort)));
        sHumidityData      = getExoInstData(
                gshSettings.getSettingStringValue(getString(R.string.keyHumidityPort)));
        sPressureData      = getExoInstData(
                gshSettings.getSettingStringValue(getString(R.string.keyPressurePort)));
        sAltitudeData      = getExoInstData(
                gshSettings.getSettingStringValue(getString(R.string.keyAltitudePort)));
        sVisibleLightData  = getExoInstData(
                gshSettings.getSettingStringValue(getString(R.string.keyVisibleLightPort)));
        sInfraredLightData = getExoInstData(
                gshSettings.getSettingStringValue(getString(R.string.keyInfraredLightPort)));
        // Save sensor data into application private settings
        gshSettings.setStringValue(getString(R.string.keyTemperatureData),
                sTemperatureData.split("=")[1].replaceAll("\\n",""));
        gshSettings.setStringValue(getString(R.string.keyHumidityData),
                sHumidityData.split("=")[1].replaceAll("\\n",""));
        gshSettings.setStringValue(getString(R.string.keyPressureData),
                sPressureData.split("=")[1].replaceAll("\\n",""));
        gshSettings.setStringValue(getString(R.string.keyAltitudeData),
                sAltitudeData.split("=")[1].replaceAll("\\n",""));
        gshSettings.setStringValue(getString(R.string.keyVisibleLightData),
                sVisibleLightData.split("=")[1].replaceAll("\\n",""));
        gshSettings.setStringValue(getString(R.string.keyInfraredLightData),
                sInfraredLightData.split("=")[1].replaceAll("\\n",""));
        String sCurrDateTime = DateFormat.getDateTimeInstance().format(new Date());
        gshSettings.setStringValue(getString(R.string.keyLastUpdateTime),
                "Latest Update done at: " + sCurrDateTime);
        // Now that we got sensor data, issue a Notification if needed
        String sTempVal = gshSettings.getSettingStringValue(getString(R.string.keyTemperatureData));
        if ( 15 <= Float.parseFloat(sTempVal) || 20 >= Float.parseFloat(sTempVal) ) {
            issueNotification();
        }
        // Release the device WakeLock, this was locked by AlarmReceiver
        AlarmReceiver.completeWakefulIntent(intent);
    }

    /**
     * @author Tiago Sarmento Santos
     * @func getExoInstData
     * @desc This function gets from Exosite Platform the latest available value for a sensor
     * @param dataPort
     * @return sExoData
     */
    private String getExoInstData(String dataPort) {
        // Local Data
        InputStream isExoData = null;
        String      sExoData  = null;
        // Create URL for TI Exosite
        URL url_exosite = null;
        try {
            url_exosite = new URL("http://m2.exosite.com/onep:v1/stack/alias?" + dataPort);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // Create HTTP URL Connection for TI Exosite
        HttpURLConnection url_conn_exosite = null;
        try {
            url_conn_exosite = (HttpURLConnection) url_exosite.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Generic settings for HTTP request
        try {
            url_conn_exosite.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        // HTTP Request Header properties
        url_conn_exosite.addRequestProperty("X-Exosite-CIK", gshSettings.getSettingStringValue(getString(R.string.keyCIK)));
        url_conn_exosite.addRequestProperty("Accept", "application/x-www-form-urlencoded; charset=utf-8");
        // Connect to TI Exosite
        try {
            url_conn_exosite.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Get HTTP response with ExoData
        int retCode = 0;
        try {
            retCode = url_conn_exosite.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (retCode == HttpURLConnection.HTTP_OK) {
            try {
                isExoData = url_conn_exosite.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Convert the InputStream into a string
            //Reader reader = null;
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(isExoData));
            StringBuilder total = new StringBuilder();
            String line;
            try {
                while ((line = buffReader.readLine()) != null) {
                    total.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            sExoData = total.toString();
            Log.d(gTag, "The response is = " + sExoData);
        } else {
            Log.d(gTag, "The response code = " + retCode);
        }
        // Close HTTP URL connection
        url_conn_exosite.disconnect();
        return sExoData;
    }

    /**
     * @author Tiago Sarmento Santos
     * @func issueNotification
     * @desc This functions issues an alert Notification on device's task bar.
     */
    private void issueNotification() {
        // Set an ID for the notification, you can use this ID to update the notification later on
        int nID = 1;
        // Create NotificationBuilder
        NotificationCompat.Builder nNotiBuilder = new NotificationCompat.Builder(this);
        // Set Notification builder mandatory parameters
        nNotiBuilder.setSmallIcon(R.mipmap.ic_launcher);
        nNotiBuilder.setContentTitle("HomeMonitorIOT Alert");
        // Set other attributes
        nNotiBuilder.setAutoCancel(true);
        // Set the Alert case message
        String sTempVal = gshSettings.getSettingStringValue(getString(R.string.keyTemperatureData));
        if ( 15 <= Float.parseFloat(sTempVal) ) {
            nNotiBuilder.setContentText("Low Temperature registered: " + sTempVal + "°C");
        } else if ( 20 >= Float.parseFloat(sTempVal)) {
            nNotiBuilder.setContentText("High Temperature registered: " + sTempVal + "°C");
        } else {
            nNotiBuilder.setContentText("Generic weather alert!");
        }
        // Create Intent to call MainActivity of your app
        Intent iMainActivity = new Intent(this, MainActivity.class);
        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of your application
        // to the Home screen.
        TaskStackBuilder tsbBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        tsbBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        tsbBuilder.addNextIntent(iMainActivity);
        PendingIntent piMainActivity = tsbBuilder.getPendingIntent( 0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        nNotiBuilder.setContentIntent(piMainActivity);
        NotificationManager nmNotiManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nmNotiManager.notify(nID, nNotiBuilder.build());
    }
}