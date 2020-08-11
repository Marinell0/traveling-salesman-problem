/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

/**
 *
 * @author Gabriel Marinello Moura Leite
 */
public class Point implements Cloneable{
    public double x;
    public double y;
    
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString(){
        return String.format("(%.2f, %.2f)", this.x, this.y);
    }
    
    @Override
    public Object clone(){
        try{
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            return new Point(this.x, this.y);
        }
    }
}
