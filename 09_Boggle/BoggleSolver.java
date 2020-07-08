import java.util.HashSet;
import java.util.HashMap;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private final TrieSET dict;
    private HashSet<String> words;
    private int m, n;
    private boolean[][] checked;

    private static final int R = 26;
    private static final int OFFSET = 'A';

    // 26-way trie node
    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
    }

    private static class TrieSET {

        private Node root;

        public TrieSET() {
        }

        public boolean contains(String key) {
            if (key == null)
                throw new IllegalArgumentException("argument to contains() is null");
            Node x = get(root, key, 0);
            if (x == null)
                return false;
            return x.isString;
        }

        private Node get(Node x, String key, int d) {
            if (x == null)
                return null;
            if (d == key.length())
                return x;
            char c = key.charAt(d);
            if (c == 'Q') {
                d += 1;
                if (d == key.length() || key.charAt(d) != 'U')
                    return null;
                return get(x.next[c - OFFSET], key, d + 1);
            } else {
                return get(x.next[c - OFFSET], key, d + 1);
            }
        }

        public void add(String key) {
            if (key == null)
                throw new IllegalArgumentException("argument to add() is null");
            root = add(root, key, 0);
        }

        private Node add(Node x, String key, int d) {
            if (x == null)
                x = new Node();
            if (d == key.length()) {
                x.isString = true;
            } else {
                char c = key.charAt(d);
                if (c == 'Q') {
                    d += 1;
                    if (d == key.length() || key.charAt(d) != 'U')
                        return x;
                    x.next[c - OFFSET] = add(x.next[c - OFFSET], key, d + 1);
                } else {
                    x.next[c - OFFSET] = add(x.next[c - OFFSET], key, d + 1);
                }
            }
            return x;
        }
    }

    // Initializes the data structure using the given array of strings as the
    // dictionary.
    // (You can assume each word in the dictionary contains only the uppercase
    // letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dict = new TrieSET();
        for (String s : dictionary)
            dict.add(s);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        words = new HashSet<>();
        m = board.rows();
        n = board.cols();
        checked = new boolean[m][n];
        Node x = dict.root;
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                searchWords(board, i, j, x);

        return words;
    }

    private void searchWords(BoggleBoard board, int i, int j, Node x) {
        dfs(board, i, j, checked, "", x);
    }

    private void dfs(BoggleBoard board, int i, int j, boolean[][] checked, String prefix, Node x) {
        if (checked[i][j])
            return;
        char c = board.getLetter(i, j);
        prefix += (c == 'Q' ? "QU" : c);
        x = x.next[c - OFFSET];

        if (x == null)
            return;
        if (prefix.length() > 2 && x.isString)
            words.add(prefix);

        checked[i][j] = true;

        if (i > 0) { // up
            dfs(board, i - 1, j, checked, prefix, x);
            if (j > 0)
                dfs(board, i - 1, j - 1, checked, prefix, x);
            if (j < n - 1)
                dfs(board, i - 1, j + 1, checked, prefix, x);
        }
        if (i < m - 1) { // down
            dfs(board, i + 1, j, checked, prefix, x);
            if (j > 0)
                dfs(board, i + 1, j - 1, checked, prefix, x);
            if (j < n - 1)
                dfs(board, i + 1, j + 1, checked, prefix, x);
        }
        if (j > 0) // left
            dfs(board, i, j - 1, checked, prefix, x);
        if (j < n - 1) // right
            dfs(board, i, j + 1, checked, prefix, x);
        checked[i][j] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero
    // otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dict.contains(word))
            return 0;
        int len = word.length();
        if (len < 3) return 0;
        if (len < 5) return 1;
        if (len < 6) return 2;
        if (len < 7) return 3;
        if (len < 8) return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word + "  " + solver.scoreOf(word));
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
