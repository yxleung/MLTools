package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.NumberFormat;

/**
 * @author tanshichang
 * @create 2018-08-13 18:12
 */
public class TextFileUtils {

    public static int countLineNum(String path) throws IOException {
        File file = new File(path);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int lineNum = 0;
            while (reader.ready()) {
                reader.readLine();
                lineNum++;
            }
            return lineNum;
        }
    }

    public static int countLineNumWithProgress(String path) throws IOException {
        File file = new File(path);
        long totalSize = file.length();
        String totalSizeStr = MemorySizeUtil.transformByteSize(totalSize, false, false);
        long currentSize = 0;
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        long lastTime = System.currentTimeMillis();
        long t1 = System.currentTimeMillis();
        long lastSize = 0;
        long gap = 500;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int lineNum = 0;
            while (reader.ready()) {
                long size = reader.readLine().getBytes().length+1;
                currentSize += size;
                lastSize += size;
                lineNum++;
                long now = System.currentTimeMillis();
                if (now - lastTime > gap) {
                    lastTime = now;
                    System.out.print('\r');
                    System.out.print(MessageFormat.format("Counting Lines: {5} \t{0} \t {1} / {2} \t {3} \t {4}",
                            TimeUtil.normalizeTime(System.currentTimeMillis() - t1)
                            , MemorySizeUtil.transformByteSize(currentSize, false, false)
                            , totalSizeStr
                            , nf.format(((double) currentSize) / totalSize)
                            , calSpeed(lastSize, gap)
                            , lineNum));
                    lastSize = 0;
                }
            }
            System.out.print('\r');
            System.out.print(MessageFormat.format("Counting Lines: {5} \t{0} \t {1} / {2} \t {3} \t {4}",
                    TimeUtil.normalizeTime(System.currentTimeMillis() - t1)
                    , MemorySizeUtil.transformByteSize(currentSize, false, false)
                    , totalSizeStr
                    , nf.format(((double) currentSize) / totalSize)
                    , calSpeed(lastSize, gap)
                    , lineNum));
            System.out.println();
            return lineNum;
        }
    }

    private static String calSpeed(long size, long gap) {
        double timeRate = ((double) 1000) / gap;
        long size2 = (long) (size * timeRate);
        String s = MessageFormat.format("{0}/s", MemorySizeUtil.transformByteSize(size2, false, false));
        return s;
    }
}
