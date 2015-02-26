package jan.juice.control;

/**
 * Created by Jan on 10.02.15.
 */
public class Main {

    public static void main(String[] args) {
        Mixer mixer = new Mixer("juice.in");

        SortingThread sorting = new SortingThread(mixer.getAllComponents());
        sorting.start();

        mixer.writeAllComponents("juice1.out");
        mixer.countMinWashings();

        if (sorting.isAlive()) {
            try {
                sorting.join();
            } catch(InterruptedException e) { e.printStackTrace(); }
        }

        mixer.writeSortedComponents("juice2.out", sorting.getSortedComponents());
        mixer.writeWashingNumber("juice3.out");
    }
}
