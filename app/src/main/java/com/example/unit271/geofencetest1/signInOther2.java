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

public class signInOther2 extends AppCompatActivity {

    Button signOther;
    String otherName;
    TextView textViewWho;
    boolean currentlySignedInRobotics;
    DatabaseReference mDatabase, dataRef3, dataRef4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_other2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        otherName = getIntent().getStringExtra("com.example.unit271.geofencetest1/signInOther");
        signOther = (Button) findViewById(R.id.buttonSignOther);
        textViewWho = (TextView) findViewById(R.id.textViewWho);
        textViewWho.setText(otherName);
        setTitle("Automatic Login App");
        signOther.setEnabled(false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        dataRef4 = mDatabase.child("People").child(otherName);
        dataRef3 = mDatabase.child("People").child(otherName).child("Logins");
        dataRef4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot infoSnapshot: dataSnapshot.getChildren()) {
                    if(infoSnapshot.getKey().equals("CurrentlySignedInRobotics")){
                        currentlySignedInRobotics = (boolean) infoSnapshot.getValue();
                    }
                }
                signOther.setEnabled(true);
                setViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setViews(){
        if(currentlySignedInRobotics){
            signOther.setText("SIGN OUT");
        } else {
            signOther.setText("SIGN IN");
        }
        signOther.setTextColor(Color.BLACK);
    }

    public void onSignButtonClick(View view){
        LoginoutObject signOtherObject = new LoginoutObject();
        Date date = new Date(System.currentTimeMillis());

        SimpleDateFormat uploadFormatter = new SimpleDateFormat("MM-dd-yyyy-HHmm", Locale.US);
        String uploadDate = uploadFormatter.format(date);

        signOtherObject.setTime(uploadDate);
        signOtherObject.setLocation("Robotics");
        if(currentlySignedInRobotics){
            signOtherObject.setAction("Out");
            dataRef4.child("CurrentlySignedInRobotics").setValue(false);
        } else {
            signOtherObject.setAction("In");
            dataRef4.child("CurrentlySignedInRobotics").setValue(true);
        }
        dataRef3.push().setValue(signOtherObject);
        Intent returnIntent = new Intent(this, MainActivity.class);
        startActivity(returnIntent);
    }
}
