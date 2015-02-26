package jan.juice.control;

/**
 * Created by Jan on 10.02.15.
 */
public class Component implements Comparable<Component> {
    private String name;

    public Component(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(Component component) {
        return this.name.compareTo(component.getName());
    }

    @Override
    public boolean equals(Object obj) {
        return (this.name.compareTo(((Component)obj).getName()) == 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int length() {
        return this.name.length();
    }
}
