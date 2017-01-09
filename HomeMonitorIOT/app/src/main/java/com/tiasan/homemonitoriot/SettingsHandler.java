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
 * @class SettingsHandler
 */
public class SettingsHandler {

    // Set Global data
    private String gTag = "DBG - SettingsHandler";
    private Context gContext = null;

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
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.gContext);

        if (sharedPref.contains(this.gContext.getString(R.string.keyTemperatureData)) == false) {
            setStringValue(this.gContext.getString(R.string.keyTemperatureData),"");
        }
        if (sharedPref.contains(this.gContext.getString(R.string.keyHumidityData)) == false) {
            setStringValue(this.gContext.getString(R.string.keyHumidityData),"");
        }
        if (sharedPref.contains(this.gContext.getString(R.string.keyPressureData)) == false) {
            setStringValue(this.gContext.getString(R.string.keyPressureData),"");
        }
        if (sharedPref.contains(this.gContext.getString(R.string.keyAltitudeData)) == false) {
            setStringValue(this.gContext.getString(R.string.keyAltitudeData),"");
        }
        if (sharedPref.contains(this.gContext.getString(R.string.keyVisibleLightData)) == false) {
            setStringValue(this.gContext.getString(R.string.keyVisibleLightData),"");
        }
        if (sharedPref.contains(this.gContext.getString(R.string.keyInfraredLightData)) == false) {
            setStringValue(this.gContext.getString(R.string.keyInfraredLightData),"");
        }
        if (sharedPref.contains(this.gContext.getString(R.string.keyLastUpdateTime)) == false) {
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
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.gContext);
        SharedPreferences.Editor edSharedPref = sharedPref.edit();
        edSharedPref.putString(sKey, sValue);
        edSharedPref.commit();
    }

    /**
     * @func printSettings
     * @desc Used to print the settings values
     */
    public void printSettings() {
        // Public SharedPreferences values
        String sCIK               = null;
        String sTemperaturePort   = null;
        String sHumidityPort      = null;
        String sPressurePort      = null;
        String sAltitudePort      = null;
        String sVisibleLightPort  = null;
        String sInfraredLightPort = null;
        Boolean bAutoUpdate       = false;
        // Private SharedPreferences values
        String sTemperatureData   = null;
        String sHumidityData      = null;
        String sPressureData      = null;
        String sAltitudeData      = null;
        String sVisibleLightData  = null;
        String sInfraredLightData = null;
        String sLastUpdateTime    = null;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(gContext);
        // read settings as <key>,<default>
        sCIK               = sharedPref.getString(gContext.getString(R.string.keyCIK), "");
        sTemperaturePort   = sharedPref.getString(gContext.getString(R.string.keyTemperaturePort), "");
        sHumidityPort      = sharedPref.getString(gContext.getString(R.string.keyHumidityPort), "");
        sPressurePort      = sharedPref.getString(gContext.getString(R.string.keyPressurePort), "");
        sVisibleLightPort  = sharedPref.getString(gContext.getString(R.string.keyVisibleLightPort), "");
        sAltitudePort      = sharedPref.getString(gContext.getString(R.string.keyAltitudePort), "");
        sInfraredLightPort = sharedPref.getString(gContext.getString(R.string.keyInfraredLightPort), "");
        bAutoUpdate        = sharedPref.getBoolean(gContext.getString(R.string.keyNotifications), false);

        sTemperatureData   = sharedPref.getString(gContext.getString(R.string.keyTemperatureData), "");
        sHumidityData      = sharedPref.getString(gContext.getString(R.string.keyHumidityData), "");
        sPressureData      = sharedPref.getString(gContext.getString(R.string.keyPressureData), "");
        sAltitudeData      = sharedPref.getString(gContext.getString(R.string.keyAltitudeData), "");
        sVisibleLightData  = sharedPref.getString(gContext.getString(R.string.keyVisibleLightData), "");
        sInfraredLightData = sharedPref.getString(gContext.getString(R.string.keyInfraredLightData), "");
        sLastUpdateTime    = sharedPref.getString(gContext.getString(R.string.keyLastUpdateTime), "");

        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyCIK)               + " Value: " + sCIK);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyTemperaturePort)   + " Value: " + sTemperaturePort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyHumidityPort)      + " Value: " + sHumidityPort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyPressurePort)      + " Value: " + sPressurePort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyAltitudePort)      + " Value: " + sAltitudePort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyVisibleLightPort)  + " Value: " + sVisibleLightPort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyInfraredLightPort) + " Value: " + sInfraredLightPort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyNotifications)        + " Value: " + bAutoUpdate);

        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyTemperatureData)   + " Value: " + sTemperatureData);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyHumidityData)      + " Value: " + sHumidityData);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyPressureData)      + " Value: " + sPressureData);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyAltitudeData)      + " Value: " + sAltitudeData);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyVisibleLightData)  + " Value: " + sVisibleLightData);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyInfraredLightData) + " Value: " + sInfraredLightData);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyLastUpdateTime) + " Value: " + sLastUpdateTime);
    }

    /**
     * @func getSettingStringValue
     * @desc Used to get String setting value
     * @param sKey
     * @return settingValue
     */
    public String getSettingStringValue(String sKey) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(gContext);
        return sharedPref.getString(sKey, "");
    }

    /**
     * @func getSettingBooleanValue
     * @desc Used to get Boolean setting value
     * @param sKey
     * @return booleanValue
     */
    public Boolean getSettingBooleanValue(String sKey) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(gContext);
        return sharedPref.getBoolean(sKey, false);
    }
}
