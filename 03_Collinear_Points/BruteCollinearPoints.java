import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private LineSegment[] segs;
    private int num;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.IllegalArgumentException();
        int length = points.length;
        Point[] aux = new Point[length];
        num = 0;
        segs = new LineSegment[1];

        for (int i = 0; i < length; i++) {
            if (points[i] == null)
                throw new java.lang.IllegalArgumentException();
            aux[i] = points[i];
        }

        Arrays.sort(aux);
        for (int i = 1; i < length; i++) {
            if (aux[i].compareTo(aux[i-1]) == 0)
                throw new java.lang.IllegalArgumentException();
        }

        for (int i = 0; i < length; i++)
            for (int j = i + 1; j < length; j++)
                for (int k = j + 1; k < length; k++)
                    for (int m = k + 1; m < length; m++) {
                        Point p1 = aux[i], p2 = aux[j], p3 = aux[k], p4 = aux[m];
                        double p12 = p1.slopeTo(p2);
                        double p13 = p1.slopeTo(p3);
                        double p14 = p1.slopeTo(p4);
                        if (p12 == p13 && p12 == p14) {
                            if (num == segs.length)
                                resize(2*num);
                            segs[num++] = new LineSegment(p1, p4);
                        }
                    }
        resize(num);
    }

    // the number of line segments
    public int numberOfSegments() {
        return num;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] temp  = new LineSegment[num];
        for (int i = 0; i < num; i++)
            temp[i] = segs[i];
        return temp;
    }

    private void resize(int capacity) {
        LineSegment[] temp = new LineSegment[capacity];
        for (int i = 0; i < num; i++)
            temp[i] = segs[i];
        segs = temp;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdOut.println("finish");
        StdDraw.show();
    }
}
