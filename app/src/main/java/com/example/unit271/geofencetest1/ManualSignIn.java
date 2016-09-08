package com.example.unit271.geofencetest1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ManualSignIn extends AppCompatActivity {

    String teamName;
    TextView teamNameDisplay;
    Button signInSelf;
    DatabaseReference mDatabase, dataRef7;
    boolean currentlySignedInRobotics;
    int totalRoboticsTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_sign_in);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("Automatic Login App");
        Button button = (Button) findViewById(R.id.buttonManualSignIn);
        if(currentlySignedInRobotics){
            button.setText("Logout");
        } else {
            button.setText("Login");
        }
        teamName = getIntent().getStringExtra("com.example.unit271.geofencetest1/MainActivity2");
        teamNameDisplay = (TextView) findViewById(R.id.textViewManualName);
        signInSelf = (Button) findViewById(R.id.buttonManualSignIn);
        teamNameDisplay.setText(teamName);
        signInSelf.setEnabled(false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        dataRef7 = mDatabase.child("People").child(teamName);
        dataRef7.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot infoSnapshot : dataSnapshot.getChildren()) {
                    if (infoSnapshot.getKey().equals("CurrentlySignedInRobotics")) {
                        currentlySignedInRobotics = (boolean) infoSnapshot.getValue();
                    }
                    if (infoSnapshot.getKey().equals("TotalRobotics")){
                        totalRoboticsTime = (int) infoSnapshot.getValue();
                    }
                }
                signInSelf.setEnabled(true);
                setViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setViews(){
        if(currentlySignedInRobotics){
            signInSelf.setText("SIGN OUT");
        } else {
            signInSelf.setText("SIGN IN");
        }
        signInSelf.setTextColor(Color.BLACK);
    }

    public void onSignButtonClick2(View view){
        Date date = new Date(System.currentTimeMillis());

        SimpleDateFormat uploadFormatter = new SimpleDateFormat("MM-dd-yyyy-HHmm", Locale.US);
        String uploadDate = uploadFormatter.format(date);

        if(currentlySignedInRobotics){
            dataRef7.child("CurrentlySignedInRobotics").setValue(false);
            dataRef7.child("Logins").child(uploadDate).child("Status").setValue("Out");
            dataRef7.child("Logins").child(uploadDate).child("Time").setValue(uploadDate);
        } else {
            LoginoutObject signSelfObject = new LoginoutObject();
            signSelfObject.setTime(0);
            signSelfObject.setAction("In");
            dataRef7.child("CurrentlySignedInRobotics").setValue(true);
            dataRef7.child("Logins").child(uploadDate).setValue(signSelfObject);
        }
        Intent returnIntent = new Intent(this, MainActivity.class);
        startActivity(returnIntent);
    }
}
