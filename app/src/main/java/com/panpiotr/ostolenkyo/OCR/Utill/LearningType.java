package com.panpiotr.ostolenkyo.OCR.Utill;

import java.util.ArrayList;

/**
 * Created by Pan Piotr on 03/12/2015.
 */
public class LearningType implements Comparable<LearningType> {
    private static ArrayList<LearningType> Types = new ArrayList<>();
    private final int mTypeNumber;
    private LearningType() {
        mTypeNumber = Types.size();
        Types.add(this);

    }

    public static LearningType getType(int i) {
        while ((Types.size()) < i + 1) addNewType();
        return Types.get(i);

    }

    protected static int addNewType() {
        new LearningType();
        return Types.size() - 1;
    }

    public int getTypeNumber() {
        return mTypeNumber;
    }

    public boolean equals(Object learningType) {
        return ((LearningType) learningType).getTypeNumber() == getTypeNumber();
    }

    @Override
    public int compareTo(LearningType anotherType) {
        if (getTypeNumber() > anotherType.getTypeNumber()) return 1;
        if (getTypeNumber() == anotherType.getTypeNumber()) return 0;
        return -1;
    }

    public int hashCode() {
        Integer i = getTypeNumber();
        return i.hashCode();

    }

}
