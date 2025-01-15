package memento;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

import entities.*;
import org.xml.sax.SAXException;
import states.*;

public class readSaveFile {

    private int count = 0;

    public readSaveFile() {}

    public void save(Memento memento) {
        try {
            File file = new File("./zapis" + count + ".xml");
            Document doc = createDocument();
            Element populationElement = doc.createElement("population");
            doc.appendChild(populationElement);

            for (Person person : memento.getPopulationSnapshot()) {
                Element personElement = createPersonElement(doc, person);
                populationElement.appendChild(personElement);
            }
            saveDocumentToFile(doc, file);

        } catch (ParserConfigurationException | TransformerException e) {
            System.out.println("Error during saving the file: " + e.getMessage());
            e.printStackTrace();
        }
        count++;
    }

    public void read(String path) {
        try {
            Document doc = parseXMLFile(path);
            new Parse(doc).createPopulation();
        } catch (Exception e) {
            System.out.println("Error during reading the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Document createDocument() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        return docBuilder.newDocument();
    }

    private Element createPersonElement(Document doc, Person person) {
        Element personElement = doc.createElement("person");

        Element xElement = doc.createElement("x");
        xElement.appendChild(doc.createTextNode(String.valueOf(person.getX())));
        personElement.appendChild(xElement);

        Element yElement = doc.createElement("y");
        yElement.appendChild(doc.createTextNode(String.valueOf(person.getY())));
        personElement.appendChild(yElement);

        Element stateElement = doc.createElement("state");
        stateElement.appendChild(doc.createTextNode(person.getState().getClass().getName()));
        personElement.appendChild(stateElement);

        if (person.getState() instanceof ISick || person.getState() instanceof Healthy) {
            Element counterElement = doc.createElement("counter");
            int counterValue = getStateCounter(person);
            counterElement.appendChild(doc.createTextNode(String.valueOf(counterValue)));
            stateElement.appendChild(counterElement);
        }

        return personElement;
    }

    private int getStateCounter(Person person) {
        if (person.getState() instanceof ISick) {
            return ((ISick) person.getState()).getCounter();
        } else if (person.getState() instanceof Healthy) {
            return ((Healthy) person.getState()).getCounter();
        }
        return 0;
    }

    private void saveDocumentToFile(Document doc, File file) throws TransformerException {
        StreamResult result = new StreamResult(file);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
    }

    private Document parseXMLFile(String path) throws Exception {
        File inputFile = new File("./" + path);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(inputFile);
        doc.getDocumentElement().normalize();
        return doc;
    }
}
