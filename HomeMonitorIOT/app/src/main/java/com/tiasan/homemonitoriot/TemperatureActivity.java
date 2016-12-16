package com.tiasan.homemonitoriot;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.jjoe64.graphview.GraphView;


public class TemperatureActivity extends Activity {
    
    // Set Global data
    String gTag = "DBG - TemperatureActivity";
    ProgressDialog pdTemperature = null;
    GraphView      gvTemperature = null;

    /**
     * Called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(gTag, "The onStart() event");

        setContentView(R.layout.activity_temperature);

        pdTemperature =  new ProgressDialog(this);

        gvTemperature = (GraphView) findViewById(R.id.gvTemperature);
    }

    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(gTag, "The onResume() event");
        GetDataAsyncTask atkTemperature = new GetDataAsyncTask();
        atkTemperature.setParameters(pdTemperature, "bmp180_tempc", gvTemperature);
        atkTemperature.execute();
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