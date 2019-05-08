package com.example.googlemapactivity;

/*
Project:  Where Did I Park My Car?
Author:   Jentry Maxwell and David Baker
Contributions:  Portions of code and code framing provided by Steve Osburn
Class:  CIS165, Spring 2019, Final Project
 */

/* Changelog
    2019-04-30 initial commit to Git and GitHub
    2019-05-08 rebase at version 17 into GitHub
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TextView;
import android.util.Log;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.RadioButton;
import android.widget.Spinner;
import java.util.List;
import android.widget.AdapterView;


public class MainActivity extends AppCompatActivity implements OnClickListener {

    Button getMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getMap = (Button) findViewById(R.id.getMap);
        getMap.setOnClickListener(this);
    }





    // Options Menu:  this works just as is... the menu gets created, and the onOptionsItemsSelected triggers
    @Override
    public boolean onCreateOptionsMenu(Menu bob) {
        // or you could call this m
        // this puts three dots on the screen
        bob.add(0, 1, 1, "Help");
        bob.add(0, 2, 2, "Settings");
        bob.add(0, 3, 3, "About");
        bob.add(0, 4, 4, "Debug Database");

        return super.onCreateOptionsMenu(bob);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case 1:  // HELP
                 //Toast.makeText(this, "HELP was selected", Toast.LENGTH_LONG).show();
                 // Snackbar.make(findViewById(android.R.id.content), "SNACKBAR: Restaurant bill saved!", Snackbar.LENGTH_LONG).action("Action", null).show();
                Intent HelpActivityIntent = new Intent(this, HelpActivity.class);
                startActivity(HelpActivityIntent);
                break;
            case 2:  // SETTINGS
                Toast.makeText(this, "SETTINGS was selected", Toast.LENGTH_LONG).show();
                //Intent SettingsActivityIntent = new Intent(this, SettingsActivity.class);
                //startActivity(SettingsActivityIntent);
                break;
            case 3:  // ABOUT
                //Toast.makeText(this, "ABOUT was selected", Toast.LENGTH_LONG).show();
                Intent AboutActivityIntent = new Intent(this, AboutActivity.class);
                startActivity(AboutActivityIntent);
                break;
            case 4:  // DEBUG DATABASE
                Intent DebugDBActivityIntent = new Intent(this, DebugDBActivity.class);
                startActivity(DebugDBActivityIntent);
                break;

        } // end of switch statement

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.getMap:
                Intent MapsActivity = new Intent(this, MapsActivity.class);
                startActivity(MapsActivity);
                break;
        }

    }
}  // that's all folks ... end of MainActivity
