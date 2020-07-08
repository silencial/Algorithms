import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private boolean[] open;
    private boolean[] contop;
    private boolean[] conbot;
    private boolean percolate;
    private int opensites;
    private final WeightedQuickUnionUF uf;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new java.lang.IllegalArgumentException();

        int size = n*n;
        this.n = n;
        open = new boolean[size];
        contop = new boolean[size];
        conbot = new boolean[size];
        opensites = 0;
        percolate = false;
        uf = new WeightedQuickUnionUF(size);
    }
    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        check(row, col);

        boolean top = false;
        boolean bot = false;
        int index = xyTo1D(row, col);
        if (open[index])
            return;
        opensites++;
        open[index] = true;

        if (row == 1)
            top = true;
        if (row == n)
            bot = true;
        // up
        if (row > 1 && open[index-n]) {
            if (contop[uf.find(index)] || contop[uf.find(index-n)])
                top = true;
            if (conbot[uf.find(index)] || conbot[uf.find(index-n)])
                bot = true;
            uf.union(index, index-n);
        }
        // bottom
        if (row < n && open[index+n]) {
            if (contop[uf.find(index)] || contop[uf.find(index+n)])
                top = true;
            if (conbot[uf.find(index)] || conbot[uf.find(index+n)])
                bot = true;
            uf.union(index, index+n);
        }
        // left
        if (col > 1 && open[index-1]) {
            if (contop[uf.find(index)] || contop[uf.find(index-1)])
                top = true;
            if (conbot[uf.find(index)] || conbot[uf.find(index-1)])
                bot = true;
            uf.union(index, index-1);
        }
        // right
        if (col < n && open[index+1]) {
            if (contop[uf.find(index)] || contop[uf.find(index+1)])
                top = true;
            if (conbot[uf.find(index)] || conbot[uf.find(index+1)])
                bot = true;
            uf.union(index, index+1);
        }

        contop[uf.find(index)] = top;
        conbot[uf.find(index)] = bot;
        if (top && bot)
            percolate = true;
    }
    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        check(row, col);
        return open[xyTo1D(row, col)];
    }
    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        check(row, col);
        return contop[uf.find(xyTo1D(row, col))];
    }
    // number of open sites
    public int numberOfOpenSites() {
        return opensites;
    }
    // does the system percolate?
    public boolean percolates() {
        return percolate;
    }

    // test client (optional)
    public static void main(String[] args) {
        //
    }

    private int xyTo1D(int i, int j) {
        return n*(i-1)+j-1;
    }
    private void check(int i, int j) {
        if (i < 1 || i > n || j < 1 || j > n)
            throw new java.lang.IllegalArgumentException();
    }
}
