package states;

import entities.*;

public class Healthy implements INotImmune{
    private int counter = 0;
    private boolean symptomsPerson = false;

    @Override
    public void handle(Person person) {
        if (this.getCounter() >= 75) { // 3 * 25 kroków = 75 kroków = 75 * 40ms = 3s
            if (this.symptomsPerson)
                this.randomSymptoms(person);
            else
                if (Math.random() > 0.5)
                    this.randomSymptoms(person);
        } // nie jest powiedziane, z jakim prawdopodobieństem nowo zakażony osobnik dostaje symptomy, a z jakim nie
        this.counter++;
        this.setSymptomsPerson(false);
    }

    private void randomSymptoms(Person person){ // 50/50 objawy / brak objawów
        if (Math.random() > 0.5)
            person.setState(new Symptoms());
        else
            person.setState(new NoSymptoms());
    }

    public void setSymptomsPerson(boolean symptomsPerson) {
        this.symptomsPerson = symptomsPerson;
    }

    public boolean isSymptomsPerson() {
        return symptomsPerson;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }
}
