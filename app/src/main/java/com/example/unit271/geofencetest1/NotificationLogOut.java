package com.example.unit271.geofencetest1;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by unit271 on 6/27/16.
 */
//public class NotificationLogOut extends Service {
//    public static String filename = "NumberHolder";
//    String teamID;
//    SharedPreferences teamNumData;
//    DatabaseReference mDatabase, personDirectory;
//    String LastSigninRobotics;
//    int totalRoboticsTime;
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        teamNumData = getSharedPreferences(filename, 0);
//        teamID = teamNumData.getString("newIDKey", "NONE");
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        personDirectory = mDatabase.child("People").child(teamID);
//        personDirectory.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot infoSnapshot : dataSnapshot.getChildren()) {
//                    if (infoSnapshot.getKey().equals("LastSigninRobotics")) {
//                        LastSigninRobotics = infoSnapshot.getValue().toString();
//                    }
//                    if (infoSnapshot.getKey().equals("TotalRobotics")){
//                        totalRoboticsTime = ((Long) infoSnapshot.getValue()).intValue();
//                    }
//                }
//                if (LastSigninRobotics != null) {
//                    personDirectory.removeEventListener(this);
//                    notificationLogoutMethod();
//                } else {
//                    personDirectory.removeEventListener(this);
//                    stopNotificationService();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        return Service.START_STICKY;
//    }
//
//    public void notificationLogoutMethod(){
//        Date date = new Date(System.currentTimeMillis());
//        SimpleDateFormat uploadFormatter = new SimpleDateFormat("MM-dd-yyyy-HHmm", Locale.US);
//        Date lastDate = null;
//        try {
//            if(LastSigninRobotics != null) {
//                lastDate = uploadFormatter.parse(LastSigninRobotics);
//            }
//        } catch(ParseException pe){
//            pe.printStackTrace();
//        }
//        int finalDateInt = 0;
//        if(lastDate != null) {
//            finalDateInt = (int) (date.getTime() - lastDate.getTime());
//        }
//        mDatabase.child("People").child(teamID).child("TotalRobotics").setValue(totalRoboticsTime + (finalDateInt / (1000*60)));
//        mDatabase.child("People").child(teamID).child("RoboticsLogs").child(LastSigninRobotics).child("Status").setValue("Out");
//        mDatabase.child("People").child(teamID).child("RoboticsLogs").child(LastSigninRobotics).child("Time").setValue(finalDateInt / (1000*60));
//        mDatabase.child("People").child(teamID).child("LastSigninRobotics").setValue(null);
//        stopNotificationService();
//    }
//
//    public void stopNotificationService(){
//        this.stopSelf();
//    }
//}
