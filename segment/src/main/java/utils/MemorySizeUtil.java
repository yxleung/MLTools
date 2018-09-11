package utils;

import com.google.common.base.Joiner;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 谭仕昌
 * @date 2017-10-11 09:36
 */
public class MemorySizeUtil {

    private static final int UNIT_INDEX_GB = 0;
    private static final int UNIT_INDEX_MB = 1;
    private static final int UNIT_INDEX_KB = 2;
    private static final int UNIT_INDEX_B = 3;

    private static final String[] unitLong = new String[]{
            "GB"
            , "MB"
            , "KB"
            , "Byte"
    };
    private static final String[] unitShort = new String[]{
            "G"
            , "M"
            , "K"
            , "B"
    };


    public static String transformByteSize(long size, boolean usingFullPath, boolean usingLongUnit) {
        long mb = 0, kb = 0, b = 0, gb = 0;
        kb = size / 1024;
        mb = kb / 1024;
        gb = mb / 1024;
        kb = kb % 1024;
        b = size % 1024;
        String[] units = unitShort;
        if (usingLongUnit) {
            units = unitLong;
        }
        List<String> unitBuffer = new LinkedList<>();
        String result = "";
        if (gb > 0) {
            unitBuffer.add(gb + units[UNIT_INDEX_GB]);
        }
        if (mb > 0) {
            unitBuffer.add(mb + units[UNIT_INDEX_MB]);
        }
        if (kb > 0) {
            unitBuffer.add(kb + units[UNIT_INDEX_KB]);
        }
        if (b > 0) {
            unitBuffer.add(b + units[UNIT_INDEX_B]);
        }
        int len = 2;
        if (usingFullPath) {
            len = 100;
        }
        result = Joiner.on(" ").join(unitBuffer.subList(0, len > unitBuffer.size() ? unitBuffer.size() : len));
        return result;
    }

}