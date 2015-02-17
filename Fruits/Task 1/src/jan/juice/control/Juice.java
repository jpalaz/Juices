package jan.juice.control;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class Juice  implements Comparable<Juice> {
    private TreeSet<Component> components;

    public Juice() {
        this.components = new TreeSet<Component>();
    }

    public TreeSet<Component> getComponents() {
        return components;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public boolean isSubJuiceOf(Juice juice) {
        return this.getComponents().containsAll(juice.getComponents());
    }

    public int compareTo(Juice juice) {
        return juice.components.size() - components.size();
    }
}
