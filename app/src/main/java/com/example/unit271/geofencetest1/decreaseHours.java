package com.example.unit271.geofencetest1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class decreaseHours extends AppCompatActivity {

    String teamID;
    EditText decreaseHoursText, decreaseHoursTextFM, decreaseHoursTextCompetition;
    Button decreaseHoursButton;
    DatabaseReference dataRef6, dataBase;
    int TotalRobotics, TotalFM, TotalCompetition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrease_hours);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Automatic Login App");
        teamID = getIntent().getStringExtra("com.example.unit271.geofencetest1/MainActivity");
        dataBase = FirebaseDatabase.getInstance().getReference();
        dataRef6 = dataBase.child("People").child(teamID);
        TotalRobotics = 0;
        TotalCompetition = 0;
        TotalFM = 0;
        decreaseHoursText = (EditText) findViewById(R.id.hoursEditText);
        decreaseHoursTextFM = (EditText) findViewById(R.id.hoursEditTextFM);
        decreaseHoursTextCompetition = (EditText) findViewById(R.id.hoursEditTextCompetition);
        decreaseHoursButton = (Button) findViewById(R.id.decreaseHoursButton);
        decreaseHoursButton.setEnabled(false);
        dataRef6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot infoSnapshot2: dataSnapshot.getChildren()) {
                    if(infoSnapshot2.getKey().equals("TotalRobotics")){
                        TotalRobotics = ((Long) infoSnapshot2.getValue()).intValue();
                    }
                    if(infoSnapshot2.getKey().equals("TotalFM")){
                        TotalFM = ((Long) infoSnapshot2.getValue()).intValue();
                    }
                    if(infoSnapshot2.getKey().equals("TotalCompetition")){
                        TotalCompetition = ((Long) infoSnapshot2.getValue()).intValue();
                    }
                }
                decreaseHoursButton.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onDecreaseHoursClick(View view){
        String strValRobotics = String.valueOf(decreaseHoursText.getText());
        String strValFM = String.valueOf(decreaseHoursTextFM.getText());
        String strValCompetition = String.valueOf(decreaseHoursTextCompetition.getText());
        int intValRobotics = 0;
        int intValFM = 0;
        int intValCompetition = 0;
        boolean numFormatException = false;
        try {
            if (!strValRobotics.equals("")) {
                intValRobotics = Integer.parseInt(strValRobotics);
            }
            if (!strValFM.equals("")) {
                intValFM = Integer.parseInt(strValFM);
            }
            if (!strValCompetition.equals("")) {
                intValCompetition = Integer.parseInt(strValCompetition);
            }
        } catch(NumberFormatException nfe){
            Toast.makeText(getBaseContext(), "Use Numbers Only.", Toast.LENGTH_SHORT).show();
            //nfe.printStackTrace();
            numFormatException = true;
        }
        if(intValRobotics >= 0 && intValFM >= 0 && intValCompetition >= 0 && !numFormatException) {
            if (!strValRobotics.equals("")) {
                dataRef6.child("SubtractHoursRobotics").setValue(TotalRobotics - intValRobotics);
            }
            if (!strValFM.equals("")) {
                dataRef6.child("SubtractHoursFM").setValue(TotalFM - intValFM);
            }
            if (!strValCompetition.equals("")) {
                dataRef6.child("SubtractHoursCompetition").setValue(TotalCompetition - intValCompetition);
            }
            Intent returnIntent = new Intent(this, MainActivity.class);
            startActivity(returnIntent);
        } else if(!numFormatException){
            Toast.makeText(getBaseContext(), "Cannot Subtract Negative Hours.", Toast.LENGTH_SHORT).show();
        }
    }
}
