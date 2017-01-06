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

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * @author Tiago Sarmento Santos
 * @class AlarmService
 * @desc This class extends IntentService, and it does the actual Alarm work.
 */
public class AlarmService extends IntentService {

    // Set Global data
    private static final String gTag    = "DBG - AlarmService";
    private SettingsHandler gshSettings = null;

    /**
     * @author Tiago Sarmento Santos
     * @desc This is the AlarmService class constructor
     * @constructor AlarmService
     */
    public AlarmService() {
        // Set a string name, to be used to identify the Alarm in the system
        super("UpdateDataAlarmService");
    }

    /**
     * @author Tiago Sarmento Santos
     * @func onHandleIntent
     * @desc This function is called when the service is triggered, it does the Service work
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(gTag, "The onHandleIntent() event");
        // Create hook on application settings, private settings are needed to hold sensor data
        gshSettings = new SettingsHandler(this);
        SensorDataHandler sdHandler = new SensorDataHandler(this, false);
        sdHandler.updateSensorData();
        Log.d(gTag, "The updateSensorData() done");
        gshSettings.printSettings();
        // Now that we got sensor data, issue a Notification if needed
        String sTempVal = gshSettings.getSettingStringValue(getString(R.string.keyTemperatureData));
        if ( 15 >= Float.parseFloat(sTempVal) || 20 <= Float.parseFloat(sTempVal) ) {
            issueNotification();
        }
        // Release the device WakeLock, this was locked by AlarmReceiver
        AlarmReceiver.completeWakefulIntent(intent);
    }

    /**
     * @author Tiago Sarmento Santos
     * @func issueNotification
     * @desc This functions issues an alert Notification on device's task bar.
     */
    private void issueNotification() {
        // Set an ID for the notification, you can use this ID to update the notification later on
        int nID = 1;
        // Create NotificationBuilder
        NotificationCompat.Builder nNotiBuilder = new NotificationCompat.Builder(this);
        // Set Notification builder mandatory parameters
        nNotiBuilder.setSmallIcon(R.mipmap.ic_launcher);
        nNotiBuilder.setContentTitle("HomeMonitorIOT Alert");
        // Set other attributes
        nNotiBuilder.setAutoCancel(true);
        // Set the Alert case message
        String sTempVal = gshSettings.getSettingStringValue(getString(R.string.keyTemperatureData));
        if ( 15 >= Float.parseFloat(sTempVal) ) {
            nNotiBuilder.setContentText("Low Temperature registered: " + sTempVal + "°C");
        } else if ( 20 <= Float.parseFloat(sTempVal)) {
            nNotiBuilder.setContentText("High Temperature registered: " + sTempVal + "°C");
        } else {
            nNotiBuilder.setContentText("Generic weather alert!");
        }
        // Create Intent to call MainActivity of your app
        Intent iMainActivity = new Intent(this, MainActivity.class);
        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of your application
        // to the Home screen.
        TaskStackBuilder tsbBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        tsbBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        tsbBuilder.addNextIntent(iMainActivity);
        PendingIntent piMainActivity = tsbBuilder.getPendingIntent( 0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        nNotiBuilder.setContentIntent(piMainActivity);
        NotificationManager nmNotiManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nmNotiManager.notify(nID, nNotiBuilder.build());
    }
}