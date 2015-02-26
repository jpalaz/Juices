package jan.juice.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jan on 17.02.15.
 */
public class Washings {
    private int size;
    private int[] matching;
    private boolean[] used;
    private boolean[] used1;

    private List<List<Integer>> edges;
    private List<Juice> juices;

    public Washings(List<Juice> juices) {
        this.juices = juices;
        this.edges = new ArrayList<List<Integer>>();
        this.size = juices.size();

        this.matching = new int[size];
        this.used = new boolean[size];
        this.used1 = new boolean[size];
    }

    public int solve() {
        makeGraph();
        makeMaxMatching();
        return countMinWashing();
    }

    private void makeGraph() {
        Collections.sort(juices);
        edges.clear();

        for (int i = 0; i < juices.size(); i++) {
            edges.add(new ArrayList<Integer>());
            for (int j = i + 1; j < juices.size(); j++) {
                if (juices.get(i).isSubJuiceOf(juices.get(j))) {
                    edges.get(i).add(j);
                }
            }
        }
    }

    private void makeMaxMatching() {
        for (int i = 0; i < size; i++) {
            matching[i] = -1;
            used1[i] = false;
        }

        for (int i = 0; i < edges.size(); i++) {
            for (int j = 0; j < edges.get(i).size(); j++) {
                if (matching[edges.get(i).get(j)] == -1) {
                    matching[edges.get(i).get(j)] = i;
                    used1[i] = true;
                    break;
                }
            }
        }

        for (int i = 0; i < size; i++) {
            if (used1[i]) {
                continue;
            }

            for (int j = 0; j < size; j++) {
                used[j] = false;
            }
            tryKuhn(i);
        }
    }

    private boolean tryKuhn(int vertex) {
        if (used[vertex]) {
            return false;
        }

        used[vertex] = true;
        List<Integer> g = edges.get(vertex);
        for (int i = 0; i < g.size(); i++) {
            int to = g.get(i);
            if (matching[to] == -1 || tryKuhn(matching[to])) {
                matching[to] = vertex;
                return true;
            }
        }
        return false;
    }

    private int countMinWashing() {
        int edgesInMatching = 0;
        for (int i = 0; i < size; i++) {
            if (matching[i] != -1) {
                edgesInMatching++;
            }
        }

        return size - edgesInMatching;
    }
}
