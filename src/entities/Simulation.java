package entities;

import states.*;
import memento.*;

public class Simulation {
    private int n;
    private int m;
    private int startNumber;
    private static SimulationWindow simWindow;
    private final Area area;
    private static final int SLEEP_TIME_MS = 40;
    private static final double IMMUNITY_PROBABILITY = 0.5;
    //private static Person person;
    //private static Person person2;
    private static Population population;
    private final static Caretaker caretaker = new Caretaker();
    private final static readSaveFile file = new readSaveFile();

    public Simulation(int n, int m, int startNumber) {
        this.n = n;
        this.m = m;
        this.startNumber = startNumber;
        simWindow = new SimulationWindow(n , m);
        this.area = new Area(m, n);
    }

    public Simulation(int n, int m, String path){
        this.n = n;
        this.m = m;
        this.area = new Area(m, n);
        population = new Population(area);
        simWindow = new SimulationWindow(n , m);

        new readSaveFile().read(path);

    }

    public static Population getPopulation() {
        return population;
    }

    public void startSimulation(boolean immunity) {
        initializePopulation(immunity);

        while (true) {
            updateSimulationState(immunity);
            displaySimulation();
            pauseSimulation();
        }
    }

    private void initializePopulation(boolean immunity) {
        if (population == null) {
            population = new Population(area);
            for (int i = 0; i < startNumber; i++) {
                double x = Math.random() * (m * 0.01);
                double y = Math.random() * (n * 0.01);
                IState state = new Healthy();
                if (immunity && Math.random() > IMMUNITY_PROBABILITY) {
                    state = new Immune();
                }
                population.addPerson(new Person(state, x, y));
            }
        }
    }

    private void updateSimulationState(boolean immunity) {
        Person personToRemove = population.newCoordinatesGenerator();
        if (personToRemove != null) {
            population.removePerson(personToRemove);
            population.preservePopulation(immunity);
        }
        population.immunityHandler();
        population.sicknessHandler();
    }

    private void displaySimulation() {
        simWindow.displaySimulation(population);
    }

    private void pauseSimulation() {
        try {
            Thread.sleep(SLEEP_TIME_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void saveSimulation(){
        caretaker.addMemento(population);
        file.save(caretaker.getMemento(caretaker.getMementos().size() - 1));
    }
    public static void returnToLastSavedSimulation(){
        Memento memento = caretaker.getMemento(caretaker.getMementos().size() - 1);
        population.clearPopulation();

        for(Person person: memento.getPopulationSnapshot()){
            population.addPerson(person);
        }
        caretaker.removeMemento(memento);
        simWindow.displaySimulation(population);
    }

    public static boolean shouldBeEnabled(){
        return caretaker.getMementos().size() != 0;
    }

}
