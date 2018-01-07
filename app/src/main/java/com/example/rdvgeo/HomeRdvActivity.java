package com.example.rdvgeo;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeRdvActivity extends AppCompatActivity {

    private static final String TAG = "HomeRdvActivity";
    static final int READ_SMS_REQUEST = 2;  // The request code
    private static HomeRdvActivity inst;

    RendezVousGeoLocDbHelper dbHelper;
    BroadcastReceiver broadcastReceiver;

    RdvsAdapter adapter;
    ArrayList<Rendezvous> arrayList;

    private ListView mListView;
    private LinearLayout mLinearLayout;

    public static HomeRdvActivity instance() {
        return inst;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_rdv);

        mListView = (ListView) findViewById(R.id.rdvlist);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear_home);

        //dbHelper = new RendezVousGeoLocDbHelper(this);
        //dbHelper.onDowngrade(dbHelper,1,1);
        //this.deleteDatabase(DATABASE_NAME);
        /**
         * CRUD Operations
         * */
        // add rdvs
        //dbHelper.addRendezVous(new Rendezvous("Medecin", 32.3664377f, 32.3664377f, 2));
        //dbHelper.addRendezVous(new Rendezvous("Coiffeur", 32.3664377f, 32.3664377f, 1));
        //dbHelper.addRendezVous(new Rendezvous("FAC", 32.3664377f, 32.3664377f, 6));

        // get all rdvs
        //List<Rendezvous> list = dbHelper.getAllRDV();

        //mListView.setAdapter(new CustomArrayAdapter(HomeRdvActivity.this, list));
        // delete one rdv
        //dbHelper.deleteRDV(list.get(0));

        // get all rdvs
        //dbHelper.getAllRDV();
        if (ActivityCompat.checkSelfPermission(HomeRdvActivity.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestReadSMSPermission();
        }
        else {
            arrayList = new ArrayList<Rendezvous>();
            adapter = new RdvsAdapter(this, arrayList);
            mListView.setAdapter(adapter);

            refreshSmsInbox();
        }

    }

    private boolean requestReadSMSPermission() {
        if (ContextCompat.checkSelfPermission(HomeRdvActivity.this,
                Manifest.permission.READ_SMS) + ContextCompat
                .checkSelfPermission(HomeRdvActivity.this,
                        Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (HomeRdvActivity.this, Manifest.permission.READ_SMS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (HomeRdvActivity.this, Manifest.permission.READ_PHONE_STATE) || ActivityCompat.shouldShowRequestPermissionRationale
                    (HomeRdvActivity.this, Manifest.permission.RECEIVE_SMS)) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(HomeRdvActivity.this,
                                        new String[]{Manifest.permission
                                                .READ_SMS, Manifest.permission.READ_PHONE_STATE},
                                        READ_SMS_REQUEST);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(HomeRdvActivity.this,
                        new String[]{Manifest.permission
                                .READ_SMS, Manifest.permission.READ_PHONE_STATE},
                        READ_SMS_REQUEST);
            }
        } else {
            return true;
        }
        return false;
    }
    @Override
    protected void onStart() {
        super.onStart();
        inst = this;
    }

    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayList.clear();
        while (smsInboxCursor.moveToNext()&& isRDVGeoMessage(smsInboxCursor.getString(indexBody)))
        {
            int numero =  Integer.parseInt(smsInboxCursor.getString(indexAddress).substring(smsInboxCursor.getString(indexAddress).length()-4));
            double[] loc = getLocalisation(smsInboxCursor.getString(indexBody));
            Rendezvous rdv = new Rendezvous("Titre",numero,loc[0],loc[1]);
            String str = "De: " + smsInboxCursor.getString(indexAddress).substring(smsInboxCursor.getString(indexAddress).length()-4) +
                    "\n" + "Longitude : "+loc[0] + "\n" + "Latitude : "+loc[1] ;
            //arrayAdapter.add(str);
            Log.d("TAG",""+ numero);
            arrayList.add(rdv);
        }
    }

    public boolean isRDVGeoMessage(String message){
        String regexRDV = "^RDVGeo";
        Pattern rdvPattern = Pattern.compile(regexRDV);
        Matcher m = rdvPattern.matcher(message);
        return m.find();
    }

    public double [] getLocalisation (String message) {
        String regexCoord = "([-+]?[0-9]*\\.?[0-9]+)";
        Pattern coordsPattern = Pattern.compile(regexCoord + ";" + regexCoord);
        Matcher m = coordsPattern.matcher(message);
        double [] localisation = {0,0};
        if (m.find()) {
            localisation[0] = Double.parseDouble(m.group(1)); // Longitude
            localisation[1] = Double.parseDouble(m.group(2)); // Latitude
        }
        return localisation;
    }

    public void updateList(final Rendezvous rdv) {
        arrayList.add(rdv);
        adapter.notifyDataSetChanged();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu_test à l'ActionBar
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    //gère le click sur une action de l'ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_nouveau:
                nouveauRdv();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void nouveauRdv() {
        Intent intent = new Intent(this, NouveauRdvActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();

        IntentFilter intentFilter = new IntentFilter("mycustombroadcast");
        broadcastReceiver =  new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bundle = intent.getExtras();

                String incoming_number= bundle.getString("phone_num");
                double incoming_longitude = bundle.getDouble("longitude");
                double incoming_latitude = bundle.getDouble("latitude");
                Log.d("incoming number", "" + incoming_number);
                Log.d("incoming longitude", "" + incoming_longitude);
                Log.d("incoming latitude", "" + incoming_latitude);


                //TextView tv = new TextView(context);
                //tv.setText(incoming_number);
                //mLinearLayout.addView(tv);

            }
        };
        this.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //if (broadcastReceiver != null)
        this.unregisterReceiver(this.broadcastReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}


