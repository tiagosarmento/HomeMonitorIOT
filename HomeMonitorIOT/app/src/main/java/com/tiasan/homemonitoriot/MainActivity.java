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

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Set Global data
    private static final String gTag  = "DBG - MainActivity";
    Button bTemperature               = null;
    Button bHumidity                  = null;
    Button bPressure                  = null;
    Button bLight                     = null;
    TextView tvTextIntro              = null;
    TextView tvTextUpd                = null;
    TextView tvTemperature            = null;
    TextView tvHumidity               = null;
    TextView tvPressure               = null;
    TextView tvAltitude               = null;
    TextView tvVisibleLight           = null;
    TextView tvInfraredLight          = null;
    SettingsHandler shSettings        = null;
    ErrorHandler    errorHandler      = null;
    AlarmReceiver   arUpdateDataAlarm = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(gTag, "The onCreate() event");

        setContentView(R.layout.activity_main);

        // Do setup
        initButtons();
        initTextViews();

        // Create hook on AlarmReceiver
        arUpdateDataAlarm = new AlarmReceiver();

        // Create hook on Settings (Shared Preferences)
        shSettings = new SettingsHandler(this);
        shSettings.printSettings();

        // Create hook on ErrorHandler
        errorHandler = new ErrorHandler(this);
        // DBG CALL: errorHandler.showErrorMsg("Ups something wrong happened!!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.mitRefresh) {
            Log.d(gTag, "Refresh button clicked");
            // TODO: Push new data, instead of taking the last update on private settings
            // Populate TableView with sensor data
            populateSensorDataTableView();
            return true;
        }
        if (id == R.id.mitSettings) {
            Log.d(gTag, "Settings button clicked");
            Intent iSettings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(iSettings);
            return true;
        }
        if (id == R.id.mitAbout) {
            Log.d(gTag, "About button clicked");
            showAboutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    public void showAboutDialog() {
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);

        // Create our AlertDialog contents as a Linear Layout to have a fancy format
        LinearLayout llinear = new LinearLayout(this);
        llinear.setOrientation(LinearLayout.VERTICAL);
        llinear.setPadding(10,10,10,10);

        // Sub-layouts, to hold several different messages
        // Author Credits
        final TextView tvMsgAuthor = new TextView(this);
        tvMsgAuthor.setText("Author: Tiago Sarmento Santos.");
        llinear.addView(tvMsgAuthor);
        // Contact
        final TextView tvMsgContact = new TextView(this);
        final SpannableString ssGitHub = new SpannableString("tiagosarmentosantos@gmail.com");
        Linkify.addLinks(ssGitHub, Linkify.EMAIL_ADDRESSES);
        tvMsgContact.setText(ssGitHub);
        tvMsgContact.setMovementMethod(LinkMovementMethod.getInstance());
        llinear.addView(tvMsgContact);
        // Code page
        final TextView tvMsgGitRepo = new TextView(this);
        final SpannableString ssGmail = new SpannableString("https://github.com/tiagosarmento/HomeMonitorIOT");
        Linkify.addLinks(ssGmail, Linkify.WEB_URLS);
        tvMsgGitRepo.setText(ssGmail);
        tvMsgGitRepo.setMovementMethod(LinkMovementMethod.getInstance());
        llinear.addView(tvMsgGitRepo);
        // Add contents to AlertDialog builder
        adBuilder.setView(llinear);
        adBuilder.setTitle("About");
        adBuilder.setPositiveButton("Dismiss", null);
        // Show AlertDialog
        AlertDialog dialog = adBuilder.create();
        dialog.show();
    }

    public void initButtons() {
        // Set the interaction buttons
        bTemperature = (Button) findViewById(R.id.bTemperature);
        bTemperature.setOnClickListener(this);
        
        bHumidity = (Button) findViewById(R.id.bHumidity);
        bHumidity.setOnClickListener(this);
        
        bPressure = (Button) findViewById(R.id.bPressure);
        bPressure.setOnClickListener(this);
        
        bLight = (Button) findViewById(R.id.bLight);
        bLight.setOnClickListener(this);
    }

    public void initTextViews() {
        tvTextIntro     = (TextView) findViewById(R.id.tvGenericMsg_Intro);
        tvTextUpd       = (TextView) findViewById(R.id.tvGenericMsg_Upd);
        tvTemperature   = (TextView) findViewById(R.id.tvTemperature);
        tvHumidity      = (TextView) findViewById(R.id.tvHumidity);
        tvPressure      = (TextView) findViewById(R.id.tvPressure);
        tvAltitude      = (TextView) findViewById(R.id.tvAltitude);
        tvVisibleLight  = (TextView) findViewById(R.id.tvVisibleLight);
        tvInfraredLight = (TextView) findViewById(R.id.tvInfraredLight);
    }

    public void populateSensorDataTableView() {
        // Temperature Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyTemperatureData)) == "") {
            tvTemperature.setText("No data yet available");
        } else {
            tvTemperature.setText(this.shSettings.getSettingStringValue(getString(R.string.keyTemperatureData)) + "°C");
        }
        // Humidity Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyHumidityData)) == "") {
            tvHumidity.setText("No data yet available");
        } else {
            tvHumidity.setText(this.shSettings.getSettingStringValue(getString(R.string.keyHumidityData)) + " %");
        }
        // Pressure Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyPressureData)) == "") {
            tvPressure.setText("No data yet available");
        } else {
            tvPressure.setText(this.shSettings.getSettingStringValue(getString(R.string.keyPressureData)) + " mBar");
        }
        // Altitude Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyAltitudeData)) == "") {
            tvAltitude.setText("No data yet available");
        } else {
            tvAltitude.setText(this.shSettings.getSettingStringValue(getString(R.string.keyAltitudeData)) + " m");
        }
        // Visible Light Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyVisibleLightData)) == "") {
            tvVisibleLight.setText("No data yet available");
        } else {
            tvVisibleLight.setText(this.shSettings.getSettingStringValue(getString(R.string.keyVisibleLightData)) + " Lux");
        }
        // Infrared Light Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyInfraredLightData)) == "") {
            tvInfraredLight.setText("No data yet available");
        } else {
            tvInfraredLight.setText(this.shSettings.getSettingStringValue(getString(R.string.keyInfraredLightData)) + " Lux");
        }
        // Last update Received
        if (this.shSettings.getSettingStringValue(getString(R.string.keyLastUpdateTime)) == "") {
            tvTextUpd.setText("Press 'Refresh' on ActionBar to get new data...");
        } else {
            tvTextUpd.setText(this.shSettings.getSettingStringValue(getString(R.string.keyLastUpdateTime)) + " Lux");
        }
    }

    /**
     * Called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(gTag, "The onStart() event");
    }

    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(gTag, "The onResume() event");

        // Check if Update Alarm should enabled or disabled
        if (shSettings.getSettingBooleanValue(getString(R.string.keyAutoUpdate)) == true) {
            arUpdateDataAlarm.setAlarm(this);
            Log.d(gTag, "UpdateAlarm ENABLED");
        } else {
            arUpdateDataAlarm.cancelAlarm(this);
            Log.d(gTag, "UpdateAlarm DISABLED");
        }

        // Populate TableView with sensor data
        populateSensorDataTableView();
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
    }

    /**
     * Called just before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(gTag, "The onDestroy() event");
    }

     /**
     * Called when a button is clicked.
     */
    @Override
    public void onClick(View v) {
        Log.d(gTag, "The onClick() event");
        Intent intent = null;
        // Switch into button id case
        switch(v.getId()) {
            case R.id.bTemperature:
                // Jump to Temperature Activity
                Log.d(gTag, "Temperature button clicked.");
                intent = new Intent(MainActivity.this, TemperatureActivity.class);
                startActivity(intent);
                break;
            case R.id.bHumidity:
                // Jump to Humidity Activity
                Log.d(gTag, "Humidity button clicked.");
                intent = new Intent(MainActivity.this, HumidityActivity.class);
                startActivity(intent);
                break;
            case R.id.bPressure:
                // Jump to Pressure Activity
                Log.d(gTag, "Pressure button clicked.");
                intent = new Intent(MainActivity.this, PressureActivity.class);
                startActivity(intent);
                break;
            case R.id.bLight:
                // Jump to Light Activity
                Log.d(gTag, "Light button clicked.");
                intent = new Intent(MainActivity.this, LightActivity.class);
                startActivity(intent);
                break;
        }
    }

}
