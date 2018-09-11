package utils;

import com.google.common.base.Joiner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Stopwords {
    private Set<String> stopwords = new HashSet<>();

    public Stopwords() {
        List<String> files = new ArrayList<>();
        files.add("中文停用词-简体.txt");
        files.add("业务停用词.txt");
        files.add("中文停用词-繁体.txt");
        init(files);
    }

    public Stopwords(List<String> fileNames) {
        init(fileNames);
    }


    private void init(List<String> fileNames) {
        try {
            for (String str : fileNames) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/stopwords/" + str), "UTF-8"))) {
                    while (reader.ready()) {
                        String line = reader.readLine();
                        line = line.trim();
                        if (!line.equals("")) {
                            stopwords.add(line);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String filter(String words, String separator) {
        String[] strs = words.split(separator);
        List<String> terms = new ArrayList<>();
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i];
            if (!this.stopwords.contains(str)) {
                terms.add(str);
            }
        }
        String result = Joiner.on(separator).join(terms.stream().collect(Collectors.toList()));
        return result;
    }

    public String filter(String words) {
        return filter(words, " ");
    }
}
