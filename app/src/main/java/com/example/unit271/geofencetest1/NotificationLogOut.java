package com.example.unit271.geofencetest1;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by unit271 on 6/27/16.
 */
public class NotificationLogOut extends Service {
    public static String filename = "NumberHolder";
    String teamID;
    SharedPreferences teamNumData;
    Firebase personDirectory;
    boolean currentlySignedInRobotics;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Firebase.setAndroidContext(this);
        teamNumData = getSharedPreferences(filename, 0);
        teamID = teamNumData.getString("newIDKey", "NONE");
        personDirectory = new Firebase("https://loginapptestcc.firebaseio.com/People/" + teamID);
        personDirectory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot infoSnapshot : dataSnapshot.getChildren()) {
                    if (infoSnapshot.getKey().equals("CurrentlySignedInRobotics")) {
                        currentlySignedInRobotics = (boolean) infoSnapshot.getValue();
                    }
                }
                if (currentlySignedInRobotics) {
                    personDirectory.removeEventListener(this);
                    notificationLogoutMethod();
                } else {
                    personDirectory.removeEventListener(this);
                    stopNotificationService();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return Service.START_STICKY;
    }

    public void notificationLogoutMethod(){
        LoginoutObject notificationLogOut = new LoginoutObject();
        notificationLogOut.setAction("Out");
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat uploadFormatter = new SimpleDateFormat("MM-dd-yyyy-HHmm", Locale.US);
        String finalDateFormat = uploadFormatter.format(date);
        notificationLogOut.setTime(finalDateFormat);
        notificationLogOut.setLocation("Robotics");
        Firebase dataRef = new Firebase("https://loginapptestcc.firebaseio.com/");
        dataRef.child("People").child(teamID).child("Logins").push().setValue(notificationLogOut);
        dataRef.child("People").child(teamID).child("CurrentlySignedInRobotics").setValue(false);
        stopNotificationService();
    }

    public void stopNotificationService(){
        this.stopSelf();
    }
}
