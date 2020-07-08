import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;

public class KdTree {
    private static final boolean VERTICAL = true;

    private Node root;
    private int size;
    private Point2D minq;
    private double minDistance;
    private double rectxmin;
    private double rectymin;
    private double rectxmax;
    private double rectymax;

    private class Node {
        private final Point2D point;
        private final RectHV rect;
        private Node left;
        private Node right;
        private final boolean direction;

        public Node(Point2D point, RectHV rect, boolean direction) {
            this.point = point;
            this.rect = rect;
            this.direction = direction;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }
    // is the set empty? 
    public boolean isEmpty() {
        return root == null;
    }
    // number of points in the set 
    public int size() {
        return size;
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        rectxmin = 0.0;
        rectxmax = 1.0;
        rectymin = 0.0;
        rectymax = 1.0;
        root = insert(root, p, VERTICAL);
    }

    private Node insert(Node x, Point2D p, boolean direction) {
        if (x == null) {
            size++;
            RectHV newrect = new RectHV(rectxmin, rectymin, rectxmax, rectymax);
            return new Node(p, newrect, direction);
        }
        if (x.point.equals(p)) return x;

        if (direction == VERTICAL) {
            if (p.x() < x.point.x()) {
                rectxmax = x.point.x();
                x.left = insert(x.left, p, !direction);
            }
            else {
                rectxmin = x.point.x();
                x.right = insert(x.right, p, !direction);
            }
        }
        else {
            if (p.y() < x.point.y()) {
                rectymax = x.point.y();
                x.left = insert(x.left, p, !direction);
            }
            else {
                rectymin = x.point.y();
                x.right = insert(x.right, p, !direction);
            }
        }

        return x;
    }
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        return contains(root, p);
    }

    private boolean contains(Node x, Point2D p) {
        if (x == null) return false;

        if (p.equals(x.point)) return true;

        double diff;
        if (x.direction == VERTICAL)
            diff = p.x() - x.point.x();
        else
            diff = p.y() - x.point.y();

        if (diff < 0)
            return contains(x.left, p);
        else
            return contains(x.right, p);
    }
    // draw all points to standard draw 
    public void draw() {
        draw(root, 0.0, 1.0, 0.0, 1.0);
    }
    private void draw(Node x, double xmin, double xmax, double ymin, double ymax) {
        if (x == null) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        x.point.draw();
        if (x.direction == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.point.x(), ymin, x.point.x(), ymax);
            draw(x.left, xmin, x.point.x(), ymin, ymax);
            draw(x.right, x.point.x(), xmax, ymin, ymax);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(xmin, x.point.y(), xmax, x.point.y());
            draw(x.left, xmin, xmax, ymin, x.point.y());
            draw(x.right, xmin, xmax, x.point.y(), ymax);
        }
    }
    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.IllegalArgumentException();
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        range(root, rect, points);
        return points;
    }
    private void range(Node x, RectHV rect, ArrayList<Point2D> points) {
        if (x == null) return;
        if (rect.contains(x.point)) points.add(x.point);

        if (x.direction == VERTICAL) {
            if (x.point.x() > rect.xmax())
                range(x.left, rect, points);
            else if (x.point.x() < rect.xmin())
                range(x.right, rect, points);
            else {
                range(x.left, rect, points);
                range(x.right, rect, points);
            }
        }
        else {
            if (x.point.y() > rect.ymax())
                range(x.left, rect, points);
            else if (x.point.y() < rect.ymin())
                range(x.right, rect, points);
            else {
                range(x.left, rect, points);
                range(x.right, rect, points);
            }
        }
    }
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        minq = null;
        minDistance = Double.POSITIVE_INFINITY;
        nearest(root, p);
        return minq;
    }
    private void nearest(Node x, Point2D p) {
        if (x == null) return;

        double distance = p.distanceSquaredTo(x.point);
        if (distance < minDistance) {
            minDistance = distance;
            minq = x.point;
        }

        if (x.direction == VERTICAL) {
            if (x.point.x() > p.x()) {
                nearest(x.left, p);
                if (x.right != null && minDistance > x.right.rect.distanceSquaredTo(p))
                    nearest(x.right, p);
            }
            else {
                nearest(x.right, p);
                if (x.left != null && minDistance > x.left.rect.distanceSquaredTo(p))
                    nearest(x.left, p);
            }
        }
        else {
            if (x.point.y() > p.y()) {
                nearest(x.left, p);
                if (x.right != null && minDistance > x.right.rect.distanceSquaredTo(p))
                    nearest(x.right, p);
            }
            else {
                nearest(x.right, p);
                if (x.left != null && minDistance > x.left.rect.distanceSquaredTo(p))
                    nearest(x.left, p);
            }
        }
    }
    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        //
    }
}