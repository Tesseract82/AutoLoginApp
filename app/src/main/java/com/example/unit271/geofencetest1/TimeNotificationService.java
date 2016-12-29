package com.example.unit271.geofencetest1;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by unit271 on 12/28/16.
 */
public class TimeNotificationService extends Service {
    public static String filename = "NumberHolder";
    SharedPreferences teamNumData;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){ //TODO : Stop service if person logs out?
        super.onStartCommand(intent, flags, startId);
        teamNumData = getSharedPreferences(filename, 0);
        Log.i("TIMENOTIFICATIONSERVICE", "ONSTARTCOMMAND");
        final Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final NotificationCompat.Builder timeBuilder = new NotificationCompat.Builder(getBaseContext());
        timeBuilder.setContentTitle("Robotics Logout Reminder");
        timeBuilder.setContentText("Remember to log out if you are leaving or have left.");
        timeBuilder.setSound(alarmSound);
        timeBuilder.setLights(Color.GREEN, 500, 1000);
        timeBuilder.setSmallIcon(R.drawable.doublecheck);

        int notifHours = teamNumData.getInt("notifHours", 0);
        int notifMinutes = teamNumData.getInt("notifMinutes", 0);
        int notifSeconds = (notifMinutes * 60) + (notifHours * 3600);
        int notifMilliSeconds = notifSeconds * 1000;
        CountDownTimer loginTimer = new CountDownTimer(notifMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("LOGINTIMER", String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mNotificationManager.notify(0, timeBuilder.build());
                stopSelf();
            }
        };
        loginTimer.start();
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
