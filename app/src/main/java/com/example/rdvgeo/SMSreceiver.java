package com.example.rdvgeo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSreceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "ON RECEIVE BROADCAST", Toast.LENGTH_LONG).show();
        Log.d("ON ", "RECEIVE");
        SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        SmsMessage smsMessage = msgs[0];
        Log.d("Msg ", "" + smsMessage.getDisplayMessageBody());
    }
}