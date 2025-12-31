import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
  Trie (Prefix Tree) Veri Yapısı
   Bu sınıf, kelimeleri ağaç yapısında saklar ve hızlı arama/tamamlama sağlar.

  Özellikler:
   - Hızlı Ekleme ve Arama: O(L) - L: Kelime uzunluğu
   - Frekans Bazlı Sıralama: Sık kullanılan kelimeler üstte çıkar.
 */

public class Trie {
    private TrieNode root;

    private class KelimeSonuc implements Comparable<KelimeSonuc> {
        String kelime;
        int frekans;

        public KelimeSonuc(String kelime, int frekans) {
            this.kelime = kelime;
            this.frekans = frekans;
        }

        @Override
        public int compareTo(KelimeSonuc diger) {
            return diger.frekans - this.frekans;
        }
    }

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
        }
        current.isEndOfWord = true;
        current.frequency++;
    }

    private TrieNode searchPrefix(String prefix) {
        TrieNode current = root;
        for (char ch : prefix.toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return null;
            }
            current = current.children.get(ch);
        }
        return current;
    }

    /**
     * DFS (Depth First Search) Algoritması
     * Verilen düğümden aşağıya doğru inerek tamamlanmış tüm kelimeleri toplar.
     * Bu metod RECURSIVE (Özyinelemeli) çalışır.
     */
    private void suggestHelper(TrieNode root, String currentPrefix, List<KelimeSonuc> results) {
        if (root.isEndOfWord) {
            results.add(new KelimeSonuc(currentPrefix, root.frequency));
        }

        for (Character ch : root.children.keySet()) {
            suggestHelper(root.children.get(ch), currentPrefix + ch, results);
        }
    }


    public List<String> autoComplete(String prefix) {
        List<KelimeSonuc> hamSonuclar = new ArrayList<>();
        TrieNode node = searchPrefix(prefix);

        if (node == null) {
            return new ArrayList<>();
        }

        suggestHelper(node, prefix, hamSonuclar);

        Collections.sort(hamSonuclar);

        List<String> siraliKelimeler = new ArrayList<>();
        for (KelimeSonuc ks : hamSonuclar) {
            siraliKelimeler.add(ks.kelime);
        }

        return siraliKelimeler;
    }
}