package entities;

import vector.*;
import states.*;

public class Person {
    private IState state;
    private double x;
    private double y;
    private double angle;

    public Person(IState state, double x, double y) {
        this.state = state;
        this.x = x;
        this.y = y;
        this.angle = Math.random() * 360;
    }

    public IState getState() {
        return state;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setState(IState state) {
        this.state = state;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getAngle() { return angle; }

    public void setAngle(double angle) { this.angle = angle; }

    public double getDistance(Person person){
        IVector vector1 = new Vector2D(this.x, this.y);
        return vector1.abs(person.getX(), person.getY());
    }

    private static double getRandomNumber(double min, double max){
        return min + Math.random() * (max - min);
    }

    private IVector smoothDisplacement(double maxStep, double maxAngleChange) {

        this.angle += getRandomNumber(-maxAngleChange, maxAngleChange);

        double deltaX = Math.cos(Math.toRadians(this.angle)) * maxStep;
        double deltaY = Math.sin(Math.toRadians(this.angle)) * maxStep;

        double newX = this.x + deltaX;
        double newY = this.y + deltaY;

        return new Vector2D(newX, newY);
    }

    public void newCoordinates(){
        IVector vector = this.smoothDisplacement(Math.random() * 0.1,30);
        if(vector.abs(this.x, this.y) <= 0.1) {
            this.x = vector.getComponents()[0];
            this.y = vector.getComponents()[1];
        }else{
            //System.out.println("niepowodzenie");
            this.newCoordinates();
        }
    }


}
