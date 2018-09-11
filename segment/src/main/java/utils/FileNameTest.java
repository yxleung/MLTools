package utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tanshichang
 * @create 2018-08-14 14:32
 */
public class FileNameTest {

    public static void main(String[] args) {
        File file = new File("E:\\BaiduNetdiskDownload\\汉语词库\\data");
        List<File> files = Arrays.stream(file.listFiles()).filter(f -> f.getName().endsWith("txt")).collect(Collectors.toList());
        files.forEach(f -> {
            if (f.getName().contains("-")) {
                String p = f.getParent() + File.separator + f.getName().replaceAll("-", "_");
                f.renameTo(new File(p));
            }
        });

    }
}
