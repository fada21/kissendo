package com.fada21.android.kissendo.sending;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;

import static com.fada21.android.kissendo.utils.Utils.canImplicitIntentBeHandled;
import static com.fada21.android.kissendo.utils.Utils.hasSmsPermission;

public class SendSMSBroadcastReceiver extends BroadcastReceiver {

    public static final String KISSENDO_SEND_SMS_BROADCAST = "com.fada21.android.kissendo.SEND_SMS_BROADCAST";
    public static final String KISSENDO_SEND_SMS_BROADCAST_NUMBER = "KISSENDO_SEND_SMS_BROADCAST_NUMBER";
    public static final String KISSENDO_SEND_SMS_BROADCAST_MESSAGE = "KISSENDO_SEND_SMS_BROADCAST_MESSAGE";

    @Override public void onReceive(Context context, Intent intent) {
        final String number = intent.getStringExtra(KISSENDO_SEND_SMS_BROADCAST_NUMBER);
        final String message = intent.getStringExtra(KISSENDO_SEND_SMS_BROADCAST_MESSAGE);
        sendSms(context, number, message, hasSmsPermission(context));
    }

    public static final Intent createIntent(@NonNull String number, @NonNull String message) {
        final Intent intent = new Intent(KISSENDO_SEND_SMS_BROADCAST);
        intent.putExtra(KISSENDO_SEND_SMS_BROADCAST_NUMBER, number);
        intent.putExtra(KISSENDO_SEND_SMS_BROADCAST_MESSAGE, message);
        return intent;
    }

    private boolean sendSms(Context context, String number, String message, boolean explicitly) {
        if (explicitly) {
            try {
                final SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, message, null, null);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("sms:" + number));
            smsIntent.putExtra("sms_body", message);
            if (canImplicitIntentBeHandled(context, smsIntent)) {
                context.startActivity(smsIntent);
                return true;
            } else {
                return false;
            }

        }
    }
}
