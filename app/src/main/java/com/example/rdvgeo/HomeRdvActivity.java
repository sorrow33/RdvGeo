package com.example.rdvgeo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeRdvActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_rdv);
    }

    public void nouveauRdv(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
