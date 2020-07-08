import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private static final double BORDERENERGY = 1000.0;

    private Picture p;
    private int width;
    private int height;
    private double[][] energy;
    private boolean horizontal = false;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();

        p = new Picture(picture);
        width = p.width();
        height = p.height();

        energy = new double[height][width];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                energy[y][x] = computeEnergy(x, y);
        // StdOut.println(Arrays.deepToString(energy));
    }

    // current picture
    public Picture picture() {
        return new Picture(p);
    }

    // width of current picture
    public int width() {
        return p.width();
    }

    // height of current picture
    public int height() {
        return p.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (!horizontal && !inBound(x, y))
            throw new IllegalArgumentException();
        if (horizontal && !inBound(y, x))
            throw new IllegalArgumentException();

        if (horizontal)
            return energy[x][y];
        else
            return energy[y][x];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (!horizontal) {
            transpose();
            horizontal = true;
        }

        return findSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (horizontal) {
            transpose();
            horizontal = false;
        }

        return findSeam();
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (!horizontal) {
            transpose();
            horizontal = true;
        }

        removeSeam(seam);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (horizontal) {
            transpose();
            horizontal = false;
        }

        removeSeam(seam);
    }

    private int[] findSeam() {
        double[] oldDis = new double[width];
        double[] dis = new double[width];
        int[][] parent = new int[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                relax(x, y, oldDis, dis, parent);
            System.arraycopy(dis, 0, oldDis, 0, width);
        }

        double min = oldDis[0];
        int ind = 0;
        for (int i = 0; i < width; i++) {
            if (oldDis[i] < min) {
                min = oldDis[i];
                ind = i;
            }
        }

        int[] seam = new int[height];
        seam[height - 1] = ind;
        for (int i = height - 2; i >= 0; i--) {
            ind = parent[ind][i + 1];
            seam[i] = ind;
        }

        return seam;
    }

    private void removeSeam(int[] seam) {
        check(seam);

        width--;

        // Update picture
        Picture newP = new Picture(width, height);
        if (horizontal)
            newP = new Picture(height, width);
        for (int y = 0; y < height; y++) {
            int k = 0;
            for (int x = 0; x < width + 1; x++) {
                if (x == seam[y])
                    continue;
                if (horizontal)
                    newP.set(y, k, p.get(y, x));
                else
                    newP.set(k, y, p.get(x, y));
                k++;
            }
        }
        p = newP;

        // Update energy
        double[][] newEnergy = new double[height][width];
        for (int y = 0; y < height; y++) {
            if (seam[y] > 0) {
                System.arraycopy(energy[y], 0, newEnergy[y], 0, seam[y]);
                newEnergy[y][seam[y] - 1] = computeEnergy(seam[y] - 1, y);
            }
            if (seam[y] < width) {
                System.arraycopy(energy[y], seam[y] + 1, newEnergy[y], seam[y], width - seam[y]);
                newEnergy[y][seam[y]] = computeEnergy(seam[y], y);
            }
        }
        energy = newEnergy;
    }

    private void relax(int x, int y, double[] oldDis, double[] dis, int[][] parent) {
        if (y == 0) {
            dis[x] = BORDERENERGY;
            return;
        }

        double left = Double.POSITIVE_INFINITY;
        double right = Double.POSITIVE_INFINITY;
        double mid = oldDis[x];
        if (x != 0)
            left = oldDis[x - 1];
        if (x != width - 1)
            right = oldDis[x + 1];

        if (left <= mid && left <= right) {
            dis[x] = left + energy[y][x];
            parent[x][y] = x - 1;
        } else if (mid <= left && mid <= right) {
            dis[x] = mid + energy[y][x];
            parent[x][y] = x;
        } else {
            dis[x] = right + energy[y][x];
            parent[x][y] = x + 1;
        }
    }

    private double computeEnergy(int x, int y) {
        if (!inBound(x, y))
            throw new IllegalArgumentException();

        if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
            return BORDERENERGY;

        if (horizontal) {
            int temp = x;
            x = y;
            y = temp;
        }

        double e = centerDifference(x - 1, y, x + 1, y) + centerDifference(x, y - 1, x, y + 1);

        return Math.sqrt(e);
    }

    private double centerDifference(int x0, int y0, int x1, int y1) {
        int rgb0 = p.getRGB(x0, y0);
        int r0 = (rgb0 >> 16) & 0xFF;
        int g0 = (rgb0 >> 8) & 0xFF;
        int b0 = (rgb0 >> 0) & 0xFF;

        int rgb1 = p.getRGB(x1, y1);
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF;
        int b1 = (rgb1 >> 0) & 0xFF;

        return Math.pow(r0 - r1, 2) + Math.pow(g0 - g1, 2) + Math.pow(b0 - b1, 2);
    }

    private void transpose() {
        double[][] newEnergy = new double[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                newEnergy[x][y] = energy[y][x];
        energy = newEnergy;

        int temp = width;
        width = height;
        height = temp;
    }

    private boolean inBound(int x, int y) {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }

    private void check(int[] arr) {
        if (arr == null)
            throw new IllegalArgumentException();
        if (arr.length != height || width <= 1)
            throw new IllegalArgumentException();
        int prev = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < 0 || arr[i] >= width)
                throw new IllegalArgumentException();
            if (Math.abs(arr[i] - prev) > 1)
                throw new IllegalArgumentException();
            prev = arr[i];
        }
    }

    public static void main(String[] args) {
        // unit testing (optional)
    }
}
