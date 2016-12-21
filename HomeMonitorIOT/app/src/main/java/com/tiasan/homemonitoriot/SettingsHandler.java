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
    String gTag = "DBG - SettingsHandler";

    Context gContext = null;

    // SharedPreferences values
    private String sCIK               = null;
    private String sTemperaturePort   = null;
    private String sHumidityPort      = null;
    private String sPressurePort      = null;
    private String sAltitudePort      = null;
    private String sVisibleLightPort  = null;
    private String sInfraredLightPort = null;
    private Boolean bAutoUpdate       = false;

    /**
     * @func SettingsHandler Constructor
     * @param cContext
     */
    SettingsHandler(Context cContext) {
        gContext = cContext;
        // When called first time we init the settings with the default values
        PreferenceManager.setDefaultValues(gContext, R.xml.activity_settings, false);
    }

    /**
     * @func printSettings
     * @desc Used to print the settings values
     */
    public void printSettings() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(gContext);
        // read settings as <key>,<default>
        sCIK               = sharedPref.getString(gContext.getString(R.string.keyCIK), "");
        sTemperaturePort   = sharedPref.getString(gContext.getString(R.string.keyTemperaturePort), "");
        sHumidityPort      = sharedPref.getString(gContext.getString(R.string.keyHumidityPort), "");
        sPressurePort      = sharedPref.getString(gContext.getString(R.string.keyPressurePort), "");
        sVisibleLightPort  = sharedPref.getString(gContext.getString(R.string.keyVisibleLightPort), "");
        sAltitudePort      = sharedPref.getString(gContext.getString(R.string.keyAltitudePort), "");
        sInfraredLightPort = sharedPref.getString(gContext.getString(R.string.keyInfraredLightPort), "");
        bAutoUpdate        = sharedPref.getBoolean(gContext.getString(R.string.keyAutoUpdate), false);

        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyCIK)               + " Value: " + sCIK);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyTemperaturePort)   + " Value: " + sTemperaturePort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyHumidityPort)      + " Value: " + sHumidityPort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyPressurePort)      + " Value: " + sPressurePort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyAltitudePort)      + " Value: " + sAltitudePort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyVisibleLightPort)  + " Value: " + sVisibleLightPort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyInfraredLightPort) + " Value: " + sInfraredLightPort);
        Log.d(gTag, "Settings key: " + gContext.getString(R.string.keyAutoUpdate)        + " Value: " + bAutoUpdate);

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
