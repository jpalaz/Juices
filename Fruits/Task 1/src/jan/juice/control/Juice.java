package jan.juice.control;

import java.util.Set;
import java.util.TreeSet;

public class Juice implements Comparable<Juice> {
    private Set<Component> components;

    public Juice() {
        this.components = new TreeSet<Component>();
    }

    public Set<Component> getComponents() {
        return components;
    }

    public void addComponent(Component component) {
        this.components.add(component);
    }

    public boolean isSubJuiceOf(Juice juice) {
        return this.getComponents().containsAll(juice.getComponents());
    }

    /**
     * @param juice to compare
     * @return value to sort by increase
     */
    public int compareTo(Juice juice) {
        return juice.getComponents().size() - this.components.size();
    }
}
