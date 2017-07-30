package com.krzysztofwitczak.androwearapp.emotions;

public class Emotion {
    private EmotionType emotionType;
    private float certainty;

    public Emotion(EmotionType emotionType, float certainty){
        this.emotionType = emotionType;
        this.certainty = certainty;
    }

    public EmotionType getEmotionType() {
        return emotionType;
    }

    public void setEmotionType(EmotionType emotionType) {
        this.emotionType = emotionType;
    }

    public float getCertainty() {
        return certainty;
    }

    public void setCertainty(float certainty) {
        this.certainty = certainty;
    }
}
