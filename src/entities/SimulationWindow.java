package entities;

import states.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

public class SimulationWindow implements ActionListener {

    private static JFrame frame;
    private static JLabel label;
    private BufferedImage image;
    private final int height;
    private final int width;
    private final BufferedImage CLEAR;
    private JPanel legendPanel;
    private JButton buttonSave, buttonReturn;

    public SimulationWindow(int height, int width) {
        this.height = height;
        this.width = width;
        frame = null;
        label = null;
        this.image = new BufferedImage(width , height, BufferedImage.TYPE_INT_ARGB);
        this.CLEAR = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        legendPanel = new JPanel();
        legendPanel.setLayout(new GridLayout(0, 2));
    }

    /*public void displayPopulation(Population population) {
        image.setData(CLEAR.getData());
        Graphics2D g = this.image.createGraphics();
        List<Person> popList = population.getPopulation();
        for(Person person: popList) {
            int x = (int)((person.getX() * 10)/.1);
            int y = (int)((person.getY() * 10) / .1);
            IState personState = person.getState();
            if(personState instanceof Immune) g.setPaint(Color.GREEN);
            else if(personState instanceof Symptoms) g.setPaint(Color.RED);
            else if(personState instanceof NoSymptoms) g.setPaint(Color.ORANGE);
            else if(personState instanceof Healthy) g.setPaint(Color.DARK_GRAY);
            g.fillRoundRect(x, y, 6, 6, 3, 3);
        }
        this.display(this.image);
    }*/

    public void displaySimulation(Population pop) {

        int size = 10;
        image.setData(CLEAR.getData());
        Graphics2D g = this.image.createGraphics();
        List<Person> popList = pop.getPopulation();
        //Person samplePerson = new Person(state, x, y);
        if (frame == null) {
            this.initializeFrame(this.image);  // Initialize the frame if it's null
        }
        addLegend();

        for (Person p : popList) {
            IState state = p.getState();
            int x = (int) (p.getX() * 100);
            int y = (int) (p.getY() * 100);

            if (state instanceof Immune) g.setPaint(Color.GREEN);
            else if (state instanceof Symptoms) g.setPaint(Color.RED);
            else if (state instanceof NoSymptoms) g.setPaint(Color.ORANGE);
            else if (state instanceof Healthy) g.setPaint(Color.DARK_GRAY);
            g.fillRoundRect(x - size / 2, y - size / 2, size, size, 10, 10);
        }
        //drawAxes(g);
        this.initializeFrame(this.image);
    }

    private void initializeFrame(BufferedImage image) {
        if (frame == null) {

            frame = new JFrame();
            frame.setTitle("Simulation");
            frame.setSize(image.getWidth(), image.getHeight());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            label = new JLabel();
            label.setIcon(new ImageIcon(image));
            frame.getContentPane().add(label, BorderLayout.WEST);

            buttonSave = new JButton("Save");
            buttonReturn = new JButton("Return");
            buttonSave.addActionListener(this);
            buttonReturn.addActionListener(this);
            JPanel buttons = new JPanel();
            buttons.add(buttonSave);
            buttons.add(buttonReturn);
            buttonReturn.setEnabled(false);

            label.setBounds(width, height, 200, 100);
            frame.getContentPane().add(buttons, BorderLayout.SOUTH);
            frame.setLocationRelativeTo(null);
            frame.pack();
            frame.setVisible(true);
        } else {
            label.setIcon(new ImageIcon(image));
        }
    }

    private void addLegend() {
        // This should only be done once, when the frame is initialized
        if (legendPanel.getComponentCount() == 0) {
            legendPanel.add(createLegendItem(Color.GREEN, "Immune"));
            legendPanel.add(createLegendItem(Color.RED, "Symptoms"));
            legendPanel.add(createLegendItem(Color.ORANGE, "No Symptoms"));
            legendPanel.add(createLegendItem(Color.DARK_GRAY, "Healthy"));
        }
        legendPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        frame.getContentPane().add(legendPanel, BorderLayout.NORTH);
        frame.revalidate();
        frame.repaint();
    }

    private JPanel createLegendItem(Color color, String description) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel colorLabel = new JLabel();
        colorLabel.setBackground(color);
        colorLabel.setOpaque(true);
        colorLabel.setPreferredSize(new Dimension(20, 20));  // Kwadratowy kolor
        panel.add(colorLabel);
        panel.add(new JLabel(description));
        return panel;
    }

    private void drawAxes(Graphics2D g) {

        g.setPaint(Color.GRAY);
        g.drawLine(0, height / 2, width, height / 2); // O X
        g.drawLine(width / 2, 0, width / 2, height); // O Y

        g.setPaint(Color.BLACK);
        Font font = new Font("Arial", Font.PLAIN, 12);
        g.setFont(font);

        for (int i = 0; i < width; i += 50) {
            g.drawString(Integer.toString(i), i, height / 2 + 15);
        }

        for (int i = 0; i < height; i += 50) {
            g.drawString(Integer.toString(i), width / 2 + 15, i);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttonSave){
            Simulation.saveSimulation();
        } else if(e.getSource() == buttonReturn){
            Simulation.returnToLastSavedSimulation();
        }

        buttonReturn.setEnabled(Simulation.shouldBeEnabled());
    }
}
