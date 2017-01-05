package com.example.unit271.geofencetest1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ChangeTeamNumber extends AppCompatActivity {

    public static String filename = "NumberHolder";
    SharedPreferences teamNumData;
    private String teamID;
    private String selection;
    private CheckBox cb7;
    private CheckBox cb6;
    private CheckBox cb1;
    ValueEventListener nameDatabaseListener;
    ArrayList<PersonObject> existingPeople;
    Boolean free1;
    Boolean free6;
    Boolean free7;
    String schoolName;
    RadioButton rb;
    TextView changeName;
    DatabaseReference nameChangeDatabase;
    PersonObject oldPersonFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_team_num);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Setup");
        teamNumData = getSharedPreferences(filename, 0);
        teamID = teamNumData.getString("newIDKey", "NONE");

        final Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setEnabled(false);
        existingPeople = new ArrayList<>();
        existingPeople.clear();
        oldPersonFirebase = null;
        nameChangeDatabase = FirebaseDatabase.getInstance().getReference();
        nameDatabaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot personSnapshot: dataSnapshot.getChildren()) {
                    PersonObject existingPerson = personSnapshot.getValue(PersonObject.class);
                    existingPerson.setPersonName(personSnapshot.getKey());
                    existingPeople.add(existingPerson);
                    if(personSnapshot.getKey().equals(teamID)){
                        oldPersonFirebase = personSnapshot.getValue(PersonObject.class);
                    }
                }
                saveButton.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        nameChangeDatabase.addValueEventListener(nameDatabaseListener);
        cb1 = (CheckBox) findViewById(R.id.checkPer1);
        cb6 = (CheckBox) findViewById(R.id.checkPer6);
        cb7 = (CheckBox) findViewById(R.id.checkPer7);

        free1 = teamNumData.getBoolean("free1", false);
        free6 = teamNumData.getBoolean("free6", false);
        free7 = teamNumData.getBoolean("free7", false);
        schoolName = teamNumData.getString("schoolName", "none");

        changeName = (TextView) findViewById(R.id.changeNumEdtTxt);
        changeName.setText(teamID);

        cb1.setChecked(free1);
        cb6.setChecked(free6);
        cb7.setChecked(free7);

        if(schoolName.equals("Holmes")){
            rb = (RadioButton) findViewById(R.id.radioHolJS);
            rb.setChecked(true);
        }else if(schoolName.equals("DHS")){
            rb = (RadioButton) findViewById(R.id.radioDHS);
            rb.setChecked(true);
        }else if(schoolName.equals("Harper")){
            rb = (RadioButton) findViewById(R.id.radioHarJS);
            rb.setChecked(true);
        }else if(schoolName.equals("Emerson")){
            rb = (RadioButton) findViewById(R.id.radioEmersonJS);
            rb.setChecked(true);
        }else if(schoolName.equals("DaVinci JrHigh")){
            rb = (RadioButton) findViewById(R.id.radioDavinciJS);
            rb.setChecked(true);
        } else if(schoolName.equals("DaVinci High")){
            rb = (RadioButton) findViewById(R.id.radioDavinciHS);
            rb.setChecked(true);
        } else if(schoolName.equals("none")){
        }

    }

    public void onSaveClick(View view){
        nameChangeDatabase.removeEventListener(nameDatabaseListener);
        boolean containsIllegalCharacters = false;
        ArrayList<String> illegalCharacters = new ArrayList<>(Arrays.asList(".", "#", "$", "[", "]"));
        for(int a = 0; a <= illegalCharacters.size() - 1; a++){
            if(changeName.getText().toString().contains(illegalCharacters.get(a))){
                containsIllegalCharacters = true;
                break;
            }
        }
        if(!changeName.getText().toString().equals("") && !containsIllegalCharacters) {
            if (!changeName.getText().toString().equals(teamID)) {
                Log.i("CHANGENAME TEAMID", teamID);
                Log.i("CHANGENAME", changeName.getText().toString());
                Log.i("CHANGENAME", "NPF=TRUE");
                boolean createNewPersonFirebase = true;
                for (int a = 0; a <= existingPeople.size() - 1; a++) {
                    if (changeName.getText().toString().equals(existingPeople.get(a).getPersonName())) {
                        createNewPersonFirebase = false;
                        Toast.makeText(getBaseContext(), "This name already has a profile.",
                                Toast.LENGTH_SHORT).show();
                        Log.i("CHANGENAME", "NPF=FALSE");
                    }
                }
                if (createNewPersonFirebase) {
                    String newName = changeName.getText().toString();
                    if(oldPersonFirebase != null){
                        nameChangeDatabase.child(newName).child("Password").setValue(oldPersonFirebase.getPassword());
                        if(oldPersonFirebase.getStatus() != null) {
                            nameChangeDatabase.child(newName).child("Status").setValue(oldPersonFirebase.getStatus());
                        } else {
                            nameChangeDatabase.child(newName).child("Status").setValue("OUT");
                        }
                        nameChangeDatabase.child(newName).child("StartTime").setValue(oldPersonFirebase.getStartTime());
                        nameChangeDatabase.child(newName).child("StartDate").setValue(oldPersonFirebase.getStartDate());
                        nameChangeDatabase.child(newName).child("TotalRobotics").setValue(oldPersonFirebase.getTotalRobotics());
                        nameChangeDatabase.child(newName).child("TotalOutreach").setValue(oldPersonFirebase.getTotalOutreach());
                        nameChangeDatabase.child(newName).child("Type").setValue(oldPersonFirebase.getType());
                        if(oldPersonFirebase.getActivity() != null) {
                            nameChangeDatabase.child(newName).child("Activity").setValue(oldPersonFirebase.getActivity());
                        } else {
                            nameChangeDatabase.child(newName).child("Activity").setValue("Robotics");
                        }
                        nameChangeDatabase.child(teamID).setValue(null);
                        SharedPreferences.Editor newNameEditor = teamNumData.edit();
                        newNameEditor.putString("newIDKey", newName);
                        newNameEditor.commit();
                    } else {
                        Toast.makeText(getBaseContext(), "Cannot change name, no current profile exists.",
                                Toast.LENGTH_SHORT);
                    }
                }
            }
            SharedPreferences.Editor editor = teamNumData.edit();
            RadioGroup rGroup = (RadioGroup) findViewById(R.id.radioGroup1);
            if (rGroup.getCheckedRadioButtonId() != -1) {
                int id = rGroup.getCheckedRadioButtonId();
                View radioButton = rGroup.findViewById(id);
                int radioId = rGroup.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) rGroup.getChildAt(radioId);
                selection = (String) btn.getText();
                editor.putString("schoolName", selection);
            }
            editor.putBoolean("free1", cb1.isChecked());
            editor.putBoolean("free6", cb6.isChecked());
            editor.putBoolean("free7", cb7.isChecked());

            if (!teamNumData.getBoolean("setupComplete", false)) {
                editor.putBoolean("setupComplete", true);
            }
            editor.commit();
            Intent returnIntent = new Intent(this, MainActivity.class);
            startActivity(returnIntent);
        } else {
            Toast.makeText(getBaseContext(), "Enter a Valid Name.", Toast.LENGTH_SHORT).show();
        }
    }
}
