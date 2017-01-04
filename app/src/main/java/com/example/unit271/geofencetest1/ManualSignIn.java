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
    String currentLoginType, Status, Activity, startTime;
    TextView teamNameDisplay;
    Button signInSelf;
    DatabaseReference mDatabase, dataRef7;
    double totalRoboticsTime, totalOutreachTime;
    boolean firstDataChange;
    Button button;
    Spinner loginTypeSpinner;
    EditText hoursText, minutesText;
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

        firstDataChange = true;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        dataRef7 = mDatabase.child("People").child(teamName);
        mDatabase.child("People").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("NULLPOINTEREXCEPTION", "ONDATACHANGE");
                for (DataSnapshot infoSnapshot : dataSnapshot.getChildren()) {
                    Log.i("PERSONSEARCHSTRING", teamName);
                    if (infoSnapshot.getKey().equals(teamName)) {
                        Log.i("PERSONSEARCHSTRING", "FOUND");
                        currentPerson = infoSnapshot.getValue(PersonObject.class);
                        currentPerson.setPersonName(teamName);
                        break;
                    }
                }
                Log.i("NULLPOINTEREXCEPTION", "RETRIEVED SUCCESSFULLY");
                totalRoboticsTime = currentPerson.getTotalRobotics();
                totalOutreachTime = currentPerson.getTotalOutreach();
                startTime = currentPerson.getStartTime();
                Status = currentPerson.getStatus();
                Activity = currentPerson.getActivity();
                firstDataChange = false;
                setUpSpinner();
                loginTypeSpinner.setEnabled(true);
                signInSelf.setEnabled(true);
                setViews(); //Necessary for initial setting, which is why it only checks Robotics
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setUpSpinner(){
        loginTypeSpinner = (Spinner) findViewById(R.id.loginTypeSpinner);
        loginTypeSpinner.setEnabled(false);
        ArrayList<String> spinnerTypeList = new ArrayList<>();
        spinnerTypeList.clear();
        spinnerTypeList.add("Robotics");
        spinnerTypeList.add("Outreach");
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
    }

    public void processSpinnerSelection(String spinnerSelection){
        currentLoginType = spinnerSelection;
        if(currentLoginType.equals("Robotics")){
            Log.i("LOGINTYPE", "ROBOTICS");
            if(currentPerson.getActivity().equals("Robotics") && currentPerson.getStatus().equals("IN")){
                signInSelf.setText("Sign Out");
            } else {
                signInSelf.setText("Sign In");
                Log.i("LOGINTYPE", "SIGNIN");
            }
        } else if(currentLoginType.equals("Outreach")){
            if(currentPerson.getActivity().equals("Outreach") && currentPerson.getStatus().equals("IN")){
                signInSelf.setText("Sign Out");
            } else {
                signInSelf.setText("Sign In");
            }
        }
    }

    public void setViews(){
        if(Status.equals("IN") && Activity.equals("Robotics") && currentLoginType.equals("Robotics")){
            signInSelf.setText("Sign Out");
        } else if(currentLoginType.equals("Robotics") && (Status.equals("OUT") || !Activity.equals("Robotics"))) {
            signInSelf.setText("Sign In");
        }
        signInSelf.setTextColor(Color.BLACK);
    }

    public void onSignButtonClick2(View view) {
        Date date = new Date(System.currentTimeMillis());

        SimpleDateFormat uploadFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss +0000", Locale.US);
        String uploadDate = uploadFormatter.format(date);
        double finalDateInt = 0.0;
        String currentTypeLastSignin = null;
        double totalTime = 0.0;
        otherTypesLoginStatus.clear();
        if(currentLoginType.equals("Robotics")){
            if(Activity.equals("Robotics") && Status.equals("IN")) {
                currentTypeLastSignin = startTime;
            } else {
                currentTypeLastSignin = null;
            }
            totalTime = totalRoboticsTime;
        } else if(currentLoginType.equals("Outreach")){
            if(Activity.equals("Outreach") && Status.equals("IN")) {
                currentTypeLastSignin = startTime;
            } else {
                currentTypeLastSignin = null;
            }
            totalTime = totalOutreachTime;
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
                finalDateInt = (double) (date.getTime() - lastDate.getTime());
            }
            dataRef7.child("Total" + currentLoginType).setValue(totalTime + (finalDateInt / (1000 * 60)));
            dataRef7.child("Status").setValue("OUT");
            dataRef7.child("Activity").setValue(currentLoginType);

            Intent returnIntent = new Intent(this, MainActivity.class);
            startActivity(returnIntent);
        } else {
            if(!(Status.equals("IN") && !Activity.equals(currentLoginType))) {
                dataRef7.child("Status").setValue("IN");
                dataRef7.child("Activity").setValue(currentLoginType);
                dataRef7.child("StartTime").setValue(uploadDate);
                AlertDialog.Builder timeDialogBuilder = new AlertDialog.Builder(appContext);
                LinearLayout timeHolder = new LinearLayout(this);

                hoursText = new EditText(this);
                hoursText.setTextColor(Color.BLACK);
                hoursText.setTextSize(35);
                hoursText.setHint("HH");
                hoursText.setInputType(InputType.TYPE_CLASS_NUMBER);
                hoursText.setEnabled(true);
                minutesText = new EditText(this);
                minutesText.setTextColor(Color.BLACK);
                minutesText.setTextSize(35);
                minutesText.setHint("mm");
                minutesText.setInputType(InputType.TYPE_CLASS_NUMBER);
                minutesText.setEnabled(true);
                TextView timeDialogTitle = new TextView(this);
                timeDialogTitle.setText("How Long do You Expect to Stay?");
                timeDialogTitle.setTextColor(Color.BLACK);
                timeDialogTitle.setTextSize(20);

                timeHolder.addView(hoursText);
                timeHolder.addView(minutesText);
                timeDialogBuilder.setView(timeHolder);
                timeDialogBuilder.setCancelable(true).setCustomTitle(timeDialogTitle).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hoursInt = 0;
                        int minutesInt = 0;
                        if(!hoursText.getText().toString().equals("")) {
                            hoursInt = Integer.valueOf(hoursText.getText().toString());
                        }
                        if(!minutesText.getText().toString().equals("")) {
                            minutesInt = Integer.valueOf(minutesText.getText().toString());
                        }
                        SharedPreferences.Editor editor = teamNumData.edit();
                        editor.putInt("notifHours", hoursInt);
                        editor.putInt("notifMinutes", minutesInt);
                        editor.commit();
                        dialog.dismiss();
                        returnHome();
                    }
                });
                AlertDialog timeDialog = timeDialogBuilder.create();
                timeDialog.show();
            } else {
                Toast.makeText(getBaseContext(), "Please Sign Out of Your Current Activity First.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void returnHome(){
        Intent timeServiceIntent = new Intent(this, TimeNotificationService.class);
        Intent returnIntent = new Intent(this, MainActivity.class);
        startService(timeServiceIntent);
        startActivity(returnIntent);
    }
}
