/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Gabriel Marinello Moura Leite
 */
public class Antenna implements Cloneable{
    public final int ID;
    public final Point origin;
    public Point[] generatedPoints;
    public final double radius;
    
    public Antenna(int ID, Point origin, double radius) {
        this.ID = ID;
        this.origin = origin;
        this.radius = radius;
    }
    
    public Antenna(int ID, Point origin, double radius, Point[] generatedPoints){
        this.ID = ID;
        this.origin = origin;
        this.radius = radius;
        this.generatedPoints = generatedPoints;
    }
    
    @Override
    public String toString(){
        return String.format("%s - %.2f | ", origin.toString(), radius) + Arrays.toString(generatedPoints);
    }

    /**
     * Generate points inside the antenna radius.
     * @param qtyPoints Number of random points inside radius.
     * @param random {@link Random} object to generate point location.
     */
    public void generatePointsInRadius(int qtyPoints, Random random){
        generatedPoints = new Point[qtyPoints];
        for (int i = 0; i < generatedPoints.length; i++) {
            double r = this.radius * sqrt(random.nextDouble());
            double theta = random.nextDouble() * 2 * PI;
            double x_aux = r * cos(theta);
            double y_aux = r * sin(theta);

            generatedPoints[i] = new Point(this.origin.x + x_aux, this.origin.y + y_aux);
        }
    }
    
    /**
     * Creates a clone for the Antenna object.
     * @return Clone from the object.
     */
    @Override
    public Object clone(){
        try{
            Antenna clone = (Antenna)super.clone();
            return clone;
        } catch (CloneNotSupportedException ex) {
            return new Antenna(this.ID, (Point)this.origin.clone(), this.radius, this.generatedPoints);
        }
    }
}
