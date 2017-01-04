package com.example.unit271.geofencetest1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
//
//    Button signOther;
//    String otherName;
//    TextView textViewWho;
//    String currentLoginType;
//    String lastSignedInRobotics, lastSignedInFM, lastSignedInCompetition;
//    int totalRoboticsTime, totalFMTime, totalCompetitionTime;
//    DatabaseReference mDatabase, dataRef3, dataRef4;
//    Spinner loginTypeSpinner;
//    PersonObject currentPerson;
//    ArrayList<String> otherTypesLoginStatus;
//    Context appContext;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_in_other2);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//        otherName = getIntent().getStringExtra("com.example.unit271.geofencetest1/signInOther");
//        appContext = this;
//        otherTypesLoginStatus = new ArrayList<>();
//        currentLoginType = "Robotics";
//        currentPerson = new PersonObject();
//        signOther = (Button) findViewById(R.id.buttonSignOther);
//        textViewWho = (TextView) findViewById(R.id.textViewWho);
//        textViewWho.setText(otherName);
//        setTitle("Automatic Login App");
//        signOther.setEnabled(false);
//        loginTypeSpinner = (Spinner) findViewById(R.id.typeSpinner);
//        loginTypeSpinner.setEnabled(false);
//        ArrayList<String> spinnerTypeList = new ArrayList<>();
//        spinnerTypeList.clear();
//        spinnerTypeList.add("Robotics");
//        spinnerTypeList.add("FM");
//        spinnerTypeList.add("Competition");
//        ArrayAdapter<String> spinnerTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerTypeList);
//        loginTypeSpinner.setAdapter(spinnerTypeAdapter);
//        loginTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                otherTypesLoginStatus.clear();
//                processSpinnerSelection(parent.getItemAtPosition(position).toString());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                //Do Nothing
//            }
//        });
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        dataRef4 = mDatabase.child("People");
//        dataRef3 = mDatabase.child("People").child(otherName);
//        dataRef4.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot infoSnapshot: dataSnapshot.getChildren()) {
//                    if (infoSnapshot.getKey().equals(otherName)) {
//                        Log.i("PERSONSEARCHSTRING", "FOUND");
//                        currentPerson = infoSnapshot.getValue(PersonObject.class);
//                        currentPerson.setPersonName(otherName);
//                        break;
//                    }
//                }
//                totalRoboticsTime = currentPerson.getTotalRobotics();
//                totalFMTime = currentPerson.getTotalFM();
//                totalCompetitionTime = currentPerson.getTotalCompetition();
//                lastSignedInRobotics = currentPerson.getLastSignInRobotics();
//                lastSignedInFM = currentPerson.getLastSignInFM();
//                lastSignedInCompetition = currentPerson.getLastSignInCompetition();
//                loginTypeSpinner.setEnabled(true);
//                signOther.setEnabled(true);
//                setViews(); //Necessary for initial setting, which is why it only checks Robotics
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    public void processSpinnerSelection(String spinnerSelection){
//        currentLoginType = spinnerSelection;
//        if(currentPerson == null){
//            Log.e("CURRENTPERSONIS", " NULL");
//        } else {
//            Log.e("CURRENTPERSONIS", " NOT NULL!");
//        }
//        if(currentLoginType.equals("Robotics")){
//            Log.i("LOGINTYPE", "ROBOTICS");
//            if(currentPerson.getLastSignInRobotics() != null){
//                signOther.setText("Sign Out");
//            } else {
//                signOther.setText("Sign In");
//                Log.i("LOGINTYPE", "SIGNIN");
//            }
//        } else if(currentLoginType.equals("FM")){
//            if(currentPerson.getLastSignInFM() != null){
//                signOther.setText("Sign Out");
//            } else {
//                signOther.setText("Sign In");
//            }
//        } else if(currentLoginType.equals("Competition")){
//            if(currentPerson.getLastSignInCompetition() != null){
//                signOther.setText("Sign Out");
//            } else {
//                signOther.setText("Sign In");
//            }
//        }
//    }
//
//    public void setViews(){
//        if(lastSignedInRobotics != null && currentLoginType.equals("Robotics")){
//            signOther.setText("Sign Out");
//        } else if(currentLoginType.equals("Robotics")) {
//            signOther.setText("Sign In");
//        }
//        signOther.setTextColor(Color.BLACK);
//    }
//
//    public void onSignButtonClick(View view){
//        Date date = new Date(System.currentTimeMillis());
//
//        SimpleDateFormat uploadFormatter = new SimpleDateFormat("MM-dd-yyyy-HHmm", Locale.US);
//        String uploadDate = uploadFormatter.format(date);
//        int finalDateInt = 0;
//        String currentTypeLastSignin = null;
//        int totalTime = 0;
//        otherTypesLoginStatus.clear();
//        if(currentLoginType.equals("Robotics")){
//            currentTypeLastSignin = lastSignedInRobotics;
//            totalTime = totalRoboticsTime;
//            otherTypesLoginStatus.add(lastSignedInCompetition);
//            otherTypesLoginStatus.add(lastSignedInFM);
//        } else if(currentLoginType.equals("FM")){
//            currentTypeLastSignin = lastSignedInFM;
//            totalTime = totalFMTime;
//            otherTypesLoginStatus.add(lastSignedInCompetition);
//            otherTypesLoginStatus.add(lastSignedInRobotics);
//        } else if(currentLoginType.equals("Competition")){
//            currentTypeLastSignin = lastSignedInCompetition;
//            totalTime = totalCompetitionTime;
//            otherTypesLoginStatus.add(lastSignedInRobotics);
//            otherTypesLoginStatus.add(lastSignedInFM);
//        }
//        Date lastDate = null;
//        try {
//            if(currentTypeLastSignin != null) {
//                lastDate = uploadFormatter.parse(currentTypeLastSignin);
//            }
//        } catch(ParseException pe){
//            pe.printStackTrace();
//        }
//        if(currentTypeLastSignin != null){
//            if(lastDate != null) {
//                finalDateInt = (int) (date.getTime() - lastDate.getTime());
//            }
//            dataRef3.child("Total" + currentLoginType).setValue(totalTime + (finalDateInt / (1000 * 60)));
//            dataRef3.child("LastSignIn" + currentLoginType).setValue(null);
//            dataRef3.child(currentLoginType + "Logs").child(currentTypeLastSignin).child("time").setValue(finalDateInt / (1000 * 60));
//            dataRef3.child(currentLoginType + "Logs").child(currentTypeLastSignin).child("status").setValue("Out");
//        } else {
//            if(otherTypesLoginStatus.get(0) == null && otherTypesLoginStatus.get(1) == null) {
//                LoginoutObject signSelfObject = new LoginoutObject();
//                signSelfObject.setTime(0);
//                signSelfObject.setStatus("In");
//                dataRef3.child("LastSignIn" + currentLoginType).setValue(uploadDate);
//                dataRef3.child(currentLoginType + "Logs").child(uploadDate).setValue(signSelfObject);
//            } else {
//                Toast.makeText(getBaseContext(), "Already Doing Something.", Toast.LENGTH_SHORT).show();
//            }
//        }
//        Intent returnIntent = new Intent(this, MainActivity.class);
//        startActivity(returnIntent);
//    }
}
