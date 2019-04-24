package pl.pawelkleczkowski.customgauge;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.SweepGradient;

import java.util.ArrayList;
import java.util.List;

import static pl.pawelkleczkowski.customgauge.Utils.convertIntegers;
import static pl.pawelkleczkowski.customgauge.Utils.mixTwoColors;

/**
 * Created by Neo on 2018-08-08.
 * http://m3n.ir/
 */

class LightEffect {
    private static final int GRADIENT_COLORS_ARRAY_SIZE = 280;
    private static final int LIGHT_RANGE_PERCENT = 12; // 1 to 100 %
    private static final float LIGHT_RANGE_SIZE = (GRADIENT_COLORS_ARRAY_SIZE * LIGHT_RANGE_PERCENT) / 100; // 1 to 100 %
    private int[] mGradientColorsRange;
    private float mLightPosition = 0;
    private float mLightMoveSpeed = .5f;
    private List<Integer> mColorsRangeList = new ArrayList<>();
//    private Matrix matrix = new Matrix();
    private int colorStart, colorEnd;

    LightEffect(int colorStart, int colorEnd) {
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
        makeGradientRangeArrayColor();
    }

    void renderGradient(Paint mPaint, int width, int height){
        mColorsRangeList.clear();
        boolean lightGradientAdded = false;

        int colorI = 0;
        while (mColorsRangeList.size() < GRADIENT_COLORS_ARRAY_SIZE) {
            if (colorI == (int) mLightPosition && !lightGradientAdded) {
                //Arrays.copyOfRange(gradientColorsArea, colorI, (int)mLightRangeSize)
                for (int color : makeLightAreaArrayColor(getGradientLightColorsRange(colorI))) {
                    if (colorI >= GRADIENT_COLORS_ARRAY_SIZE)
                        mColorsRangeList.set(colorI - GRADIENT_COLORS_ARRAY_SIZE, color);
                    else
                        mColorsRangeList.add(color);
                    colorI++;
                }
                lightGradientAdded = true;
                continue;
            } else {
                mColorsRangeList.add(mGradientColorsRange[colorI]);
            }

            if (mLightPosition > GRADIENT_COLORS_ARRAY_SIZE - 1) {
                mLightPosition = 0;
            }
            colorI++;
        }

        mLightPosition += mLightMoveSpeed;
        SweepGradient gradient = new SweepGradient(width / 2, height / 2,
                convertIntegers(mColorsRangeList, GRADIENT_COLORS_ARRAY_SIZE), null );
        mPaint.setShader(gradient);
    }


    private List<Integer> makeLightAreaArrayColor(List<Integer> colors) {
        List<Integer> newColors = new ArrayList<>();
        int halfGradientSize = colors.size() / 2;

        int j = 0;
        for (int i = 0; i < halfGradientSize; i++) {
            newColors.add(mixTwoColors(Color.WHITE, colors.get(j), (i * 1.0f) / halfGradientSize));
            j++;
        }
        for (int i = halfGradientSize - 1; i > -1; i--) {
            newColors.add(mixTwoColors(Color.WHITE, colors.get(j), (i * 1.0f) / halfGradientSize));
            j++;
        }

        return newColors;
    }



    private List<Integer> getGradientLightColorsRange(int position) {
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < LIGHT_RANGE_SIZE; i++) {
            int index = position + i;
            if (index >= GRADIENT_COLORS_ARRAY_SIZE) index -= GRADIENT_COLORS_ARRAY_SIZE;
            colors.add(mGradientColorsRange[index]);
        }
        return colors;
    }

    private void makeGradientRangeArrayColor() {
        List<Integer> colors = new ArrayList<>();

        int areaSize = GRADIENT_COLORS_ARRAY_SIZE / 2;
        for (int i = 0; i < areaSize; i++) {
            colors.add(mixTwoColors(colorStart, colorEnd, (i * 1.0f) / areaSize));
        }

        for (int i = areaSize - 1; i > -1; i--) {
            colors.add(mixTwoColors(colorStart, colorEnd, (i * 1.0f) / areaSize));
        }

        mGradientColorsRange = convertIntegers(colors);
    }
}
