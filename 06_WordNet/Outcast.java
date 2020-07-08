import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet w;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        w = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int n = nouns.length;
        int[][] d = new int[n][n];
        int dmax = 0;
        int id = 0;
        for (int i = 0; i < n; i++) {
            int dsum = 0;
            for (int j = 0; j < i; j++)
                dsum += d[j][i];
            for (int j = i + 1; j < n; j++) {
                d[i][j] = w.distance(nouns[i], nouns[j]);
                dsum += d[i][j];
            }
            if (dsum > dmax) {
                dmax = dsum;
                id = i;
            }
        }
        return nouns[id];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
