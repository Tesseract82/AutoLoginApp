package com.example.unit271.geofencetest1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class CreateNewPerson extends AppCompatActivity {

    Firebase dataRef5, dataRef6;
    ArrayList<String> teamNameList2;
    Button saveButton;
    EditText nameText, passText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_person);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Firebase.setAndroidContext(this);
        setTitle("Setup");
        saveButton = (Button) findViewById(R.id.newPersonButton);
        saveButton.setEnabled(false);
        nameText = (EditText) findViewById(R.id.newPersonET1);
        passText = (EditText) findViewById(R.id.newPersonET2);
        teamNameList2 = new ArrayList<String>();
        teamNameList2.clear();
        dataRef5 = new Firebase("https://loginapptestcc.firebaseio.com/");
        dataRef6 = dataRef5.child("People");
        dataRef6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot personSnapshot: dataSnapshot.getChildren()) {
                    teamNameList2.add(personSnapshot.getKey());
                }
                saveButton.setEnabled(true);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void onClickCreate(View view){
        String newPassword = passText.getText().toString();
        String newName = nameText.getText().toString();
        boolean newPersonFirebase = true;
        if(newName.length() > 0 && newPassword.length() > 0){
            for(int a = 0; a <= teamNameList2.size() - 1; a++){
                if(newName.equals(teamNameList2.get(a))){
                    newPersonFirebase = false;
                }
            }
            if(newPersonFirebase){
                dataRef5.child("People").child(newName).child("CurrentlySignedInRobotics").setValue(false);
                dataRef5.child("People").child(newName).child("CurrentlySignedInFM").setValue(false);
                dataRef5.child("People").child(newName).child("CurrentlySignedInCompetition").setValue(false);
                dataRef5.child("People").child(newName).child("SubtractHoursRobotics").setValue(0);
                dataRef5.child("People").child(newName).child("SubtractHoursCompetition").setValue(0);
                dataRef5.child("People").child(newName).child("SubtractHoursFM").setValue(0);
                dataRef5.child("People").child(newName).child("Password").setValue(newPassword);

                Toast.makeText(getApplicationContext(), ("New Profile : " + newName),
                        Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent(this, chooseTeamMember.class);
                startActivity(returnIntent);
            } else {
                Toast.makeText(getApplicationContext(), "Profile Already Exists.",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Invalid Entry",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
