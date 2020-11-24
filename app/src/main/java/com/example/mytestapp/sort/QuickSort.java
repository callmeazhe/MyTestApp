package com.example.mytestapp.sort;

public class QuickSort {

    private int mCount = 0;

    public void quicksort(int[] args, int start, int end) {
        if (end - start > 1) {
            int mid = dividerAndChange(args, start, end);
            quicksort(args, start, mid);
            quicksort(args, mid + 1, end);
        }
    }

    public int dividerAndChange(int[] array, int left, int right) {
        int pivot = array[left];
        while (left < right) {
            while (array[right] >= pivot && left < right)
                right--;
            if (left < right) {
                swap(array, left, right);
                left++;
                mCount++;
            }
            while (array[left] < pivot && left < right)
                left++;
            if (left < right) {
                swap(array, right, left);
                right--;
                mCount++;
            }
        }

        array[left] = pivot;
        return left;
    }

    private void swap(int[] args, int fromIndex, int toIndex) {
        args[fromIndex] = args[toIndex];
    }

}
