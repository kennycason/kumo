package com.kennycason.kumo.padding

import com.kennycason.kumo.Word

interface Padder {
    fun pad(word: Word, padding: Int)
}
