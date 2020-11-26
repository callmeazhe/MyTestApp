package com.example.mytestapp.nfc.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by 30038070 on 2018/07/24.
 */

public class NumberUtil {

    public static String correctSymbol(String value) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            float res = convertCultureStringToFloat(value);
            if (res >= 0) {
                stringBuffer.append("+");
            }
            stringBuffer.append(value);
            return stringBuffer.toString();
        } catch (Exception e) {
            return value;
        }
    }

    /**
     * Set Format Decimal Value
     *
     * @param value
     * @param decimalsLength
     * @return the return value will be rounded depending on the specified decimal
     * e.g. input value = 0.9996, decimals length = 3 => return value = 1.000
     */
    public static String setFormatDecimalsValue(float value, int decimalsLength) {
        BigDecimal bd = new BigDecimal(String.valueOf(value));
        bd = bd.setScale(decimalsLength, BigDecimal.ROUND_HALF_UP);
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
        nf.setGroupingUsed(false);
        nf.setMinimumFractionDigits(decimalsLength);
        nf.setMaximumFractionDigits(decimalsLength);
        DecimalFormat df = (DecimalFormat) nf;
        return df.format(bd.doubleValue());
    }

    /**
     * Get the format value string
     *
     * @param value
     * @param decimalsLength
     * @return the returned value will be cut depending on the specified decimal
     * e.g. input value = 0.9996, decimals length = 3 => return value = 0.999
     */
    public static String subDecimalsMaxValue(float value, int decimalsLength) {
        String formatValue = String.valueOf(value);
        Pattern compile = Pattern.compile("[.,]");
        String[] splitString = compile.split(formatValue);

        if (splitString.length == 1) {
            if (decimalsLength == 1) {
                return formatValue + ".0";
            } else if (decimalsLength == 3) {
                return formatValue + ".000";
            }
        } else {
            BigDecimal bd = new BigDecimal(formatValue);
            bd = bd.setScale(decimalsLength, value > 0 ? BigDecimal.ROUND_DOWN : BigDecimal.ROUND_UP);
            NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
            nf.setGroupingUsed(false);
            nf.setMinimumFractionDigits(decimalsLength);
            nf.setMaximumFractionDigits(decimalsLength);
            DecimalFormat df = (DecimalFormat) nf;
            formatValue = df.format(bd.doubleValue());
        }
        return formatValue;
    }

    public static float subDecimalMaxValue(float value, int decimalsLength) {
        String formatValue = String.valueOf(value);
        BigDecimal bd = new BigDecimal(formatValue);
        bd = bd.setScale(decimalsLength, value > 0 ? BigDecimal.ROUND_DOWN : BigDecimal.ROUND_UP);
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
        nf.setGroupingUsed(false);
        nf.setMinimumFractionDigits(decimalsLength);
        nf.setMaximumFractionDigits(decimalsLength);
        return bd.floatValue();
    }

    public static Float convertCultureStringToFloat(String value) {
        char localizedSeparator = DecimalFormatSymbols.getInstance().getDecimalSeparator();
        String floatString = value.replace(localizedSeparator, '.');
        float result = Float.MAX_VALUE;
        try {
            result = Float.valueOf(floatString);
        } catch (Exception e) {
            LogUtil.outLog(e.toString());
        }
        return result;
    }

    public static String convertFloatToCultureString(float value) {
        char localizedSeparator = DecimalFormatSymbols.getInstance().getDecimalSeparator();
        String floatString = String.valueOf(value).replace('.', localizedSeparator);
        return floatString;
    }

    /**
     * Get the format value string
     *
     * @param value
     * @param decimalsLength
     * @return the returned value will be cut depending on the specified decimal
     * e.g. input value = 0.9972, decimals length = 3 => return value = 0.998
     */
    public static String subDecimalsMinValue(float value, int decimalsLength) {
        String formatValue = String.valueOf(value);
        Pattern compile = Pattern.compile("[.,]");
        String[] splitString = compile.split(formatValue);

        if (splitString.length == 1) {
            if (decimalsLength == 1) {
                return formatValue + ".0";
            } else if (decimalsLength == 3) {
                return formatValue + ".000";
            }
        } else {
            BigDecimal bd = new BigDecimal(formatValue);
            bd = bd.setScale(decimalsLength, value > 0 ? BigDecimal.ROUND_UP : BigDecimal.ROUND_DOWN);
            NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
            nf.setGroupingUsed(false);
            nf.setMinimumFractionDigits(decimalsLength);
            nf.setMaximumFractionDigits(decimalsLength);
            DecimalFormat df = (DecimalFormat) nf;
            formatValue = df.format(bd.doubleValue());
        }
        return formatValue;
    }

    public static float subDecimalMinValue(float value, int decimalsLength) {
        String formatValue = String.valueOf(value);
        BigDecimal bd = new BigDecimal(formatValue);
        bd = bd.setScale(decimalsLength, value > 0 ? BigDecimal.ROUND_UP : BigDecimal.ROUND_DOWN);
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
        nf.setGroupingUsed(false);
        nf.setMinimumFractionDigits(decimalsLength);
        nf.setMaximumFractionDigits(decimalsLength);
        return bd.floatValue();
    }
}
