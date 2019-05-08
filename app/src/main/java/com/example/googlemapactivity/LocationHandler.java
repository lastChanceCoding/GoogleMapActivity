package com.example.googlemapactivity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


public class LocationHandler extends SQLiteOpenHelper {

    /*  Some Debug Notes for the Databases
    I have several databases in this program, containing different data
    You can select different databases by editing the name below.  Here is what they contain on my local emulator:
    test1.db          This is an empty database
    test2.db          This one has 4 good known locations:  Null Island, three more at CGCC, plus junk
    test3.db          Over 65,000 entries (oops)
    test4.db          See if I can force the initial database to have known values
    locations1.db     This one is the real deal, with the first 4 locations useful, plus one oddball
     */

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "test4.db";  //DB name
    private static final String TABLE_NAME = "userdata";
    private static final String COLUMN_ID = "id";  // this is the primary key
    private static final String COLUMN_LOCATION_NAME = "locationName";
    private static final String COLUMN_LOCATION_LAT = "latitude";
    private static final String COLUMN_LOCATION_LNG = "longitude";



    // Now, set up the db
    SQLiteDatabase database;

    // make a Constructor

    public LocationHandler (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();  // boom, a new database is created.  No structure, empty

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // set up tables and columns
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                " ( "
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_LOCATION_NAME + " TEXT, "
                + COLUMN_LOCATION_LAT + " TEXT, "
                + COLUMN_LOCATION_LNG + " TEXT" +          // no semicolon on this last column
                ")");
        // that ( after TABLE_NAME is enclosing one long string...

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    // there are three flavors of insertLocation


    public boolean insertLocation(String latitude,
                                  String longitude){
        database = getWritableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();  // open a connection
        ContentValues contentValues = new ContentValues();  // get the content values
        contentValues.put(COLUMN_LOCATION_NAME, "");
        contentValues.put(COLUMN_LOCATION_LAT, latitude);
        contentValues.put(COLUMN_LOCATION_LNG, longitude);


        long result = database.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }


    }

    // this version takes a location name

    public boolean insertLocation(String locationName, String latitude,
                                  String longitude){
        database = getWritableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();  // open a connection
        ContentValues contentValues = new ContentValues();  // get the content values
        contentValues.put(COLUMN_LOCATION_NAME, locationName);
        contentValues.put(COLUMN_LOCATION_LAT, latitude);
        contentValues.put(COLUMN_LOCATION_LNG, longitude);


        long result = database.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }


    }

    // this version takes two doubles

    public boolean insertLocation(double dlatitude,
                                  double dlongitude){
        database = getWritableDatabase();
        SQLiteDatabase database = this.getWritableDatabase();  // open a connection
        ContentValues contentValues = new ContentValues();  // get the content values
        contentValues.put(COLUMN_LOCATION_NAME, "");
        contentValues.put(COLUMN_LOCATION_LAT, Double.toString(dlatitude));
        contentValues.put(COLUMN_LOCATION_LNG, Double.toString(dlongitude));


        long result = database.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }


    }


    // get all the records of the database
    // returns the result in Cursor object
    private Cursor getAllRecords(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor res = database.rawQuery("select * from " + TABLE_NAME, null);
        return res;

    }

    public void forceFirstLocations(){
        // the purpose of this method is to force the loading of the initial database records
        // with known locations

        // Let's get all the records in the existing database and see what we have to work with
        Cursor res = getAllRecords();

        // first, assume that this might be a new database.  If yes, we need to force enough
        // entries to hold our initial records
        while (res.getCount() < 5){
            insertLocation(0.0, 0.0);  // WHOA..how did I get away with no boolean?
            res = getAllRecords(); // update the counter
        }

        // we now know that we have at least the initial entry slots needed, indexed from zero
        // now, load them up
        boolean flag;
        flag = updateLocation("1", "Null Island", "0.0", "0.0");
        // used to be north lot... now I'm changing it to By Ironwood
        // flag = updateLocation("2", "CGCC North Lot", "33.296195", "-111.795448");
        flag = updateLocation("2", "By Ironwood", "33.29598904", "-111.79727761");
        flag = updateLocation("3", "CGCC SE Lot", "33.293144", "-111.794764");
        flag = updateLocation("4", "CGCC West Lot", "33.295356", "-111.797526");
        flag = updateLocation("5", "Who knows where", "55.111", "-55.222");

    }

    public boolean updateLocation(String id,
                                  String locationName, String latitude,
                                  String longitude){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_LOCATION_NAME, locationName);
        contentValues.put(COLUMN_LOCATION_LAT, latitude);
        contentValues.put(COLUMN_LOCATION_LNG, longitude);
        database.update(TABLE_NAME, contentValues, "id = ?", new String[] { id});
        return true;
    }


    public String showAllLocations() {
        Cursor res = getAllRecords();

        // next two lines are debug statements just to check the counter
        //String debugCounter = Integer.toString(res.getCount());
        //Toast.makeText(MainActivity.this, debugCounter + " records in db", Toast.LENGTH_LONG).show();


        if (res.getCount() == 0) {
            // supply a zero record message
            return("Empty database");
        }

        StringBuffer buffer = new StringBuffer();

        while (res.moveToNext()) {
            buffer.append("ID # " + res.getString(0) + "\n");
            buffer.append("Location Name: " + res.getString(1) + "\n");
            buffer.append("Latitude: " + res.getString(2) + "\n");
            buffer.append("Longitude: " + res.getString(3) + "\n");

        }
        // provide the results in a string
        return (buffer.toString());

    }




    public String showRecentLocations(Integer locationstoShow) {
        Cursor res = getAllRecords();

        // next two lines are debug statements just to check the counter
        //String debugCounter = Integer.toString(res.getCount());
        //Toast.makeText(MainActivity.this, debugCounter + " records in db", Toast.LENGTH_LONG).show();


        if (res.getCount() == 0) {
            // supply a zero record message
            return("Empty database");
        }

        // Ok, now, let's see if we have enough records to cover the request
        if (res.getCount() > locationstoShow) {
            // move the cursor up to the starting point... minus one due to the way the while loop works
            // the while loop will jump the cursor forward again... hence the -1 math
            res.moveToPosition(res.getCount() - locationstoShow -1);
        }

        StringBuffer buffer = new StringBuffer();

        while (res.moveToNext()) {
            buffer.append("ID # " + res.getString(0) + "\n");
            buffer.append("Location Name: " + res.getString(1) + "\n");
            buffer.append("Latitude: " + res.getString(2) + "\n");
            buffer.append("Longitude: " + res.getString(3) + "\n");


        }
        // provide the results in a string
        return (buffer.toString());

    }


    public String showLastLocation() {
        Cursor res = getAllRecords();

        // next two lines are debug statements just to check the counter
        //String debugCounter = Integer.toString(res.getCount());
        //Toast.makeText(MainActivity.this, debugCounter + " records in db", Toast.LENGTH_LONG).show();


        if (res.getCount() == 0) {
            // supply a zero record message
            return("Empty database");
        }

        StringBuffer buffer = new StringBuffer();

        //move to the last and most recent record
        res.moveToPosition(res.getCount() -1);

        buffer.append("ID # " + res.getString(0) + "\n");
        buffer.append("Location Name: " + res.getString(1) + "\n");
        buffer.append("Latitude: " + res.getString(2) + "\n");
        buffer.append("Longitude: " + res.getString(3) + "\n");


        // provide the results in a string
        return (buffer.toString());

    }



    public double getLastLat() {
        String dbstring;
        double result = 0.0;  // this initial value is also our null value if the database is empty

        Cursor res = getAllRecords();


        if (res.getCount() == 0) {
            // supply a zero record message
            return result;
        }


        //move to the last and most recent record
        res.moveToPosition(res.getCount() -1);

        dbstring = res.getString(2); // get the third record which is latitude in string format


        // provide the results in a double
        return Double.parseDouble(dbstring);

    }

    // Add two additional functions for getting latitude...
    // First one takes no arguments, and acts sames as getLastLat()
    // The second one takes an integer which is the index into the locations database.  If the
    // database has any records, the index starts at 1 [surprise] and runs upward
    public double getLat() {
        return getLastLat();
    }

    public double getLat(Integer index) {
        String dbstring;
        Integer id = index;
        double result = 0.0;  // this initial value is also our null value if the database is empty

        Cursor res = getAllRecords();


        if (res.getCount() == 0) {
            // supply a zero record message
            return result;
        }

        // clamp the index supplied
        if (index > res.getCount()){
            id = res.getCount();  // set to the last and most recent database record
        }
        if (index < 1) {
            id = 1;
        }
        //move to proper record, which will be the index number minus 1, since the database is zero-based
        res.moveToPosition(id - 1);
        dbstring = res.getString(2); // get the third record which is latitude in string format

        // provide the results in a double
        return Double.parseDouble(dbstring);
    }


    public double getLastLng() {
        String dbstring;
        double result = 0.0;  // this initial value is also our null value if the database is empty

        Cursor res = getAllRecords();


        if (res.getCount() == 0) {
            // supply a zero record message
            return result;
        }


        //move to the last and most recent record
        res.moveToPosition(res.getCount() -1);

        dbstring = res.getString(3); // get the third record which is longitude in string format


        // provide the results in a double
        return Double.parseDouble(dbstring);

    }

    // Add two additional functions for getting longitude...
    // First one takes no arguments, and acts sames as getLastLng()
    // The second one takes an integer which is the index into the locations database.  If the
    // database has any records, the index starts at 1 [surprise] and runs upward
    public double getLng() {
            return getLastLng();
    }

    public double getLng(Integer index) {
        String dbstring;
        Integer id = index;
        double result = 0.0;  // this initial value is also our null value if the database is empty

        Cursor res = getAllRecords();


        if (res.getCount() == 0) {
            // supply a zero record message
            return result;
        }

        // clamp the index supplied
        if (index > res.getCount()){
            id = res.getCount();  // set to the last and most recent database record
        }
        if (index < 1) {
            id = 1;
        }
        //move to proper record, which will be the index number minus 1, since the database is zero-based
        res.moveToPosition(id - 1);
        dbstring = res.getString(3); // get the fourth record which is longitude in string format

        // provide the results in a double
        return Double.parseDouble(dbstring);
    }





} // end of file
