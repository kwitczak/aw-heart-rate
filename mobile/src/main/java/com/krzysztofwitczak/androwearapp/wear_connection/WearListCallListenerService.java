package com.krzysztofwitczak.androwearapp.wear_connection;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.krzysztofwitczak.androwearapp.emotions.Emotion;
import com.krzysztofwitczak.androwearapp.game_server_connection.GameConnectionHelper;

public class WearListCallListenerService extends WearableListenerService {
    public static final String LOG_KEY = "WEAR_SERVICE";
    public static final String HEART_RATE = "heart_rate";
    public static final String BROADCAST_NAME = WearListCallListenerService.class.getName() + "HearRateBroadcast";


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOG_KEY, "Service onCreate() called!");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        String heartRate = messageEvent.getPath();
        Log.i(LOG_KEY, "Hear rate gathered from the watch!: " + heartRate);

        // TODO: Calculate Emotion here, send it to UI + HearRate

        if (GameConnectionHelper.connectionSuccessful) {
            GameConnectionHelper.transmitHeartData(
                    Emotion.randomEmotion(),
                    Integer.parseInt(heartRate));
        }

        sendBroadcastMessage(heartRate);
    }

    private void sendBroadcastMessage(String hearRate) {
            Intent intent = new Intent(BROADCAST_NAME);
            intent.putExtra(HEART_RATE, hearRate);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
