package com.example.mytestapp.sort;

public class SelectSort {

    public void sort(int[] args) {
        int len = args.length;
        for (int i = 0, k = 0; i < len; i++, k = i) {
            // 在这一层循环中找最小
            for (int j = i + 1; j < len; j++) {
                // 如果后面的元素比前面的小，那么就交换下标，每一趟都会选择出来一个最小值的下标
                if (args[k] > args[j]) k = j;
            }
            if (i != k) {
                int tmp = args[i];
                args[i] = args[k];
                args[k] = tmp;
            }
        }
    }
}
