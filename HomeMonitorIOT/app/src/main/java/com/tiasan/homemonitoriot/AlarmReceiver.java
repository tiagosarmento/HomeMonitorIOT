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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * @author Tiago Sarmento Santos
 * @class AlarmReceiver
 * @desc TODO
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    // Set Global data
    private static final String gTag = "DBG - AlarmReceiver";

    private AlarmManager amAlarmManager = null; // AlarmManager to access system alarms
    private PendingIntent piAlarmIntent = null; // PendingIntent to be used when the alarm fires

    /**
     * @author Tiago Sarmento Santos
     * @func onReceive
     * @desc This function triggers the action to be done when the alarm fires
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(gTag, "The onReceive() event");
        // Create and start the Alarm Service, keeping the device awake while it is running
        // Device WakeLock is released by the Alarm Service
        Intent iService = new Intent(context, AlarmService.class);
        startWakefulService(context, iService);
    }

    /**
     * From Android documentation:
     * This class provides access to the system alarm services. These allow you to schedule your
     * application to be run at some point in the future. When an alarm goes off, the Intent that
     * had been registered for it is broadcast by the system, automatically starting the target
     * application if it is not already running. Registered alarms are retained while the device
     * is asleep (and can optionally wake the device up if they go off during that time), but will
     * be cleared if it is turned off and rebooted.
     *
     * @author Tiago Sarmento Santos
     * @func setAlarm
     * @desc This function sets the AlarmManager and the Intent for the Alarm Action (Service)
     * @param context
     */
    public void setAlarm(Context context) {
        Log.d(gTag, "The setAlarm() event");
        amAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent iAlarmIntent = new Intent(context, AlarmReceiver.class);
        piAlarmIntent = PendingIntent.getBroadcast(context, 0, iAlarmIntent, 0);
        // Set the alarm to fire every 15 minutes (1000*60*15)
        amAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(),
                1000*60*15,
                piAlarmIntent);
        // Create hook to enable AlarmBootReceiver to automatically restart the alarm when
        // the device is rebooted
        ComponentName arAlarmReceiver = new ComponentName(context, AlarmBootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(arAlarmReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * @author Tiago Sarmento Santos
     * @func cancelAlarm
     * @desc This function cancels the alarm
     * @param context
     */
    public void cancelAlarm(Context context) {
        Log.d(gTag, "The cancelAlarm() event");
        // If the alarm has been set, cancel it.
        if (amAlarmManager!= null) {
            amAlarmManager.cancel(piAlarmIntent);
        }
        // Disable AlarmBootReceiver so that it does not automatically restart the alarm when the
        // device is rebooted
        ComponentName arAlarmReceiver = new ComponentName(context, AlarmBootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(arAlarmReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
