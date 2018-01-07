package com.example.rdvgeo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSreceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        SmsMessage smsMessage = msgs[0];
        String numero = smsMessage.getDisplayOriginatingAddress();
        numero = numero.substring(numero.length()-4);
        if (isRDVGeoMessage(smsMessage.getDisplayMessageBody())) {

            if (isRDVDemande(smsMessage.getDisplayMessageBody())) {
                double[] loc = getLocalisation(smsMessage.getDisplayMessageBody());
                Log.d("Longitude ", "" + loc[0]);
                Log.d("Latitude ", "" + loc[1]);

                //Bundle extras = intent.getExtras();
                Intent i = new Intent("mycustombroadcast");
                i.putExtra("phone_num", numero);
                i.putExtra("longitude", loc[0]);
                i.putExtra("latitude", loc[1]);
                context.sendBroadcast(i);
                Toast.makeText(context, smsMessage.getDisplayMessageBody().toString(), Toast.LENGTH_SHORT).show();
                HomeRdvActivity inst = HomeRdvActivity.instance();
                Log.d("TAG", "" + Integer.parseInt(numero));
                inst.updateList(new Rendezvous("Titre", Integer.parseInt(numero), loc[0], loc[1]));
            }
            else
                Log.d("RDVGeo", "Reponse : On ne traite pas ce sms");
        }
        else
            Log.d("TAG", "On ne traite pas ce sms");
    }


    public boolean isRDVGeoMessage(String message){
        String regexRDV = "^RDVGeo";
        Pattern rdvPattern = Pattern.compile(regexRDV);
        Matcher m = rdvPattern.matcher(message);
        return m.find();
    }
    public boolean isRDVDemande(String message) {
        String regexRDV = "Nouvelle demande";
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
}