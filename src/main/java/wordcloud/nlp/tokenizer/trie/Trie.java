package wordcloud.nlp.tokenizer.trie;

import java.util.HashMap;
import java.util.Map;

public class Trie<T> {

	private Node<T> root;

	private int numberEntries;

	public Trie(T rootNodeValue) {
		root = new Node<>(rootNodeValue); // "empty value", usually some "null"  value or "empty string"
		numberEntries = 0;
	}

	public void add(T[] values) {
		Node<T> current = root;
        if (values.length == 0) { // "empty value"
            current.setTerminal(true);
        }
        for (int i = 0; i < values.length; i++) {
            Node<T> child = current.find(values[i]);
            if (child == null) {
                current = current.add(values[i]);
            } else {
                current = child;
            }
            if (i == values.length - 1) {
                if (!current.isTerminal()) {
                    current.setTerminal(true);
                    numberEntries++;
                }
            }
        }
	}

	public boolean contains(T[] values) {
		Node<T> current = root;
		for (int i = 0; i < values.length; i++) {
			if (current.find(values[i]) == null) {
				return false;
			} else {
				current = current.find(values[i]);
			}
		}
		if (current.isTerminal()) {
			return true;
		}
	    return false;
	}

	public int size() {
		return numberEntries;
	}

    @Override
    public String toString() {
        return "Trie{" +
                "root=" + root +
                ", numberEntries=" + numberEntries +
                '}';
    }


    private static class Node<T> {

        private final T value;

        private boolean terminal = false;

        public Map<T, Node<T>> children;


        public Node(T value) {
            this.value = value;
            this.children = new HashMap<>();
        }

        public boolean contains(T value) {
            return children.containsKey(value);
        }

        public Node<T> find(T value) {
            return children.get(value);
        }

        public T getValue() {
            return value;
        }

        public boolean isTerminal() {
            return terminal;
        }

        public void setTerminal(boolean terminal) {
            this.terminal = terminal;
        }

        public Node<T> add(T value) {
            Node<T> child = new Node<>(value);
            this.children.put(value, child);
            return child;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", terminal=" + terminal +
                    ", children=" + children +
                    '}';
        }

    }

}
