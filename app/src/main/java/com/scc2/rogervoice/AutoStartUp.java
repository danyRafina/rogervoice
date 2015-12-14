package com.scc2.rogervoice;

import android.app.Service;
import android.content.Context;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import android.content.Intent;

public class AutoStartUp extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        // do something when the service is created
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // register PhoneStateListener
        PhoneStateListener callStateListener = new PhoneStateListener() {
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    Toast.makeText(getApplicationContext(), "Phone Is Riging", Toast.LENGTH_LONG).show();
                    Intent dialogIntent = new Intent(AutoStartUp.this, PhoneCall.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);

                }
                // If incoming call received
                if (state == TelephonyManager.CALL_STATE_OFFHOOK) {

                    Toast.makeText(getApplicationContext(), "Phone is Currently in A call", Toast.LENGTH_LONG).show();
                }
            }
        };
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);


    }
}