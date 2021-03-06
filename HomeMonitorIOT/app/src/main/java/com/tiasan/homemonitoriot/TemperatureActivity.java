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
import android.view.Menu;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;

/**
 * @author Tiago Sarmento Santos
 * @class TemperatureActivity
 * @desc This class handles the Temperature Activity
 */
public class TemperatureActivity extends AppCompatActivity {
    
    // Set Global data
    private static final String gTag          = "DBG - TemperatureAct";
    private ProgressDialog      pdTemperature = null;
    private GraphView           gvTemperature = null;
    private SettingsHandler     shSettings    = null;

    /**
     * Called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Init layout objects
        setContentView(R.layout.activity_temperature);
        pdTemperature =  new ProgressDialog(this);
        gvTemperature = (GraphView) findViewById(R.id.gvTemperature);
        // Create hook on Settings (Shared Preferences)
        shSettings = new SettingsHandler(this);
    }

    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Show Graph
        executeGraphAsyncTask();
    }

    /**
     * Called when another activity is taking focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Called when the activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Called just before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * @author Tiago Sarmento Santos
     * @func executeGraphAsyncTask
     * @desc This function fires the async task to plot the temperature graphic
     */
    private void executeGraphAsyncTask() {
        String sSensorNameID = "Temperature";
        String sCIK = shSettings.getSettingStringValue(getString(R.string.keyCIK));
        String sDataPort = shSettings.getSettingStringValue(getString(R.string.keyTemperaturePort));
        GetDataAsyncTask atkTemperature = new GetDataAsyncTask();
        atkTemperature.setParameters(pdTemperature, gvTemperature, sCIK, sDataPort, sSensorNameID);
        atkTemperature.execute();
    }

    /**
     * @author Tiago Sarmento Santos
     * @func onCreateOptionsMenu
     * @desc This function is called to create the options menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_graph_menu, menu);
        return true;
    }

    /**
     * @author Tiago Sarmento Santos
     * @func onOptionsItemSelected
     * @desc This function is called when a menu item is pressed
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.mitGraphRefresh) {
            executeGraphAsyncTask();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}