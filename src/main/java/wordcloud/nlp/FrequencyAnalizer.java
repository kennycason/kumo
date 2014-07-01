package wordcloud.nlp;

import ch.lambdaj.Lambda;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import wordcloud.WordFrequency;
import wordcloud.nlp.filter.StopWordFilter;
import wordcloud.nlp.sanitize.BasicTextSanitizer;
import wordcloud.nlp.sanitize.Sanitizer;
import wordcloud.nlp.tokenizer.WhiteSpaceWordTokenizer;
import wordcloud.nlp.tokenizer.WordTokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

/**
 * Created by kenny on 7/1/14.
 */
public class FrequencyAnalizer {

    private static final int MAX_LENGTH = 32;

    private StopWordFilter stopWordFilter = new StopWordFilter(Collections.EMPTY_SET);

    private Sanitizer sanitizer = new BasicTextSanitizer();

    private static final int DEFAULT_WORD_FREQUENCIES_TO_RETURN = 50;

    private int wordFrequencesToReturn = DEFAULT_WORD_FREQUENCIES_TO_RETURN;

    private int minWordLength = 3;

    public List<WordFrequency> load(InputStream fileInputStream) throws IOException {
        final WordTokenizer wordTokenizer = new WhiteSpaceWordTokenizer();
        final List<WordFrequency> wordFrequencies = new ArrayList<>();
        final List<String> texts = IOUtils.readLines(fileInputStream);

        // generate all word counts
        final Map<String, Integer> cloud = calculateCloud(texts, wordTokenizer);
        for(String key : cloud.keySet()) {
            if(key.length() > minWordLength
                    && key.length() < MAX_LENGTH) {

                wordFrequencies.add(new WordFrequency(key, cloud.get(key)));
            }
        }
        return takeTopFrequencies(wordFrequencies);
    }

    private Map<String, Integer> calculateCloud(List<String> texts, WordTokenizer tokenizer) {
        final Map<String, Integer> cloud = new HashMap<>();
        for(String text : texts) {
            final List<String> words = Lambda.filter(stopWordFilter, tokenizer.tokenize(sanitizer.sanitize(text)));
            for(String word : words) {
                word = word.toLowerCase();
                if(StringUtils.isNotBlank(word)) {
                    if(cloud.containsKey(word)) {
                        cloud.put(word, cloud.get(word) + 1);
                    } else {
                        cloud.put(word, 1);
                    }
                }
            }
        }
        return cloud;
    }

    private List<WordFrequency> takeTopFrequencies(Collection<WordFrequency> wordCloudEntities) {
        final List<WordFrequency> sorted = sort(wordCloudEntities, on(WordFrequency.class).getFrequency());
        Collections.reverse(sorted);
        if(sorted.isEmpty()) {
            return sorted;
        }
        return sorted.subList(0, Math.min(sorted.size(), wordFrequencesToReturn));
    }

    public void setStopWords(Collection<String> stopWords) {
        this.stopWordFilter = new StopWordFilter(stopWords);
    }

    public void setWordFrequencesToReturn(int wordFrequencesToReturn) {
        this.wordFrequencesToReturn = wordFrequencesToReturn;
    }

    public void setMinWordLength(int minWordLength) {
        this.minWordLength = minWordLength;
    }

    public void setSanitizer(Sanitizer sanitizer) {
        this.sanitizer = sanitizer;
    }
}
