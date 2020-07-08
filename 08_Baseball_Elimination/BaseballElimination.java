import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

import java.util.HashMap;
import java.util.ArrayList;

public class BaseballElimination {
    private final int n;
    private final int[] win;
    private final int[] lose;
    private final int[] left;
    private final int[][] game;
    private int winMaxnow = 0;
    private String leader;
    private final HashMap<String, Integer> name;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();
        win = new int[n];
        lose = new int[n];
        left = new int[n];
        game = new int[n][n];
        name = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String team = in.readString();
            name.put(team, i);
            win[i] = in.readInt();
            lose[i] = in.readInt();
            left[i] = in.readInt();
            if (winMaxnow < win[i]) {
                winMaxnow = win[i];
                leader = team;
            }
            for (int j = 0; j < n; j++)
                game[i][j] = in.readInt();
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return name.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!name.containsKey(team))
            throw new IllegalArgumentException();
        return win[name.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!name.containsKey(team))
            throw new IllegalArgumentException();
        return lose[name.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!name.containsKey(team))
            throw new IllegalArgumentException();
        return left[name.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!name.containsKey(team1) || !name.containsKey(team2))
            throw new IllegalArgumentException();
        return game[name.get(team1)][name.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!name.containsKey(team))
            throw new IllegalArgumentException();
        int id = name.get(team);
        if (trivialEliminated(id))
            return true;
        FlowNetwork graph = constructGraph(id);
        FordFulkerson ff = new FordFulkerson(graph, n - 1, n);

        // StdOut.println(graph.toString());

        for (FlowEdge e : graph.adj(n - 1)) {
            if (e.flow() < e.capacity())
                return true;
        }
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!name.containsKey(team))
            throw new IllegalArgumentException();

        ArrayList<String> proof = new ArrayList<>();
        int id = name.get(team);
        if (trivialEliminated(id)) {
            proof.add(leader);
            return proof;
        }

        FlowNetwork graph = constructGraph(id);
        FordFulkerson ff = new FordFulkerson(graph, n - 1, n);
        for (FlowEdge e : graph.adj(n - 1)) {
            if (e.flow() < e.capacity()) {
                for (String t : teams()) {
                    int idj = name.get(t);
                    if (idj == id)
                        continue;
                    if (idj > id)
                        idj--;
                    if (ff.inCut(idj))
                        proof.add(t);
                }
                return proof;
            }
        }
        return null;
    }

    private boolean trivialEliminated(int id) {
        if (win[id] + left[id] < winMaxnow)
            return true;
        return false;
    }

    private FlowNetwork constructGraph(int id) {
        int v = 2 + n * (n - 1) / 2;
        int s = n - 1;
        int t = n;
        int nodei = 0;
        int gameNode = n + 1;
        int maxWin = win[id] + left[id];
        FlowNetwork graph = new FlowNetwork(v);
        for (int i = 0; i < n; i++) {
            if (i == id)
                continue;
            graph.addEdge(new FlowEdge(nodei, t, maxWin - win[i]));
            int nodej = nodei + 1;
            for (int j = i + 1; j < n; j++) {
                if (j == id)
                    continue;
                graph.addEdge(new FlowEdge(s, gameNode, game[i][j]));
                graph.addEdge(new FlowEdge(gameNode, nodei, Double.POSITIVE_INFINITY));
                graph.addEdge(new FlowEdge(gameNode, nodej, Double.POSITIVE_INFINITY));
                nodej++;
                gameNode++;
            }
            nodei++;
        }
        return graph;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
