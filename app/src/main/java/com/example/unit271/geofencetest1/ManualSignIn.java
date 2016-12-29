package com.example.unit271.geofencetest1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unit271.geofencetest1.com.example.unit271.geofencetest1.TimeNotificationService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ManualSignIn extends AppCompatActivity {

    public static String filename = "NumberHolder";
    SharedPreferences teamNumData;
    String teamName;
    String currentLoginType;
    TextView teamNameDisplay;
    Button signInSelf;
    DatabaseReference mDatabase, dataRef7;
    String lastSignedInRobotics, lastSignedInFM, lastSignedInCompetition;
    int totalRoboticsTime, totalFMTime, totalCompetitionTime;
    Button button;
    Spinner loginTypeSpinner;
    TextView hoursText, minutesText;
    PersonObject currentPerson;
    ArrayList<String> otherTypesLoginStatus;
    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_sign_in);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("Automatic Login App");
        teamNumData = getSharedPreferences(filename, 0);
        appContext = this;
        otherTypesLoginStatus = new ArrayList<>();
        currentLoginType = "Robotics";
        currentPerson = new PersonObject();
        button = (Button) findViewById(R.id.buttonManualSignIn);
        teamName = getIntent().getStringExtra("com.example.unit271.geofencetest1/MainActivity2");
        teamNameDisplay = (TextView) findViewById(R.id.textViewManualName);
        signInSelf = (Button) findViewById(R.id.buttonManualSignIn);
        teamNameDisplay.setText(teamName);
        signInSelf.setEnabled(false);
        loginTypeSpinner = (Spinner) findViewById(R.id.loginTypeSpinner);
        loginTypeSpinner.setEnabled(false);
        ArrayList<String> spinnerTypeList = new ArrayList<>();
        spinnerTypeList.clear();
        spinnerTypeList.add("Robotics");
        spinnerTypeList.add("FM");
        spinnerTypeList.add("Competition");
        ArrayAdapter<String> spinnerTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerTypeList);
        loginTypeSpinner.setAdapter(spinnerTypeAdapter);
        loginTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                otherTypesLoginStatus.clear();
                processSpinnerSelection(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        dataRef7 = mDatabase.child("People").child(teamName);
        mDatabase.child("People").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot infoSnapshot : dataSnapshot.getChildren()) {
                    Log.i("PERSONSEARCHSTRING", teamName);
                    if (infoSnapshot.getKey().equals(teamName)) {
                        Log.i("PERSONSEARCHSTRING", "FOUND");
                        currentPerson = infoSnapshot.getValue(PersonObject.class);
                        currentPerson.setPersonName(teamName);
                        break;
                    }
                }
                totalRoboticsTime = currentPerson.getTotalRobotics();
                totalFMTime = currentPerson.getTotalFM();
                totalCompetitionTime = currentPerson.getTotalCompetition();
                lastSignedInRobotics = currentPerson.getLastSignInRobotics();
                lastSignedInFM = currentPerson.getLastSignInFM();
                lastSignedInCompetition = currentPerson.getLastSignInCompetition();
                loginTypeSpinner.setEnabled(true);
                signInSelf.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void processSpinnerSelection(String spinnerSelection){
        currentLoginType = spinnerSelection;
        if(currentPerson == null){
            Log.e("CURRENTPERSONIS", " NULL");
        } else {
            Log.e("CURRENTPERSONIS", " NOT NULL!");
        }
        if(currentLoginType.equals("Robotics")){
            Log.i("LOGINTYPE", "ROBOTICS");
            if(currentPerson.getLastSignInRobotics() != null){
                signInSelf.setText("Sign Out");
            } else {
                signInSelf.setText("Sign In");
                Log.i("LOGINTYPE", "SIGNIN");
            }
        } else if(currentLoginType.equals("FM")){
            if(currentPerson.getLastSignInFM() != null){
                signInSelf.setText("Sign Out");
            } else {
                signInSelf.setText("Sign In");
            }
        } else if(currentLoginType.equals("Competition")){
            if(currentPerson.getLastSignInCompetition() != null){
                signInSelf.setText("Sign Out");
            } else {
                signInSelf.setText("Sign In");
            }
        }
    }

    public void onSignButtonClick2(View view) {
        Date date = new Date(System.currentTimeMillis());

        SimpleDateFormat uploadFormatter = new SimpleDateFormat("MM-dd-yyyy-HHmm", Locale.US);
        String uploadDate = uploadFormatter.format(date);
        int finalDateInt = 0;
        String currentTypeLastSignin = null;
        int totalTime = 0;
        otherTypesLoginStatus.clear();
        if(currentLoginType.equals("Robotics")){
            currentTypeLastSignin = lastSignedInRobotics;
            totalTime = totalRoboticsTime;
            otherTypesLoginStatus.add(lastSignedInCompetition);
            otherTypesLoginStatus.add(lastSignedInFM);
        } else if(currentLoginType.equals("FM")){
            currentTypeLastSignin = lastSignedInFM;
            totalTime = totalFMTime;
            otherTypesLoginStatus.add(lastSignedInCompetition);
            otherTypesLoginStatus.add(lastSignedInRobotics);
        } else if(currentLoginType.equals("Competition")){
            currentTypeLastSignin = lastSignedInCompetition;
            totalTime = totalCompetitionTime;
            otherTypesLoginStatus.add(lastSignedInRobotics);
            otherTypesLoginStatus.add(lastSignedInFM);
        }
        Date lastDate = null;
        try {
            if(currentTypeLastSignin != null) {
                lastDate = uploadFormatter.parse(currentTypeLastSignin);
            }
        } catch(ParseException pe){
            pe.printStackTrace();
        }
        if(currentTypeLastSignin != null){
            if (lastDate != null) {
                finalDateInt = (int) (date.getTime() - lastDate.getTime());
            }
            dataRef7.child("Total" + currentLoginType).setValue(totalTime + (finalDateInt / (1000 * 60)));
            dataRef7.child("LastSignIn" + currentLoginType).setValue(null);
            dataRef7.child(currentLoginType + "Logs").child(currentTypeLastSignin).child("time").setValue(finalDateInt / (1000 * 60));
            dataRef7.child(currentLoginType + "Logs").child(currentTypeLastSignin).child("status").setValue("Out");

            Intent returnIntent = new Intent(this, MainActivity.class);
            startActivity(returnIntent);
        } else {
            if(otherTypesLoginStatus.get(0) == null && otherTypesLoginStatus.get(1) == null) {
                LoginoutObject signSelfObject = new LoginoutObject();
                signSelfObject.setTime(0);
                signSelfObject.setStatus("In");
                dataRef7.child("LastSignIn" + currentLoginType).setValue(uploadDate);
                dataRef7.child(currentLoginType + "Logs").child(uploadDate).setValue(signSelfObject);
                AlertDialog.Builder timeDialogBuilder = new AlertDialog.Builder(appContext);
                LinearLayout timeHolder = new LinearLayout(this);

                hoursText = new TextView(this);
                hoursText.setTextColor(Color.BLACK);
                hoursText.setHint("HH");
                hoursText.setInputType(InputType.TYPE_CLASS_NUMBER);
                minutesText = new TextView(this);
                minutesText.setTextColor(Color.BLACK);
                minutesText.setHint("mm");
                TextView timeDialogTitle = new TextView(this);
                timeDialogTitle.setText("How Long do You Expect to Stay?");
                timeDialogTitle.setTextColor(Color.BLACK);
                timeDialogTitle.setTextSize(20);

                timeHolder.addView(hoursText);
                timeHolder.addView(minutesText);
                timeDialogBuilder.setCancelable(true).setCustomTitle(timeDialogTitle).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = teamNumData.edit();
                        editor.putInt("notifHours", Integer.valueOf(hoursText.getText().toString()));
                        editor.putInt("notifMinutes", Integer.valueOf(minutesText.getText().toString()));
                        editor.commit();
                        dialog.dismiss();
                        returnHome();
                    }
                });
            } else {
                Toast.makeText(getBaseContext(), "Please Sign Out of Your Current Activity First.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void returnHome(){
        Intent timeServiceIntent = new Intent(this, TimeNotificationService.class);
        Intent returnIntent = new Intent(this, MainActivity.class);
        startActivity(timeServiceIntent);
        startActivity(returnIntent);
    }
}
