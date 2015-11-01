package wordcloud.nlp.filter;

import java.util.List;

/**
 * Created by kenny
 */
public class CompositeFilter extends Filter {

    private final List<Filter> filters;

    public CompositeFilter(final List<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean apply(final String word) {
        for(Filter filter : filters) {
            if(!filter.apply(word)) { return false; }
        }
        return true;
    }

}
