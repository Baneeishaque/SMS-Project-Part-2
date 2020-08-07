package com.nqr.smsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Objects;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle intentExtras = intent.getExtras();

        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            StringBuilder smsMessageStr = new StringBuilder();
            for (Object sm : Objects.requireNonNull(sms)) {

                String format = intentExtras.getString("format");
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sm, format);

                String smsBody = smsMessage.getMessageBody();
                String address = smsMessage.getOriginatingAddress();

                smsMessageStr.append("SMS From: ").append(address).append("\n");
                smsMessageStr.append(smsBody).append("\n");

            }


            Toast.makeText(context, "Message Received!", Toast.LENGTH_SHORT).show();

            if (MainActivity.active) {
                MainActivity inst = MainActivity.instance();
                inst.updateInbox(smsMessageStr.toString());
            } else {
                Intent i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }

        }
    }
}
