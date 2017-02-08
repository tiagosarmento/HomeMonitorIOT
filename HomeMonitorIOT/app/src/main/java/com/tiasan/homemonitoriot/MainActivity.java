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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.pwittchen.weathericonview.WeatherIconView;

/**
 * @author Tiago Sarmento Santos
 * @class MainActivity
 * @desc This class is the application Main Activity
 */
public class MainActivity extends AppCompatActivity {

    // Set Global data
    private static final String gTag                    = "DBG - MainActivity";
    private WeatherIconView     wiWeatherConditionID    = null;
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
        setupWeatherIconViewObjects();
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
        Intent iActivity = null;
        switch (item.getItemId()) {
            case R.id.mitRefresh:
                // Call data handler to fetch sensor data from Exosite Platform
                this.gsdHandler.updateSensorData();
                this.gwdHandler.updateWeatherData();
                break;
            case R.id.mitSettings:
                // Call settings activity
                iActivity = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(iActivity);
                break;
            case R.id.mitAbout:
                // Show About Dialog message
                showAboutDialog();
                break;
            case R.id.mitPlotTemperature:
                iActivity = new Intent(MainActivity.this, TemperatureActivity.class);
                startActivity(iActivity);
                break;
            case R.id.mitPlotHumidity:
                iActivity = new Intent(MainActivity.this, HumidityActivity.class);
                startActivity(iActivity);
                break;
            case R.id.mitPlotPressure:
                iActivity = new Intent(MainActivity.this, PressureActivity.class);
                startActivity(iActivity);
                break;
            case R.id.mitPlotLight:
                iActivity = new Intent(MainActivity.this, LightActivity.class);
                startActivity(iActivity);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    //
    // From here down are the MainActivity support functions
    //

    /**
     * @author Tiago Sarmento Santos
     * @func setTextViewText
     * @desc This function is used to set a Text in a specific TextView Element
     * @param resourceID
     * @param sText
     * @return TextView
     */
    private void setTextViewText(int resourceID, String sText) {
        TextView tvElement = (TextView) findViewById(resourceID);
        tvElement.setText(sText);
    }

    /**
     * @author Tiago Sarmento Santos
     * @func getWeatherIconViewObject
     * @desc This function is used to get an handler on a specific WeatherIconView Element
     * @param resourceID
     * @return TextView
     */
    public WeatherIconView getWeatherIconViewObject(int resourceID) {
        return (WeatherIconView) findViewById(resourceID);
    }

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
     * @func setupWeatherIconViewObjects
     * @desc This function does the setup of the MainActivity WeatherIconView objects
     * @return none
     */
    private void setupWeatherIconViewObjects() {
        this.wiWeatherConditionID = (WeatherIconView) findViewById(R.id.wiWeatherConditionID);
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
            setTextViewText(R.id.tvTemperature, "No data yet available");
        } else {
            setTextViewText(R.id.tvTemperature, this.shSettings.getSettingStringValue(getString(R.string.keyTemperatureData)) + "°C");
        }
        // Fill in the Humidity Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyHumidityData)) == "") {
            setTextViewText(R.id.tvHumidity, "No data yet available");
        } else {
            setTextViewText(R.id.tvHumidity, this.shSettings.getSettingStringValue(getString(R.string.keyHumidityData)) + " %");
        }
        // Fill in the Pressure Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyPressureData)) == "") {
            setTextViewText(R.id.tvPressure, "No data yet available");
        } else {
            setTextViewText(R.id.tvPressure, this.shSettings.getSettingStringValue(getString(R.string.keyPressureData)) + " mBar");
        }
        // Fill in the Altitude Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyAltitudeData)) == "") {
            setTextViewText(R.id.tvAltitude, "No data yet available");
        } else {
            setTextViewText(R.id.tvAltitude, this.shSettings.getSettingStringValue(getString(R.string.keyAltitudeData)) + " m");
        }
        // Fill in the Visible Light Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyVisibleLightData)) == "") {
            setTextViewText(R.id.tvVisibleLight, "No data yet available");
        } else {
            setTextViewText(R.id.tvVisibleLight, this.shSettings.getSettingStringValue(getString(R.string.keyVisibleLightData)) + " Lux");
        }
        // Fill in the Infrared Light Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyInfraredLightData)) == "") {
            setTextViewText(R.id.tvInfraredLight, "No data yet available");
        } else {
            setTextViewText(R.id.tvInfraredLight, this.shSettings.getSettingStringValue(getString(R.string.keyInfraredLightData)) + " Lux");
        }
        // Fill in the Last update Received
        if (this.shSettings.getSettingStringValue(getString(R.string.keyLastUpdateTime)) == "") {
            setTextViewText(R.id.tvSensorFinal, "Press 'Refresh' on ActionBar to get new data...");
        } else {
            setTextViewText(R.id.tvSensorFinal, this.shSettings.getSettingStringValue(getString(R.string.keyLastUpdateTime)) );
        }
    }

    /**
     * @author Tiago Sarmento Santos
     * @func populateWeatherDataTableView
     * @desc This function populates the MainActivity Weather TableView with the weather data
     * @return none
     */
    private void populateWeatherDataTableView() {
        // Fill in the Local/City name
        if (this.shSettings.getSettingStringValue(getString(R.string.keyCityName)) == "") {
            setTextViewText(R.id.tvWeatherLocalName, "No data yet available");
        } else {
            setTextViewText(R.id.tvWeatherLocalName, this.shSettings.getSettingStringValue(getString(R.string.keyCityName)) + "," + " " + this.shSettings.getSettingStringValue(getString(R.string.keyCountryName)));
        }
        // Fill in the weather conditions
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherCondition)) == "") {
            setTextViewText(R.id.tvWeatherCondition, "No data yet available");
        } else {
            setTextViewText(R.id.tvWeatherCondition, this.shSettings.getSettingStringValue(getString(R.string.keyWeatherCondition)) );
        }
        // Fill in the weather condition ID icon
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_day_sunny));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.darkorange));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("01d")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_day_sunny));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.darkorange));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("01n")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_night_clear));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.lightgray));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("02d")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_day_cloudy));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.darkorange));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("02n")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_night_alt_cloudy));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.lightgray));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("03d")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_cloud));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.lightgray));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("03n")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_cloud));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.lightgray));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("04d")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_cloudy));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.lightgray));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("04n")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_cloudy));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.lightgray));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("09d")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_rain));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.lightgray));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("09n")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_rain));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.lightgray));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("10d")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_day_rain));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.lightgray));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("10n")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_night_alt_rain));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.lightgray));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("11d")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_day_thunderstorm));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.lightgray));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("11n")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_night_thunderstorm));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.lightgray));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("13d")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_day_snow));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.lightgray));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("13n")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_night_snow));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.lightgray));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("50d")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_fog));
        } else if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherConditionID)).equals("50n")) {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_fog));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.lightgray));
        } else {
            this.wiWeatherConditionID.setIconResource(getString(R.string.wi_day_sunny));
            this.wiWeatherConditionID.setIconColor(ContextCompat.getColor(this, R.color.darkorange));
        }

        // Fill in the Temperature data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherTemperatureData)) == "") {
            setTextViewText(R.id.tvWeatherTemperature, "No data yet available");
        } else {
            setTextViewText(R.id.tvWeatherTemperature, this.shSettings.getSettingStringValue(getString(R.string.keyWeatherTemperatureData)) + "°C");
        }
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherTemperatureMinData)) == "") {
            setTextViewText(R.id.tvWeatherTemperatureMin, "No data yet available");
        } else {
            setTextViewText(R.id.tvWeatherTemperatureMin, this.shSettings.getSettingStringValue(getString(R.string.keyWeatherTemperatureMinData)) + "°C");
        }
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherTemperatureMaxData)) == "") {
            setTextViewText(R.id.tvWeatherTemperatureMax, "No data yet available");
        } else {
            setTextViewText(R.id.tvWeatherTemperatureMax, this.shSettings.getSettingStringValue(getString(R.string.keyWeatherTemperatureMaxData)) + "°C");
        }
        // Fill in the Humidity Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherHumidityData)) == "") {
            setTextViewText(R.id.tvWeatherHumidity, "No data yet available");
        } else {
            setTextViewText(R.id.tvWeatherHumidity, this.shSettings.getSettingStringValue(getString(R.string.keyWeatherHumidityData)) + " %");
        }
        // Fill in the Pressure Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherPressureData)) == "") {
            setTextViewText(R.id.tvWeatherPressure, "No data yet available");
        } else {
            setTextViewText(R.id.tvWeatherPressure, this.shSettings.getSettingStringValue(getString(R.string.keyWeatherPressureData)) + " hPa");
        }
        // Fill in the Visibility Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherVisibilityData)) == "") {
            setTextViewText(R.id.tvWeatherVisibility, "No data yet available");
        } else {
            setTextViewText(R.id.tvWeatherVisibility, this.shSettings.getSettingStringValue(getString(R.string.keyWeatherVisibilityData)) + " m");
        }
        // Fill in the Wind Data
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherWindSpeedData)) == "") {
            setTextViewText(R.id.tvWeatherWindSpeed, "No data yet available");
        } else {
            setTextViewText(R.id.tvWeatherWindSpeed, this.shSettings.getSettingStringValue(getString(R.string.keyWeatherWindSpeedData)) + " m/s");
        }
        // Fill in the sunrise and sunset hours
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherSunriseData)) == "") {
            setTextViewText(R.id.tvWeatherSunrise, "No data yet available");
        } else {
            setTextViewText(R.id.tvWeatherSunrise, this.shSettings.getSettingStringValue(getString(R.string.keyWeatherSunriseData)));
        }
        if (this.shSettings.getSettingStringValue(getString(R.string.keyWeatherSunsetData)) == "") {
            setTextViewText(R.id.tvWeatherSunset, "No data yet available");
        } else {
            setTextViewText(R.id.tvWeatherSunset, this.shSettings.getSettingStringValue(getString(R.string.keyWeatherSunsetData)));
        }
    }
}
