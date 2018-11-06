package com.neuedu.utils;

import java.math.BigDecimal;

/**
 * 价格运算工具类
 * */
public class BigDecimalUtil {

    /**
     * 加法
     * */
    public static BigDecimal add(Double d1,Double d2){

        BigDecimal bigDecimal1 = new BigDecimal(d1.toString());
        BigDecimal bigDecimal2 = new BigDecimal(d2.toString());
        return bigDecimal1.add(bigDecimal2);

    }

    /**
     * 减法
     * */
    public static BigDecimal sub(Double d1,Double d2){

        BigDecimal bigDecimal1 = new BigDecimal(d1.toString());
        BigDecimal bigDecimal2 = new BigDecimal(d2.toString());
        return bigDecimal1.subtract(bigDecimal2);

    }

    /**
     * 乘法
     * */
    public static BigDecimal mul(Double d1,Double d2){

        BigDecimal bigDecimal1 = new BigDecimal(d1.toString());
        BigDecimal bigDecimal2 = new BigDecimal(d2.toString());
        return bigDecimal1.multiply(bigDecimal2);

    }

    /**
     * 除法
     * */
    public static BigDecimal divi(Double d1,Double d2){

        BigDecimal bigDecimal1 = new BigDecimal(d1.toString());
        BigDecimal bigDecimal2 = new BigDecimal(d2.toString());
        return bigDecimal1.divide(bigDecimal2,2,BigDecimal.ROUND_HALF_UP);

    }






}
