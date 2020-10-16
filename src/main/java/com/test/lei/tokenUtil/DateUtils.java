package com.test.lei.tokenUtil;

import java.util.Date;

/**
 * @description: DateUtils
 * @author: leiming5
 * @date: 2020-10-09 20:54
 */
@Deprecated
public class DateUtils {
    public static long toSecondsSinceEpoch(Date date) {
        return date.getTime() / 1000L;
    }

    public static Date fromSecondsSinceEpoch(long time) {
        return new Date(time * 1000L);
    }

    public static boolean isAfter(Date date, Date reference, long maxClockSkewSeconds) {
        return (new Date(date.getTime() + maxClockSkewSeconds * 1000L)).after(reference);
    }

    public static boolean isBefore(Date date, Date reference, long maxClockSkewSeconds) {
        return (new Date(date.getTime() - maxClockSkewSeconds * 1000L)).before(reference);
    }

    private DateUtils() {
    }
}
