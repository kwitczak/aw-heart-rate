package com.krzysztofwitczak.androwearapp.wear_connection;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.krzysztofwitczak.androwearapp.emotions.Emotion;
import com.krzysztofwitczak.androwearapp.emotions.EmotionClassifier;
import com.krzysztofwitczak.androwearapp.game_server_connection.GameConnectionHelper;

public class WearListCallListenerService extends WearableListenerService {
    public static final String LOG_KEY = "WEAR_SERVICE";
    public static final String HEART_RATE = "heart_rate";
    public static final String EMOTION_NAME = "emotion_name";
    public static final String EMOTION_CERTAINTY = "emotion_certainty";
    public static final String EMOTION_THRESHOLDS = "EMOTION_THRESHOLDS";
    public static final String BROADCAST_NAME = WearListCallListenerService.class.getName() + "HearRateBroadcast";

    private EmotionClassifier emotionClassifier;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOG_KEY, "Service onCreate() called!");
        emotionClassifier = EmotionClassifier.getInstance();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        int heartRate = Integer.parseInt(messageEvent.getPath());
        Log.i(LOG_KEY, "Heart rate gathered from the watch!: " + heartRate);

        // TODO: Calculate EmotionType here, send it to UI + HeartRate
        emotionClassifier.getHeartData().add(heartRate);
        Emotion emotion = emotionClassifier.getEmotion();

        if (GameConnectionHelper.connectionSuccessful) {
            GameConnectionHelper.transmitHeartData(
                    emotion,
                    heartRate);
        }

        String emotionName = emotion.getEmotionType().toString();
        sendBroadcastMessage(Integer.toString(heartRate),
                emotionName.substring(0, 1) + emotionName.substring(1).toLowerCase(),
                Integer.toString(Math.round(emotion.getCertainty())),
                String.format("%d - %d Bpm",
                        emotionClassifier.boredThreshold,
                        emotionClassifier.stressedThreshold));
    }

    private void sendBroadcastMessage(String hearRate, String emotion, String certainty, String tresholds) {
        Intent intent = new Intent(BROADCAST_NAME);
        intent.putExtra(HEART_RATE, hearRate);
        intent.putExtra(EMOTION_NAME, emotion);
        intent.putExtra(EMOTION_CERTAINTY, certainty);
        intent.putExtra(EMOTION_THRESHOLDS, tresholds);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
