package com.example.unit271.geofencetest1;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by unit271 on 3/19/16.
 */
public class TrackingService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback {
    private GoogleApiClient mGoogleApiClient;
    public Location mLastLocation;
    public LocationRequest mLocationRequest;
    public Location mCurrentLocation;
    public boolean timerNotificationState;
    ValueEventListener personListenerObject;
    public int normStart, normEnd, wedStart, wedEnd, thursStart, thursEnd;
    public boolean nPer1, nPer6, nPer7;
    CountDownTimer dTimer1;
    String teamID;
    String weekDay;
    final Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
    HashMap school;
    SharedPreferences teamNumData;
    DatabaseReference mDatabase, personDirectory;
    public static String filename = "NumberHolder";
    public boolean isRunning;
    final NotificationCompat.Builder loginBuilder = new NotificationCompat.Builder(this);
    final NotificationCompat.Builder logoutBuilder = new NotificationCompat.Builder(this);
    String schoolName;
    public boolean rangeCheckerA;
    public boolean rangeCheckerB;
    boolean tracking = false;
    NotificationManager mNotificationManager;
    DatabaseReference connectedRef;
    public boolean stillConnected;
    public String LastSigninRobotics;
    int totalRoboticsTime;

    @Override
    public void onCreate() {
        Log.i("SERVICE1", "Service onCreate");
        super.onCreate();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SERVICE1", "Service onStartCommand");
        Log.i("INITIALRANGE", "TRACKINGSERVICERESTART");
        Log.i("INITIALRANGE", "TRACKINGSERVICERESTART");
        Log.i("INITIALRANGE", "TRACKINGSERVICERESTART");
        Log.i("INITIALRANGE", "TRACKINGSERVICERESTART");
        isRunning = false;
        teamNumData = getSharedPreferences(filename, 0);
        Log.i("INITIALRANGE", String.valueOf(teamNumData.getBoolean("setInitialRange", true)));
        teamID = teamNumData.getString("newIDKey", "NONE");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        personDirectory = mDatabase.child("People").child(teamID);
        stillConnected = true;
//        connectedRef = mDatabase.child(".info").child("connected");
//        connectedRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                boolean connected = dataSnapshot.getValue(Boolean.class);
//                if (connected) {
//                    stillConnected = true;
//                } else {
//                    stillConnected = false;
//                    SharedPreferences.Editor editor = teamNumData.edit();
//                    editor.putBoolean("setInitialRange", true);
//                    editor.commit();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        personListener();

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent resultIntent = new Intent(this, NotificationLogOut.class);
        PendingIntent contentIntent = PendingIntent.getService(this,
                0, resultIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Constants constInstance = new Constants();
        constInstance.initializeMaps();
        schoolName = teamNumData.getString("schoolName", "NONE");
        school = constInstance.getSchool(schoolName);
        nPer1 = teamNumData.getBoolean("free1", false);
        nPer6 = teamNumData.getBoolean("free6", false);
        nPer7 = teamNumData.getBoolean("free7", false);

        if(nPer1){
            normStart = (Integer) school.get("N1");
            wedStart = (Integer) school.get("WedN1");
            thursStart = (Integer) school.get("ThuN1");
        } else {
            normStart = (Integer) school.get("AStart");
            wedStart = (Integer) school.get("WedAStart");
            thursStart = (Integer) school.get("ThuAStart");
        }
        if(nPer6 && nPer7){
            normEnd = (Integer) school.get("N6N7");
            wedEnd = (Integer) school.get("WedN6N7");
            thursEnd = (Integer) school.get("ThuN6N7");
        } else if(!nPer6 && nPer7) {
            normEnd = (Integer) school.get("N7");
            wedEnd = (Integer) school.get("WedN7");
            thursEnd = (Integer) school.get("ThuN7");
        } else {
            normEnd = (Integer) school.get("AEnd");
            wedEnd = (Integer) school.get("WedAEnd");
            thursEnd = (Integer) school.get("ThuAEnd");
        }


        loginBuilder.setContentTitle(teamID + " : Logged In");
        loginBuilder.setContentText("Click to Log Out");
        loginBuilder.setContentIntent(contentIntent);
        loginBuilder.setSmallIcon(R.drawable.check);
        loginBuilder.setSound(alarmSound);
        loginBuilder.setLights(Color.GREEN, 500, 1000);
        loginBuilder.setAutoCancel(true);

        logoutBuilder.setContentTitle(teamID + " : Logged Out");
        logoutBuilder.setContentText("You have been signed out of Citrus Circuits");
        logoutBuilder.setSmallIcon(R.drawable.doublecheck);
        logoutBuilder.setSound(alarmSound);
        logoutBuilder.setLights(Color.GREEN, 500, 1000);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        dTimer1 = new CountDownTimer(40000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.i("onTick", String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                if(stillConnected) {
                    for (int a = 0; a <= 2; a++) {
                        Log.i("onFinish", "sendToForm");
                    }
                    isRunning = false;
                    rangeCheckerA = teamNumData.getBoolean("rangeBoolean", true); // should return true here after first use
                    if (!(rangeCheckerB == rangeCheckerA)) {
                        sendToForm(rangeCheckerA);
                        if (timerNotificationState) {
                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(0, loginBuilder.build());
                        } else {
                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(0, logoutBuilder.build());
                        }
                    } else {
                        Log.i("onFinish", "RangeDisagreement");
                    }
                }
            }
        };

        if(mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else {
            Log.i("LOGS1", "GOOGLEAPICLIENTISNULL");
            mGoogleApiClient.connect();
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        Log.i("INITIALRANGE", "ONDESTROY");
        stopLocationUpdates();
        personDirectory.removeEventListener(personListenerObject);
        SharedPreferences.Editor spEditor = teamNumData.edit();
        spEditor.putBoolean("setInitialRange", true);
        spEditor.commit();
        Toast.makeText(getApplicationContext(), "Tracking Disabled.",
                Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        createLocationRequest();
        tracking = true;
        Log.i("TRACKINGSERVICE", "onConnectedComplete");
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Log.i("TRACKINGSERVICE", "STARTING LOCATION UPDATES");

    }

    protected void stopLocationUpdates() {
        if(tracking){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            tracking = false;
        } else {

        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(7000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        startLocationUpdates();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("INITIALRANGE", "ONLOCATIONCHANGED");

        if(stillConnected) {
            mLastLocation = mCurrentLocation;
            mCurrentLocation = location;

            double locLatitude = location.getLatitude();
            double locLongitude = location.getLongitude();

            weekDay = dayFormat.format(calendar.getTime());
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat hourFormatter = new SimpleDateFormat("HH");
            SimpleDateFormat minuteFormatter = new SimpleDateFormat("mm");
            SimpleDateFormat summerFormatter = new SimpleDateFormat("MMdd", Locale.US);
            String monthDayString = summerFormatter.format(date);
            String hourString = hourFormatter.format(date);
            String minuteString = minuteFormatter.format(date);
            int monthDayInt = Integer.parseInt(monthDayString);
            int hourTimeInt = Integer.parseInt(hourString);
            int minuteTimeInt = Integer.parseInt(minuteString);
            int currentTime = (hourTimeInt * 100) + minuteTimeInt;
            Log.i("NORMTIMENORMTIME", normStart + "    " + normEnd);
            rangeCheckerA = teamNumData.getBoolean("rangeBoolean", false);
            if (!rangeCheckerA && (locLatitude >= 38.55608987 && locLatitude <= 38.557) && (locLongitude >= -121.7522 && locLongitude <= -121.75105237)) {
                if(monthDayInt <= 606 && monthDayInt >= 824) {
                    if (weekDay.equals("Wednesday")) {
                        if ((currentTime < wedStart) || (currentTime > wedEnd)) {
                            Log.i("INITIALRANGE", "WITHIN RANGE");
                            SharedPreferences.Editor editor = teamNumData.edit();
                            editor.putBoolean("rangeBoolean", true);
                            editor.commit();
                            timerNotificationState = true;

                            if (isRunning) {
                                Log.i("DTIMER", "CANCELLING");
                                dTimer1.cancel();
                                isRunning = false;
                            } else {
                                Log.i("DTIMER", "STARTING");
                                dTimer1.start();
                                isRunning = true;
                            }

                        }
                    } else if (weekDay.equals("Thursday")) {
                        if ((currentTime < thursStart) || (currentTime > thursEnd)) {
                            Log.i("INITIALRANGE", "WITHIN RANGE");
                            SharedPreferences.Editor editor = teamNumData.edit();
                            editor.putBoolean("rangeBoolean", true);
                            editor.commit();
                            timerNotificationState = true;
                            if (isRunning) {
                                Log.i("DTIMER", "CANCELLING");
                                dTimer1.cancel();
                                isRunning = false;
                            } else {
                                Log.i("DTIMER", "STARTING");
                                dTimer1.start();
                                isRunning = true;
                            }

                        }
                    } else if (weekDay.equals("Saturday") || weekDay.equals("Sunday")) {
                        Log.i("INITIALRANGE", "WITHIN RANGE");
                        SharedPreferences.Editor editor = teamNumData.edit();
                        editor.putBoolean("rangeBoolean", true);
                        editor.commit();
                        timerNotificationState = true;
                        if (isRunning) {
                            Log.i("DTIMER", "CANCELLING");
                            dTimer1.cancel();
                            isRunning = false;
                        } else {
                            Log.i("DTIMER", "STARTING");
                            dTimer1.start();
                            isRunning = true;
                        }
                    } else {
                        Log.i("NORMTIMENORMTIME", normStart + "    " + normEnd);
                        if ((currentTime < normStart) || (currentTime > normEnd)) {
                            Log.i("INITIALRANGE", "WITHIN RANGE");
                            SharedPreferences.Editor editor = teamNumData.edit();
                            editor.putBoolean("rangeBoolean", true);
                            editor.commit();
                            timerNotificationState = true;
                            if (isRunning) {
                                Log.i("DTIMER", "CANCELLING");
                                dTimer1.cancel();
                                isRunning = false;
                            } else {
                                Log.i("DTIMER", "STARTING");
                                dTimer1.start();
                                isRunning = true;
                            }

                        }
                    }
                } else {
                    Log.i("INITIALRANGE", "WITHIN RANGE SUMMER");
                    SharedPreferences.Editor editor = teamNumData.edit();
                    editor.putBoolean("rangeBoolean", true);
                    editor.commit();
                    timerNotificationState = true;
                    if (isRunning) {
                        Log.i("DTIMER", "CANCELLING");
                        dTimer1.cancel();
                        isRunning = false;
                    } else {
                        Log.i("DTIMER", "STARTING");
                        dTimer1.start();
                        isRunning = true;
                    }
                }
            } else if (rangeCheckerA && ((locLatitude < 38.55608987 || locLatitude > 38.557) || (locLongitude < -121.7522 || locLongitude > -121.75105237))) {
                Log.i("INITIALRANGE", "OUTSIDE RANGE");
                timerNotificationState = false;

                SharedPreferences.Editor editor = teamNumData.edit();
                editor.putBoolean("rangeBoolean", false);
                editor.commit();
                if (isRunning) {
                    Log.i("DTIMER", "CANCELLING");
                    dTimer1.cancel();
                    isRunning = false;
                } else {
                    Log.i("DTIMER", "STARTING");
                    dTimer1.start();
                    isRunning = true;
                }
            }
        }
    }

    public void sendToForm(boolean rangeCheckerBoolean){
        final String tmpTeamID = teamID;
        final boolean tmpRangeCheckerA = rangeCheckerBoolean;
        new Thread() {
            @Override
            public void run() {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat uploadFormatter = new SimpleDateFormat("MM-dd-yyyy-HHmm", Locale.US);
                String uploadDate = uploadFormatter.format(date);
                Date lastDate = null;
                try {
                    if(LastSigninRobotics != null) {
                        lastDate = uploadFormatter.parse(LastSigninRobotics);
                    }
                } catch(ParseException pe){
                    pe.printStackTrace();
                }
                int finalDateInt = 0;
                if(LastSigninRobotics != null){
                    if(lastDate != null) {
                        finalDateInt = (int) (date.getTime() - lastDate.getTime());
                    }
                    personDirectory.child("TotalRobotics").setValue(totalRoboticsTime + (finalDateInt / (1000*60)));
                    personDirectory.child("LastSigninRobotics").setValue(null);
                    personDirectory.child("RoboticsLogs").child(uploadDate).child("Time").setValue(finalDateInt / (1000*60));
                    personDirectory.child("RoboticsLogs").child(uploadDate).child("Status").setValue("Out");
                } else {
                    LoginoutObject signSelfObject = new LoginoutObject();
                    signSelfObject.setTime(0);
                    signSelfObject.setStatus("In");
                    personDirectory.child("LastSigninRobotics").setValue(uploadDate);
                    personDirectory.child("RoboticsLogs").child(uploadDate).setValue(signSelfObject);
                }

            }
        }.start();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Result result) {
        Log.i("GEOINFO", "ONRESULT: ");
    }

    public void personListener(){
        personListenerObject = (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot infoSnapshot : dataSnapshot.getChildren()) {
                    if (infoSnapshot.getKey().equals("LastSigninRobotics")) {
                        LastSigninRobotics = infoSnapshot.getValue().toString();
                        if(infoSnapshot.getValue() != null) {
                            rangeCheckerB = true;
                        } else {
                            rangeCheckerB = false;
                        } //TODO: fix manual signin, only notification should disable this, otherwise
                        //TODO : always change rangeCheckerA to infosnapshot
                        if (teamNumData.getBoolean("setInitialRange", true)) { //Return true after first use
                            SharedPreferences.Editor editor = teamNumData.edit();
                            if(infoSnapshot.getValue() != null){
                                editor.putBoolean("rangeBoolean", true);
                            } else {
                                editor.putBoolean("rangeBoolean", false);
                            }
                            editor.putBoolean("setInitialRange", false);
                            editor.commit();
                            Log.i("INITIALRANGEA", String.valueOf(teamNumData.getBoolean("rangeBoolean", false)));
                        }
                    }
                    if (infoSnapshot.getKey().equals("TotalRobotics")){
                        totalRoboticsTime = ((Long) infoSnapshot.getValue()).intValue();
                    }
                }
                stillConnected = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        personDirectory.addValueEventListener(personListenerObject);
    }
}


