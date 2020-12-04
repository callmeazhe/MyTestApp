package com.example.mytestapp.utils;

import java.math.BigDecimal;

public class BigDecimalUtil {

    public static BigDecimal getBigDecimal(Object valueF)
    {
        return new BigDecimal(String.valueOf(valueF));
    }
}
