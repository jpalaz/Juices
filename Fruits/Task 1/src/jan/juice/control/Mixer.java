package jan.juice.control;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Created by Jan on 17.02.15.
 */
public class Mixer {
    private ArrayList<Juice> juices;
    private ArrayList<Component> allComponents;
    private int minWashes;

    public Mixer(String inputFile) {
        this.juices = new ArrayList<Juice>();
        this.allComponents = new ArrayList<Component>();
        this.minWashes = 0;
        readFile(inputFile);
    }

    private void readFile(String fileName) {
        try {
            BufferedReader inputFile = new BufferedReader(new FileReader(fileName));
            StringTokenizer tokenizer;

            Component newComponent;
            Juice newJuice;
            String line;

            while( (line = inputFile.readLine()) != null) {
                tokenizer = new StringTokenizer(line);

                if(tokenizer.hasMoreElements()) {
                    newJuice = new Juice();
                    while(tokenizer.hasMoreTokens()) {
                        newComponent = new Component(tokenizer.nextToken());
                        if(newComponent.length() > 0) {
                            newJuice.addComponent(newComponent);

                            if(!allComponents.contains(newComponent))
                                allComponents.add(newComponent);
                        }
                    }

                    juices.add(newJuice);
                }
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

    public void writeAllComponents(String fileName) {
        try {
            PrintWriter outFile = new PrintWriter(new FileWriter(fileName));
            for(Component component: allComponents) {
                outFile.println(component);
            }
            outFile.flush();
            outFile.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem with output file.");
        }
    }

    public void writeSortedComponents(String fileName, ArrayList<Component> sortedComponents) {
        try {
            PrintWriter outFile = new PrintWriter(new FileWriter(fileName));
            for(Component component: sortedComponents) {
                outFile.println(component);
            }
            outFile.flush();
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem with output file.");
        }
    }

    public void writeWashingNumber(String fileName) {
        try {
            PrintWriter outFile = new PrintWriter(new FileWriter(fileName));
            outFile.print(getMinWashes());
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem with output file.");
        }
    }

    public void countMinWashings() {
        Washings washings = new Washings(getJuices());
        setMinWashes(washings.solve());
    }

    public ArrayList<Juice> getJuices() {
        return juices;
    }

    public void addJuice(ArrayList<Juice> juices) {
        this.juices = juices;
    }

    public int getMinWashes() {
        return minWashes;
    }

    public void setMinWashes(int minWashes) {
        this.minWashes = minWashes;
    }

    public ArrayList<Component> getAllComponents() {
        return allComponents;
    }
}
