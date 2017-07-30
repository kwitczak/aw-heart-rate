package com.krzysztofwitczak.androwearapp.emotions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum EmotionType {
    BORED,
    FOCUSED,
    STRESSED;

    private static final List<EmotionType> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static EmotionType randomEmotion()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
