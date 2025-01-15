package memento;

import java.util.ArrayList;
import java.util.List;
import entities.*;

public class Caretaker {
    private List<Memento> mementos = new ArrayList<>();
    public Caretaker() {}
    public List<Memento> getMementos() {
        return mementos;
    }
    public void addMemento(Population population){
        mementos.add(new Memento(population));
    }
    public Memento getMemento(int ind){
        return mementos.get(ind);
    }
    public void removeMemento(Memento memento) {
        mementos.remove(memento);
    }
}
