import entities.*;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        // 1m = 10dm = 10px

        int height = 900; // n
        int width = 1000; // m

        System.out.println("1 - Nowa symulacja");
        System.out.println("2 - Wczytaj symulację");

        Scanner s = new Scanner(System.in);
        String op = s.nextLine();

        switch (op) {
            case "1" -> {
                System.out.println("1 - Bez odporności");
                System.out.println("2 - z odpornością");
                String w = s.nextLine();
                switch (w) {
                    case "1":
                        new Simulation(height, width, 40).startSimulation(false);
                    case "2":
                        new Simulation(height, width, 40).startSimulation(true);
                    default:
                        System.out.println("Wybrana opcja nie istnieje");
                }
            }
            case "2" -> {

                File root = new File("./");
                String[] contents = root.list();
                if(contents == null) {
                    System.out.println("Brak zapisanych plików");
                    break;
                }
                System.out.println("Zapisy: ");
                Arrays.stream(contents).forEach(elem -> {
                    Pattern pattern = Pattern.compile("zapis");
                    Matcher matcher = pattern.matcher(elem);
                    boolean matchFound = matcher.find();
                    if (matchFound) {
                        System.out.println(elem);
                    }
                });
                System.out.println("Wpisz nazwe pliku");
                String file = s.nextLine();
                new Simulation(height, width, file).startSimulation(false);}
            default -> System.out.println("Wybrana opcja nie istnije");
        }
    }
}
