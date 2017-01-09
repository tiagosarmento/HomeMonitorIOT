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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

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
* @class SensorDataHandler
* @desc This class is used to handle the sensor data from Exosite Platform
        */
public class SensorDataHandler {
    // Set Global data
    private static final String gTag            = "DBG - SensorDataHandler";
    private SettingsHandler     gshSettings     = null;
    private Context             gContext        = null;
    private Boolean             bProgressDialog = false;

    /**
     * @param cContext
     * @author Tiago Sarmento Santos
     * @func SensorDataHandler Constructor
     */
    SensorDataHandler(Context cContext, Boolean enableProgressDialog) {
        this.gContext = cContext;
        // Create hook on application settings, private settings are needed to hold sensor data
        this.gshSettings = new SettingsHandler(cContext);
        // Enable or Disable the ProgressDialog pop-up
        this.bProgressDialog = enableProgressDialog;
    }

    /**
     * @author Tiago Sarmento Santos
     * @func updateSensorData
     * @desc This function is used to get the latest sensor data available on Exosite Platform
     */
    public void updateSensorData() {
        UpdateSensorDataAsyncTask atkSensorData = new UpdateSensorDataAsyncTask();
        atkSensorData.setParameters(this.gContext, this.gshSettings, this.bProgressDialog);
        atkSensorData.execute();
    }

    /**
     * @author Tiago Sarmento Santos
     * @class UpdateSensorDataAsyncTask
     * @desc This is the asynchronous task to fetch sensor data from Exosite Platform
     */
    private class UpdateSensorDataAsyncTask extends AsyncTask<String, Integer, Double> {

        // Set Global data
        private String          sTemperatureData   = null;
        private String          sHumidityData      = null;
        private String          sPressureData      = null;
        private String          sAltitudeData      = null;
        private String          sVisibleLightData  = null;
        private String          sInfraredLightData = null;
        private SettingsHandler gshSettings        = null;
        private Context         gContext           = null;
        private ProgressDialog  pdUpdateData       = null;
        private Boolean         bProgressDialog    = false;

        /**
         * @author Tiago Sarmento Santos
         * @func getExoInstData
         * @desc This function does the asynchronous task work in background
         * @param params
         * @return
         */
        @Override
        protected Double doInBackground(String... params) {
            // Get Sensor Data from Exosite Platform
            sTemperatureData   = getSensorData(
                    gshSettings.getSettingStringValue(
                            this.gContext.getString(R.string.keyTemperaturePort)));
            sHumidityData      = getSensorData(
                    gshSettings.getSettingStringValue(
                            this.gContext.getString(R.string.keyHumidityPort)));
            sPressureData      = getSensorData(
                    gshSettings.getSettingStringValue(
                            this.gContext.getString(R.string.keyPressurePort)));
            sAltitudeData      = getSensorData(
                    gshSettings.getSettingStringValue(
                            this.gContext.getString(R.string.keyAltitudePort)));
            sVisibleLightData  = getSensorData(
                    gshSettings.getSettingStringValue(
                            this.gContext.getString(R.string.keyVisibleLightPort)));
            sInfraredLightData = getSensorData(
                    gshSettings.getSettingStringValue(
                            this.gContext.getString(R.string.keyInfraredLightPort)));
            return null;
        }

        /**
         * @author Tiago Sarmento Santos
         * @func onPostExecute
         * @desc This functions is called when the asynchronous task work is finished
         *       after doInBackground()
         * @param result
         */
        protected void onPostExecute(Double result){
            // Save sensor data into application private settings
            gshSettings.setStringValue(this.gContext.getString(R.string.keyTemperatureData),
                    sTemperatureData.split("=")[1].replaceAll("\\n",""));
            gshSettings.setStringValue(this.gContext.getString(R.string.keyHumidityData),
                    sHumidityData.split("=")[1].replaceAll("\\n",""));
            gshSettings.setStringValue(this.gContext.getString(R.string.keyPressureData),
                    sPressureData.split("=")[1].replaceAll("\\n",""));
            gshSettings.setStringValue(this.gContext.getString(R.string.keyAltitudeData),
                    sAltitudeData.split("=")[1].replaceAll("\\n",""));
            gshSettings.setStringValue(this.gContext.getString(R.string.keyVisibleLightData),
                    sVisibleLightData.split("=")[1].replaceAll("\\n",""));
            gshSettings.setStringValue(this.gContext.getString(R.string.keyInfraredLightData),
                    sInfraredLightData.split("=")[1].replaceAll("\\n",""));
            String sCurrDateTime = DateFormat.getDateTimeInstance().format(new Date());
            gshSettings.setStringValue(this.gContext.getString(R.string.keyLastUpdateTime),
                    "Latest Update done at: " + sCurrDateTime);
            // Dismiss ProgressDialog if needed
            if (bProgressDialog != false && pdUpdateData != null) {
                pdUpdateData.dismiss();
            }
        }

        /**
         * @author Tiago Sarmento Santos
         * @func onProgressUpdate
         * @desc This functions is called during the asynchronous task work
         * @param progress
         */
        protected void onProgressUpdate(Integer... progress){
            // nothing to be done here
        }

        /**
         * @author Tiago Sarmento Santos
         * @func onPreExecute
         * @desc This functions is called before the asynchronous task work is started
         * @param
         */
        protected void onPreExecute() {
            pdUpdateData = new ProgressDialog(this.gContext);
            if (bProgressDialog != false && pdUpdateData != null) {
                pdUpdateData.setTitle("Fetching Sensor Data");
                pdUpdateData.setMessage("Please wait a moment...");
                pdUpdateData.show();
            }
        }

        /**
         * @author Tiago Sarmento Santos
         * @func setParameters
         * @desc This function sets the asynchronous task parameters
         * @param cContext shSettings enableProgressDialog
         */
        public void setParameters(Context cContext,
                                  SettingsHandler shSettings,
                                  Boolean enableProgressDialog) {
            this.gContext        = cContext;
            this.gshSettings     = shSettings;
            this.bProgressDialog = enableProgressDialog;
        }

        /**
         * @author Tiago Sarmento Santos
         * @func getExoInstData
         * @desc This function gets from Exosite Platform the latest available value for a sensor
         * @param dataPort
         * @return sExoData
         */
        private String getSensorData(String dataPort) {
            // Local Data
            InputStream isExoData = null;
            String      sExoData  = null;
            // Create URL for TI Exosite
            URL url_exosite = null;
            try {
                url_exosite = new URL(this.gContext.getString(R.string.exosite_http_base) + dataPort);
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
            url_conn_exosite.addRequestProperty("X-Exosite-CIK", gshSettings.getSettingStringValue(this.gContext.getString(R.string.keyCIK)));
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
            } else {
                // Handle error
            }
            // Close HTTP URL connection
            url_conn_exosite.disconnect();
            return sExoData;
        }
    }
}