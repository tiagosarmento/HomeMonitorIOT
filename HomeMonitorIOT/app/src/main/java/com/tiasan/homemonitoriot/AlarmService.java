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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

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
 * This {@code IntentService} does the app's actual work.
 * {@code SampleAlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class AlarmService extends IntentService {
    /**
     * AlarmService Constructor
     */
    public AlarmService() {
        super("UpdateDataAlarmService");
    }

    private static final String gTag = "DBG - AlarmService";

    // Set Global Exosite data
    private String gsISL29023_I = null;
    private String gsISL29023_V = null;
    private String gsBMP180_T   = null;
    private String gsBMP180_P   = null;
    private String gsBMP180_A   = null;
    private String gsBMP180_H   = null;

    @Override
    protected void onHandleIntent(Intent intent) {
        // BEGIN_INCLUDE(service_onhandle)
        Log.d(gTag, "The onHandleIntent() event");

        SettingsHandler gshSettings = new SettingsHandler(this);
        gshSettings.printSettings();

        // Get Light Data
        Log.d(gTag, "onHandleIntent(): Fetching Light data" );
        gsISL29023_I = getExoInstData(gshSettings.getSettingStringValue(getString(R.string.keyInfraredLightPort))); //"isl29023_infrared"
        gsISL29023_V = getExoInstData(gshSettings.getSettingStringValue(getString(R.string.keyVisibleLightPort))); // "isl29023_visible"
        // Get Temperature
        Log.d(gTag, "onHandleIntent(): Fetching Temperature data" );
        gsBMP180_T = getExoInstData(gshSettings.getSettingStringValue(getString(R.string.keyTemperaturePort))); // bmp180_tempc
        // Get Pressure
        Log.d(gTag, "onHandleIntent(): Fetching Pressure data" );
        gsBMP180_P = getExoInstData(gshSettings.getSettingStringValue(getString(R.string.keyPressurePort))); // bmp180_press
        // Get Altitude
        Log.d(gTag, "onHandleIntent(): Fetching Altitude data" );
        gsBMP180_A = getExoInstData(gshSettings.getSettingStringValue(getString(R.string.keyAltitudePort))); // bmp180_alt
        // Get Humidity
        Log.d(gTag, "onHandleIntent(): Fetching Humidity data" );
        gsBMP180_H = getExoInstData(gshSettings.getSettingStringValue(getString(R.string.keyHumidityPort))); // sht21_humid


        // Save sensor data in SharedPreferences
        gshSettings.setStringValue(getString(R.string.keyTemperatureData),
                gsBMP180_T.split("=")[1].replaceAll("\\n",""));
        gshSettings.setStringValue(getString(R.string.keyHumidityData),
                gsBMP180_H.split("=")[1].replaceAll("\\n",""));
        gshSettings.setStringValue(getString(R.string.keyPressureData),
                gsBMP180_P.split("=")[1].replaceAll("\\n",""));
        gshSettings.setStringValue(getString(R.string.keyAltitudeData),
                gsBMP180_A.split("=")[1].replaceAll("\\n",""));
        gshSettings.setStringValue(getString(R.string.keyVisibleLightData),
                gsISL29023_V.split("=")[1].replaceAll("\\n",""));
        gshSettings.setStringValue(getString(R.string.keyInfraredLightData),
                gsISL29023_I.split("=")[1].replaceAll("\\n",""));
        String sCurrDateTime = DateFormat.getDateTimeInstance().format(new Date());
        gshSettings.setStringValue(getString(R.string.keyLastUpdateTime),
                "Latest Update done at: " + sCurrDateTime);

        gshSettings.printSettings();
        // Release the wake lock provided by the BroadcastReceiver.
        AlarmReceiver.completeWakefulIntent(intent);
        // END_INCLUDE(service_onhandle)
    }

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
        url_conn_exosite.addRequestProperty("X-Exosite-CIK", "ed253edce4b1e6a0bcfd74e18d0397c592530000");
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
}