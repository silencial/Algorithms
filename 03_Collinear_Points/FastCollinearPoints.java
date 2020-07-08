import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private LineSegment[] segs;
    private int num;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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

        for (int i = 0; i < length; i++)
            for (int j = i + 1; j < length; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new java.lang.IllegalArgumentException();
            }

        for (int i = 0; i < length; i++) {
            Point p = points[i];
            Arrays.sort(aux);
            Arrays.sort(aux, p.slopeOrder());
            int min = 1;
            while (min < length) {
                int max = min + 1;
                while (max < length && p.slopeTo(aux[min]) == p.slopeTo(aux[max]))
                    max++;
                if (max - min >= 3) {
                    Point pmin = p.compareTo(aux[min]) < 0 ? p : aux[min];
                    Point pmax = p.compareTo(aux[max-1]) > 0 ? p : aux[max-1];
                    if (p == pmin) {
                        if (num == segs.length)
                            resize(2*num);
                        segs[num++] = new LineSegment(pmin, pmax);
                    }
                }
                min = max;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdOut.println("finish");
        StdDraw.show();
    }
}