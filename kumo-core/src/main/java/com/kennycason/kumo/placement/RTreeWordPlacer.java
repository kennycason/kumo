package com.kennycason.kumo.placement;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.kennycason.kumo.Word;
import rx.Observable;

/**
 * Created by kenny on 2/21/16.
 */
public class RTreeWordPlacer implements RectangleWordPlacer {

    private RTree<String, Rectangle> placedWordRTree;

    @Override
    public void reset() {
        placedWordRTree = RTree.maxChildren(4).create();
    }

    @Override
    public boolean place(final Word word) {
        final Rectangle wordRectangle = Geometries.rectangle(
                word.getPosition().getX(),
                word.getPosition().getY(),
                word.getPosition().getX() + word.getDimension().getWidth(),
                word.getPosition().getY() + word.getDimension().getHeight());

        final Observable<Entry<String, Rectangle>> results = placedWordRTree.search(
                wordRectangle);

        final int matches = results.count().toBlocking().single();
        if (matches > 0) {
            return false;
        }
        placedWordRTree = placedWordRTree.add(word.getWord(), wordRectangle);
        return true;
    }

}
