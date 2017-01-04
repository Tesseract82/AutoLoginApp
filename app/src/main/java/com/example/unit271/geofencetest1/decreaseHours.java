package com.example.unit271.geofencetest1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class decreaseHours extends AppCompatActivity {

    String teamID;
    Button decreaseHoursButton;
    DatabaseReference dataRef6, dataBase;
    double TotalRobotics, TotalOutreach;
    Context appContext;
    TextView roboticsView, outreachView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrease_hours);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Automatic Login App");
        appContext = this;
        teamID = getIntent().getStringExtra("com.example.unit271.geofencetest1/MainActivity");
        dataBase = FirebaseDatabase.getInstance().getReference();
        dataRef6 = dataBase.child(teamID);
        TotalRobotics = 0;
        TotalOutreach = 0;
        roboticsView = (TextView) findViewById(R.id.decRobot);
        outreachView = (TextView) findViewById(R.id.decOut);

        decreaseHoursButton = (Button) findViewById(R.id.decreaseHoursButton);
        decreaseHoursButton.setEnabled(false);
        roboticsView.setEnabled(false);
        outreachView.setEnabled(false);
        dataRef6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot infoSnapshot2: dataSnapshot.getChildren()) {
                    if(infoSnapshot2.getKey().equals("TotalRobotics")){
                        try {
                            TotalRobotics = (Double) infoSnapshot2.getValue();
                        } catch(ClassCastException cce){
                            TotalRobotics = (Long) infoSnapshot2.getValue();
                        }
                    }
                    if(infoSnapshot2.getKey().equals("TotalOutreach")){
                        try {
                            TotalOutreach = (Double) infoSnapshot2.getValue();
                        } catch(ClassCastException cce){
                            TotalOutreach = (Long) infoSnapshot2.getValue();
                        }
                    }
                }
                decreaseHoursButton.setEnabled(true);
                roboticsView.setEnabled(true);
                outreachView.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onDecRobotClick(View view){
        final TextView customTitleRobot = new TextView(this);
        customTitleRobot.setText("Decrease Robotics Hours");
        customTitleRobot.setTextSize(20);
        customTitleRobot.setTextColor(Color.BLACK);
        final EditText hoursInputRobot = new EditText(this);
        hoursInputRobot.setText(roboticsView.getText());
        hoursInputRobot.setTextColor(Color.RED);
        hoursInputRobot.setTextSize(30);
        hoursInputRobot.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog.Builder robotBuilder = new AlertDialog.Builder(appContext);
        robotBuilder.setCustomTitle(customTitleRobot);
        robotBuilder.setView(hoursInputRobot);
        robotBuilder.setCancelable(true).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String roboticsText = hoursInputRobot.getText().toString();
                if(!roboticsText.equals("") && Integer.parseInt(roboticsText) >= 0) {
                    roboticsView.setText(roboticsText);
                    dialog.dismiss();
                } else {
                    Toast.makeText(getBaseContext(), "Enter a Valid Number",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog robotDialog = robotBuilder.create();
        robotDialog.show();
    }

    public void onDecOutClick(View view){
        final TextView customTitleOut = new TextView(this);
        customTitleOut.setText("Decrease Outreach Hours");
        customTitleOut.setTextSize(20);
        customTitleOut.setTextColor(Color.BLACK);
        final EditText hoursInputOut = new EditText(this);
        hoursInputOut.setText(outreachView.getText());
        hoursInputOut.setTextColor(Color.RED);
        hoursInputOut.setTextSize(30);
        hoursInputOut.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog.Builder outBuilder = new AlertDialog.Builder(appContext);
        outBuilder.setCustomTitle(customTitleOut);
        outBuilder.setView(hoursInputOut);
        outBuilder.setCancelable(true).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String outreachText = hoursInputOut.getText().toString();
                if(!outreachText.equals("") && Integer.parseInt(outreachText) >= 0) {
                    outreachView.setText(outreachText);
                    dialog.dismiss();
                } else {
                    Toast.makeText(getBaseContext(), "Enter a Valid Number",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog outDialog = outBuilder.create();
        outDialog.show();
    }

    public void onDecreaseHoursClick(View view){
        String strValRobotics = roboticsView.getText().toString();
        String strValOutreach = outreachView.getText().toString();
        int intValRobotics = 0;
        int intValOutreach = 0;
        boolean numFormatException = false;
        try {
            if (!strValRobotics.equals("")) {
                intValRobotics = Integer.parseInt(strValRobotics);
            }
            if (!strValOutreach.equals("")) {
                intValOutreach = Integer.parseInt(strValOutreach);
            }
        } catch(NumberFormatException nfe){
            Toast.makeText(getBaseContext(), "Use Numbers Only.", Toast.LENGTH_SHORT).show();
            //nfe.printStackTrace();
            numFormatException = true;
        }
        if(intValRobotics >= 0 && intValOutreach >= 0 && !numFormatException) {
            if (!strValRobotics.equals("")) {
                if(TotalRobotics - (intValRobotics * 60) >= 0) {
                    dataRef6.child("TotalRobotics").setValue(TotalRobotics - (intValRobotics * 60));
                } else {
                    dataRef6.child("TotalRobotics").setValue(0);
                }
            }
            if (!strValOutreach.equals("")) {
                if(TotalOutreach - (intValOutreach * 60) >= 0) {
                    dataRef6.child("TotalOutreach").setValue(TotalOutreach - (intValOutreach * 60));
                } else {
                    dataRef6.child("TotalOutreach").setValue(0);
                }
            }
            Intent returnIntent = new Intent(this, MainActivity.class);
            startActivity(returnIntent);
        } else if(!numFormatException){
            Toast.makeText(getBaseContext(), "Cannot Subtract Negative Hours.", Toast.LENGTH_SHORT).show();
        }
    }
}
