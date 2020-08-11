/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Solution;

import Objects.Antenna;
import Objects.Point;
import Objects.Solution;
import static Solution.Utilities.getDistance;
import java.util.Random;

/**
 * Pulls the solution to the edges of the radius. <u style="color:red"><b>This method will create local minimums.</b></u>
 * @author Gabriel Marinello Moura Leite
 */
public class PullPoints {
    private PullPoints(){}
    
    /**
     * Generate points on the antennas, find the minimum path between the current antenna, one of the points in the next antenna and the next antenna, finding the minimum path.<u style="color:red"><b>This method will create local minimums.</b></u>
     * @param solution Solution array containing the antenna to be utilized.
     * @param antenna Antenna array to get other generated points inside radius of antennas.
     * @param qtyPoints Number of points per antenna to be generated.
     * @param random Random object to be used to generate the random points.
     * @return Distance difference between the new path and the old path.
     */
    public static double pull(Solution[] solution, Antenna[] antenna, int qtyPoints, Random random){
        double change = 0;
        
        double currentChange;
        do{
            currentChange = 0;
            for (int i = 0; i < solution.length-2; i++) {
                currentChange += changePath(solution, antenna, i, i+1, i+2);
            }
            currentChange += changePath(solution, antenna, solution.length-2, solution.length-1, 0);
            currentChange += changePath(solution, antenna, solution.length-1, 0, 1);
            change += currentChange;
            //System.out.println("currentChange: "+currentChange);
        }while(Math.round(currentChange*100) != 0);
        Solve.frame.repaint();
        
        return change;
    }
    
    /**
     * Search every random generated point inside the radius of the middle antenna, finding the minimum path between the first, middle and last antenna.
     * @param solution Solution array to get the current point and change to fit the minimum path.
     * @param antenna Antenna array to get points inside radius from the middle antenna.
     * @param first Index of first antenna on the solution array.
     * @param middle Index of middle antenna on the solution array.
     * @param lastIndex of first middle on the solution array.
     * @return Distance difference between the new path and the old path.
     */
    private static double changePath(Solution[] solution, Antenna[] antenna, int first, int middle, int last){
            Point[] generatedPoints = antenna[solution[middle].antennaID].generatedPoints;

            Point a = solution[first].currentPoint;
            Point b = solution[last].currentPoint;
            double distance = distance3points(a, solution[middle].currentPoint, b);
            double distanceAfter = distance;
            int indexPoint=-1;
            for (int j = 0; j < generatedPoints.length; j++) {
                double aux = distance3points(a, generatedPoints[j], b);
                if(aux < distanceAfter){
                    distanceAfter = aux;
                    indexPoint = j;
                }
            }
            if(indexPoint != -1){
                solution[middle].currentPoint = generatedPoints[indexPoint];
                return distanceAfter - distance;
            }
            return 0;
    }
    
    private static double distance3points(Point a, Point b, Point c){
        return getDistance(a, b) + getDistance(b, c);
    }
}
