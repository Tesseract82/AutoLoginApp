package com.example.unit271.geofencetest1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class chooseTeamMember extends AppCompatActivity {
    public String searchString;
    public ArrayList<PersonObject> teamHoursList, permanentTeamHoursList, formattedTeamHoursList;
    DatabaseReference dataRef;
    public static String filename = "NumberHolder";
    SharedPreferences teamNumData;
    private ListView teamNameListView;
    private ImageButton additionButton;
    SimpleDateFormat logTimeFormat;
    PersonObject po;
    Context appContext;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_team_member);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Automatic Login App");
        appContext = this;
        logTimeFormat = new SimpleDateFormat("MM-dd-yyyy-HHmm", Locale.US);
        teamNumData = getSharedPreferences(filename, 0);
        teamHoursList = new ArrayList<PersonObject>();
        formattedTeamHoursList = new ArrayList<PersonObject>();
        permanentTeamHoursList = new ArrayList<PersonObject>();
        teamHoursList.clear();
        formattedTeamHoursList.clear();
        permanentTeamHoursList.clear();
        teamNameListView = (ListView) findViewById(R.id.newTeamListView);
        additionButton = (ImageButton) findViewById(R.id.newPersonButton);
        additionButton.setEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        dataRef = mDatabase;
        retreiveAllPersonData();
    }

    public void retreiveAllPersonData() {
        teamHoursList.clear();
        dataRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot personSnapshot : dataSnapshot.getChildren()) {
                        PersonObject newPerson = personSnapshot.getValue(PersonObject.class);
                        newPerson.setPersonName(personSnapshot.getKey());
                        teamHoursList.add(newPerson);
                        permanentTeamHoursList.add(newPerson);
                    }
                    dataRef.removeEventListener(this);
                    generatePersonList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void generatePersonList() {
        teamNameListView.setAdapter(new BaseAdapter() {

            @Override
            public boolean areAllItemsEnabled() {
                return true;
            }

            @Override
            public boolean isEnabled(int position) {
                return true;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return teamHoursList.size();
            }

            @Override
            public String getItem(int position) {
                return teamHoursList.get(position).getPersonName();
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (convertView == null) {
                    convertView = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                }
                TextView personString = (TextView) convertView.findViewById(android.R.id.text1);
                personString.setText(teamHoursList.get(position).getPersonName());
                personString.setTextColor(Color.BLACK);
                personString.setTextSize(20);

                return convertView;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        });

        teamNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                po = new PersonObject();
                final String personString = parent.getItemAtPosition(position).toString();
                for (int x = 0; x <= teamHoursList.size() - 1; x++) {
                    if (teamHoursList.get(x).getPersonName().equals(personString)) {
                        po = teamHoursList.get(x);
                    }
                }
                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(appContext);
                final RelativeLayout rl1 = new RelativeLayout(getBaseContext());
                rl1.setId(1);
                rl1.setLayoutParams(params1);
                final TextView rlPasswordView = new TextView(getBaseContext());
                params2.addRule(RelativeLayout.ALIGN_PARENT_START);
                params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                rlPasswordView.setId(2);
                rlPasswordView.setTextSize(20);
                rlPasswordView.setTextColor(Color.BLACK);
                rlPasswordView.setText("Enter Password");
                final EditText passwordText = new EditText(getBaseContext());
                params3.addRule(RelativeLayout.BELOW, rlPasswordView.getId());
                params3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params3.addRule(RelativeLayout.ALIGN_PARENT_START);
                passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordText.setId(3);
                passwordText.setHint("Password");
                passwordText.setTextSize(20);
                passwordText.setTextColor(Color.BLACK);
                rl1.addView(rlPasswordView, params2);
                rl1.addView(passwordText, params3);

                alertDialogBuilder.setView(rl1);
                alertDialogBuilder.setCancelable(true).setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (passwordText.getText().toString().equals(po.getPassword())) {
                            SharedPreferences.Editor editor = teamNumData.edit();
                            editor.putString("newIDKey", personString);
                            editor.commit();
                            if(!teamNumData.getBoolean("setupComplete", false)) {
                                Intent afterMemberChosen = new Intent(getBaseContext(), ChangeTeamNumber.class);
                                startActivity(afterMemberChosen);
                            } else {
                                Intent afterMemberChosen = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(afterMemberChosen);
                            }
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getBaseContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        adaptPersonList();
    }

    public void adaptPersonList() {
        TextView searchText = (TextView) findViewById(R.id.newPersonSearchText);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchString = s.toString();
                teamHoursList.clear();
                for (int a = 0; a <= permanentTeamHoursList.size() - 1; a++) {
                    teamHoursList.add(permanentTeamHoursList.get(a));
                }
                formatString(searchString);
            }
        });
    }

    public void formatString(String searchString) {
        BaseAdapter teamNameAdapter = (BaseAdapter) teamNameListView.getAdapter();
        for (int a = 0; a <= teamHoursList.size() - 1; a++) {
            if (!((teamHoursList.get(a)).getPersonName().toLowerCase().startsWith(searchString.toLowerCase()))) {
                teamHoursList.remove(a);
                a--;
            } else if (searchString.equals("")) {
                continue;
            } else {
                continue;
            }
        }
        teamNameAdapter.notifyDataSetChanged();
    }

    public void onAdditionClick(View view){
        Intent newMemberCreation = new Intent(this, CreateNewPerson.class);
        startActivity(newMemberCreation);
    }

    @Override
    public void onBackPressed() {
        if(teamNumData.getBoolean("setupComplete", false)){
            super.onBackPressed();
        } else {
            //DO NOTHING!!!
        }
    }
}
