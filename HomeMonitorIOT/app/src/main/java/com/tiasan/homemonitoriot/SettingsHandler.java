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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * @author Tiago Sarmento Santos
 * @class SettingsHandler
 * @desc This class handles the application Settings
 */
public class SettingsHandler {

    // Set Global data
    private static final String gTag        = "DBG - SettingsHandler";
    private Context             gContext    = null;
    private SharedPreferences   gsharedPref = null;
    /**
     * @func SettingsHandler Constructor
     * @param cContext
     */
    SettingsHandler(Context cContext) {
        this.gContext = cContext;
        // When called first time we init the settings with the default values
        // Create public settings
        PreferenceManager.setDefaultValues(this.gContext, R.xml.activity_settings, false);
        // Create private settings (key-pairs to hold sensor data), if not yet existing
        this.gsharedPref = PreferenceManager.getDefaultSharedPreferences(this.gContext);

        if (!this.gsharedPref.contains(this.gContext.getString(R.string.keyTemperatureData))) {
            setStringValue(this.gContext.getString(R.string.keyTemperatureData),"");
        }
        if (!this.gsharedPref.contains(this.gContext.getString(R.string.keyHumidityData))) {
            setStringValue(this.gContext.getString(R.string.keyHumidityData),"");
        }
        if (!this.gsharedPref.contains(this.gContext.getString(R.string.keyPressureData))) {
            setStringValue(this.gContext.getString(R.string.keyPressureData),"");
        }
        if (!this.gsharedPref.contains(this.gContext.getString(R.string.keyAltitudeData))) {
            setStringValue(this.gContext.getString(R.string.keyAltitudeData),"");
        }
        if (!this.gsharedPref.contains(this.gContext.getString(R.string.keyVisibleLightData))) {
            setStringValue(this.gContext.getString(R.string.keyVisibleLightData),"");
        }
        if (!this.gsharedPref.contains(this.gContext.getString(R.string.keyInfraredLightData))) {
            setStringValue(this.gContext.getString(R.string.keyInfraredLightData),"");
        }
        if (!this.gsharedPref.contains(this.gContext.getString(R.string.keyLastUpdateTime))) {
            setStringValue(this.gContext.getString(R.string.keyLastUpdateTime),"");
        }
    }

    /**
     * @func Set SharedPreference String Value
     * @param sKey
     * @param sValue
     * @desc Used to set an shared preference with a value
     */
    public void setStringValue(String sKey, String sValue) {
        SharedPreferences.Editor edSharedPref = this.gsharedPref.edit();
        edSharedPref.putString(sKey, sValue);
        if (!edSharedPref.commit()) {
            // handle error
            Log.d(gTag, "SharedPreferences failed to commit!");
        }
    }

    /**
     * @func printSettings
     * @desc Used to print the settings values
     */
    public void printSettings() {
        // Public SharedPreferences values: read settings as <key>,<default>
        String sCIK               = this.gsharedPref.getString(gContext.getString(R.string.keyCIK), "");
        String sTemperaturePort   = this.gsharedPref.getString(gContext.getString(R.string.keyTemperaturePort), "");
        String sHumidityPort      = this.gsharedPref.getString(gContext.getString(R.string.keyHumidityPort), "");
        String sPressurePort      = this.gsharedPref.getString(gContext.getString(R.string.keyPressurePort), "");
        String sVisibleLightPort  = this.gsharedPref.getString(gContext.getString(R.string.keyVisibleLightPort), "");
        String sAltitudePort      = this.gsharedPref.getString(gContext.getString(R.string.keyAltitudePort), "");
        String sInfraredLightPort = this.gsharedPref.getString(gContext.getString(R.string.keyInfraredLightPort), "");
        Boolean bAutoUpdate       = this.gsharedPref.getBoolean(gContext.getString(R.string.keyNotifications), false);
        // Private SharedPreferences values: read settings as <key>,<default>
        String sTemperatureData   = this.gsharedPref.getString(gContext.getString(R.string.keyTemperatureData), "");
        String sHumidityData      = this.gsharedPref.getString(gContext.getString(R.string.keyHumidityData), "");
        String sPressureData      = this.gsharedPref.getString(gContext.getString(R.string.keyPressureData), "");
        String sAltitudeData      = this.gsharedPref.getString(gContext.getString(R.string.keyAltitudeData), "");
        String sVisibleLightData  = this.gsharedPref.getString(gContext.getString(R.string.keyVisibleLightData), "");
        String sInfraredLightData = this.gsharedPref.getString(gContext.getString(R.string.keyInfraredLightData), "");
        String sLastUpdateTime    = this.gsharedPref.getString(gContext.getString(R.string.keyLastUpdateTime), "");

        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyCIK)               + " Value: " + sCIK);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyTemperaturePort)   + " Value: " + sTemperaturePort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyHumidityPort)      + " Value: " + sHumidityPort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyPressurePort)      + " Value: " + sPressurePort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyAltitudePort)      + " Value: " + sAltitudePort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyVisibleLightPort)  + " Value: " + sVisibleLightPort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyInfraredLightPort) + " Value: " + sInfraredLightPort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyNotifications)     + " Value: " + bAutoUpdate);

        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyTemperatureData)   + " Value: " + sTemperatureData);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyHumidityData)      + " Value: " + sHumidityData);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyPressureData)      + " Value: " + sPressureData);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyAltitudeData)      + " Value: " + sAltitudeData);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyVisibleLightData)  + " Value: " + sVisibleLightData);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyInfraredLightData) + " Value: " + sInfraredLightData);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyLastUpdateTime)    + " Value: " + sLastUpdateTime);
    }

    /**
     * @func getSettingStringValue
     * @desc Used to get String setting value
     * @param sKey
     * @return settingValue
     */
    public String getSettingStringValue(String sKey) {
        return this.gsharedPref.getString(sKey, "");
    }

    /**
     * @func getSettingBooleanValue
     * @desc Used to get Boolean setting value
     * @param sKey
     * @return booleanValue
     */
    public Boolean getSettingBooleanValue(String sKey) {
        return this.gsharedPref.getBoolean(sKey, false);
    }
}
