package com.example.rdvgeo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class RdvsAdapter extends ArrayAdapter<Rendezvous> {


    public RdvsAdapter(Context context, ArrayList<Rendezvous> rdvs) {
        super(context, 0, rdvs);


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Rendezvous rdv = getItem(position);

        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_rdv, parent, false);
            // inflate custom layout called item_rdv
            holder = new ViewHolder();
            holder.tv1 = (TextView) convertView.findViewById(R.id.tvTitre);
            holder.tv2 = (TextView) convertView.findViewById(R.id.tvEmetteur);
            holder.tv3 = (TextView) convertView.findViewById(R.id.tvLongitude);
            holder.tv4 = (TextView) convertView.findViewById(R.id.tvLatitude);
            holder.bt1 = (Button) convertView.findViewById(R.id.btAccepter);
            holder.bt2 = (Button) convertView.findViewById(R.id.btRefuser);
            holder.bt3 = (Button) convertView.findViewById(R.id.btVoir);
            // initialize textview
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv1.setText(rdv.getTitre());
        holder.tv2.setText(""+rdv.getEmetteur());
        holder.tv3.setText(""+rdv.getLongitude());
        holder.tv4.setText(""+rdv.getLatitude());

        holder.bt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Row " + position + " was clicked!", Toast.LENGTH_SHORT).show();
                String s;
                s = "RDVGeo : Reponse à l'invitation \n" +
                        "Acceptation";
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(""+rdv.getEmetteur(), null, s, null, null);
            }
        });

        holder.bt2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Row " + position + " was clicked!", Toast.LENGTH_SHORT).show();
                String s;
                s = "RDVGeo : Reponse à l'invitation \n" +
                        "Refus";
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(""+rdv.getEmetteur(), null, s, null, null);
            }
        });

        holder.bt3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Creates an Intent that will load a map of San Francisco
                Uri gmmIntentUri = Uri.parse("geo:"+ rdv.getLongitude()+", "+rdv.getLatitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                getContext().startActivity(mapIntent);
            }
        });
        // set the name to the text;

        return convertView;

    }

    static class ViewHolder {
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        Button bt1;
        Button bt2;
        Button bt3;
    }
}