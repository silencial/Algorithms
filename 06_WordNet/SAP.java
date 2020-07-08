import java.util.ArrayList;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph G;
    private final int V;
    private Queue<Integer> q1;
    private Queue<Integer> q2;
    private boolean[] marked1;
    private boolean[] marked2;
    private int[] distance1;
    private int[] distance2;
    private int distance;
    private int ancestor;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();

        this.G = new Digraph(G);
        V = G.V();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= V || w < 0 || w >= V)
            throw new IllegalArgumentException();

        computeSAP(v, w);
        return distance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= V || w < 0 || w >= V)
            throw new IllegalArgumentException();

        computeSAP(v, w);
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in
    // w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        check(v);
        check(w);

        computeSAP(v, w);
        return distance;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such
    // path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        check(v);
        check(w);

        computeSAP(v, w);
        return ancestor;
    }

    private void computeSAP(int v, int w) {
        reInit();

        q1.enqueue(v);
        q2.enqueue(w);

        bfsMain();
    }

    private void computeSAP(Iterable<Integer> v, Iterable<Integer> w) {
        reInit();

        for (int i : v)
            q1.enqueue(i);
        for (int i : w)
            q2.enqueue(i);

        bfsMain();
    }

    private void reInit() {
        q1 = new Queue<Integer>();
        q2 = new Queue<Integer>();
        marked1 = new boolean[V];
        marked2 = new boolean[V];
        distance1 = new int[V];
        distance2 = new int[V];
        distance = -1;
        ancestor = -1;
    }

    private void bfsMain() {
        int d = 0;
        while (!q1.isEmpty() || !q2.isEmpty()) {
            // Stop early when single depth >= the min distance
            if (distance != -1 && d > distance)
                break;
            d++;
            bfs(q1, marked1, marked2, distance1, distance2, d);
            bfs(q2, marked2, marked1, distance2, distance1, d);
        }
    }

    private void bfs(Queue<Integer> q, boolean[] marked1, boolean[] marked2, int[] d1, int[] d2, int d) {
        if (q.isEmpty())
            return;

        ArrayList<Integer> temp = new ArrayList<Integer>();

        while (!q.isEmpty()) {
            int v = q.dequeue();
            if (marked1[v])
                continue;
            marked1[v] = true;
            temp.add(v);

            if (marked2[v] && (distance == -1 || d1[v] + d2[v] < distance)) {
                distance = d1[v] + d2[v];
                ancestor = v;
            }
        }
        for (int i : temp) {
            for (int w : G.adj(i)) {
                if (marked1[w])
                    continue;
                q.enqueue(w);
                d1[w] = d;
            }
        }
    }

    private void check(Iterable<Integer> v) {
        if (v == null)
            throw new IllegalArgumentException();

        for (Object obj : v) {
            if (obj == null)
                throw new IllegalArgumentException();
            int i = (int) obj;
            if (i < 0 || i >= V)
                throw new IllegalArgumentException();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
