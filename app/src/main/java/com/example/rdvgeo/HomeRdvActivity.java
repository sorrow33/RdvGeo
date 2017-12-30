package com.example.rdvgeo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import static com.example.rdvgeo.RendezVousGeoLocDbHelper.DATABASE_NAME;

public class HomeRdvActivity extends Activity {

    private static final String TAG = "HomeRdvActivity";

    RendezVousGeoLocDbHelper dbHelper;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_rdv);

        mListView = (ListView) findViewById(R.id.rdvlist);
        dbHelper = new RendezVousGeoLocDbHelper(this);

        //dbHelper.onDowngrade(dbHelper,1,1);
        //this.deleteDatabase(DATABASE_NAME);
        /**
         * CRUD Operations
         * */
        // add rdvs
        dbHelper.addRendezVous(new Rendezvous("Medecin",32.3664377f,32.3664377f,2));
        dbHelper.addRendezVous(new Rendezvous("Coiffeur",32.3664377f,32.3664377f,1));
        dbHelper.addRendezVous(new Rendezvous("FAC",32.3664377f,32.3664377f,6));

        // get all rdvs
        List<Rendezvous> list = dbHelper.getAllRDV();

        mListView.setAdapter(new CustomArrayAdapter(HomeRdvActivity.this,list));
        // delete one rdv
        //dbHelper.deleteRDV(list.get(0));

        // get all rdvs
        //dbHelper.getAllRDV();
    }

    public void nouveauRdv(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");
        List<Rendezvous> list = dbHelper.getAllRDV();

    }
}
