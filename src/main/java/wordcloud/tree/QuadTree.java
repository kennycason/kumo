package wordcloud.tree;

import wordcloud.Word;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kenny on 7/2/14.
 */
public class QuadTree {

    private static class Node {
        public final Word word;
        public Node NE;
        public Node SE;
        public Node SW;
        public Node NW;

        private Node(final Word word) {
            this.word = word;
        }

    }

    private Node root;

    public void add(final Word word) {
        if(root == null) {
            root = new Node(word);
            return;
        }
        add(word, root);
    }

    private void add(final Word word, final Node node) {
        if(word.equals(node.word)) { return; }

        addByVertix(word, word.getX(), word.getY(), node);
        addByVertix(word, word.getX() + word.getWidth(), word.getY(), node);
        addByVertix(word, word.getX(), word.getY() + word.getHeight(), node);
        addByVertix(word, word.getX() + word.getWidth(), word.getY() + word.getHeight(), node);
    }

    private void addByVertix(final Word word, int x2, int y2, Node node) {

        final int x = node.word.getX();
        final int y = node.word.getY();

        if(x2 < x) {
            if(y2 < y) {
                if(node.NW == null) {
                    node.NW = new Node(word);
                } else {
                    add(word, node.NW);
                }
            } else {
                if(node.SW == null) {
                    node.SW = new Node(word);
                } else {
                    add(word, node.SW);
                }
            }
        } else {
            if(y2 < y) {
                if(node.NE == null) {
                    node.NE = new Node(word);
                } else {
                    add(word, node.NE);
                }
            } else {
                if(node.SE == null) {
                    node.SE = new Node(word);
                } else {
                    add(word, node.SE);
                }
            }
        }
    }

    public Set<Word> getNearby(final Word word) {
        if(root == null) { return Collections.EMPTY_SET; }

        final Set<Word> nearby = new HashSet<>();
        getNearby(word, root, nearby);
        return nearby;
    }

    private void getNearby(Word word, Node node, Set<Word> nearby) {
        if(word.equals(node.word)) { return; }
        if(node.word == null) { return; }

        nearby.add(node.word);

        getNearbyByVertix(word, word.getX(), word.getY(), node, nearby);
        getNearbyByVertix(word, word.getX() + word.getWidth(), word.getY(), node, nearby);
        getNearbyByVertix(word, word.getX(), word.getY() + word.getHeight(), node, nearby);
        getNearbyByVertix(word, word.getX() + word.getWidth(), word.getY() + word.getHeight(), node, nearby);
    }

    private void getNearbyByVertix(Word word, int x2, int y2, Node node, Set<Word> nearby) {
        final int x = node.word.getX();
        final int y = node.word.getY();

        if(x2 < x) {
            if(y2 < y) {
                if(node.NW != null) {
                    getNearby(word, node.NW, nearby);
                }
            } else {
                if(node.SW != null) {
                    getNearby(word, node.SW, nearby);
                }
            }
        } else {
            if(y2 < y) {
                if(node.NE != null) {
                    getNearby(word, node.NE, nearby);
                }
            } else {
                if(node.SE != null) {
                    getNearby(word, node.SE, nearby);
                }
            }
        }
    }

}
