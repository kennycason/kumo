package com.kennycason.kumo.placement

import com.kennycason.kumo.Word

interface RectangleWordPlacer {
    fun reset()
    fun place(word: Word): Boolean
}
