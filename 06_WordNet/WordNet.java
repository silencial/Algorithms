import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private final Digraph G;
    private final ArrayList<String> synset;
    // A word can have multiple id
    private final HashMap<String, ArrayList<Integer>> word;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();

        synset = new ArrayList<String>();
        word = new HashMap<String, ArrayList<Integer>>();

        In s = new In(synsets);
        while (!s.isEmpty()) {
            String line = s.readLine();
            int id = Integer.parseInt(line.split(",")[0]);
            String words = line.split(",")[1];
            synset.add(words);
            String[] temp = words.split(" ");
            for (int j = 0; j < temp.length; j++) {
                if (word.get(temp[j]) == null) {
                    ArrayList<Integer> l = new ArrayList<>();
                    l.add(id);
                    word.put(temp[j], l);
                } else
                    word.get(temp[j]).add(id);
            }
        }

        G = new Digraph(synset.size());
        s = new In(hypernyms);
        while (!s.isEmpty()) {
            String line = s.readLine();
            String[] ids = line.split(",");
            int v = Integer.parseInt(ids[0]);
            for (int j = 1; j < ids.length; j++)
                G.addEdge(v, Integer.parseInt(ids[j]));
        }

        checkRootedDAG();
        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return word.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();

        return this.word.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        return sap.length(word.get(nounA), word.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA
    // and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        int ancestor_id = sap.ancestor(word.get(nounA), word.get(nounB));

        return synset.get(ancestor_id);
    }

    private void checkRootedDAG() {
        int roots = 0;
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0) {
                roots++;
            }
        }
        if (roots != 1)
            throw new IllegalArgumentException();

        DirectedCycle d = new DirectedCycle(G);
        if (d.hasCycle())
            throw new IllegalArgumentException();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        String a = "a";
        String b = "b";
        StdOut.println(wn.distance(a, b));
        StdOut.println(wn.sap(a, b));
    }
}
