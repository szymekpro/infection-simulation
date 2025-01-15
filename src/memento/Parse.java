package memento;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import entities.*;
import states.*;

public class Parse {
    private Document data;

    public Parse(Document data) {
        this.data = data;
    }

    public void createPopulation() throws Exception {
        validateData();

        NodeList nList = data.getElementsByTagName("person");
        if (nList.getLength() == 0) {
            throw new Exception("Tag <person> does not exist in the document.");
        }

        Population population = Simulation.getPopulation();

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            Element eElement = (Element) nNode;
            try {
                Person person = parsePerson(eElement);
                population.addPerson(person);
            } catch (Exception e) {
                System.out.println("An error occurred while trying to add a person: " + e.getMessage());
            }
        }
    }

    private void validateData() throws Exception {
        if (data == null) {
            throw new Exception("The input document is null.");
        }
    }

    private Person parsePerson(Element eElement) throws Exception {
        double x = getDoubleValue(eElement, "x");
        double y = getDoubleValue(eElement, "y");
        String loadedState = eElement.getElementsByTagName("state").item(0).getTextContent();

        IState statePerson = new Immune();  // Default state if no match is found

        if (loadedState.contains("Symptoms")) {
            statePerson = new Symptoms();
            int counter = getIntValue(eElement, "counter");
            ((Symptoms) statePerson).setCounter(counter);
        } else if (loadedState.contains("NoSymptoms")) {
            statePerson = new NoSymptoms();
            int counter = getIntValue(eElement, "counter");
            ((NoSymptoms) statePerson).setCounter(counter);
        } else if (loadedState.contains("Healthy")) {
            statePerson = new Healthy();
            int counter = getIntValue(eElement, "counter");
            ((Healthy) statePerson).setCounter(counter);
        }

        return new Person(statePerson, x, y);
    }

    private double getDoubleValue(Element eElement, String tagName) throws Exception {
        String value = eElement.getElementsByTagName(tagName).item(0).getTextContent();
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new Exception("Invalid value for " + tagName + ": " + value);
        }
    }


    private int getIntValue(Element eElement, String tagName) throws Exception {
        String value = eElement.getElementsByTagName(tagName).item(0).getTextContent();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new Exception("Invalid value for " + tagName + ": " + value);
        }
    }
}
