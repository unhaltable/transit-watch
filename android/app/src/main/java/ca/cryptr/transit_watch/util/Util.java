package ca.cryptr.transit_watch.util;

public class Util {

    /**
     * Concatenates all of the given arrays in a new array
     *
     * @param arrays arrays to concatenate
     * @return the concatenation of all arrays
     */
    public static String[] concatAll(String[]... arrays) {
        int len = 0;
        for (final String[] array : arrays)
            len += array.length;

        final String[] result = new String[len];

        int currentPos = 0;
        for (final String[] array : arrays) {
            System.arraycopy(array, 0, result, currentPos, array.length);
            currentPos += array.length;
        }

        return result;
    }

}
