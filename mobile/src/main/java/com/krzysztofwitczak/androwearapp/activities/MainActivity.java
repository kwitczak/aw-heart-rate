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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.krzysztofwitczak.androwearapp.R;
import com.krzysztofwitczak.androwearapp.emotions.EmotionClassifier;
import com.krzysztofwitczak.androwearapp.wear_connection.WearListCallListenerService;

public class MainActivity extends AppCompatActivity {
    static final String LOG_KEY = "MOBILE_MAIN_ACTIVITY";

    private TextView mHeartRateView;
    private TextView mEmotionView;
    private TextView mThresholdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_KEY, "onCreate() called!");

        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mHeartRateView = (TextView) findViewById(R.id.heart_rate_mobile);
        mEmotionView = (TextView) findViewById(R.id.emotionType);
        mThresholdView = (TextView) findViewById(R.id.thresholds);

        ImageView imageView = (ImageView) findViewById(R.id.circle);
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        imageView.startAnimation(pulse);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        mHeartRateView.setText(
                                String.format(
                                        "%s Bpm",
                                        intent.getStringExtra(
                                                WearListCallListenerService.HEART_RATE)));

                        String emotionText;
                        String certainty = intent.getStringExtra(
                                WearListCallListenerService.EMOTION_CERTAINTY);
                        if(certainty.equals("0")) {
                            emotionText = "Analyzing...";
                        } else {
                            emotionText = String.format(
                                    "%s (%s%%)",
                                    intent.getStringExtra(WearListCallListenerService.EMOTION_NAME),
                                    certainty);
                        }
                        mEmotionView.setText(emotionText);

                        String thresholds = intent.getStringExtra(
                                WearListCallListenerService.EMOTION_THRESHOLDS);
                        if(thresholds.equals("0 - 0 Bpm")) thresholds = "Analyzing...";
                        mThresholdView.setText(thresholds);
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
    }

    public void openGameSetting(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        EmotionClassifier.currentAlgorithm = EmotionClassifier.Algorithm.DEFAULT;
    }

    public void showEmulateModal(View view) {
        new EmulateDialog().show(getSupportFragmentManager(), "EmulateDialog");
    }

    public void resetProfile(MenuItem item) {
        EmotionClassifier.getInstance().resetProfile();
    }
}
