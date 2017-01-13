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
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author Tiago Sarmento Santos
 * @class WeatherDataHandler
 * @desc This class is used to handle the weather data from OpenWeatherMap
 * @credits https://code.tutsplus.com/tutorials/create-a-weather-app-on-android--cms-21587
 *
 * OpenWeatherMap json data structure
 * {
 *    "base": "cmc stations",
 *    "clouds": {
 *        "all": 90
 *     },
 *    "cod": 200,
 *    "coord": {
 *        "lat": -35.28,
 *        "lon": 149.13
 *     },
 *    "dt": 1404390600,
 *    "id": 2172517,
 *    "main": {
 *        "humidity": 100,
 *        "pressure": 1023,
 *        "temp": -1,
 *        "temp_max": -1,
 *        "temp_min": -1
 *    },
 *    "name": "Canberra",
 *    "sys": {
 *        "country": "AU",
 *        "message": 0.313,
 *        "sunrise": 1404335563,
 *        "sunset": 1404370965
 *    },
 *    "weather": [
 *        {
 *        "description": "overcast clouds",
 *        "icon": "04n",
 *        "id": 804,
 *        "main": "Clouds"
 *        }
 *    ],
 *    "wind": {
 *        "deg": 305.004,
 *        "speed": 1.07
 *    }
 * }
 */
public class WeatherDataHandler {

    // Set Global data
    private static final String gTag            = "DBG - WeatherData";
    private SettingsHandler     gshSettings     = null;
    private Context             gContext        = null;
    private Boolean             bProgressDialog = false;

    /**
     * @param cContext
     * @author Tiago Sarmento Santos
     * @func WeatherDataHandler Constructor
     */
    WeatherDataHandler(Context cContext, Boolean enableProgressDialog) {
        this.gContext = cContext;
        this.gshSettings = new SettingsHandler(cContext);
        this.bProgressDialog = enableProgressDialog;
    }

    /**
     * @author Tiago Sarmento Santos
     * @func updateSensorData
     * @desc This function is used to get the latest sensor data available on Exosite Platform
     */
    public void updateWeatherData() {
        UpdateWeatherDataAsyncTask atkWeatherData = new UpdateWeatherDataAsyncTask();
        atkWeatherData.setParameters(this.gContext, this.gshSettings, this.bProgressDialog);
        atkWeatherData.execute();
    }

    /**
     * @author Tiago Sarmento Santos
     * @class UpdateWeatherDataAsyncTask
     * @desc This is the asynchronous task to fetch weather data from OpenWeatherMap
     */
    private class UpdateWeatherDataAsyncTask extends AsyncTask<String, Integer, Double> {

        // Set Global data
        private SettingsHandler gshSettings = null;
        private Context gContext            = null;
        private Boolean bProgressDialog     = false;
        private ProgressDialog pdUpdateData = null;
        private JSONObject gjsWeatherData   = null;

        /**
         * @param params
         * @return
         * @author Tiago Sarmento Santos
         * @func getExoInstData
         * @desc This function does the asynchronous task work in background
         */
        @Override
        protected Double doInBackground(String... params) {
            // Get Weather Data from OpenWeatherMap
            this.gjsWeatherData = this.getWeatherData();
            if (this.gjsWeatherData != null) {
                this.saveWeatherData();
            } else {
                // handle error case here!
                Log.d(gTag, "Failed to fetch WeatherData!");
            }
            return null;
        }

        /**
         * @param result
         * @author Tiago Sarmento Santos
         * @func onPostExecute
         * @desc This functions is called when the asynchronous task work is finished
         * after doInBackground()
         */
        protected void onPostExecute(Double result) {
            // Dismiss ProgressDialog if needed
            if (bProgressDialog != false && pdUpdateData != null) {
                pdUpdateData.dismiss();
            }
        }

        /**
         * @param progress
         * @author Tiago Sarmento Santos
         * @func onProgressUpdate
         * @desc This functions is called during the asynchronous task work
         */
        protected void onProgressUpdate(Integer... progress) {
            // nothing to be done here
        }

        /**
         * @param
         * @author Tiago Sarmento Santos
         * @func onPreExecute
         * @desc This functions is called before the asynchronous task work is started
         */
        protected void onPreExecute() {
            pdUpdateData = new ProgressDialog(this.gContext);
            if (bProgressDialog != false && pdUpdateData != null) {
                pdUpdateData.setTitle("Fetching Weather Data");
                pdUpdateData.setMessage("Please wait a moment...");
                pdUpdateData.show();
            }
        }

        /**
         * @param cContext shSettings enableProgressDialog
         * @author Tiago Sarmento Santos
         * @func setParameters
         * @desc This function sets the asynchronous task parameters
         */
        public void setParameters(Context cContext,
                                  SettingsHandler shSettings,
                                  Boolean enableProgressDialog) {
            this.gContext = cContext;
            this.gshSettings = shSettings;
            this.bProgressDialog = enableProgressDialog;
        }

        private JSONObject getWeatherData() {
            try {
                URL url = new URL(String.format(
                        this.gContext.getString(R.string.open_weather_json),
                        this.gshSettings.getSettingStringValue(this.gContext.getString(R.string.keyCityName))));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("x-api-key",
                        this.gContext.getString(R.string.open_weather_key));
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuffer json = new StringBuffer(1024);
                String tmp = "";
                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                JSONObject data = new JSONObject(json.toString());

                // This value will be 404 if the request was not
                // successful
                if (data.getInt("cod") != 200) {
                    return null;
                }
                return data;
            } catch (Exception e) {
                return null;
            }
        }

        private void saveWeatherData() {
            try {
                // Get from weather object: "weather"
                this.gshSettings.setStringValue(this.gContext.getString(R.string.keyWeatherCondition),
                        this.gjsWeatherData.getJSONArray("weather").getJSONObject(0).getString("description"));

                // Get from weather object: "main"
                this.gshSettings.setStringValue(this.gContext.getString(R.string.keyWeatherTemperatureData),
                        this.gjsWeatherData.getJSONObject("main").getString("temp"));
                this.gshSettings.setStringValue(this.gContext.getString(R.string.keyWeatherTemperatureMinData),
                        this.gjsWeatherData.getJSONObject("main").getString("temp_min"));
                this.gshSettings.setStringValue(this.gContext.getString(R.string.keyWeatherTemperatureMaxData),
                        this.gjsWeatherData.getJSONObject("main").getString("temp_max"));
                this.gshSettings.setStringValue(this.gContext.getString(R.string.keyWeatherHumidityData),
                        this.gjsWeatherData.getJSONObject("main").getString("humidity"));
                this.gshSettings.setStringValue(this.gContext.getString(R.string.keyWeatherPressureData),
                        this.gjsWeatherData.getJSONObject("main").getString("pressure"));

                // Get from weather object: "visibility"
                this.gshSettings.setStringValue(this.gContext.getString(R.string.keyWeatherVisibilityData),
                        this.gjsWeatherData.getString("visibility"));

                // Get from weather object: "wind"
                this.gshSettings.setStringValue(this.gContext.getString(R.string.keyWeatherWindSpeedData),
                        this.gjsWeatherData.getJSONObject("wind").getString("speed"));

                // Get from weather object: "sys"
                this.gshSettings.setStringValue(this.gContext.getString(R.string.keyCountryName),
                        this.gjsWeatherData.getJSONObject("sys").getString("country"));

                DateFormat dfDate = DateFormat.getTimeInstance(DateFormat.SHORT);
                String sSunDate;

                sSunDate = dfDate.format(new Date(
                        this.gjsWeatherData.getJSONObject("sys").getLong("sunrise") * 1000));
                this.gshSettings.setStringValue(this.gContext.getString(R.string.keyWeatherSunriseData), sSunDate);

                sSunDate = dfDate.format(new Date(
                        this.gjsWeatherData.getJSONObject("sys").getLong("sunset") * 1000));
                this.gshSettings.setStringValue(this.gContext.getString(R.string.keyWeatherSunsetData), sSunDate);

            } catch (Exception e) {
                Log.e("SimpleWeather", "One or more fields not found in the JSON data");


            }
        }
    }
}
