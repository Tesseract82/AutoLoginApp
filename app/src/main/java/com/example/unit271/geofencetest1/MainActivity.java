package com.example.unit271.geofencetest1;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends FragmentActivity {

    SharedPreferences teamNumData;
    private String teamID;
    private TextView numView;
    public boolean switchPermission;
    public boolean buttonPermission;
    public boolean startPerm;
    public Switch locationSwitch;
    public static String filename = "NumberHolder";
    private DatabaseReference mDatabase, fbRef1;
    public Button manualButton;
    CountDownTimer dTimer2;
    boolean countingDown;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("EXIT", false)) {
            finish();
        }

        setContentView(R.layout.activity_main);
        setTitle("Automatic Login App");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        teamNumData = getSharedPreferences(filename, 0);
        setTitle("Citrus Circuits");

        manualButton = (Button) findViewById(R.id.manualButton);
        numView = (TextView) findViewById(R.id.teamNumView2);
        locationSwitch = (Switch) findViewById(R.id.locationSwitch);
        switchPermission = teamNumData.getBoolean("switchPermission", true);
        locationSwitch.setChecked(switchPermission);
        SharedPreferences.Editor editor = teamNumData.edit();
        editor.putBoolean("setInitialRange", locationSwitch.isChecked());
        editor.commit();
        if(!teamNumData.getBoolean("setupComplete", false)) {
            Intent numChange = new Intent(this, chooseTeamMember.class);
            startActivity(numChange);
        }

        countingDown = false;
        dTimer2 = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countingDown = true;
            }

            @Override
            public void onFinish() {
                countingDown = false;
            }
        };

        teamID = teamNumData.getString("newIDKey", "NONE");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        fbRef1 = mDatabase.child("People").child(teamID).child("CurrentlySignedInRobotics");
        fbRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if((boolean) dataSnapshot.getValue()){
                    manualButton.setText("Sign Out");
                } else {
                    manualButton.setText("Sign In");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(teamID.equals("NONE")){
            startPerm = false;
            numView.setText("No Name");
            numView.setTextSize(30);
        } else {
            startPerm = true;
            numView.setText(teamID);
            numView.setTextSize(30);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 167801);
            buttonPermission = false;
            return;
        } else {
            buttonPermission = true;
        }

        if(startPerm && buttonPermission && switchPermission){
            startService();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case 167801 :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    buttonPermission = true;
                    if(startPerm && switchPermission){
                        startService();
                    }
                }
        }
    }


    public Intent startService() {
        Toast.makeText(getApplicationContext(), "Tracking Initialized.",
                Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, TrackingService.class);
        startService(i);
        Log.i("INITIALIZATION", "SERVICE STARTED");
        return i;
    }

    public Intent stopService() {
        Intent i = new Intent(this, TrackingService.class);
        stopService(i);
        Log.i("INITIALIZATION", "SERVICE STOPPED");
        return i;
    }

    public void changeNumber(View view){
        if(!locationSwitch.isChecked()) {
            Intent numChange = new Intent(this, ChangeTeamNumber.class);
            startActivity(numChange);
        } else {
            Toast.makeText(getApplicationContext(), "Please Disable Automatic Mode First",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void signInOther(View view){
        if(!locationSwitch.isChecked()) {
            Intent signOther = new Intent(this, signInOther.class);
            startActivity(signOther);
        } else {
            Toast.makeText(getApplicationContext(), "Please Disable Automatic Mode First",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void processSwitch(View view){
        boolean switchState = locationSwitch.isChecked();
        SharedPreferences.Editor editor = teamNumData.edit();
        if(switchState){
            switchPermission = true;
            editor.putBoolean("switchPermission", true);
        } else {
            switchPermission = false;
            editor.putBoolean("switchPermission", false);
            stopService();
        }
        editor.putBoolean("setInitialRange", switchState);
        editor.commit();

        switchPermission = teamNumData.getBoolean("switchPermission", true);
        if(switchPermission && buttonPermission && startPerm){
            startService();
        }
    }

    public void decreaseHours(View view){
        if(!locationSwitch.isChecked()) {
            if(startPerm) {
                Intent decreaseHoursIntent = new Intent(this, decreaseHours.class);
                decreaseHoursIntent.putExtra("com.example.unit271.geofencetest1/MainActivity", teamID);
                startActivity(decreaseHoursIntent);
            } else {
                Toast.makeText(getApplicationContext(), "Create a Team Name First.",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please Disable Automatic Mode First",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void manualSignIn(View view){
        if(!locationSwitch.isChecked()) {
            if(startPerm) {
                Intent manualSignInIntent = new Intent(this, ManualSignIn.class);
                manualSignInIntent.putExtra("com.example.unit271.geofencetest1/MainActivity2", teamID);
                startActivity(manualSignInIntent);
            } else {
                Toast.makeText(getApplicationContext(), "Create a Team Name First.",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please Disable Automatic Mode First",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onNewProfClick(View view){
        if(!locationSwitch.isChecked()) {
            Intent chooseTeamMemberIntent = new Intent(this, chooseTeamMember.class);
            startActivity(chooseTeamMemberIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Please Disable Automatic Mode First",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed(){
        if(!countingDown){
            dTimer2.start();
            countingDown = true;
            Toast.makeText(getBaseContext(), "Press Back Again to Exit", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
    }
}
