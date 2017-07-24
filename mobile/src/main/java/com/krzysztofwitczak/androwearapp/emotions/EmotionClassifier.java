package com.krzysztofwitczak.androwearapp.emotions;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EmotionClassifier {
    public static final String LOG_KEY = "EMOTION_CLASSIFIER";

    private List<Integer> heartData;

    public EmotionClassifier() {
        heartData = new ArrayList<Integer>();
    }

    public List<Integer> getHeartData() {
        return heartData;
    }

    public Emotion getEmotion() {

        // TODO: Implement different algorithms
        return simpleEmotionDetection();
    }

    private Emotion simpleEmotionDetection() {
        if (heartData.size() <= 5) {
            Log.i(LOG_KEY, "Need more than " + heartData.size() + " data points...");
            return Emotion.FOCUSED;
        }

        List<Integer> lastFiveDataPoints = heartData.subList(heartData.size() - 5, heartData.size());
        int average = calculateAverage(lastFiveDataPoints);
        Log.i(LOG_KEY, "Average of last 5 points: " + average);

        Emotion detectedEmotion;
        if (isBetween(average, 0, 60)) {
            detectedEmotion = Emotion.BORED;
        } else if (isBetween(average, 61, 75)) {
            detectedEmotion = Emotion.FOCUSED;
        } else {
            detectedEmotion = Emotion.STRESSED;
        }

        return detectedEmotion;
    }

    private int calculateAverage(List <Integer> data) {
        int sum = 0;
        for (int i=0; i< data.size(); i++) {
            sum += data.get(i);
        }

        return sum / data.size();
    }

    private boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
}
