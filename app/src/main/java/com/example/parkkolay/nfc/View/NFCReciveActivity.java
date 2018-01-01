package com.example.parkkolay.nfc.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.parkkolay.nfc.R;

public class NFCReciveActivity extends AppCompatActivity {
    BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcrecive);
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
         receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("NFCRECIVED","test");
            }
        };
        registerReceiver(receiver, ndefIntent);
    }

    @Override
    protected void onPause() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onPause();
    }
}
