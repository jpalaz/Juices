package jan.juice.control;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jan on 17.02.15.
 */
public class Washings {
    private int size;
    int[] matching;
    boolean[] used;
    boolean[] used1;

    ArrayList<ArrayList<Integer>> edges;
    ArrayList<Juice> juices;

    public Washings(ArrayList<Juice> juices) {
        this.juices = juices;
        edges = new ArrayList<ArrayList<Integer>>();
        size = juices.size();

        matching = new int[size];
        used = new boolean[size];
        used1 = new boolean[size];
    }

    public int solve() {
        makeGraph();
        makeMaxMatching();
        return countMinWashing();
    }

    private void makeGraph() {
        Collections.sort(juices);
        edges = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < juices.size(); i++) {
            edges.add(new ArrayList<Integer>());
            for (int j = i + 1; j < juices.size(); j++) {
                if(juices.get(i).isSubJuiceOf(juices.get(j))) {
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
                if(matching[edges.get(i).get(j)] == -1) {
                    matching[edges.get(i).get(j)] = i;
                    used1[i] = true;
                    break;
                }
            }
        }

        for (int i = 0; i < size; i++) {
            if(used1[i])
                continue;
            for (int j = 0; j < size; j++) {
                used[j] = false;
            }
            tryKuhn(i);
        }
    }

    private boolean tryKuhn(int vertex) {
        if(used[vertex])
            return false;

        used[vertex] = true;
        for (int i = 0; i < edges.get(vertex).size(); i++) {
            int to = edges.get(vertex).get(i);
            if(matching[to] == -1 || tryKuhn(matching[to])) {
                matching[to] = vertex;
                return true;
            }
        }
        return false;
    }

    private int countMinWashing() {
        int edgesInMatching = 0;
        for (int i = 0; i < size; i++) {
            if(matching[i] != -1)
                edgesInMatching++;
        }

        return size - edgesInMatching;
    }
}
