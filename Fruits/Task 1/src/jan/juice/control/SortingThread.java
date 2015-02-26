package jan.juice.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jan on 10.02.15.
 */
public class SortingThread extends Thread {

    private List<Component> components;
    private List<Component> sortedComponents;

    public SortingThread(List<Component> components) {
        this.components = components;
    }

    @Override
    public void run() {
        this.sortedComponents = new ArrayList<Component>(components);
        Collections.sort(sortedComponents);
    }

    public List<Component> getSortedComponents() {
        return sortedComponents;
    }
}
