package wordcloud.nlp.filter;

import ch.lambdaj.function.matcher.Predicate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kenny on 7/1/14.
 */
public class StopWordFilter extends Predicate<String> {

    private final Set<String> stopWords = new HashSet<>();

    public StopWordFilter(Collection<String> stopWords) {
        this.stopWords.addAll(stopWords);
    }
    @Override
    public boolean apply(String word) {
        return !this.stopWords.contains(word.toLowerCase());
    }

}

