package wordcloud.nlp.tokenizer;

import java.util.Arrays;
import java.util.List;

import wordcloud.nlp.tokenizer.WordTokenizer;


/**
 * Disables tokenization
 * 
 * @author Posdorfer
 *
 */
public class NoTokenizer implements WordTokenizer
{

    public List<String> tokenize(String sentence)
    {
        return Arrays.asList(new String[] { sentence });
    }

}
