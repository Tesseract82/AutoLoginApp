package com.example.unit271.geofencetest1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class decreaseHours extends AppCompatActivity {

    String teamID;
    EditText decreaseHoursText, decreaseHoursTextFM, decreaseHoursTextCompetition;
    Button decreaseHoursButton;
    Firebase dataRef6;
    int SubtractHoursRobotics, SubtractHoursFM, SubtractHoursCompetition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrease_hours);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Decrease Hours");
        Firebase.setAndroidContext(this);
        teamID = getIntent().getStringExtra("com.example.unit271.geofencetest1/MainActivity");
        dataRef6 = new Firebase("https://loginapptestcc.firebaseio.com/People/" + teamID);
        SubtractHoursRobotics = 0;
        SubtractHoursCompetition = 0;
        SubtractHoursFM = 0;
        decreaseHoursText = (EditText) findViewById(R.id.hoursEditText);
        decreaseHoursTextFM = (EditText) findViewById(R.id.hoursEditTextFM);
        decreaseHoursTextCompetition = (EditText) findViewById(R.id.hoursEditTextCompetition);
        decreaseHoursButton = (Button) findViewById(R.id.decreaseHoursButton);
        decreaseHoursButton.setEnabled(false);
        dataRef6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot infoSnapshot2: dataSnapshot.getChildren()) {
                    if(infoSnapshot2.getKey().equals("SubtractHoursRobotics")){
                        SubtractHoursRobotics = ((Long) infoSnapshot2.getValue()).intValue();
                    }
                    if(infoSnapshot2.getKey().equals("SubtractHoursFM")){
                        SubtractHoursFM = ((Long) infoSnapshot2.getValue()).intValue();
                    }
                    if(infoSnapshot2.getKey().equals("SubtractHoursCompetition")){
                        SubtractHoursCompetition = ((Long) infoSnapshot2.getValue()).intValue();
                    }
                }
                decreaseHoursButton.setEnabled(true);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

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
                dataRef6.child("SubtractHoursRobotics").setValue(SubtractHoursRobotics + intValRobotics);
            } else {
                dataRef6.child("SubtractHoursRobotics").setValue(SubtractHoursRobotics);
            }
            if (!strValFM.equals("")) {
                dataRef6.child("SubtractHoursFM").setValue(SubtractHoursFM + intValFM);
            } else {
                dataRef6.child("SubtractHoursFM").setValue(SubtractHoursFM);
            }
            if (!strValCompetition.equals("")) {
                dataRef6.child("SubtractHoursCompetition").setValue(SubtractHoursCompetition + intValCompetition);
            } else {
                dataRef6.child("SubtractHoursCompetition").setValue(SubtractHoursCompetition);
            }
            Intent returnIntent = new Intent(this, MainActivity.class);
            startActivity(returnIntent);
        } else if(!numFormatException){
            Toast.makeText(getBaseContext(), "Cannot Subtract Negative Hours.", Toast.LENGTH_SHORT).show();
        }
    }
}
