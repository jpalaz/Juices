package jan.juice.algorithm;

import java.io.*;
import java.util.*;

/**
 * Created by Jan on 15.02.15.
 */

public class KuhnAlgorithm {
    private ArrayList<Juice> juices;

    class Juice implements Comparable<Juice> {
        private TreeSet<Component> components;

        public Juice() {
            this.components = new TreeSet<Component>();
        }

        public TreeSet<Component> getComponents() {
            return components;
        }

        public void addComponent(String component) {
            components.add(new Component(component));
        }

        public boolean isSubJuiceOf(Juice juice) {
            return this.getComponents().containsAll(juice.getComponents());
        }

        public int compareTo(Juice juice) {
            return juice.components.size() - components.size();
        }

        public class Component implements Comparable<Component>
        {
            private String name;

            Component(String name)
            {
                this.name = name;
            }

            public String getName(){return name;}

            @Override
            public int compareTo(Component o)
            {
                return name.compareTo(o.name);
            }

            @Override
            public boolean equals(Object o)
            {
                return (name.compareTo(((Component)o).getName())==0);
            }

            public String toString()
            {
                return name;
            }
        }
    }

    int size;

    public KuhnAlgorithm() {
        juices = new ArrayList<Juice>();
    }

    public void readJuicesFile(String fileName) {
        try {
            BufferedReader inputFile = new BufferedReader(new FileReader(fileName));
            StringTokenizer tokenizer = new StringTokenizer(inputFile.readLine(), " ");
            size = Integer.parseInt(tokenizer.nextToken());

            String newComponent;
            Juice newJuice;
            String line;

            while( (line = inputFile.readLine()) != null) {
                tokenizer = new StringTokenizer(line);
                tokenizer.nextToken();
                newJuice = new Juice();
                while (tokenizer.hasMoreTokens()) {
                    newComponent = tokenizer.nextToken();
                    newJuice.addComponent(newComponent);
                }

                juices.add(newJuice);
            }
            inputFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not exist.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("Problem with StringTokenizer.");
        }
    }

    private void writeSolution(String fileName, int washes) {
        try {
            PrintWriter outFile = new PrintWriter(new FileWriter(fileName));
            outFile.println(washes);
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem with output file.");
        }
    }

    ArrayList<ArrayList<Integer>> edges;
    int[] matching;
    boolean[] used;

    private void solve() {
        /*makeGraph();

        matching = new int[size];
        used = new boolean[size];

        for (int i = 0; i < size; i++) {
            matching[i] = -1;
        }

        for (int v = 0; v < size; v++) {
            for (int i = 0; i < size; i++) {
                used[i] = false;
            }

            tryKuhn(v);
        }*/
        makeGraph();

        boolean[] used1;
        matching = new int[size];
        used = new boolean[size];
        used1 = new boolean[size];

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

        int edgesInMatching = 0;
        for (int i = 0; i < size; i++) {
            if(matching[i] != -1)
                edgesInMatching++;
        }
        writeSolution("output.txt", size - edgesInMatching);
    }

    private boolean tryKuhn(int vertex) {
        if(used[vertex])
            return false;

        used[vertex] = true;
        ArrayList<Integer> g = edges.get(vertex);
        for (int i = 0; i < g.size(); i++) {
            int to = g.get(i);
            if(matching[to] == -1 || tryKuhn(matching[to])) {
                matching[to] = vertex;
                return true;
            }
        }
        return false;
    }

    private void makeGraph() {
        Collections.sort(juices);
        edges = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < size; i++) {
            edges.add(new ArrayList<Integer>());
            for (int j = i + 1; j < size; j++) {
                if(juices.get(i).isSubJuiceOf(juices.get(j))) {
                    edges.get(i).add(j);
                }
            }
        }
    }

    public static void main(String[] args) {
        new KuhnAlgorithm().run();
    }

    public void run() {
        readJuicesFile("input.txt");
        solve();
    }
}
