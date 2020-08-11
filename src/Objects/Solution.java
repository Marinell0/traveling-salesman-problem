/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

/**
 * Contains the antenna to be used as the solution.
 * @author Gabriel Marinello Moura Leite
 */
public class Solution implements Cloneable{
    public int antennaID;
    public Point currentPoint;
    
    public Solution(int antennaID, Point currentPoint){
        this.antennaID = antennaID;
        this.currentPoint = currentPoint;
    }
    
    /**
     * Clones the current solution.
     * @return
     */
    @Override
    public Object clone(){
        try {
            Solution clone = (Solution)super.clone();
            clone.currentPoint = (Point)this.currentPoint.clone();
            return clone;
        } catch (CloneNotSupportedException ex) {
            return new Solution(this.antennaID, (Point)this.currentPoint.clone());
        }
        
    }
    
    @Override
    public String toString(){
        return String.format("%d - %s", antennaID, currentPoint);
    }
    
    public static String pathToString(Solution[] solution){
        String start = "[";
        for (int i = 0; i < solution.length-1; i++) {
            start += solution[i].antennaID + ", ";
        }
        start += solution[solution.length-1].antennaID + "]";
        
        return start;
    }
}
