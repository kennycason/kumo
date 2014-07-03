package wordcloud.nlp.tokenizer.trie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StringTrie {
	
	private Trie<Character> trie;
	
	public StringTrie() {
		trie = new Trie<>(' ');
	}
	
	public void add(String s) {
		trie.add(toArray(s));
	}
	
	public boolean contains(String s) {
		return trie.contains(toArray(s));
	}
	
	public int size() {
		return trie.size();
	}
	
	private Character[] toArray(String s) {
		Character[] cArray = new Character[s.length()];
		for(int i = 0; i < cArray.length; i++) {
			cArray[i] = s.charAt(i);
		}
		return cArray;
	}
	
	public void loadFile(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					getClass().getClassLoader().getResourceAsStream(fileName)
			));
			String line;
			while ((line = br.readLine()) != null) {
				add(line);
			}
			System.out.println("Load Complete: " + size() + " Entries");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return trie.toString();
	}

}
