import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new java.lang.IllegalArgumentException();

        double size = n*n;
        double[] prob = new double[trials];
        for (int tri = 0; tri < trials; tri++) {
            int count = 0;
            Percolation per = new Percolation(n);
            while (!per.percolates()) {
                int i = 1 + StdRandom.uniform(n);
                int j = 1 + StdRandom.uniform(n);
                if (!per.isOpen(i, j)) {
                    per.open(i, j);
                    count++;
                }
            }
            prob[tri] = count/size;
        }
        mean = StdStats.mean(prob);
        stddev = StdStats.stddev(prob);
        confidenceLo = mean - CONFIDENCE_95*stddev/Math.sqrt(trials);
        confidenceHi = mean + CONFIDENCE_95*stddev/Math.sqrt(trials);
    }    
    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }                          
    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }                        
    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }                  
    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }                  

    // test client (described below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, trials);

        StdOut.print("mean                    = ");
        StdOut.println(ps.mean());

        StdOut.print("stddev                  = ");
        StdOut.println(ps.stddev());

        StdOut.print("95% confidence interval = [");
        StdOut.print(ps.confidenceLo());
        StdOut.print(", ");
        StdOut.print(ps.confidenceHi());
        StdOut.println("]");

    }        
}