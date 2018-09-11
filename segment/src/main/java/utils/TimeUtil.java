package utils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author 谭仕昌
 * @date 2017-10-30 21:11
 */
public class TimeUtil {

    public static final long SECOND_LEN = 1000;
    public static final long MINUTE_LEN = SECOND_LEN * 60;
    public static final long HOUR_LEN = MINUTE_LEN * 60;
    public static final long DAY_LEN = HOUR_LEN * 24;

    public static String normalizeTime(long period) {
        int day = 0, hour = 0, minute = 0, second = 0, milli = 0;
        day = (int) (period / DAY_LEN);
        hour = (int) ((period % DAY_LEN) / HOUR_LEN);
        minute = (int) ((period % HOUR_LEN) / MINUTE_LEN);
        second = (int) ((period % MINUTE_LEN) / SECOND_LEN);
        milli = (int) (period % SECOND_LEN);
        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(day).append("天");
        }
        if (hour > 0) {
            sb.append(hour).append("小时");
        }
        if (minute > 0) {
            sb.append(minute).append("分钟");
        }
        if (second > 0) {
            sb.append(second).append("秒");
        }
        if (milli > 0) {
            String milliStr = null;
            if (milli < 10) {
                milliStr = "00" + milli;
            } else if (milli < 100) {
                milliStr = "0" + milli;
            } else {
                milliStr = String.valueOf(milli);
            }
            sb.append(milliStr).append("毫秒");
        }
        return sb.toString();
    }

    private static final long hourMode = 1000 * 60 * 60;

    /**
     * 计算两个时间戳之间相差的小时数
     * <p>
     * 向下取整后再计算，
     * 假如<code>oldMillis</code>表示的是2018-01-01 01:33:10.123，那么会将其转为2018-01-01 01:00:00.000再进行计算
     *
     * @param oldMillis
     * @param newMillis
     * @return
     */
    public static long calculateFloorDiffHours(long oldMillis, long newMillis) {
        long t1 = oldMillis - (oldMillis % hourMode);
        return Duration.between(Instant.ofEpochMilli(t1), Instant.ofEpochMilli(newMillis)).toHours();
    }

    /**
     * 计算时间差
     *
     * @param oldMillis
     * @param newMillis
     * @param timeUnit  指定结果的单位，
     *                  支持 {@link TimeUnit#SECONDS} {@link TimeUnit#MINUTES} {@link TimeUnit#HOURS} {@link TimeUnit#DAYS}
     * @return
     */
    public static long calculateTimeDiff(long oldMillis, long newMillis, TimeUnit timeUnit) {
        Duration duration = Duration.between(Instant.ofEpochMilli(oldMillis), Instant.ofEpochMilli(newMillis));
        if (TimeUnit.SECONDS.equals(timeUnit)) return duration.getSeconds();
        if (TimeUnit.MINUTES.equals(timeUnit)) return duration.toMinutes();
        if (TimeUnit.HOURS.equals(timeUnit)) return duration.toHours();
        if (TimeUnit.DAYS.equals(timeUnit)) return duration.toDays();
        throw new UnsupportedOperationException(MessageFormat.format("不支持计算时间单位为 [{0}] 的时间差计算", timeUnit == null ? "null" : timeUnit.toString()));
    }

    public static long getTodayFirstMilli() {
        return getDayFirstMilli(System.currentTimeMillis());
    }

    public static long getDayFirstMilli(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getTodayLastMilli() {
        return getDayLastMilli(System.currentTimeMillis());
    }

    public static long getDayLastMilli(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    public static String getTimeDesc(long millis, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(millis));
    }

    public static String descNowFully() {
        return getTimeDesc(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS");
    }


}