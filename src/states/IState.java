package states;

import entities.Person;

public interface IState {
    void handle(Person person);
}
