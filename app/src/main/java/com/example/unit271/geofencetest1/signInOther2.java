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

public class signInOther2 extends AppCompatActivity {

    Button signOther;
    String otherName;
    TextView textViewWho;
    String currentLoginType, Status, Activity, startTime;
    double totalRoboticsTime, totalOutreachTime;
    DatabaseReference mDatabase, dataRef3, dataRef4;
    Spinner loginTypeSpinner;
    PersonObject currentPerson;
    ArrayList<String> otherTypesLoginStatus;
    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_other2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        otherName = getIntent().getStringExtra("com.example.unit271.geofencetest1/signInOther");
        appContext = this;
        otherTypesLoginStatus = new ArrayList<>();
        currentLoginType = "Team Meeting";
        currentPerson = new PersonObject();
        signOther = (Button) findViewById(R.id.buttonSignOther);
        textViewWho = (TextView) findViewById(R.id.textViewWho);
        textViewWho.setText(otherName);
        setTitle("Automatic Login App");
        signOther.setEnabled(false);
        loginTypeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        loginTypeSpinner.setEnabled(false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        dataRef4 = mDatabase;
        dataRef3 = mDatabase.child(otherName);
        Status = "OUT";
        Activity = "Team Meeting";
        dataRef4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot infoSnapshot: dataSnapshot.getChildren()) {
                    if (infoSnapshot.getKey().equals(otherName)) {
                        Log.i("PERSONSEARCHSTRING", "FOUND");
                        currentPerson = infoSnapshot.getValue(PersonObject.class);
                        currentPerson.setPersonName(otherName);
                        break;
                    }
                }
                totalRoboticsTime = currentPerson.getTotalRobotics();
                totalOutreachTime = currentPerson.getTotalOutreach();
                startTime = currentPerson.getStartTime();
                if(currentPerson.getStatus() != null) {
                    Status = currentPerson.getStatus();
                }
                if(currentPerson.getActivity() != null) {
                    Activity = currentPerson.getActivity();
                }
                if(currentPerson != null) {
                    setUpSpinner();
                    loginTypeSpinner.setEnabled(true);
                    signOther.setEnabled(true);
                    setViews(); //Necessary for initial setting, which is why it only checks Robotics
                } else {
                    //Stop, Shouldn't do anything.
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setUpSpinner(){
        ArrayList<String> spinnerTypeList = new ArrayList<>();
        spinnerTypeList.clear();
        spinnerTypeList.add("Team Meeting");
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
        if(currentLoginType.equals("Team Meeting")){
            Log.i("LOGINTYPE", "ROBOTICS");
            if(currentPerson.getActivity().equals("Team Meeting") && currentPerson.getStatus().equals("IN")){
                signOther.setText("Sign Out");
            } else {
                signOther.setText("Sign In");
                Log.i("LOGINTYPE", "SIGNIN");
            }
        } else if(currentLoginType.equals("Outreach")){
            if(currentPerson.getActivity().equals("Outreach") && currentPerson.getStatus().equals("IN")){
                signOther.setText("Sign Out");
            } else {
                signOther.setText("Sign In");
            }
        }
    }

    public void setViews(){
        if(Status.equals("IN") && Activity.equals("Team Meeting") && currentLoginType.equals("Team Meeting")){
            signOther.setText("Sign Out");
        } else if(currentLoginType.equals("Team Meeting") && (Status.equals("OUT") || !Activity.equals("Team Meeting"))) {
            signOther.setText("Sign In");
        }
        signOther.setTextColor(Color.BLACK);
    }

    public void onSignButtonClick(View view){
        if(currentPerson != null) {
            Date date = new Date(System.currentTimeMillis());

            SimpleDateFormat uploadFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss +0000", Locale.US);
            String uploadDate = uploadFormatter.format(date);
            double finalDateInt = 0.0;
            String currentTypeLastSignin = null;
            double totalTime = 0.0;
            otherTypesLoginStatus.clear();
            if (currentLoginType.equals("Team Meeting")) {
                if (Activity.equals("Team Meeting") && Status.equals("IN")) {
                    currentTypeLastSignin = startTime;
                } else {
                    currentTypeLastSignin = null;
                }
                totalTime = totalRoboticsTime;
            } else if (currentLoginType.equals("Outreach")) {
                if (Activity.equals("Outreach") && Status.equals("IN")) {
                    currentTypeLastSignin = startTime;
                } else {
                    currentTypeLastSignin = null;
                }
                totalTime = totalOutreachTime;
            }
            Date lastDate = null;
            try {
                if (currentTypeLastSignin != null) {
                    lastDate = uploadFormatter.parse(currentTypeLastSignin);
                }
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            if (currentTypeLastSignin != null) {
                if (lastDate != null) {
                    finalDateInt = (double) (date.getTime() - lastDate.getTime());
                }
                if(currentLoginType.equals("Team Meeting")) {
                    dataRef3.child("TotalRobotics").setValue(totalTime + (finalDateInt / (1000 * 60)));
                }
                if(currentLoginType.equals("Outreach")){
                    dataRef3.child("TotalOutreach").setValue(totalTime + (finalDateInt / (1000 * 60)));
                }
                dataRef3.child("Status").setValue("OUT");
                dataRef3.child("Activity").setValue(currentLoginType);

                Intent returnIntent = new Intent(this, MainActivity.class);
                startActivity(returnIntent);
            } else {
                if (!(Status.equals("IN") && !Activity.equals(currentLoginType))) {
                    dataRef3.child("Status").setValue("IN");
                    dataRef3.child("Activity").setValue(currentLoginType);
                    dataRef3.child("StartTime").setValue(uploadDate);
                    Intent returnIntent = new Intent(this, MainActivity.class);
                    startActivity(returnIntent);
                } else {
                    Toast.makeText(getBaseContext(), "Already Doing Something.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }  else {
            Toast.makeText(getBaseContext(), "Could not complete request, selected profile does not exist.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
