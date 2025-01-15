package entities;

import states.*;

import java.util.ArrayList;
import java.util.List;

public class Population {
    private final List<Person> population;
    private final Area area;

    public Population(Area area) {
        this.population = new ArrayList<>();
        this.area=area;
    }

    public List<Person> getPopulation() {
        return population;
    }
    public void addPerson(Person person){
        this.population.add(person);
    }
    public void removePerson(Person person){
        this.population.remove(person);
    }
    public void clearPopulation(){
        population.clear();
    }

    public void preservePopulation(boolean immunity){
        double heightMeter=area.getHeight() * 0.01;
        double widthMeter=area.getWidth() * 0.01;
        double x, y;

        String[] edges = {"LEFT", "TOP", "RIGHT", "BOTTOM"};
        int edgeIndex = (int)(Math.random() * edges.length);

        switch (edges[edgeIndex]) {
            case "LEFT":
                x = 0.0;
                y = Math.random() * heightMeter;
                break;
            case "TOP":
                x = Math.random() * widthMeter;
                y = 0.0;
                break;
            case "RIGHT":
                x = widthMeter;
                y = Math.random() * heightMeter;
                break;
            case "BOTTOM":
                x = Math.random() * widthMeter;
                y = heightMeter;
                break;
            default:
                throw new IllegalStateException("Unexpected edge value");
        }

        IState state = new Healthy();

        if (Math.random() < 0.1) { // 10% szans, że nowy osobnik jest zakażony.
            if(Math.random() < 0.5) // jeśli zakażony, 50% że posiada objawy, lub ich nie posiada.
                state = new Symptoms();
            else
                state = new NoSymptoms();
        }
        else if(immunity) // jeśli przypadek część początkowo jest odporna( w tym przypadku "część" definiuje się jako 50% ).
            if(Math.random() > 0.5) //
                state = new Immune();

        this.addPerson(new Person(state, x, y));
    }

    public void personRepositionHandler(Person person) {
        double areaWidthMeter = area.getWidth() * 0.01;
        double areaHeightMeter = area.getHeight() * 0.01;
        double random = 0.15 + Math.random() * 0.05; // Zakres [0.15, 0.2]

        person.setX(person.getX() > areaWidthMeter ? person.getX() - random : person.getX() + random);
        person.setY(person.getY() > areaHeightMeter ? person.getY() - random : person.getY() + random);
    }

    public Person newCoordinatesGenerator(){
        for(Person person : population){
            person.newCoordinates();
            if(!this.area.insideArea(person)){
                if(Math.random() < 0.5) { //50% szansy że osoba zostanie usunięta? spr
                    return person;
                }
                this.personRepositionHandler(person);
            }
        }
        return null;
    }

    public void sicknessHandler() {
        List<Person> sickPeople = new ArrayList<>();
        for (Person person : population) {
            if (person.getState() instanceof ISick) {
                sickPeople.add(person);
            }
        }

        for (Person person : population) {
            if (person.getState() instanceof Healthy healthyState) {
                boolean inContactWithSick = sickPeople.stream()
                        .anyMatch(sick -> person.getDistance(sick) <= 2.0);

                if (inContactWithSick) {
                    Person sickPerson = sickPeople.stream()
                            .filter(sick -> person.getDistance(sick) <= 2.0)
                            .findFirst().orElse(null);

                    if (sickPerson != null && sickPerson.getState() instanceof Symptoms) {
                        healthyState.setSymptomsPerson(true);
                    }
                    person.getState().handle(person);
                } else {
                    healthyState.setCounter(0);
                }
            }
        }
    }

    public void immunityHandler(){
        for(Person person : this.population){
            if(person.getState() instanceof ISick) {
                person.getState().handle(person);
            }
        }
    }

}
