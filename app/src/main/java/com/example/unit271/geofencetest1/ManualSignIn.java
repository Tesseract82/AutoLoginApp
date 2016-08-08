package com.example.unit271.geofencetest1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ManualSignIn extends AppCompatActivity {

    String teamName;
    TextView teamNameDisplay;
    Button signInSelf;
    Firebase dataRef7;
    boolean currentlySignedInRobotics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_sign_in);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Firebase.setAndroidContext(this);

        setTitle("Sign In");
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
        dataRef7 = new Firebase("https://loginapptestcc.firebaseio.com/People/" + teamName);
        dataRef7.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot infoSnapshot: dataSnapshot.getChildren()) {
                    if(infoSnapshot.getKey().equals("CurrentlySignedInRobotics")){
                        currentlySignedInRobotics = (boolean) infoSnapshot.getValue();
                    }
                }
                signInSelf.setEnabled(true);
                setViews();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

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
        LoginoutObject signSelfObject = new LoginoutObject();
        Date date = new Date(System.currentTimeMillis());

        SimpleDateFormat uploadFormatter = new SimpleDateFormat("MM-dd-yyyy-HHmm", Locale.US);
        String uploadDate = uploadFormatter.format(date);

        signSelfObject.setTime(uploadDate);
        signSelfObject.setLocation("Robotics");
        if(currentlySignedInRobotics){
            signSelfObject.setAction("Out");
            dataRef7.child("CurrentlySignedInRobotics").setValue(false);
        } else {
            signSelfObject.setAction("In");
            dataRef7.child("CurrentlySignedInRobotics").setValue(true);
        }
        dataRef7.child("Logins").push().setValue(signSelfObject);
        Intent returnIntent = new Intent(this, MainActivity.class);
        startActivity(returnIntent);
    }
}
