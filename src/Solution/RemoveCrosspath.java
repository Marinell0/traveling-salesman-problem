/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Solution;

import Objects.Antenna;
import Objects.Point;
import Objects.Solution;
import static Solution.Utilities.newPath;

/**
 * Class made to test if 2 line segments intersect or are colinear.
 * @author Gabriel Marinello Moura Leite
 */
public class RemoveCrosspath {
    /**
     * Constructor to make this class non-instanceable.
     */
    private RemoveCrosspath(){
    }
    
    /**
     * Given three colinear points p, q, r, the function checks if point q lies on line segment 'pr' 
     * @param p First point
     * @param q Point in the middle
     * @param r Second point
     * @return True if is on line, false otherwise.
     */
    static boolean onSegment(Point p, Point q, Point r) 
    { 
        return q.x <= Math.max(p.x, r.x) && 
               q.x >= Math.min(p.x, r.x) && 
               q.y <= Math.max(p.y, r.y) && 
               q.y >= Math.min(p.y, r.y); 
    } 
    
    /**
     * Finds the orientation of the ordered triplet (p, q, r).
     * @param p Point p.
     * @param q Point q.
     * @param r Point r.
     * @return 0 if p, q and r are colinear;<br> 1 if Clockwise;<br> 2 if Counterclockwise.
     */
    static int orientation(Point p, Point q, Point r) 
    { 
        // See https://www.geeksforgeeks.org/orientation-3-ordered-points/ 
        // for details of below formula. 
        double val = (q.y - p.y) * (r.x - q.x) - 
                (q.x - p.x) * (r.y - q.y); 

        if (val == 0) return 0; // colinear 

        return (val > 0)? 1: 2; // clock or counterclock wise 
    } 
    
    /**
     * Tests if line segment (p1, q1) intersects (p2, q2).
     * @param p1 Start of line segment 1.
     * @param q1 End of line segment 1.
     * @param p2 Start of line segment 2.
     * @param q2 End of line segment 2.
     * @return True if lines intersect or are colinear. False otherwise.
     */
    static boolean doIntersect(Point p1, Point q1, Point p2, Point q2) 
    { 
        // Find the four orientations needed for general and 
        // special cases 
        int o1 = orientation(p1, q1, p2); 
        int o2 = orientation(p1, q1, q2); 
        int o3 = orientation(p2, q2, p1); 
        int o4 = orientation(p2, q2, q1); 

        // General case 
        if (o1 != o2 && o3 != o4) 
            return true; 

        // Special Cases 
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1 
        if (o1 == 0 && onSegment(p1, p2, q1)) return true; 

        // p1, q1 and q2 are colinear and q2 lies on segment p1q1 
        if (o2 == 0 && onSegment(p1, q2, q1)) return true; 

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2 
        if (o3 == 0 && onSegment(p2, p1, q2)) return true; 

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2 
        if (o4 == 0 && onSegment(p2, q1, q2)) return true; 

        return false; // Doesn't fall in any of the above cases 
    } 
    
    /**
     * Test and remove every intersection on the solution array.
     * @param solutionArray Solution array to remove intersections.
     * @return The amount of distance removed or gained from removing the intersections.
     */
    public static double removeIntersections(Solution[] solutionArray){
        double change = 0;
        double currentChange;
        //Test until there is no intersections
        double saveLastChange;
        double changeCache=0;
        int looping = 0;
        do {
            //For every edge in the graph
            saveLastChange = change;
            
            currentChange = 0;
            
            //Tests first iteraction of points.
            for(int j = 2; j+1 < solutionArray.length; j++){
                currentChange += removeIntersection(0, 1, j, j+1, solutionArray);
            }
            
            //Tests points between 1 and solution.length-2. You don't need to test the last one
            //because every point already tests it, removing an if statement at the end of the for loop.
            for (int i = 1; i < solutionArray.length-2; i++) {
                //Look if theres another edge in the graph that crosses it.
                //Goes until solution.length - 1;
                for(int j = i+2; j+1 < solutionArray.length; j++){
                    currentChange += removeIntersection(i, i+1, j, j+1, solutionArray);
                }
                //Checks the last edge in the solution array. Without it I would have to use mod or ternary operator, so I divided the code. 
               currentChange += removeIntersection(i, i+1, solutionArray.length-1, 0, solutionArray);
            }
            
            change += currentChange;
            //Tests double imprecision.
            
            if(Math.round(changeCache - change) == 0){
                return 0;
            }
            
            
            if(looping++ == 2_500){
                return change;
            }
            changeCache = saveLastChange;
        } while(Math.abs(currentChange) > 0);
        //Solve.frame.repaint();
        return change;
    }
    
    /**
     * Test and remove every intersection on the solution array.
     * @param solutionArray Solution array to remove intersections.
     * @param distances Cached distances to be used 
     * @return The amount of distance removed or gained from removing the intersections.
     */
    public static double removeIntersections(Solution[] solutionArray, double[][] distances){
        double change = 0;
        double currentChange;
        //Test until there is no intersections
        double saveLastChange;
        double changeCache=0;
        int looping = 0;
        do {
            //For every edge in the graph
            saveLastChange = change;
            
            currentChange = 0;
            
            //Tests first iteraction of points.
            for(int j = 2; j+1 < solutionArray.length; j++){
                currentChange += removeIntersection(0, 1, j, j+1, solutionArray, distances);
            }
            
            //Tests points between 1 and solution.length-2. You don't need to test the last one
            //because every point already tests it, removing an if statement at the end of the for loop.
            for (int i = 1; i < solutionArray.length-2; i++) {
                //Look if theres another edge in the graph that crosses it.
                //Goes until solution.length - 1;
                for(int j = i+2; j+1 < solutionArray.length; j++){
                    currentChange += removeIntersection(i, i+1, j, j+1, solutionArray, distances);
                }
                //Checks the last edge in the solution array. Without it I would have to use mod or ternary operator, so I divided the code. 
               currentChange += removeIntersection(i, i+1, solutionArray.length-1, 0, solutionArray, distances);
            }
            
            change += currentChange;
            //Tests double imprecision.
            
            if(Math.round(changeCache - change) == 0){
                return 0;
            }
            
            
            if(looping++ == 2_500){
                return change;
            }
            changeCache = saveLastChange;
        } while(Math.abs(currentChange) > 0);
        //Solve.frame.repaint();
        return change;
    }
    
    /**
     * Test if the points in the solution intersect. If they intersect, removes the intersection.
     * @param pi1 Index of p1.
     * @param qi1 Index of q1.
     * @param pi2 Index of p2.
     * @param qi2 Index of q2.
     * @param solutionArray Array to use as the solution.
     * @param antennas Has every coordinate to antennas. Can receive points as well.
     * @param distances Cached distances from and to every point.
     * @return The change in distance from removing the intersection. Returns 0 if nothing changed.
     */
    private static double removeIntersection(int pi1, int qi1, int pi2, int qi2, Solution[] solutionArray){
        Point p1 = solutionArray[pi1].currentPoint; //Point or Antenna p1
        Point q1 = solutionArray[qi1].currentPoint; //Point or Antenna q1
        Point p2 = solutionArray[pi2].currentPoint; //Point or Antenna p2
        Point q2 = solutionArray[qi2].currentPoint; //Point or Antenna q2
        
        //If the path intersects, remove the intersection and return 1.
        if(RemoveCrosspath.doIntersect(p1, q1, p2, q2)){
            //int solutionq1 = solution[qi1];
            //solution[qi1] = solution[pi2];
            //solution[pi2] = solutionq1;
            return newPath(qi1, pi2, solutionArray);
        }
        return 0; //No intersection was found.
    }
    
    /**
     * Test if the points in the solution intersect. If they intersect, removes the intersection.
     * @param pi1 Index of p1.
     * @param qi1 Index of q1.
     * @param pi2 Index of p2.
     * @param qi2 Index of q2.
     * @param solutionArray Array to use as the solution.
     * @param antennas Has every coordinate to antennas. Can receive points as well.
     * @param distances Cached distances from and to every point.
     * @return The change in distance from removing the intersection. Returns 0 if nothing changed.
     */
    private static double removeIntersection(int pi1, int qi1, int pi2, int qi2, Solution[] solutionArray, double[][] distances){
        Point p1 = solutionArray[pi1].currentPoint; //Point or Antenna p1
        Point q1 = solutionArray[qi1].currentPoint; //Point or Antenna q1
        Point p2 = solutionArray[pi2].currentPoint; //Point or Antenna p2
        Point q2 = solutionArray[qi2].currentPoint; //Point or Antenna q2
        
        //If the path intersects, remove the intersection and return 1.
        if(RemoveCrosspath.doIntersect(p1, q1, p2, q2)){
            //int solutionq1 = solution[qi1];
            //solution[qi1] = solution[pi2];
            //solution[pi2] = solutionq1;
            return newPath(qi1, pi2, solutionArray, distances);
        }
        return 0; //No intersection was found.
    }
}
