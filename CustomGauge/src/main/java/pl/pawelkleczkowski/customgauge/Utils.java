package pl.pawelkleczkowski.customgauge;

import java.util.List;

/**
 * Created by Neo on 2018-08-06.
 * http://m3n.ir/
 */

class Utils {

    static int mixTwoColors(int color1, int color2, float amount) {
        final byte ALPHA_CHANNEL = 24;
        final byte RED_CHANNEL = 16;
        final byte GREEN_CHANNEL = 8;
        final byte BLUE_CHANNEL = 0;

        final float inverseAmount = 1.0f - amount;

        int a = ((int) (((float) (color1 >> ALPHA_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> ALPHA_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int r = ((int) (((float) (color1 >> RED_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> RED_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int g = ((int) (((float) (color1 >> GREEN_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> GREEN_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int b = ((int) (((float) (color1 & 0xff) * amount) +
                ((float) (color2 & 0xff) * inverseAmount))) & 0xff;

        return a << ALPHA_CHANNEL | r << RED_CHANNEL | g << GREEN_CHANNEL | b << BLUE_CHANNEL;
    }

    static int[] convertIntegers(List<Integer> integers) {
        return convertIntegers(integers, integers.size());
    }

    static int[] convertIntegers(List<Integer> integers, int count) {
        int arraySize = count;
        if(integers.size()<count)
            arraySize = integers.size();
        int[] ret = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            ret[i] = integers.get(i);
        }
        return ret;
    }
}
