package com.kennycason.kumo.placement;

import com.kennycason.kumo.Word;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by kenny on 2/21/16.
 */
public class LinearWordPlacer implements RectangleWordPlacer {
    private final Set<Word> placedWords = new HashSet<>();
    private final Map<Word, Word> lastHitCache = new HashMap<Word, Word>();

    @Override
    public void reset() {
        placedWords.clear();
    }

    @Override
    public boolean place(final Word word) {
    	if (lastHitCache.containsKey(word)) {
    		if (lastHitCache.get(word).collide(word)) {
    			return false;
    		}
    	}
        for (final Word placeWord : this.placedWords) {
            if (placeWord.collide(word)) {
            	lastHitCache.put(word, placeWord);
                return false;
            }
        }
        placedWords.add(word);
        lastHitCache.remove(word);
        return true;
    }

}
