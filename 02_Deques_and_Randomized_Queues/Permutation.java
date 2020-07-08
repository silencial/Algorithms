import edu.princeton.cs.algs4.StdIn;  
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int count = 0;
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            count++;
            if (count <= n) {
                rq.enqueue(StdIn.readString());
            }
            else {
                int i = StdRandom.uniform(0, count);
                String s = StdIn.readString();
                if (i < n) {
                    rq.dequeue();
                    rq.enqueue(s);
                }
            }
        }
        for (int i = 0; i < n; i++)
            StdOut.println(rq.dequeue());
    }
}