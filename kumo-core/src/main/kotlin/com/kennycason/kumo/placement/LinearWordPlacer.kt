package com.kennycason.kumo.placement

import com.kennycason.kumo.Word

class LinearWordPlacer : RectangleWordPlacer {

    private val placedWords = HashSet<Word>()
    private val lastHitCache = HashMap<Word, Word>()

    override fun reset() {
        placedWords.clear()
        lastHitCache.clear()
    }

    override fun place(word: Word): Boolean {
        val lastHit = lastHitCache[word]
        if (lastHit != null && lastHit.collide(word)) {
            return false
        }

        for (placedWord in placedWords) {
            if (placedWord.collide(word)) {
                lastHitCache[word] = placedWord
                return false
            }
        }
        placedWords.add(word)
        lastHitCache.remove(word)
        return true
    }
}
