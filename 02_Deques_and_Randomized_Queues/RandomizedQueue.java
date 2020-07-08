import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[1];
        n = 0;
    }
    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }
    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++)
            temp[i] = a[i];
        a = temp;
    }
    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();
        if (n == a.length)
            resize(2*a.length);
        a[n++] = item;
    }
    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int i = StdRandom.uniform(0, n);
        Item item = a[i];
        a[i] = a[--n];
        a[n] = null;
        if (n > 0 && n == a.length/4)
            resize(a.length/2);
        return item;
    }
    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int i = StdRandom.uniform(0, n);
        return a[i];
    }
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {
        private int i;
        private final int[] permutation;

        public RandomIterator() {
            i = 0;
            permutation = StdRandom.permutation(n);
        }
        public boolean hasNext() {
            return i < n;
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return a[permutation[i++]];
        }
    }
    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-") && !item.equals("s"))
                rq.enqueue(item);
            else if (!rq.isEmpty()) {
                if (item.equals("-"))
                    StdOut.print(rq.dequeue() + " ");
                else
                    StdOut.print("s" + rq.sample() + " ");
            }
        }
        StdOut.println("(" + rq.size() + " left on deque)");
        for (String s : rq)
            StdOut.println(s);
    }
}