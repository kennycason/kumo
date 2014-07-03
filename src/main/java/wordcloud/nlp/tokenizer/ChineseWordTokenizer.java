package wordcloud.nlp.tokenizer;

import wordcloud.nlp.tokenizer.trie.StringTrie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenny on 7/3/14.
 */
public class ChineseWordTokenizer implements WordTokenizer {

    private StringTrie library;

    private boolean loadTraditional;

    public ChineseWordTokenizer() {
        this(true);
    }

    public ChineseWordTokenizer(boolean loadTraditional) {
        loadLibrary();
        this.loadTraditional = loadTraditional;
    }

    @Override
    public List<String> tokenize(String sentence) {
        List<String> words = new ArrayList<>();
        String word;
        int maxMisses = 6;
        for (int i = 0; i < sentence.length(); i++) {
            int len = 1;
            boolean loop;
            int misses = 0;
            int lastCorrectLen = 1;
            boolean somethingFound = false;
            do {
                word = sentence.substring(i, i + len);
                if (library.contains(word)) {
                    somethingFound = true;
                    lastCorrectLen = len;
                    loop = true;
                } else {
                    misses++;
                    loop = misses < maxMisses;
                }
                len++;
                if(i + len > sentence.length()) {
                    loop = false;
                }
            } while (loop);
            if(somethingFound) {
                word = sentence.substring(i, i + lastCorrectLen);
                if (!"".equals(word)) {
                    words.add(word);
                    i += lastCorrectLen - 1;
                }
            }
        }
        return words;
    }

    private void loadLibrary() {
        library = new StringTrie();
        library.loadFile("nlp/dict/chinese_simple.list");
        if(loadTraditional) {
            library.loadFile("nlp/dict/chinese_traditional.list");
        }
    }

}
