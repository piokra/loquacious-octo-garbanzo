package com.panpiotr.ostolenkyo.OCR.Utill;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Pan Piotr on 03/12/2015.
 */
public class LearningSet {
    private final ArrayList<LearningData> mData = new ArrayList<>();
    private final Random mRandom = new Random(System.currentTimeMillis());
    int mNext = 0;

    public LearningSet() {

    }

    public void addData(LearningData data) {
        mData.add(data);
    }

    LearningData getNext() {
        int t = mNext % mData.size();
        mNext = (mNext + 1) % mData.size();
        return mData.get(t);
    }

    LearningData get(int i) {
        return mData.get(i % mData.size());
    }

    LearningData getRandom() {
        return mData.get(Math.abs(mRandom.nextInt()) % mData.size());
    }

}
