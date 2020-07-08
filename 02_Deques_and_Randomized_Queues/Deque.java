import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private int n;
    private Node first;
    private Node last;

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        n = 0;
    }
    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }
    // return the number of items on the deque
    public int size() {
        return n;
    }
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        first.prev = null;
        if (isEmpty())
            last = first;
        else
            oldfirst.prev = first;
        n++;
    }
    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.prev = oldlast;
        last.next = null;
        if (isEmpty())
            first = last;
        else
            oldlast.next = last;
        n++;
    }
    // remove and return the item from the front
    public Item removeFirst() {
        if (n == 0) throw new java.util.NoSuchElementException();
        Item item = first.item;
        first = first.next;
        n--;
        if (isEmpty())
            last = null;
        else
            first.prev = null;
        return item;
    }
    // remove and return the item from the end
    public Item removeLast() {
        if (n == 0) throw new java.util.NoSuchElementException();
        Item item = last.item;
        last = last.prev;
        n--;
        if (isEmpty())
            first = null;
        else
            last.next = null;
        return item;
    }
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator();
    }
    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() {
            return current != null;
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    // unit testing (optional)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            String sub = item.substring(0, 1);
            if (!item.equals("f") && !item.equals("l")) {
                if (sub.equals("f"))
                    deque.addFirst(item.substring(1));
                else
                    deque.addLast(item.substring(1));
            }
            else if (!deque.isEmpty()) {
                if (item.equals("f"))
                    StdOut.print("f" + deque.removeFirst() + " ");
                else if (item.equals("l"))
                    StdOut.print("l" + deque.removeLast() + " ");
            }
        }
        StdOut.println("(" + deque.size() + " left on deque)");
        for (String s : deque)
            StdOut.println(s);
    }
}