package com.yikekong.util;

import java.math.BigDecimal;

public class Calculator{

    /**
     * 获取百分比
     * @param source 被除数
     * @param divisor 除数
     * @return
     */
    public static BigDecimal getRate(Long source, Long divisor){
        if(divisor <= 0) return new BigDecimal(0);
        BigDecimal sourceNumber = new BigDecimal(source);
        BigDecimal divideNumber = new BigDecimal(divisor);

        return sourceNumber.divide(divideNumber,4,BigDecimal.ROUND_HALF_UP);
    }
}
