package com.fada21.android.kissendo.home;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.fada21.android.kissendo.R;
import com.fada21.android.kissendo.contact.ContactPickerFragment;

import static com.fada21.android.kissendo.sending.SendSMSBroadcastReceiver.createIntent;
import static com.fada21.android.kissendo.utils.Utils.hasSmsPermission;

public class HomeActivity extends AppCompatActivity {

    public static final int SMS_PERMISSION_REQUEST_CODE = 1;
    private CallbackSMSPermissions smsPermissionsCallback;

    private EditText phoneNumberInput;
    private EditText smsContentInput;

    private final String defaultMessage = "test";

    private Button btnNotification;
    private int KISSENDO_NOTIFICATION_ID = 0x0000ad01;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            final FragmentManager sfm = getSupportFragmentManager();
            sfm.beginTransaction()
                    .add(R.id.contact_picker_fragment_container,
                         new ContactPickerFragment(),
                         ContactPickerFragment.TAG_ContactPickerFragment)
                    .commit();
        }

        phoneNumberInput = (EditText) findViewById(R.id.edit_sms_content);
        smsContentInput = (EditText) findViewById(R.id.edit_sms_content);

        btnNotification = (Button) findViewById(R.id.btn_issue_notification);
        btnNotification.setOnClickListener(v -> showNotification());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(view -> {
                final Intent broadcastIntent = createIntent(getNumber(), getMessage());
                sendBroadcast(broadcastIntent);
                checkSmsPermissions(new CallbackSMSPermissions() {
                    @Override public void onGranted() {
                        Snackbar.make(view, "Sending sms to " + getNumber(), Snackbar.LENGTH_LONG).setDuration(Snackbar.LENGTH_SHORT).show();
                    }

                    @Override public void onDenied() {
                    }
                });
            });
            fab.requestFocus();
        }

    }

    private void pickContact() {

    }

    private void showNotification() {
        final Notification notification = buildNotification();
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(KISSENDO_NOTIFICATION_ID, notification);
    }

    private Notification buildNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        final String message = getMessage();
        builder.setSmallIcon(R.drawable.ic_favorite_black_24dp)
                .setContentTitle("Kissendo time")
                .setContentText(message);
        final Intent brodcastIntent = createIntent(getNumber(), getMessage());
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(this, 0, brodcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        return builder.build();
    }

    private String getNumber() {
        final String text = phoneNumberInput.getText().toString();
        if (TextUtils.isEmpty(text) || !PhoneNumberUtils.isGlobalPhoneNumber(text)) {
            throw new IllegalStateException("Nope");
        } else return text;
    }


    private String getMessage() {
        final String text = smsContentInput.getText().toString();
        if (TextUtils.isEmpty(text)) {
            return defaultMessage;
        } else return text;
    }

    private void checkSmsPermissions(CallbackSMSPermissions callback) {
        if (!hasSmsPermission(this)) {
            smsPermissionsCallback = callback;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
            return;
        } else {
            callback.onGranted();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                smsPermissionsCallback.onGranted();
            } else {
                smsPermissionsCallback.onDenied();
                smsPermissionsCallback = null;
            }
        }
    }

    private interface CallbackSMSPermissions {
        void onGranted();

        void onDenied();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
