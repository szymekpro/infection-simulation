package vector;

public class Vector2D implements IVector {
    private final double x;
    private final double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double abs(double xb, double yb) {
        return Math.sqrt(Math.pow(xb - this.x, 2) + Math.pow(yb - this.y, 2));
    }

    @Override
    public double[] getComponents() {
        return new double[]{this.x, this.y};
    }
}
