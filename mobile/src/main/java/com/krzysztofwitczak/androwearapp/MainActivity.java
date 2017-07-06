package com.krzysztofwitczak.androwearapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    static final String LOG_KEY = "MOBILE_MAIN_ACTIVITY";

    private TextView mHeartRateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(LOG_KEY, "onCreate() called!");

        mHeartRateView = (TextView) findViewById(R.id.heart_rate_mobile);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        mHeartRateView.setText(
                                intent.getStringExtra(WearListCallListenerService.HEART_RATE));
                    }
                }, new IntentFilter(WearListCallListenerService.BROADCAST_NAME)
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_KEY, "onStart() called!");
        startService(new Intent(MainActivity.this, WearListCallListenerService.class));
    }
}
