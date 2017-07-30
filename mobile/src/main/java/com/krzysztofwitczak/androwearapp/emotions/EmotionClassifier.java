package com.krzysztofwitczak.androwearapp.emotions;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EmotionClassifier {
    public static final String LOG_KEY = "EMOTION_CLASSIFIER";

    private List<Integer> heartData;
    private int baseHeartRate;
    private int boredThreshold;
    private int stressedThreshold;

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
        if (heartData.size() < 5) {
            Log.i(LOG_KEY, "Need more than " + heartData.size() + " data points...");
            return new Emotion(EmotionType.FOCUSED, 0f);
        }

        List<Integer> lastFiveDataPoints = heartData.subList(heartData.size() - 5, heartData.size());
        int average = calculateAverage(lastFiveDataPoints);
        Log.i(LOG_KEY, "Average of last 5 points: " + average);

        if (heartData.size() == 5) {
            Log.i(LOG_KEY, "Having enough data to count starting heart rate! It is: " + average);
            baseHeartRate = average;

            boredThreshold = baseHeartRate - (int)(baseHeartRate * 0.07);
            stressedThreshold = baseHeartRate + (int)(baseHeartRate * 0.07);
            Log.i(LOG_KEY, "Focus is between: " + boredThreshold + " and " + stressedThreshold);
        }

        float certainty;
        EmotionType detectedEmotionType;
        if (isBetween(average, 0, boredThreshold)) {
            detectedEmotionType = EmotionType.BORED;

            float range = boredThreshold - average;
            certainty = (float) Math.log10(range) * 100;
        } else if (isBetween(average, boredThreshold + 1, stressedThreshold)) {
            detectedEmotionType = EmotionType.FOCUSED;

            float range = (stressedThreshold - boredThreshold)/2;
            float middle = range + boredThreshold;
            certainty = ((range - Math.abs(average - middle))/range) * 100;
        } else {
            detectedEmotionType = EmotionType.STRESSED;

            float range = average - stressedThreshold;
            certainty = (float) Math.log10(range) * 100;
        }

        return new Emotion(detectedEmotionType, Math.min(Math.max(certainty, 15), 100));
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
