package com.example.rdvgeo;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NouveauRdvActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    static final int PICK_CONTACT_REQUEST = 1;  // The request code
    static final int SEND_SMS_REQUEST = 2;  // The request code
    static final int READ_PHONE_STATE_REQUEST = 3;  // The request code
    private String locationProvider;


    private EditText phoneNumbers;
    private TextView textView1;
    private TextView textView2;
    private TextView longitudetv;
    private TextView latitudetv;

    private double longitude;
    private double latitude;

    private BroadcastReceiver BReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //put here whaterver you want your activity to do with the intent received
            boolean s = intent.getBooleanExtra("success", false);
            setLongitude(intent.getDoubleExtra("longitude", 0));
            setLatitude(intent.getDoubleExtra("latitude", 0));
        }
    };

    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(BReceiver, new IntentFilter("message"));
    }

    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(BReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumbers = (EditText) findViewById(R.id.contacts);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        longitudetv = (TextView) findViewById(R.id.textView4);
        latitudetv = (TextView) findViewById(R.id.textView6);

        if (ActivityCompat.checkSelfPermission(NouveauRdvActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) + ActivityCompat
                .checkSelfPermission(NouveauRdvActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();

        } else {

            startService(new Intent(NouveauRdvActivity.this, GeoLoc.class));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ajoute les entrées de menu_test à l'ActionBar
        getMenuInflater().inflate(R.menu.menu_nouveau_rdv, menu);
        return true;
    }

    //gère le click sur une action de l'ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_annuler:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private boolean requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(NouveauRdvActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) + ContextCompat
                .checkSelfPermission(NouveauRdvActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (NouveauRdvActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (NouveauRdvActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(NouveauRdvActivity.this,
                                        new String[]{Manifest.permission
                                                .ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                        SEND_SMS_REQUEST);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(NouveauRdvActivity.this,
                        new String[]{Manifest.permission
                                .ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        SEND_SMS_REQUEST);
            }
        } else {
            return true;
        }
        return false;
    }

    private boolean requestSMSPermission() {
        if (ContextCompat.checkSelfPermission(NouveauRdvActivity.this,
                Manifest.permission.SEND_SMS) + ContextCompat.checkSelfPermission(NouveauRdvActivity.this,
                Manifest.permission.RECEIVE_SMS) + ContextCompat
                .checkSelfPermission(NouveauRdvActivity.this,
                        Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (NouveauRdvActivity.this, Manifest.permission.SEND_SMS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (NouveauRdvActivity.this, Manifest.permission.READ_PHONE_STATE) || ActivityCompat.shouldShowRequestPermissionRationale
                    (NouveauRdvActivity.this, Manifest.permission.RECEIVE_SMS)) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(NouveauRdvActivity.this,
                                        new String[]{Manifest.permission
                                                .SEND_SMS, Manifest.permission
                                                .RECEIVE_SMS, Manifest.permission.READ_PHONE_STATE},
                                        SEND_SMS_REQUEST);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(NouveauRdvActivity.this,
                        new String[]{Manifest.permission
                                .SEND_SMS, Manifest.permission
                                .RECEIVE_SMS, Manifest.permission.READ_PHONE_STATE},
                        SEND_SMS_REQUEST);
            }
        } else {
            return true;
        }
        return false;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longi) {
        this.longitude = longi;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double lati) {
        this.latitude = lati;
    }

    public void pickContact(View view) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    public void getLoc(View view) {
        longitudetv.setText("" + getLongitude());
        latitudetv.setText("" + getLatitude());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case PICK_CONTACT_REQUEST:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            phoneNo = formatPhoneNumber(phoneNo);
            textView2.setText(phoneNo);
            phoneNo += ",";
            name = cursor.getString(nameIndex);
            textView1.setText(name);
            phoneNumbers.append(phoneNo);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean phoneNumberChecker(String phoneNumber) {
        return phoneNumber.length() == 4;
    }

    public void send(View view) {
        String message;
        String[] phoneNumbers;

        message = demandeRdv(localisationToString());
        phoneNumbers = getPhoneNumber();

        if (!message.isEmpty())

            for (int i = 0; i < phoneNumbers.length; i++) {
                if (!phoneNumberChecker(phoneNumbers[i]))
                    Toast.makeText(this.getApplicationContext(), phoneNumbers[i] + " est invalide", Toast.LENGTH_SHORT).show();
                else {
                    if (requestSMSPermission())
                        sendSMS(phoneNumbers[i], message);
                    finish();
                }

            }

        else
            Toast.makeText(this.getApplicationContext(), "Aucun message", Toast.LENGTH_SHORT).show();
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public String demandeRdv (String localisation) {
        String s;
        s = "RDVGeo : Nouvelle demande de rendez-vous \n" +
                "Localisation : " + localisation + "\n" +
                "Accepter ou rejeter ?";
        return s;
    }

    // (Longitude;Latitude)
    public String localisationToString() {
        return "(" + getLongitude() + ";" + getLatitude() + ")";
    }

    public String formatPhoneNumber(String s) {
        return s.replaceAll("\\D+", "");
    }

    public String[] getPhoneNumber() {
        String delim = "[,]";
        String[] tokens = (this.phoneNumbers.getText().toString()).split(delim);
        return tokens;
    }
}
