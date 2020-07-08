import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private final boolean solvable;
    private final int moves;
    private Stack<Board> sol;

    private class Node implements Comparable<Node> {
        private final Board board;
        private final Node prev;
        private final int moves;
        private final int manhattan;

        public Node(Board board, Node prev, int moves) {
            this.board = board;
            this.prev = prev;
            this.moves = moves;
            manhattan = board.manhattan();
        }

        public int compareTo(Node that) {
            return this.manhattan + this.moves - that.manhattan - that.moves;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new java.lang.IllegalArgumentException();

        MinPQ<Node> pq = new MinPQ<Node>();
        MinPQ<Node> pq2 = new MinPQ<Node>();
        sol = new Stack<Board>();

        pq.insert(new Node(initial, null, 0));
        pq2.insert(new Node(initial.twin(), null, 0));

        while (true) {
            Node temp = pq.delMin();
            if (temp.board.isGoal()) {
                solvable = true;
                moves = temp.moves;
                while (temp != null) {
                    sol.push(temp.board);
                    temp = temp.prev;
                }
                break;
            }
            for (Board b : temp.board.neighbors())
                if (temp.prev == null || !b.equals(temp.prev.board))
                    pq.insert(new Node(b, temp, temp.moves + 1));

            temp = pq2.delMin();
            if (temp.board.isGoal()) {
                solvable = false;
                moves = -1;
                sol = null;
                break;
            }
            for (Board b : temp.board.neighbors())
                if (temp.prev == null || !b.equals(temp.prev.board))
                    pq2.insert(new Node(b, temp, temp.moves + 1));
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return sol;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}