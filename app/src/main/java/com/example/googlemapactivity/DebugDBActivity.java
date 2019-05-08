package com.example.googlemapactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DebugDBActivity extends AppCompatActivity {

    EditText etID, etlocationName, etlatitude, etlongitude;
    Button btn_insert, btn_View, btn_Update, btn_ViewRecent, btn_Last;

    boolean DEBUG_MESSAGES = true;

    LocationHandler locationHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_db);
        // set up the database and buttons and associations
        locationHandler = new LocationHandler(this);
        btn_insert = (Button) findViewById(R.id.btn_insert);
        btn_View = (Button) findViewById(R.id.btn_View);
        btn_Update = (Button) findViewById(R.id.btn_Update);
        btn_ViewRecent = (Button) findViewById(R.id.btn_ViewRecent);
        btn_Last = (Button) findViewById(R.id.btn_Last);

        etID = (EditText) findViewById(R.id.etID);
        etlocationName = (EditText) findViewById(R.id.etlocationName);
        etlatitude = (EditText) findViewById(R.id.etlatitude);
        etlongitude = (EditText) findViewById(R.id.etlongitude);

        // fire up all the listeners
        addLocation();  // this little step is critical... fires up the button listener
        // otherwise... NOTHING HAPPENS when you click!!
        viewAll();
        updateLocation();
        viewRecent();
        viewLastLocation();


        // The next is a bit unusual, but I want to force some standard locations into any database
        // we create, so here I'm forcing the first entries to known GPS locations
        // Every time you enter this debug screen, you'll create (or reload) the first entries
        locationHandler.forceFirstLocations(); // this method forces the initial database loading

    }

    public void addLocation(){
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = locationHandler.insertLocation(
                        etlocationName.getText().toString(),
                        etlatitude.getText().toString(),
                        etlongitude.getText().toString());

                // this next line is a test instruction to try the method that uses two doubles
                // isInserted = locationHandler.insertLocation(1212.1212, 34.34);




                if (isInserted && DEBUG_MESSAGES){
                    Toast.makeText(DebugDBActivity.this, "Data was inserted!", Toast.LENGTH_LONG).show();
                }
                else if (DEBUG_MESSAGES){
                    Toast.makeText(DebugDBActivity.this, "NO Data inserted!", Toast.LENGTH_LONG).show();
                }



            }
        });
    }

    public void viewAll(){
        btn_View.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String viewAllFromFunction;
                viewAllFromFunction = locationHandler.showAllLocations();
                showmessage("All locations", viewAllFromFunction);

            }
        });
    }


    public void viewRecent(){
        btn_ViewRecent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String viewRecentFromFunction;
                viewRecentFromFunction = locationHandler.showRecentLocations(5);
                showmessage("Last 5 locations", viewRecentFromFunction);

            }
        });
    }







    public void updateLocation(){
        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdated = locationHandler.updateLocation(
                        etID.getText().toString(),
                        etlocationName.getText().toString(),
                        etlatitude.getText().toString(),
                        etlongitude.getText().toString());

                if (isUpdated == true){
                    Toast.makeText(DebugDBActivity.this, "Data was updated!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(DebugDBActivity.this, "NO Data updated!", Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    public void viewLastLocation(){
        btn_Last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringresult, parseLAT, parseLNG;
                stringresult = locationHandler.showLastLocation();

                // ok, now build a way to see if you can get real doubles to come back
                // not a string, doubles, maybe packed into an object
                // start simple, just get a double back for latitude and toast it

                double resultLAT, resultLNG;
                resultLAT = locationHandler.getLastLat();
                parseLAT = Double.toString(resultLAT);

                resultLNG = locationHandler.getLastLng();
                parseLNG = Double.toString(resultLNG);

                // These next lines are just test code so I can test several methods
                // Toast.makeText(DebugDBActivity.this, "LAT: " + parseLAT + "  LNG: " + parseLNG, Toast.LENGTH_LONG).show();
                //Integer debugindex = 4;
                //double debugLat = 111.111;
                //Toast.makeText(DebugDBActivity.this, "Index " + debugindex + "= Lat: " + locationHandler.getLat(debugindex) + "  LNG: " + locationHandler.getLng(debugindex), Toast.LENGTH_LONG).show();






                showmessage("Last location", stringresult);
            }
        });


    }


    public void showmessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


} // end of DebugDBActivity
