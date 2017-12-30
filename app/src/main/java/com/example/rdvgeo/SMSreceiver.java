package com.example.rdvgeo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSreceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "ON RECEIVE BROADCAST", Toast.LENGTH_LONG).show();
        Log.d("ON ", "RECEIVE");
        SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        SmsMessage smsMessage = msgs[0];

        Pattern p = Pattern.compile("([-+]?[0-9]*\\.?[0-9]+);([-+]?[0-9]*\\.?[0-9]+)");
        Matcher m = p.matcher(smsMessage.getDisplayMessageBody());

        Log.d("Message",smsMessage.getDisplayMessageBody());

        double [] loc = receptionMessage(smsMessage.getDisplayMessageBody());
        Log.d("Longitude ", ""+ loc[0] );
        Log.d("Latitude ", ""+loc[1] );

    }

    public double [] receptionMessage (String message) {
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