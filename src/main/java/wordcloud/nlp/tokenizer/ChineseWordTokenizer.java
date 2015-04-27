package wordcloud.nlp.tokenizer;

import org.languagetool.language.Chinese;
import org.languagetool.tokenizers.Tokenizer;

import java.util.ArrayList;
import java.util.List;

public class ChineseWordTokenizer implements WordTokenizer {

    private static final Chinese CHINESE = new Chinese();

    public ChineseWordTokenizer() {}

    @Override
    public List<String> tokenize(String sentence) {
        final Tokenizer tokenizer = CHINESE.getWordTokenizer();
        final List<String> rawTokens = tokenizer.tokenize(sentence);
        final List<String> tokens = new ArrayList<>();
        for(String rawToken : rawTokens) {   // parse parts-of-speech tags away (政府/n, 依照/p, 法律/n, 行/ng, 使/v, 执法/vn)
            tokens.add(rawToken.substring(0, rawToken.indexOf('/')));
        }
        return tokens;
    }

}
