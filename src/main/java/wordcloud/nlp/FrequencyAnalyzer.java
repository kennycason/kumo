package wordcloud.nlp;

import ch.lambdaj.Lambda;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import wordcloud.WordFrequency;
import wordcloud.nlp.filter.CompositeFilter;
import wordcloud.nlp.filter.Filter;
import wordcloud.nlp.filter.StopWordFilter;
import wordcloud.nlp.filter.WordSizeFilter;
import wordcloud.nlp.normalize.CharacterStrippingNormalizer;
import wordcloud.nlp.normalize.LowerCaseNormalizer;
import wordcloud.nlp.normalize.Normalizer;
import wordcloud.nlp.normalize.TrimToEmptyNormalizer;
import wordcloud.nlp.tokenizer.ColonTokenizer;
import wordcloud.nlp.tokenizer.WhiteSpaceWordTokenizer;
import wordcloud.nlp.tokenizer.WordTokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

/**
 * Created by kenny on 7/1/14.
 */
public class FrequencyAnalyzer {

    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final int DEFAULT_WORD_MAX_LENGTH = 32;

    public static final int DEFAULT_WORD_MIN_LENGTH = 3;

    public static final int DEFAULT_WORD_FREQUENCIES_TO_RETURN = 50;

    public static final long DEFAULT_URL_LOAD_TIMEOUT = 3000; // 3 sec

    private final Set<String> stopWords = new HashSet<>();

    private WordTokenizer wordTokenizer = new WhiteSpaceWordTokenizer();

    private final List<Filter> filters = new ArrayList<>();

    private final List<Normalizer> normalizers = new ArrayList<>();

    private int wordFrequencesToReturn = DEFAULT_WORD_FREQUENCIES_TO_RETURN;

    private int maxWordLength = DEFAULT_WORD_MAX_LENGTH;

    private int minWordLength = DEFAULT_WORD_MIN_LENGTH;

    private String characterEncoding = DEFAULT_ENCODING;

    private long urlLoadTimeout = DEFAULT_URL_LOAD_TIMEOUT;

    public FrequencyAnalyzer() {
        this.normalizers.add(new TrimToEmptyNormalizer());
        this.normalizers.add(new CharacterStrippingNormalizer());
        this.normalizers.add(new LowerCaseNormalizer());
    }

    public List<WordFrequency> load(InputStream fileInputStream) throws IOException {
        return load(IOUtils.readLines(fileInputStream, characterEncoding));
    }

    public List<WordFrequency> load(InputStream fileInputStream, FileType fileType) throws IOException {
        return load(IOUtils.readLines(fileInputStream, characterEncoding), fileType);
    }

    public List<WordFrequency> load(URL url) throws IOException {
        final Document doc = Jsoup.parse(url, (int) urlLoadTimeout);
        return load(Collections.singletonList(doc.body().text()));
    }

    public List<WordFrequency> load(final List<String> texts) {
        final List<WordFrequency> wordFrequencies = new ArrayList<>();

        final Map<String, Integer> cloud = buildWordFrequencies(texts, wordTokenizer);
        for (Map.Entry<String, Integer> wordCount : cloud.entrySet()) {
            wordFrequencies.add(new WordFrequency(wordCount.getKey(), wordCount.getValue()));
        }
        return takeTopFrequencies(wordFrequencies);
    }

    public List<WordFrequency> load(final List<String> texts, FileType fileType) {
        if (fileType == FileType.REGULAR) {
            return load(texts);
        }

        final List<WordFrequency> wordFrequencies = new ArrayList<>();

        final Map<String, Integer> cloud = buildKeyValueWordFrequencies(texts, new ColonTokenizer());
        for (Map.Entry<String, Integer> wordCount : cloud.entrySet()) {
            wordFrequencies.add(new WordFrequency(wordCount.getKey(), wordCount.getValue()));
        }
        return takeTopFrequencies(wordFrequencies);
    }

    private Map<String, Integer> buildKeyValueWordFrequencies(List<String> texts, WordTokenizer tokenizer) {
        final Map<String, Integer> wordFrequencies = new HashMap<>();
        for (final String text : texts) {
            final List<String> words = filter(tokenizer.tokenize(text));
            int size = words.size();
            if (size > 2) {
                continue;
            }
            for (int i = 0; i < size; i++) {
                int frequency = Integer.parseInt(words.get(1).trim());
                final String normalized = normalize(words.get(0));
                if (!wordFrequencies.containsKey(normalized)) {
                    wordFrequencies.put(normalized, frequency);
                } else {
                    wordFrequencies.put(normalized, wordFrequencies.get(normalized) + frequency);
                }
            }
        }
        return wordFrequencies;
    }

    private Map<String, Integer> buildWordFrequencies(List<String> texts, WordTokenizer tokenizer) {
        final Map<String, Integer> wordFrequencies = new HashMap<>();
        for (final String text : texts) {
            final List<String> words = filter(tokenizer.tokenize(text));

            for (final String word : words) {
                final String normalized = normalize(word);
                if (!wordFrequencies.containsKey(normalized)) {
                    wordFrequencies.put(normalized, 1);
                }
                wordFrequencies.put(normalized, wordFrequencies.get(normalized) + 1);
            }
        }
        return wordFrequencies;
    }

    private List<String> filter(final List<String> words) {
        final List<Filter> allFilters = new ArrayList<>();
        allFilters.add(new StopWordFilter(stopWords));
        allFilters.add(new WordSizeFilter(minWordLength, maxWordLength));
        allFilters.addAll(filters);
        final CompositeFilter compositeFilter = new CompositeFilter(allFilters);
        return Lambda.filter(compositeFilter, words);
    }

    private String normalize(final String word) {
        String normalized = word;
        for (Normalizer normalizer : normalizers) {
            normalized = normalizer.normalize(normalized);
        }
        return normalized;
    }

    private List<WordFrequency> takeTopFrequencies(Collection<WordFrequency> wordCloudEntities) {
        if (wordCloudEntities.isEmpty()) {
            return Collections.emptyList();
        }
        final List<WordFrequency> sorted = sort(wordCloudEntities, on(WordFrequency.class).getFrequency());
        Collections.reverse(sorted);
        return sorted.subList(0, Math.min(sorted.size(), wordFrequencesToReturn));
    }

    public void setStopWords(Collection<String> stopWords) {
        this.stopWords.clear();
        this.stopWords.addAll(stopWords);
    }

    public void setWordFrequencesToReturn(int wordFrequencesToReturn) {
        this.wordFrequencesToReturn = wordFrequencesToReturn;
    }

    public void setMinWordLength(int minWordLength) {
        this.minWordLength = minWordLength;
    }

    public void setMaxWordLength(final int maxWordLength) {
        this.maxWordLength = maxWordLength;
    }

    public void setWordTokenizer(WordTokenizer wordTokenizer) {
        this.wordTokenizer = wordTokenizer;
    }

    public void clearFilters() {
        this.filters.clear();
    }

    public void addFilter(final Filter filter) {
        this.filters.add(filter);
    }

    public void setFilter(final Filter filter) {
        this.filters.clear();
        this.filters.add(filter);
    }

    public void clearNormalizers() {
        this.normalizers.clear();
    }

    public void addNormalizer(final Normalizer normalizer) {
        this.normalizers.add(normalizer);
    }

    public void setNormalizer(final Normalizer normalizer) {
        this.normalizers.clear();
        this.normalizers.add(normalizer);
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public void setUrlLoadTimeout(final long urlLoadTimeout) {
        this.urlLoadTimeout = urlLoadTimeout;
    }
}
