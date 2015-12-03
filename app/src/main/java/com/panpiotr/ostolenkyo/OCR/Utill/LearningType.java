package com.panpiotr.ostolenkyo.OCR.Utill;

import java.util.ArrayList;

/**
 * Created by Pan Piotr on 03/12/2015.
 */
public class LearningType {
    private static ArrayList<LearningType> Types = new ArrayList<>();

    private LearningType() {
        Types.add(this);
    }

    private static LearningType getType(int i) {
        return Types.get(i % Types.size());
    }

    public static int addNewType() {
        new LearningType();
        return Types.size() - 1;
    }
}
