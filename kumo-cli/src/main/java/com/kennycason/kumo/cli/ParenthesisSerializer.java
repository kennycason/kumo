package com.kennycason.kumo.cli;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

/**
 * Created by kenny on 6/12/16.
 */
public class ParenthesisSerializer {
    private static final Pattern PARENTHESIS_SPLIT_REGEX = Pattern.compile("\\)[ | ]*,[ | ]*\\(");

    public static <T> String serialize(final Collection<T> collection) {
        if (collection.isEmpty()) { return ""; }

        final String joined = collection.stream()
                                        .map(i -> i.toString())
                                        .collect(Collectors.joining("),("));

        return '(' + joined + ')';
    }

    public static List<String> deserialize(final String value) {
        if (isBlank(value)) { return Collections.emptyList(); }

        final String normalizedValue = trimToEmpty(value);
        if (normalizedValue.startsWith("(") && normalizedValue.endsWith(")")) {
            return Arrays.asList(
                    PARENTHESIS_SPLIT_REGEX.split(
                            normalizedValue.substring(1, normalizedValue.length() - 1)));
        }

        return Collections.singletonList(normalizedValue);
    }
}
