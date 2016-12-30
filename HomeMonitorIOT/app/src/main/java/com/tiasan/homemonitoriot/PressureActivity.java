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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jjoe64.graphview.GraphView;

public class PressureActivity extends AppCompatActivity {

    // Set Global data
    private String          gTag       = "DBG - PressureActivity";
    private ProgressDialog  pdPressure = null;
    private GraphView       gvPressure = null;
    private SettingsHandler shSettings = null;

    /**
     * Called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(gTag, "The onStart() event");

        setContentView(R.layout.activity_pressure);

        pdPressure =  new ProgressDialog(this);

        gvPressure = (GraphView) findViewById(R.id.gvPressure);

        // Create hook on Settings (Shared Preferences)
        shSettings = new SettingsHandler(this);
    }

    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(gTag, "The onResume() event");
        String sSensorNameID = "Pressure";
        String sCIK = shSettings.getSettingStringValue(getString(R.string.keyCIK));
        String sDataPort = shSettings.getSettingStringValue(getString(R.string.keyPressurePort));
        GetDataAsyncTask atkPressure = new GetDataAsyncTask();
        atkPressure.setParameters(pdPressure, gvPressure, sCIK, sDataPort, sSensorNameID);
        atkPressure.execute();
    }

    /**
     * Called when another activity is taking focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(gTag, "The onPause() event");
    }

    /**
     * Called when the activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(gTag, "The onStop() event");
    }

    /**
     * Called just before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(gTag, "The onDestroy() event");
    }
    
}