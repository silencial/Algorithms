import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    // Apply Burrows-Wheeler transform,
    // Reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray cs = new CircularSuffixArray(s);
        int n = cs.length();
        int ind = 0;
        while (cs.index(ind) != 0)
            ind++;
        BinaryStdOut.write(ind);

        for (int i = 0; i < n; i++)
            BinaryStdOut.write(s.charAt((cs.index(i) + n - 1) % n));
        BinaryStdOut.close();
    }

    // Apply Burrows-Wheeler inverse transform,
    // Reading from standard input and writing to standard output
    public static void inverseTransform() {
        int ind = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        int n = t.length();
        int[] next = new int[n];
        int[] count = new int[R+1];

        for (int i = 0; i < n; i++)
            count[t.charAt(i) + 1]++;
        for (int r = 0; r < R; r++)
            count[r+1] += count[r];
        for (int i = 0; i < n; i++)
            next[count[t.charAt(i)]++] = i;
        for (int i = 0; i < n; i++) {
            ind = next[ind];
            BinaryStdOut.write(t.charAt(ind));
        }
        BinaryStdOut.close();
    }

    // If args[0] is "-", apply Burrows-Wheeler transform
    // If args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if      (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException();
    }
}
