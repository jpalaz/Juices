package jan.juice.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * Created by Jan on 10.02.15.
 */
public class SortingThread extends Thread {

    private ArrayList<Component> components;
    private ArrayList<Component> sortedComponents;

    public SortingThread(ArrayList<Component> components) {
        this.components = components;
    }

    @Override
    public void run() {
        this.sortedComponents = new ArrayList<Component>(components);
        Collections.sort(sortedComponents);
    }

    public ArrayList<Component> getSortedComponents() {
        return sortedComponents;
    }
}
