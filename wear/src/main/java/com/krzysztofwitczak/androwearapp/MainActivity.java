package com.krzysztofwitczak.androwearapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity implements SensorEventListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    static final String LOG_KEY = "WEAR_MAIN_ACTIVITY";
    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    // Layout variables
    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;

    // Connect to mobile
    private GoogleApiClient mGoogleApiClient;
    private Node mNode; // the connected device to send the message to

    // Sensor variables
    private SensorManager mSensorManager;
    private Sensor mHearRateSensor;
    private String mRateValue = "Rate unknown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mTextView = (TextView) findViewById(R.id.text);
        mClockView = (TextView) findViewById(R.id.clock);

        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                                             .addApi(Wearable.API)
                                             .addConnectionCallbacks(this)
                                             .addOnConnectionFailedListener(this)
                                             .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_KEY, "onStart");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_KEY, "onResume");

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BODY_SENSORS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.BODY_SENSORS},
                    1);
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mHearRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mSensorManager.registerListener(this, mHearRateSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause() {
        super.onPause();
        Log.i(LOG_KEY, "onPause");
    }

    protected void onStop() {
        super.onStop();
        Log.i(LOG_KEY, "onStop() called - shutting of HearRate Listener!");
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mRateValue = Integer.toString(Math.round(event.values[0]));
        Log.i(LOG_KEY, mRateValue);
        updateDisplay();

        new SendToDataLayerThread(mRateValue, mRateValue).start();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(LOG_KEY, "Rate accuracy changed!");
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
        Log.i(LOG_KEY, "onEnterAmbient() - Turning off the screen...");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOG_KEY, "Google API connection successful!");
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(@NonNull NodeApi.GetConnectedNodesResult nodes) {
                        for (Node node : nodes.getNodes()) {
                            Log.i(LOG_KEY, node.toString());
                            mNode = node;
                        }
                    }
                });
    }

    private class SendToDataLayerThread extends Thread {
        String path;
        String message;

        // Constructor to send a message to the data layer
        SendToDataLayerThread(String p, String msg) {
            path = p;
            message = msg;
        }

        public void run() {
            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                    mGoogleApiClient, mNode.getId(), path, message.getBytes()).await();

            if (result.getStatus().isSuccess()) {
                Log.v(LOG_KEY, "Message: {" + message + "} sent to: " + mNode.getDisplayName());
            } else {
                Log.v(LOG_KEY, "ERROR: failed to send Message");
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);

            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }
        mTextView.setText(mRateValue);
    }
}
