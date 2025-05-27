package com.subway.util;

/**
 * 票价计算工具类，支持普通单程票、武汉通折扣和日票计算
 */
public class FareCalculator {

    /**
     * 计算普通单程票价（按里程分段计价）
     * @param distance 乘车距离（公里）
     * @return 票价（单位：元）
     */
    public static double calculateFare(double distance) {
        // 根据武汉地铁官方规则调整以下分段逻辑
        if (distance <= 4) {
            return 2.0;
        } else if (distance <= 8) {
            return 3.0;
        } else if (distance <= 12) {
            return 4.0;
        } else if (distance <= 18) {
            return 5.0;
        } else if (distance <= 24) {
            return 6.0;
        } else {
            return 7.0; // 超过24公里每增加8公里加1元，此处简化为固定值
        }
    }

    /**
     * 计算武汉通折扣票价（9折）
     * @param distance 乘车距离（公里）
     * @return 票价（单位：元）
     */
    public static double calculateWuhanTongFare(double distance) {
        return calculateFare(distance) * 0.9;
    }

    /**
     * 计算日票票价（日票有效期内不限次数，直接返回0元）
     * @param distance 乘车距离（公里）
     * @return 0元
     */
    public static double calculateDailyPassFare(double distance) {
        return 0.0;
    }
}
