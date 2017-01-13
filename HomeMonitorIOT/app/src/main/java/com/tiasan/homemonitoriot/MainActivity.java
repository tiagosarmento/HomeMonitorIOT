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
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Tiago Sarmento Santos
 * @class MainActivity
 * @desc This class is the application Main Activity
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Set Global data
    private static final String gTag                    = "DBG - MainActivity";
    private Button              bTemperature            = null;
    private Button              bHumidity               = null;
    private Button              bPressure               = null;
    private Button              bLight                  = null;
    private TextView            tvTextIntro             = null;
    private TextView            tvTextUpd               = null;
    private TextView            tvTemperature           = null;
    private TextView            tvHumidity              = null;
    private TextView            tvPressure              = null;
    private TextView            tvAltitude              = null;
    private TextView            tvVisibleLight          = null;
    private TextView            tvInfraredLight         = null;
    private TextView            tvWeatherCity           = null;
    private TextView            tvWeatherCountryName    = null;
    private TextView            tvWeatherTemperature    = null;
    private TextView            tvWeatherTemperatureMin = null;
    private TextView            tvWeatherTemperatureMax = null;
    private TextView            tvWeatherHumidity       = null;
    private TextView            tvWeatherPressure       = null;
    private TextView            tvWeatherVisibility     = null;
    private TextView            tvWeatherWindSpeed      = null;
    private TextView            tvWeatherSunrise        = null;
    private TextView            tvWeatherSunset         = null;
    private TextView            tvWeatherCondition      = null;
    private SettingsHandler     shSettings              = null;
    private ErrorHandler        errorHandler            = null;
    private AlarmReceiver       arUpdateDataAlarm       = null;
    private SensorDataHandler   gsdHandler              = null;
    private WeatherDataHandler  gwdHandler              = null;

    private SharedPreferences.OnSharedPreferenceChangeListener gspPrefListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                /**
                 * @func onSharedPreferenceChanged
                 * @desc Called when there is a change in any SharedPreference
                 * @param prefs
                 * @param key
                 */
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    // If there is a change on sensor data stored, we must update Sensor TableView
                    populateSensorDataTableView();
                    // If there is a change on weather data stored, we must update Weather TableView
                    populateWeatherDataTableView();
                }
            };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load main layout
        setContentView(R.layout.activity_main);
        // Init main layout objects
        setupButtonObjects();
        setupTextViewObjects();
        // Create hook on AlarmReceiver
        arUpdateDataAlarm = new AlarmReceiver();
        // Create hook on Settings (Shared Preferences)
        shSettings = new SettingsHandler(this);
        // Create hook on SensorDataHandler
        gsdHandler = new SensorDataHandler(this, true);
        // Create hook on WeatherDataHandler
        gwdHandler = new WeatherDataHandler(this, true);
        // Create hook on ErrorHandler
        errorHandler = new ErrorHandler(this);
    }

    /**
     * Called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Enable or Disable Notifications, based on Notification Setting
        if (shSettings.getSettingBooleanValue(getString(R.string.keyNotifications))) {
            arUpdateDataAlarm.setAlarm(this);
        } else {
            arUpdateDataAlarm.cancelAlarm(this);
        }
        // Populate Sensor TableView
        populateSensorDataTableView();
        // Populate Weather TableView
        populateWeatherDataTableView();
        // Register settings listener
        shSettings.registerSharedPreference(gspPrefListener);

    }

    /**
     * Called when another activity is taking focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        // Register settings listener
        shSettings.unregisterSharedPreference(gspPrefListener);
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
     * Called when options menu is pressed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Load Options menu
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    /**
     * Called when an option is selected on Options Menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take an action depending on option selected
        switch (item.getItemId()) {
            case R.id.mitRefresh:
                // Call data handler to fetch sensor data from Exosite Platform
                this.gsdHandler.updateSensorData();
                this.gwdHandler.updateWeatherData();
                break;
            case R.id.mitSettings:
                // Call settings activity
                Intent iSettings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(iSettings);
                break;
            case R.id.mitAbout:
                // Show About Dialog message
                showAboutDialog();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Called when a button is clicked.
     */
    @Override
    public void onClick(View v) {
        Intent iActivity;
        // Started the consequent activity for a pressed button
        switch(v.getId()) {
            case R.id.bTemperature:
                iActivity = new Intent(MainActivity.this, TemperatureActivity.class);
                startActivity(iActivity);
                break;
            case R.id.bHumidity:
                iActivity = new Intent(MainActivity.this, HumidityActivity.class);
                startActivity(iActivity);
                break;
            case R.id.bPressure:
                iActivity = new Intent(MainActivity.this, PressureActivity.class);
                startActivity(iActivity);
                break;
            case R.id.bLight:
                iActivity = new Intent(MainActivity.this, LightActivity.class);
                startActivity(iActivity);
                break;
        }
    }

    //
    // From here down are the MainActivity support functions
    //

    /**
     * @author Tiago Sarmento Santos
     * @func showAboutDialog
     * @desc This function displays the About dialog message
     * @return none
     */
    private void showAboutDialog() {
        // Create the AlertDialogBuilder, its the main dialog
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        // Create our AlertDialog contents as a Linear Layout to have a fancy format
        LinearLayout llinear = new LinearLayout(this);
        llinear.setOrientation(LinearLayout.VERTICAL);
        llinear.setPadding(10,10,10,10);
        // Sub-layouts, to hold several different messages
        // Author Credits
        final TextView tvMsgAuthor = new TextView(this);
        tvMsgAuthor.setText("Author: " + getString(R.string.proj_author));
        llinear.addView(tvMsgAuthor);
        // Contact
        final TextView tvMsgContact = new TextView(this);
        final SpannableString ssGitHub = new SpannableString(getString(R.string.proj_email));
        Linkify.addLinks(ssGitHub, Linkify.EMAIL_ADDRESSES);
        tvMsgContact.setText(ssGitHub);
        tvMsgContact.setMovementMethod(LinkMovementMethod.getInstance());
        llinear.addView(tvMsgContact);
        // Code page
        final TextView tvMsgGitRepo = new TextView(this);
        final SpannableString ssGmail = new SpannableString(getString(R.string.proj_page));
        Linkify.addLinks(ssGmail, Linkify.WEB_URLS);
        tvMsgGitRepo.setText(ssGmail);
        tvMsgGitRepo.setMovementMethod(LinkMovementMethod.getInstance());
        llinear.addView(tvMsgGitRepo);
        // Add contents to AlertDialog contents to main dialog
        adBuilder.setView(llinear);
        adBuilder.setTitle("About");
        adBuilder.setPositiveButton("Dismiss", null);
        // Show AlertDialog
        AlertDialog dialog = adBuilder.create();
        dialog.show();
    }

    /**
     * @author Tiago Sarmento Santos
     * @func setupButtonObjects
     * @desc This function does the setup of the MainActivity Button objects
     * @return none
     */
    private void setupButtonObjects() {
        // Setup buttons objects
        this.bTemperature = (Button) findViewById(R.id.bTemperature);
        this.bTemperature.setOnClickListener(this);

        this.bHumidity = (Button) findViewById(R.id.bHumidity);
        this.bHumidity.setOnClickListener(this);

        this.bPressure = (Button) findViewById(R.id.bPressure);
        this.bPressure.setOnClickListener(this);

        this.bLight = (Button) findViewById(R.id.bLight);
        this.bLight.setOnClickListener(this);
    }

    /**
     * @author Tiago Sarmento Santos
     * @func setupTextViewObjects
     * @desc This function does the setup of the MainActivity TextView objects
     * @return none
     */
    private void setupTextViewObjects() {
        // Setup Sensor data TextView objects
        this.tvTextIntro     = (TextView) findViewById(R.id.tvSensorIntro);
        this.tvTextUpd       = (TextView) findViewById(R.id.tvSensorFinal);
        this.tvTemperature   = (TextView) findViewById(R.id.tvTemperature);
        this.tvHumidity      = (TextView) findViewById(R.id.tvHumidity);
        this.tvPressure      = (TextView) findViewById(R.id.tvPressure);
        this.tvAltitude      = (TextView) findViewById(R.id.tvAltitude);
        this.tvVisibleLight  = (TextView) findViewById(R.id.tvVisibleLight);
        this.tvInfraredLight = (TextView) findViewById(R.id.tvInfraredLight);
        // Setup Weather data TextView objects
        this.tvWeatherCity           = (TextView) findViewById(R.id.tvWeatherCityName);
        this.tvWeatherCountryName    = (TextView) findViewById(R.id.tvWeatherCountryName);
        this.tvWeatherTemperature    = (TextView) findViewById(R.id.tvWeatherTemperature);
        this.tvWeatherTemperatureMin = (TextView) findViewById(R.id.tvWeatherTemperatureMin);
        this.tvWeatherTemperatureMax = (TextView) findViewById(R.id.tvWeatherTemperatureMax);
        this.tvWeatherHumidity       = (TextView) findViewById(R.id.tvWeatherHumidity);
        this.tvWeatherPressure       = (TextView) findViewById(R.id.tvWeatherPressure);
        this.tvWeatherVisibility     = (TextView) findViewById(R.id.tvWeatherVisibility);
        this.tvWeatherWindSpeed      = (TextView) findViewById(R.id.tvWeatherWindSpeed);
        this.tvWeatherSunrise        = (TextView) findViewById(R.id.tvWeatherSunrise);
        this.tvWeatherSunset         = (TextView) findViewById(R.id.tvWeatherSunset);
        this.tvWeatherCondition      = (TextView) findViewById(R.id.tvWeatherCondition);
    }

    /**
     * @author Tiago Sarmento Santos
     * @func populateSensorDataTableView
     * @desc This function populates the MainActivity Sensor TableView with the sensor data
     * @return none
     */
    private void populateSensorDataTableView() {
        // Fill in the Temperature Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyTemperatureData)) == "") {
            this.tvTemperature.setText("No data yet available");
        } else {
            this.tvTemperature.setText(this.shSettings.getSettingStringValue(getString(R.string.keyTemperatureData)) + "°C");
        }
        // Fill in the Humidity Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyHumidityData)) == "") {
            this.tvHumidity.setText("No data yet available");
        } else {
            this.tvHumidity.setText(this.shSettings.getSettingStringValue(getString(R.string.keyHumidityData)) + " %");
        }
        // Fill in the Pressure Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyPressureData)) == "") {
            this.tvPressure.setText("No data yet available");
        } else {
            this.tvPressure.setText(this.shSettings.getSettingStringValue(getString(R.string.keyPressureData)) + " mBar");
        }
        // Fill in the Altitude Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyAltitudeData)) == "") {
            this.tvAltitude.setText("No data yet available");
        } else {
            this.tvAltitude.setText(this.shSettings.getSettingStringValue(getString(R.string.keyAltitudeData)) + " m");
        }
        // Fill in the Visible Light Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyVisibleLightData)) == "") {
            this.tvVisibleLight.setText("No data yet available");
        } else {
            this.tvVisibleLight.setText(this.shSettings.getSettingStringValue(getString(R.string.keyVisibleLightData)) + " Lux");
        }
        // Fill in the Infrared Light Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyInfraredLightData)) == "") {
            this.tvInfraredLight.setText("No data yet available");
        } else {
            this.tvInfraredLight.setText(this.shSettings.getSettingStringValue(getString(R.string.keyInfraredLightData)) + " Lux");
        }
        // Fill in the Last update Received
        if (this.shSettings.getSettingStringValue(getString(R.string.keyLastUpdateTime)) == "") {
            this.tvTextUpd.setText("Press 'Refresh' on ActionBar to get new data...");
        } else {
            this.tvTextUpd.setText(this.shSettings.getSettingStringValue(getString(R.string.keyLastUpdateTime)) );
        }
    }

    /**
     * @author Tiago Sarmento Santos
     * @func populateWeatherDataTableView
     * @desc This function populates the MainActivity Weather TableView with the weather data
     * @return none
     */
    private void populateWeatherDataTableView() {
        // Fill in the City name
        if (this.shSettings.getSettingStringValue(getString(R.string.keyCityName)) == "") {
            this.tvWeatherCity.setText("No data yet available");
        } else {
            this.tvWeatherCity.setText(this.shSettings.getSettingStringValue(getString(R.string.keyCityName)));
        }
        // Fill in the Country name
        if (this.shSettings.getSettingStringValue(getString(R.string.keyCountryName)) == "") {
            this.tvWeatherCountryName.setText("No data yet available");
        } else {
            this.tvWeatherCountryName.setText(this.shSettings.getSettingStringValue(getString(R.string.keyCountryName)));
        }
        // Fill in the Temperature data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherTemperatureData)) == "") {
            this.tvWeatherTemperature.setText("No data yet available");
        } else {
            this.tvWeatherTemperature.setText(this.shSettings.getSettingStringValue(getString(R.string.keyWeatherTemperatureData)) + "°C");
        }
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherTemperatureMinData)) == "") {
            this.tvWeatherTemperatureMin.setText("No data yet available");
        } else {
            this.tvWeatherTemperatureMin.setText(this.shSettings.getSettingStringValue(getString(R.string.keyWeatherTemperatureMinData)) + "°C");
        }
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherTemperatureMaxData)) == "") {
            this.tvWeatherTemperatureMax.setText("No data yet available");
        } else {
            this.tvWeatherTemperatureMax.setText(this.shSettings.getSettingStringValue(getString(R.string.keyWeatherTemperatureMaxData)) + "°C");
        }
        // Fill in the Humidity Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherHumidityData)) == "") {
            this.tvWeatherHumidity.setText("No data yet available");
        } else {
            this.tvWeatherHumidity.setText(this.shSettings.getSettingStringValue(getString(R.string.keyWeatherHumidityData)) + " %");
        }
        // Fill in the Pressure Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherPressureData)) == "") {
            this.tvWeatherPressure.setText("No data yet available");
        } else {
            this.tvWeatherPressure.setText(this.shSettings.getSettingStringValue(getString(R.string.keyWeatherPressureData)) + " hPa");
        }
        // Fill in the Visibility Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherVisibilityData)) == "") {
            this.tvWeatherVisibility.setText("No data yet available");
        } else {
            this.tvWeatherVisibility.setText(this.shSettings.getSettingStringValue(getString(R.string.keyWeatherVisibilityData)) + " m");
        }
        // Fill in the Wind Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherWindSpeedData)) == "") {
            this.tvWeatherWindSpeed.setText("No data yet available");
        } else {
            this.tvWeatherWindSpeed.setText(this.shSettings.getSettingStringValue(getString(R.string.keyWeatherWindSpeedData)) + " m/s");
        }
        // Fill in the sunrise and sunset hours
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherSunriseData)) == "") {
            this.tvWeatherSunrise.setText("No data yet available");
        } else {
            this.tvWeatherSunrise.setText(this.shSettings.getSettingStringValue(getString(R.string.keyWeatherSunriseData)));
        }
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherSunsetData)) == "") {
            this.tvWeatherSunset.setText("No data yet available");
        } else {
            this.tvWeatherSunset.setText(this.shSettings.getSettingStringValue(getString(R.string.keyWeatherSunsetData)));
        }
        // Fill in the weather conditions
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherCondition)) == "") {
            this.tvWeatherCondition.setText("No data yet available");
        } else {
            this.tvWeatherCondition.setText(this.shSettings.getSettingStringValue(getString(R.string.keyWeatherCondition)) );
        }
    }
}
