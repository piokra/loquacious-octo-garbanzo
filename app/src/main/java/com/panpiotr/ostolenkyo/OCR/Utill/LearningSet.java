package com.panpiotr.ostolenkyo.OCR.Utill;

import android.util.Log;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.TreeSet;

/**
 * Created by Pan Piotr on 03/12/2015.
 */
public class LearningSet {
    private final ArrayList<LearningData> mData = new ArrayList<>();
    private final Random mRandom = new Random(System.currentTimeMillis());
    private final Hashtable<LearningType, ArrayList<LearningData>> mTypedData = new Hashtable<>();
    private final ArrayList<LearningType> mTypeList = new ArrayList<>();
    private final TreeSet<LearningType> mTypeSet = new TreeSet<>();
    private int mNext = 0;

    public LearningSet() {

    }

    public static void shuffleData(ArrayList<LearningData> ld, int shuffles) {
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < shuffles; i++) {
            int l = Math.abs(random.nextInt()) % ld.size();
            int r = Math.abs(random.nextInt()) % ld.size();
            LearningData t = ld.get(l);
            ld.set(l, ld.get(r));
            ld.set(r, t);
        }
    }

    public void addData(LearningData data) {
        mData.add(data);
        if (mTypeSet.contains(data.Type)) {
            mTypedData.get(data.Type).add(data);
        } else {
            ArrayList<LearningData> newList = new ArrayList<>();
            newList.add(data);
            mTypedData.put(data.Type, newList);
            mTypeSet.add(data.Type);
        }
    }

    public void setPosition(int pos) {
        mNext = pos;
    }

    public LearningData getNext() {
        int t = mNext % mData.size();
        mNext = (mNext + 1) % mData.size();
        return mData.get(t);
    }

    public LearningData get(int i) {
        return mData.get(i);
    }

    public LearningData getOfType(int i, LearningType type) {
        return mTypedData.get(type).get(i);
    }

    public int getDataSize() {
        return mData.size();
    }

    public int getDataSizeOfType(LearningType type) {
        return mTypedData.get(type).size();
    }

    LearningData getRandom() {
        return mData.get(Math.abs(mRandom.nextInt()) % mData.size());
    }

    LearningData getRandomOfType(LearningType type) throws LearningSetException {
        if (!mTypeSet.contains(type)) throw new LearningSetException();
        return mTypedData.get(type).get(Math.abs(mRandom.nextInt()) % mTypedData.get(type).size());
    }

    LearningData getRandomOfNotType(LearningType type) {

        int randomPos = Math.abs(mRandom.nextInt()) % (mTypeList.size() - 1);
        if (mTypeList.get(randomPos) == type) {
            randomPos++;
        }
        LearningType randomType = mTypeList.get(randomPos);
        try {
            return getRandomOfType(randomType);
        } catch (LearningSetException e) {
            Log.d("LearningSet", "This is never supposed to happen.");
            e.printStackTrace();
        }
        return null;
    }

    //TODO add more variety to lSEXceptions ;=]
    public LearningSet getLearningDataSubset(LearningType selectedType, int selectedTypeCount, int otherTypeCount, double shufflePercentile) throws LearningSetException {
        if (getDataSizeOfType(selectedType) < selectedTypeCount) throw new LearningSetException();
        if (getDataSize() - getDataSizeOfType(selectedType) < otherTypeCount)
            throw new LearningSetException();
        if (!mTypeSet.contains(selectedType)) throw new LearningSetException();
        ArrayList<LearningData> untypedData = new ArrayList<>();
        untypedData.addAll(mData);


        shuffleData(untypedData, (int) (untypedData.size() * shufflePercentile));


        TreeSet<LearningType> set = new TreeSet<>();
        ArrayList<LearningData> resultUntypedData = new ArrayList<>();
        int selectedRemaining = selectedTypeCount;
        int otherRemaining = otherTypeCount;
        for (LearningData data : untypedData) {
            if (data.Type == selectedType) {
                if (selectedRemaining > 0) {
                    resultUntypedData.add(data);
                    selectedRemaining--;
                }
            } else {
                if (otherRemaining > 0) {
                    resultUntypedData.add(data);
                    otherRemaining--;
                }

            }
            if (selectedRemaining + otherRemaining == 0) break;
        }

        LearningSet result = new LearningSet();
        for (LearningData data : resultUntypedData) {
            result.addData(data);
        }
        return result;

    }
}
