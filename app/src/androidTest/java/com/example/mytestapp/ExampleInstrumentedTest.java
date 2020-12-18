package com.example.mytestapp;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.mytestapp.sort.BubbleSort;
import com.example.mytestapp.sort.SelectSort;
import com.example.mytestapp.utils.LogUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.mytestapp", appContext.getPackageName());
    }

    int[] mIntArray = {5,8,1,2,6,11,85};
    @Test
    public void testSelectSort(){
        SelectSort selectSort = new SelectSort();
        for (int i: mIntArray){
            LogUtil.i("AndroidJUnit4","sort前intArray----"+i);
        }
        selectSort.sort(mIntArray);
        for (int i: mIntArray){
            LogUtil.i("AndroidJUnit4","sort后intArray----"+i);
        }
    }
    @Test
    public void testBubbleSort(){
        BubbleSort bubbleSort = new BubbleSort();
        bubbleSort.sort(mIntArray);
        for (int i: mIntArray){
            LogUtil.i("AndroidJUnit4","sort后intArray----"+i);
        }
    }
}