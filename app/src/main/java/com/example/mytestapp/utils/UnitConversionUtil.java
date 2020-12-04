package com.example.mytestapp.utils;

import java.math.BigDecimal;

public class UnitConversionUtil {

    /**
     * Convert Kpa unit value to Mpa unit value.
     * 1MPa=1000kPa
     *
     * @param number
     * @return
     */
    public static float kPa2MPa(float number) {

        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(1000))
                .floatValue();
    }

    /**
     * Convert Mpa unit value to Kpa unit value.
     * 1MPa=1000kPa
     *
     * @param number
     * @return
     */
    public static float MPa2kPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(1000))
                .floatValue();
    }

    /**
     * Convert Mpa unit value to hPa unit value.
     * 1MPa=10000hPa
     *
     * @param number
     * @return
     */
    public static float MPa2hPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(Math.pow(10, 4)))
                .floatValue();
    }

    /**
     * Convert hpa unit value to MPa unit value.
     * 1MPa=10000hPa
     *
     * @param number
     * @return
     */
    public static float hPa2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(Math.pow(10, 4)))
                .floatValue();
    }

    /**
     * Convert MPa unit value to bar unit value.
     * 1MPa = 10bar
     *
     * @param number
     * @return
     */
    public static float MPa2bar(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(10))
                .floatValue();
    }

    /**
     * Convert bar unit value to MPa unit value.
     * 1bar = 0.1 MPa
     *
     * @param number
     * @return
     */
    public static float bar2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(10))
                .floatValue();
    }

    /**
     * Convert Mpa unit value to Kpa unit value.
     * 1MPa=1000kPa
     *
     * @param number
     * @return
     */
    public static float MPa2KPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(1000))
                .floatValue();
    }

    /**
     * Convert MPa unit value to mbar unit value.
     * 1MPa = 10000hPa = 10000mbar
     *
     * @param number
     * @return
     */
    public static float MPa2mbar(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(Math.pow(10, 4)))
                .floatValue();
    }

    /**
     * Convert mbar unit value to MPa unit value.
     * 1MPa = 10000hPa = 10000mbar
     *
     * @param number
     * @return
     */
    public static float mbar2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(Math.pow(10, 4)))
                .floatValue();
    }

    /**
     * Convert MPa unit value to Pa unit value.
     * 1MPa = 10000hPa = 1000000Pa
     *
     * @param number
     * @return
     */
    public static float MPa2Pa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(Math.pow(10, 6)))
                .floatValue();
    }

    /**
     * Convert Pa unit value to MPa unit value.
     * 1MPa = 1000000 Pa
     *
     * @param number
     * @return
     */
    public static float Pa2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(Math.pow(10, 6)))
                .floatValue();
    }

    /**
     * Convert MPa unit value to GPa unit value.
     * 1 GPa = 1000 MPa
     *
     * @param number
     * @return
     */
    public static float MPa2GPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(1000))
                .floatValue();
    }

    /**
     * Convert GPa unit value to MPa unit value.
     * 1 GPa = 1000 MPa
     *
     * @param number
     * @return
     */
    public static long GPa2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(1000))
                .longValue();
    }

    /**
     * Convert MPa unit value to psi unit value.
     * 1MPa ≈ 145.03775 psi;
     *
     * @param number
     * @return
     */
    public static float MPa2psi(float number) {;
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(6.894757 * Math.pow(10, -3)), 5, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert psi unit value to MPa unit value.
     * 1MPa ≈ 145.03775 psi;
     *
     * @param number
     * @return
     */
    public static float psi2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(6.894757 * Math.pow(10, -3)))
                .floatValue();
    }

    /**
     * Convert MPa unit value to inHg unit value.
     * 1MPa ≈ 295.2988‬ inHg
     * return (float) (number / (3.386389 * Math.pow(10, -3)));
     * @param number
     * @return
     */
    public static float MPa2inHg(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(3.386389 * Math.pow(10, -3)), 5, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert inHg unit value to MPa unit value.
     * 1MPa ≈ 295.2988‬ inHg
     * return (float) (number * (3.386389 * Math.pow(10, -3)));
     * @param number
     * @return
     */
    public static float inHg2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(3.386389 * Math.pow(10, -3)))
                .floatValue();
    }

    /**
     * Convert MPa unit value to mmHg unit value.
     * 1MPa ≈ 7,500.615 mmHg
     * return (float) (number / (1.333224 * Math.pow(10, -4)));
     * @param number
     * @return
     */
    public static float MPa2mmHg(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(1.333224 * Math.pow(10, -4)), 5, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert mmHg unit value to MPa unit value.
     * 1MPa ≈ 7,500.615 mmHg
     *  return  (float) (number * (1.333224 * Math.pow(10, -4)));
     * @param number
     * @return
     */
    public static float mmHg2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(1.333224 * Math.pow(10, -4)))
                .floatValue();
    }

    /**
     * Convert MPa unit value to mmH2O unit value.
     * 1MPa ≈ 101971.625‬ mmH2O
     *  return (float) (number / (9.806650 * Math.pow(10, -6)));
     * @param number
     * @return
     */
    public static float MPa2mmH2O(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(9.806650 * Math.pow(10, -6)), 5, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert mmH2O unit value to MPa unit value.
     * 1MPa ≈ 101971.625 mmH2O
     * return  (float) (number * (9.806650 * Math.pow(10, -6)));
     * @param number
     * @return
     */
    public static float mmH2O2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(9.806650 * Math.pow(10, -6)))
                .floatValue();
    }

    /**
     * Convert MPa unit value to mmH2O(68 ℉) unit value.
     * 1MPa ≈ 102155.195 mmH2O(68 ℉)
     * return  (float) (number / (9.789027 * Math.pow(10, -6)));
     * @param number
     * @return
     */
    public static float MPa2mmH2O68F(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(9.789027 * Math.pow(10, -6)), 5, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert mmH2O(68 ℉) unit value to MPa unit value.
     * 1MPa ≈ 102155.195 mmH2O(68 ℉)
     *  return  (float) (number * (9.789027 * Math.pow(10, -6)));
     * @param number
     * @return
     */
    public static float mmH2O68F2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(9.789027 * Math.pow(10, -6)))
                .floatValue();
    }

    /**
     * Convert MPa unit value to mmAq unit value.
     * 1MPa ≈ 101971.625 mmAq
     * return  (float) (number / (9.806650 * Math.pow(10, -6)));
     * @param number
     * @return
     */
    public static float MPa2mmAq(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(9.806650 * Math.pow(10, -6)), 5, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert mmAq unit value to MPa unit value.
     * 1MPa ≈ 101971.625 mmAq
     *  return  (float) (number * (9.806650 * Math.pow(10, -6)));
     * @param number
     * @return
     */
    public static float mmAq2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(9.806650 * Math.pow(10, -6)))
                .floatValue();
    }

    /**
     * Convert MPa unit value to mmWG unit value.
     * 1MPa ≈ 101971.625 mmWG
     * return  (float) (number / (9.806650 * Math.pow(10, -6)));
     * @param number
     * @return
     */
    public static float MPa2mmWG(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(9.806650 * Math.pow(10, -6)), 5, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert mmWG unit value to MPa unit value.
     * 1MPa ≈ 101971.625 mmWG
     * return  (float) (number * (9.806650 * Math.pow(10, -6)));
     * @param number
     * @return
     */
    public static float mmWG2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(9.806650 * Math.pow(10, -6)))
                .floatValue();
    }

    /**
     * Convert MPa unit value to gf/cm² unit value.
     * 1MPa ≈ 10197.162129 gf/cm²
     *  return  (float) (number / (9.806650 * Math.pow(10, -5)));
     * @param number
     * @return
     */
    public static float MPa2gfcm(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(9.806650 * Math.pow(10, -5)), 5, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert gf/cm² unit value to MPa unit value.
     * 1MPa ≈ 10197.162129 gf/cm²
     * return  (float) (number * (9.806650 * Math.pow(10, -5)));
     * @param number
     * @return
     */
    public static float gfcm2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(9.806650 * Math.pow(10, -5)))
                .floatValue();
    }

    /**
     * Convert MPa unit value to kgf/cm² unit value.
     * 1MPa ≈ 10.197162 kgf/cm²
     * return  (float) (number / (9.806650 * Math.pow(10, -2)));
     * @param number
     * @return
     */
    public static float MPa2kgfcm(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(9.806650 * Math.pow(10, -2)), 5, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert kgf/cm² unit value to MPa unit value.
     * 1MPa ≈ 10.19716212977928 kgf/cm²
     * return  (float) (number * (9.806650 * Math.pow(10, -2)));
     * @param number
     * @return
     */
    public static float kgfcm2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(9.806650 * Math.pow(10, -2)))
                .floatValue();
    }

    /**
     * Convert MPa unit value to inH2O unit value.
     * 1MPa ≈ 4,014.6309‬ inH2O
     * return  (float) (number / (2.490889 * Math.pow(10, -4)));
     * @param number
     * @return
     */
    public static float MPa2inH2O(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(2.490889 * Math.pow(10, -4)), 5, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert inH2O unit value to MPa unit value.
     * 1MPa ≈ 4,014.6309‬ inH2O
     * return  (float) (number * (2.490889 * Math.pow(10, -4)));
     * @param number
     * @return
     */
    public static float inH2O2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(2.490889 * Math.pow(10, -4)))
                .floatValue();
    }

    /**
     * Convert MPa unit value to inH2O(68℉) unit value.
     * 1MPa ≈ 4021.858 inH2O(68℉)
     *  return  (float) (number / (2.486413 * Math.pow(10, -4)));
     * @param number
     * @return
     */
    public static float MPa2inH2O68F(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(2.486413 * Math.pow(10, -4)), 5, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert inH2O(68℉) unit value to MPa unit value.
     * 1MPa ≈ 4021.858 inH2O(68℉)
     *  return  (float) (number * (2.486413 * Math.pow(10, -4)));
     * @param number
     * @return
     */
    public static float inH2O68F2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(2.486413 * Math.pow(10, -4)))
                .floatValue();
    }

    /**
     * Convert MPa unit value to ftH2O unit value.
     * 1MPa ≈ 334.55255 ftH2O
     * return  (float) (number / (2.989067 * Math.pow(10, -3)));
     * @param number
     * @return
     */
    public static float MPa2ftH2O(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(2.989067 * Math.pow(10, -3)), 5, BigDecimal.ROUND_DOWN)
                .floatValue();

    }

    /**
     * Convert ftH2O unit value to MPa unit value.
     * 1MPa ≈ 334.55255 ftH2O
     * return  (float) (number * (2.989067 * Math.pow(10, -3)));
     * @param number
     * @return
     */
    public static float ftH2O2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(2.989067 * Math.pow(10, -3)))
                .floatValue();
    }

    /**
     * Convert MPa unit value to ftH2O(68℉) unit value.
     * 1MPa ≈ 335.1548 ftH2O(68℉)
     * return  (float) (number / (2.983696 * Math.pow(10, -3)));
     * @param number
     * @return
     */
    public static float MPa2ftH2O68F(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(2.983696 * Math.pow(10, -3)), 5, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert ftH2O(68℉) unit value to MPa unit value.
     * 1MPa ≈ 335.1548 ftH2O(68℉)
     * return  (float) (number * (2.983696 * Math.pow(10, -3)));
     * @param number
     * @return
     */
    public static float ftH2O68F2MPa(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(2.983696 * Math.pow(10, -3)))
                .floatValue();
    }

    /**
     * Convert °F unit value to ℃ unit value.
     * return  (number - 32) / 1.8f;
     * @param number
     * @return
     */
    public static float degF2degC(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .subtract(BigDecimalUtil.getBigDecimal(32))
                .divide(BigDecimalUtil.getBigDecimal( 1.8f), 5, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert ℃ unit value to °F unit value.
     * return number * 1.8f + 32;
     * @param number
     * @return
     */
    public static float degC2degF(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal( 1.8f))
                .add(BigDecimalUtil.getBigDecimal(32))
                .floatValue();
    }

    /**
     * Convert °K unit value to ℃ unit value.
     *   return number - 273.15f;
     * @param number
     * @return
     */
    public static float degK2degC(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .subtract(BigDecimalUtil.getBigDecimal(273.15f))
                .floatValue();
    }

    /**
     * Convert ℃ unit value to °K unit value.
     *
     * @param number
     * @return
     */
    public static float degC2degK(float number) {
        return BigDecimalUtil.getBigDecimal(number)
                .add(BigDecimalUtil.getBigDecimal(273.15f))
                .floatValue();
    }

    /**
     * Convert °R unit value to ℃ unit value.
     *
     * @param number
     * @return
     */
    public static float degR2degC(float number) {
//        return number * 5 / 9 - 273.15f;
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(5))
                .divide(BigDecimalUtil.getBigDecimal( 9), 5, BigDecimal.ROUND_DOWN)
                .subtract(BigDecimalUtil.getBigDecimal(273.15f))
                .floatValue();
    }

    /**
     * Convert ℃ unit value to °R unit value.
     *
     * @param number
     * @return
     */
    public static float degC2degR(float number) {
//        return (number + 273.15f) * 9 / 5;
        return BigDecimalUtil.getBigDecimal(number)
                .add(BigDecimalUtil.getBigDecimal(273.15f))
                .multiply(BigDecimalUtil.getBigDecimal(9))
                .divide(BigDecimalUtil.getBigDecimal( 5), 5, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert V unit value to KV unit value.
     *
     * @param number
     * @return
     */
    public static float degV2degkV(float number) {
//        return number / 1000;
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(1000))
                .floatValue();
    }

    /**
     * Convert KV unit value to V unit value.
     *
     * @param number
     * @return
     */
    public static float degkV2degV(float number) {
//        return number * 1000;
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(1000))
                .floatValue();
    }

    /**
     * Convert V unit value to MV unit value.
     *
     * @param number
     * @return
     */
    public static float degV2degMV(float number) {
//        String value = String.valueOf(number / 1000000);
//        BigDecimal decimal = new BigDecimal(value);
//        return decimal.setScale(6, RoundingMode.HALF_UP).floatValue();
        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(1000000), 6, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert MV unit value to V unit value.
     *
     * @param number
     * @return
     */
    public static float degMV2degV(float number) {
//        String value = String.valueOf(number * 1000000);
//        BigDecimal decimal = new BigDecimal(value);
//        return decimal.setScale(6, RoundingMode.HALF_UP).floatValue();
        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(1000000))
                .floatValue();
    }

    /**
     * Convert V unit value to mV unit value.
     *
     * @param number
     * @return
     */
    public static float degV2degmV(float number) {
//        return number * 1000;

        return BigDecimalUtil.getBigDecimal(number)
                .multiply(BigDecimalUtil.getBigDecimal(1000))
                .floatValue();
    }

    /**
     * Convert mV unit value to V unit value.
     *
     * @param number
     * @return
     */
    public static float degmV2degV(float number) {
//        return number / 1000;

        return BigDecimalUtil.getBigDecimal(number)
                .divide(BigDecimalUtil.getBigDecimal(1000))
                .floatValue();
    }

    /**
     * Convert mm/s unit value to inch/s unit value.
     *
     * @param value
     * @return
     */
    public static float velMM2velInch(float value) {
//        return (float) (value * 0.039370);

        return BigDecimalUtil.getBigDecimal(value)
                .multiply(BigDecimalUtil.getBigDecimal(0.039370))
                .floatValue();
    }

    /**
     * Convert inch/s unit value to mm/s unit value.
     *
     * @param value
     * @return
     */
    public static float velInch2velMM(float value) {
//        return (float) (value / 0.039370);

        return BigDecimalUtil.getBigDecimal(value)
                .divide(BigDecimalUtil.getBigDecimal(0.039370), 5, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert m/s2 unit value to g unit value.
     *
     * @param value
     * @return
     */
    public static float accM2accG(float value) {
//        return (float) (value / 9.81);

        return BigDecimalUtil.getBigDecimal(value)
                .divide(BigDecimalUtil.getBigDecimal(9.81), 5, BigDecimal.ROUND_DOWN)
                .floatValue();
    }

    /**
     * Convert g unit value to m/s2 unit value.
     *
     * @param value
     * @return
     */
    public static float accG2accM(float value) {
//        return (float) (value * 9.81);

        return BigDecimalUtil.getBigDecimal(value)
                .multiply(BigDecimalUtil.getBigDecimal(9.81))
                .floatValue();
    }

}

