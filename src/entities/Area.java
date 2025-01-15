package entities;

public class Area {
    private final int width;
    private final int height;

    public Area(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean insideArea(Person person) {
        double x = person.getX() * 100; // 1 pixel = 1 dm
        double y = person.getY() * 100;

        return x > 0 && x < width && y > 0 && y < height;
    }
}

