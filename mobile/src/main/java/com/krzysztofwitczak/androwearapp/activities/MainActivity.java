package com.krzysztofwitczak.androwearapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.krzysztofwitczak.androwearapp.R;
import com.krzysztofwitczak.androwearapp.wear_connection.WearListCallListenerService;

public class MainActivity extends AppCompatActivity {
    static final String LOG_KEY = "MOBILE_MAIN_ACTIVITY";

    private TextView mHeartRateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

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

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_KEY, "onStart() called!");
        startService(new Intent(MainActivity.this, WearListCallListenerService.class));
//        new Thread(new ServerThread()).start();
    }

    public void openGameSetting(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}
