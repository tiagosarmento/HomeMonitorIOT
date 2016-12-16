package com.tiasan.homemonitoriot;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.jjoe64.graphview.GraphView;

public class PressureActivity extends Activity {

    // Set Global data
    String gTag = "DBG - PressureActivity";
    ProgressDialog pdPressure = null;
    GraphView      gvPressure = null;
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
    }

    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(gTag, "The onResume() event");
        GetDataAsyncTask atkPressure = new GetDataAsyncTask();
        atkPressure.setParameters(pdPressure, "bmp180_press", gvPressure);
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