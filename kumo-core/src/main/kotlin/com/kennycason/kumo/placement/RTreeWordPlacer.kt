package com.kennycason.kumo.placement

import com.github.davidmoten.rtree.RTree
import com.github.davidmoten.rtree.geometry.Geometries
import com.github.davidmoten.rtree.geometry.Rectangle
import com.kennycason.kumo.Word

class RTreeWordPlacer : RectangleWordPlacer {

    private var placedWordRTree: RTree<String, Rectangle> = RTree.maxChildren(4).create()

    override fun reset() {
        placedWordRTree = RTree.maxChildren(4).create()
    }

    override fun place(word: Word): Boolean {
        val wordRectangle = Geometries.rectangle(
            word.position.getX(),
            word.position.getY(),
            word.position.getX() + word.dimension.getWidth(),
            word.position.getY() + word.dimension.getHeight()
        )

        val results = placedWordRTree.search(wordRectangle)
        val matches = results.count().toBlocking().single()

        if (matches > 0) return false

        placedWordRTree = placedWordRTree.add(word.word, wordRectangle)
        return true
    }
}
