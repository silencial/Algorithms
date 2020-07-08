import java.util.ArrayList; 

public class Board {
    private final int n;
    private int[][] board;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                board[i][j] = blocks[i][j];
    }
    
    // board dimension n           
    public int dimension() {
        return n;
    }
    
    // number of blocks out of place
    public int hamming() {
        int ham = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (board[i][j] != (i*n + j + 1) && board[i][j] != 0)
                    ham++;
        return ham;
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int man = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) continue;
                int row = (board[i][j] - 1) / n;
                int col = (board[i][j] - 1) % n;
                man += Math.abs(row - i) + Math.abs(col - j);
            }
        return man;
    }
    
    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }
    
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        Board twinBoard = new Board(board);

        if (board[0][0] != 0 && board[0][1] != 0)
            twinBoard.swap(0, 0, 0, 1);
        else
            twinBoard.swap(1, 0, 1, 1);

        return twinBoard;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass()) return false;
        if (n != ((Board) y).n) return false;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (board[i][j] != ((Board) y).board[i][j])
                    return false;
        return true;
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neigh = new ArrayList<Board>();
        int row = 0, col = 0;
        here:
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                    break here;
                }
            }
        if (row > 0) {
            Board temp = new Board(board);
            temp.swap(row, col, row - 1, col);
            neigh.add(temp);
        }
        if (row < n - 1) {
            Board temp = new Board(board);
            temp.swap(row, col, row + 1, col);
            neigh.add(temp);
        }
        if (col > 0) {
            Board temp = new Board(board);
            temp.swap(row, col, row, col - 1);
            neigh.add(temp);
        }
        if (col < n - 1) {
            Board temp = new Board(board);
            temp.swap(row, col, row, col + 1);
            neigh.add(temp);
        }
        return neigh;
    }
    
    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(n).append('\n');
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                str.append(' ').append(board[i][j]).append(' ');
            str.append('\n');
        }

        return str.toString();
    }
    
    private void swap(int row, int col, int rowx, int colx) {
        int temp = board[row][col];
        board[row][col] = board[rowx][colx];
        board[rowx][colx] = temp;
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        //
    }
}