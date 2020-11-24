package com.example.mytestapp.sort;

public class BubbleSort {

    private int mCount = 0;

    public int sort(int[] array) {

        for (int j = 0; j < array.length; j++) {
            for (int i = 0; i < array.length - 1; i++) {
                if (array[i] < array[i + 1]) {
                    continue;
                } else {
                    int temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                    mCount++;
                }
            }
        }
        return mCount;
    }
}
