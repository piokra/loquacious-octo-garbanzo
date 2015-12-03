package com.panpiotr.ostolenkyo.OCR.Utill;

import com.panpiotr.ostolenkyo.Bitmap;

/**
 * Created by Pan Piotr on 03/12/2015.
 */
public class LearningData {
    public final Bitmap Bitmap;
    public final LearningType Type;

    public LearningData(Bitmap bitmap, LearningType type) {
        Bitmap = bitmap;
        Type = type;
    }
}
