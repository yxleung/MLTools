package utils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author tanshichang
 * @create 2018-08-15 18:26
 */
@Accessors(chain = true)
@Data
public class Progress {
    private long totalSize;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private LongAdder currentSize = new LongAdder();
    private LongAdder currentLineNum = new LongAdder();
    private long totalLineNum;
    private long startTime;
    private volatile long now;
    private long refreshRate = 500;
    private String prompt = "";

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private long lastEchoTime = System.currentTimeMillis();

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private NumberFormat nf = NumberFormat.getPercentInstance();

    {
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
    }

    public void addCurrentSize(long size) {
        currentSize.add(size);
    }

    public void addCurrentLineNum(long size) {
        currentLineNum.add(size);
    }

    public void echo() {
        if (isFinished() || now - lastEchoTime > refreshRate) {
            long diff = now - startTime;
            System.out.print(MessageFormat.format("\r{5}{0} \t{1} \t{2} / {3} \tavg speed: {4}"
                    , genPercentage()
                    , TimeUtil.normalizeTime(diff)
                    , MemorySizeUtil.transformByteSize(currentSize.longValue(), false, false)
                    , MemorySizeUtil.transformByteSize(totalSize, false, false)
                    , calSpeed(currentSize.longValue(), diff)
                    , prompt));
            lastEchoTime = now;
        }
    }

    private boolean isFinished() {
        if (totalSize > 0) {
            return currentSize.longValue() >= totalSize;
        }
        return currentLineNum.longValue() >= totalLineNum;
    }

    private String genPercentage() {
        if (totalSize == 0) {
            return nf.format(currentLineNum.doubleValue() / totalLineNum);
        } else {
            return nf.format(currentSize.doubleValue() / totalSize);
        }
    }

    private static String calSpeed(long size, long gap) {
        double timeRate = ((double) 1000) / gap;
        long size2 = (long) (size * timeRate);
        String s = MessageFormat.format("{0}/s", MemorySizeUtil.transformByteSize(size2, false, false));
        return s;
    }
}
