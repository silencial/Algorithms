import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private final int n;
    private final String s;
    private int[] ind;

    // Circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException();
        this.s = s;
        n = s.length();
        ind = new int[n];
        for (int i = 0; i < n; i++)
            ind[i] = i;
        sort(0, n - 1, 0);
    }

    // Length of s
    public int length() {
        return n;
    }

    // Returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= n)
            throw new IllegalArgumentException();
        return ind[i];
    }

    private void sort(int lo, int hi, int d) {
        if (hi <= lo)
            return;
        int lt = lo, gt = hi;
        int v = charAt(lo, d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(i, d);
            if (t < v)
                exch(lt++, i++);
            else if (t > v)
                exch(i, gt--);
            else
                i++;
        }
        sort(lo, lt-1, d);
        if (v >= 0)
            sort(lt, gt, d+1);
        sort(gt+1, hi, d);
    }

    private void exch(int a, int b) {
        int temp = ind[a];
        ind[a] = ind[b];
        ind[b] = temp;
    }

    private int charAt(int i, int d) {
        if (d < n)
            return s.charAt((ind[i] + d) % n);
        else
            return -1;
    }

    // Unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        String s = in.readAll();
        CircularSuffixArray cs = new CircularSuffixArray(s);
        // StdOut.println(cs.s);
        StdOut.println(cs.length());
        // for (int i = 0; i < cs.length(); i++)
        //     StdOut.print(cs.index(i) + " ");
    }
}
