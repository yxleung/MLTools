package segment;

import com.google.common.base.Joiner;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.commons.lang3.StringUtils;
import utils.Progress;
import utils.Stopwords;

import java.io.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HanlpProcess {

    private String inputPath = "";
    private String label = "";
    private Map<String, Long> typeMap = new HashMap<String, Long>();
    private BufferedWriter writer;
    private boolean isStopwords;
    private Stopwords stopwords = null;

    public HanlpProcess(String inputPath, String label, boolean isStopwords) {
        this.inputPath = inputPath;
        this.label = label;
        this.isStopwords = isStopwords;
        try {
            this.writer = new BufferedWriter(new FileWriter(inputPath + "/" + "segment"));
            if (isStopwords) {
                this.stopwords = new Stopwords();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Long getCounter(String type) {
        Long c = typeMap.get(type);
        if (c == null) {
            typeMap.put(type, new Long(1));
        }
        return typeMap.get(type);
    }

    private void accumulate(String type) {
        typeMap.put(type, getCounter(type).longValue() + 1);
    }

    private void closeWriter() {
        try {
            this.writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void process(File inputFile) {
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"))) {
                Progress progress = new Progress()
                        .setStartTime(System.currentTimeMillis())
                        .setTotalSize(inputFile.length());

                while (reader.ready()) {
                    String line = reader.readLine();

                    progress.addCurrentSize(line.getBytes("UTF-8").length + 1);
                    progress.setNow(System.currentTimeMillis());
                    progress.echo();

                    String[] attrs = line.split("\u0001");
                    String content = attrs[0];
                    String type = attrs[1];

                    if (StringUtils.isBlank(content))
                        continue;
                    // 过滤虎牙表情
                    String content_clean = content.replaceAll("\\/\\{[a-zA-Z]{2}", "");
                    // 过滤emoji
//                        content_clean = content_clean.replaceAll("[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]|[\\ud83e\\udd00-\\ud83e\\uddff]|[\\u2300-\\u23ff]|[\\u2500-\\u25ff]|[\\u2100-\\u21ff]|[\\u0000-\\u00ff]|[\\u2b00-\\u2bff]|[\\u2d06]|[\\u3030]", "");
                    String segment = segment(content_clean);
                    String result = MessageFormat.format("{0}\u0001{1}{2}{3}{4}\u0001{5}\u0001{6}\n", content, segment, " ", "__label__", label, type, getCounter(type).toString());

                    // 停用词过滤
                    if (isStopwords) {
                        result = this.stopwords.filter(result);
                    }
                    writer.write(result);
                    accumulate(type);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String segment(String text) {
        return segment(text, " ");
    }

    private String segment(String text, String separator) {
        List<Term> terms = HanLP.segment(text);
//        List<Term> terms = NLPTokenizer.segment(text);
        String result = Joiner.on(separator).join(terms.stream().map(t -> t.word).collect(Collectors.toList()));
        return result;
    }


    public void conversion() {
        File file = new File(this.inputPath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isFile() && !f.getName().equals("segment")) {
                    System.out.println("Conversion: " + f.getName());
                    process(f);
                    System.out.println(" ");
                }
            }
            closeWriter();
        }
    }


    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Input parameter error, the number of parameters is 3.");
            System.exit(0);
        }
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
        String isStopwordsStr = args[2];
        boolean isStopwords = false;
        if (isStopwordsStr.trim().toLowerCase().equals("true")) {
            isStopwords = true;
        }
        new HanlpProcess(args[0], args[1], isStopwords).conversion();

//        new HanlpProcess("D:\\NLP\\danmu\\test.txt", "D:\\NLP\\danmu\\output.txt", "legal").process();
    }
}
