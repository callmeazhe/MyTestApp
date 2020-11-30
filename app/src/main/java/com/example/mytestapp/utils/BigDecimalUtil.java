package com.example.mytestapp.utils;

import java.math.BigDecimal;

/**
 * @author 30038070
 * @date 2020/7/22
 */
public class BigDecimalUtil {

    public static BigDecimal getBigDecimal(Object valueF)
    {
        return new BigDecimal(String.valueOf(valueF));
    }
}
