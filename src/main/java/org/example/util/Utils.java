package org.example.util;

public class Utils {
    public static int[][] clone(final int[][] value) {
        int[][] cloned = new int[value.length][];
        for (int i = 0; i < value.length; i++) {
            cloned[i] = value[i].clone();
        }
        return cloned;
    }

    public static int[] toReversed(int[] value) {
        int[] reversed = new int[value.length];
        for (int i = 0; i < value.length; i++) {
            reversed[i] = value[value.length - 1 - i];
        }
        return reversed;
    }
}
