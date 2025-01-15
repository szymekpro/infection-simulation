package memento;

import java.util.ArrayList;
import java.util.List;
import entities.*;
import states.*;


public class Memento {
    private List<Person> populationSnapshot;

    public Memento(Population population) {
        this.populationSnapshot = createPopulationSnapshot(population);
    }

    private List<Person> createPopulationSnapshot(Population population) {
        List<Person> snapshot = new ArrayList<>();

        for (Person person : population.getPopulation()) {
            Person snapshotPerson = createSnapshotPerson(person);
            snapshot.add(snapshotPerson);
        }

        return snapshot;
    }

    private Person createSnapshotPerson(Person originalPerson) {
        IState originalState = originalPerson.getState();
        IState snapshotState = cloneState(originalState);

        return new Person(snapshotState, originalPerson.getX(), originalPerson.getY());
    }

    private IState cloneState(IState originalState) {
        IState clonedState;

        if (originalState instanceof Healthy) {
            clonedState = new Healthy();
            copyHealthyState((Healthy) originalState, (Healthy) clonedState);
        } else if (originalState instanceof ISick) {
            clonedState = (originalState instanceof Symptoms) ? new Symptoms() : new NoSymptoms();
            copySickState((ISick) originalState, (ISick) clonedState);
        } else {
            clonedState = new Immune();
        }

        return clonedState;
    }

    private void copyHealthyState(Healthy originalState, Healthy clonedState) {
        clonedState.setSymptomsPerson(originalState.isSymptomsPerson());
        clonedState.setCounter(originalState.getCounter());
    }

    private void copySickState(ISick originalState, ISick clonedState) {
        clonedState.setCounter(originalState.getCounter());
    }

    public List<Person> getPopulationSnapshot() {
        return populationSnapshot;
    }
}