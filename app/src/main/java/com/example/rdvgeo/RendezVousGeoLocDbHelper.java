package com.example.rdvgeo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * File: ${FILE_NAME}
 * Created: 30/12/2017
 * Last changed: 30/12/2017 15:33
 * Author: William
 */

public class RendezVousGeoLocDbHelper extends SQLiteOpenHelper {


    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RendezVousGeoLoc.db";

    // RendezVous table name
    private static final String TABLE_RENDEZVOUS = "rendezvous";

    // RendezVous Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITRE = "titre";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_GROUPE = "groupe";


    private static final String[] COLUMNS = {KEY_ID, KEY_TITRE, KEY_LATITUDE, KEY_LONGITUDE, KEY_GROUPE};


    public RendezVousGeoLocDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_RENDEZVOUS_TABLE = "CREATE TABLE rendezvous ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titre TEXT, " +
                "latitude REAL, " +
                "longitude REAL, " +
                "groupe INTEGER)";
        db.execSQL(CREATE_RENDEZVOUS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS rendezvous");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addRendezVous(Rendezvous rdv) {
        //for logging
        Log.d("addRendezVous", rdv.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITRE, rdv.getTitre()); // get titre
        values.put(KEY_LATITUDE, rdv.getLatitude()); // get latitude
        values.put(KEY_LONGITUDE, rdv.getLongitude()); // get latitude
        values.put(KEY_GROUPE, rdv.getGroupe()); // get groupe


        // 3. insert
        db.insert(TABLE_RENDEZVOUS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Rendezvous getRDV(int id) {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_RENDEZVOUS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Rendezvous rdv = new Rendezvous();
        rdv.setId(Integer.parseInt(cursor.getString(0)));
        rdv.setTitre(cursor.getString(1));
        rdv.setLatitude(Float.parseFloat(cursor.getString(2)));
        rdv.setLongitude(Float.parseFloat(cursor.getString(3)));
        rdv.setGroupe(Integer.parseInt(cursor.getString(4)));

        //log
        Log.d("getRDV(" + id + ")", rdv.toString());

        // 5. return book
        return rdv;
    }

    public List<Rendezvous> getAllRDV() {
        List<Rendezvous> rdvs = new LinkedList<Rendezvous>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_RENDEZVOUS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Rendezvous rdv = null;
        if (cursor.moveToFirst()) {
            do {
                rdv = new Rendezvous();
                rdv.setId(Integer.parseInt(cursor.getString(0)));
                rdv.setTitre(cursor.getString(1));
                rdv.setLatitude(Float.parseFloat(cursor.getString(2)));
                rdv.setLongitude(Float.parseFloat(cursor.getString(3)));
                rdv.setGroupe(Integer.parseInt(cursor.getString(4)));

                // Add book to books
                rdvs.add(rdv);
            } while (cursor.moveToNext());
        }

        Log.d("getAllRDV()", rdvs.toString());

        // return books
        return rdvs;
    }

    public int updateRDV(Rendezvous rdv) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("latitude", rdv.getLatitude()); // get latitude
        values.put("longitude", rdv.getLongitude()); // get longitude
        values.put("groupe", rdv.getGroupe()); // get groupe

        // 3. updating row
        int i = db.update(TABLE_RENDEZVOUS, //table
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[]{String.valueOf(rdv.getId())}); //selection args

        // 4. close
        db.close();

        return i;
    }

    public void deleteRDV(Rendezvous rdv) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_RENDEZVOUS, //table name
                KEY_ID + " = ?",  // selections
                new String[]{String.valueOf(rdv.getId())}); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteRDV", rdv.toString());

    }
}