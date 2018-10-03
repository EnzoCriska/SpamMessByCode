package com.example.dungtt.spammessagebycode;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;


public class SMSReceiver extends BroadcastReceiver {
    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        String message = "";
        String sender = "";
        if (bundle != null) {
            final Object[] objpdus = (Object[]) bundle.get("pdus");
            for (int i = 0; i < objpdus.length; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) objpdus[i]);
                String phoneNumber = smsMessage.getDisplayOriginatingAddress();
                sender = phoneNumber;
                String bodyMessage = smsMessage.getDisplayMessageBody();
                if (message.equals("")) {
                    message = bodyMessage;
                } else {
                    message += bodyMessage;
                }
            }
            if (checkSpamNum(context, sender)) {
                Log.w("SmsReceiver", "senderNum: " + sender + "; message: " + message.length() + " --" + message);
                deleteMessage(context, sender, message);
                soundNotice(context);
                abortBroadcast();
            }
        }
    }

    public boolean checkSpamNum(Context context, String spamNum) {
        SQLiteHandle sqLiteHandle = new SQLiteHandle(context);
        ContactSpam contactSpam = sqLiteHandle.querySpam(spamNum);
        if (contactSpam == null) {
            Log.w("SmsReceiver", "Check spam false");
            return false;
        } else {
            Log.w("SmsReceiver", "Check spam true");
            contactSpam.setCount(contactSpam.getCount() + 1);
            sqLiteHandle.updateSpam(contactSpam);
            return true;
        }
    }

    public void deleteMessage(Context context, String spamNum, String bodyMess) {
        try {
            Log.w("SmsReceiver", "Deleting SMS from inbox");
            Uri uriSms = Uri.parse("content://sms/inbox");
            Cursor c = context.getContentResolver().query(
                    uriSms, new String[]{"_id", "thread_id", "address", "person",
                            "date", "body"}, null, null, null);

            if (c != null && c.moveToFirst()) {
                do {
                    long id = c.getLong(0);
                    long threadId = c.getLong(1);
                    String address = c.getString(2);
                    String body = c.getString(5);
//                    Log.w("SmsReceiver", id + "; " + threadId + "; " + address +"; " +body);
//                    Log.w("SmsReceiver", spamNum + "; " + bodyMess);
                    if (bodyMess.equals(body) && address.equals(spamNum)) {
                        Log.w("SmsReceiver", "Deleting SMS with id: " + threadId);
                        try {
                            context.getContentResolver().delete(Uri.parse("content://sms/inbox" + id), null, null);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    } else {
//                        Log.w("SmsReceiver", "Not equals...");
                    }

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.w("SmsReceiver", "Could not delete SMS from inbox: " + e.getMessage());
        }
    }

    public void soundNotice(Context context) {
        Notification notification = new Notification.Builder(context).build();
      //  notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.cat);
    }

}
