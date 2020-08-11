/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Solution;

import Objects.Antenna;
import Objects.Solution;
import static Solution.RemoveCrosspath.removeIntersections;
import static Solution.Utilities.getTwoRandomNumbers;
import static Solution.Utilities.newPath;
import drone_refactor.Options;
import java.util.Random;

/**
 * Generates random solution values and measure if they are better than the previous solution. 
 * @author Gabriel Marinello Moura Leite
 */
public class Hillclimb {
    private Hillclimb(){}
    
    /**
     * Creates new paths by randomizing the order of the Solution array.
     * @param newPaths How many new paths to do.
     * @param randomChanges How many edges will be changed randomly.
     * @param solutionArray Solution array to test distances.
     * @param random Random object to test.
     * @return 
     */
    public static double hillClimb(int newPaths, int randomChanges, Solution[] solutionArray, Random random){
        double distance = 0;

        for (int i = 0; i < newPaths; i++) {
            Solution[] solutionAux = solutionArray.clone();
            double newPathDistance = 0;
            for (int j = 0; j < randomChanges; j++) {
                int[] points = getTwoRandomNumbers(solutionAux.length, random);
                newPathDistance += newPath(points[0], points[1], solutionAux);
            }
            newPathDistance += removeIntersections(solutionAux);
            if(newPathDistance < 0){
                System.arraycopy(solutionAux, 0, solutionArray, 0, solutionArray.length);
                distance += newPathDistance;
            }
        }
        Solve.frame.repaint();
        return distance;
    }
    
    /**
     * Creates new paths by randomizing the order of the Solution array.
     * @param newPaths How many new paths to do.
     * @param randomChanges How many edges will be changed randomly.
     * @param solutionArray Solution array to test distances.
     * @param distances_origin Uses adjacency matrix of distances instead of calculating every time.
     * @param random Random object to test.
     * @return 
     */
    public static double hillClimb(int newPaths, int randomChanges, Solution[] solutionArray, double[][] distances_origin, Random random){
        double distance = 0;

        for (int i = 0; i < newPaths; i++) {
            Solution[] solutionAux = new Solution[solutionArray.length];
            for (int j = 0; j < solutionArray.length; j++) {
                solutionAux[j] = (Solution)solutionArray[j].clone();
            }
            //Solution[] solutionAux = solutionArray.clone();
            double newPathDistance = 0;
            for (int j = 0; j < randomChanges; j++) {
                int[] points = getTwoRandomNumbers(solutionAux.length, random);
                newPathDistance += newPath(points[0], points[1], solutionAux, distances_origin);
            }
            newPathDistance += removeIntersections(solutionAux, distances_origin);
            if(newPathDistance < 0){
                for (int j = 0; j < solutionAux.length; j++) {
                    solutionArray[j] = (Solution)solutionAux[j].clone();
                }
                distance += newPathDistance;
            }
        }
        Solve.frame.repaint();
        return distance;
    }
    
    /**
     * Creates new paths by randomizing the order of the Solution array. <u style="color:red"><b>This method will create local minimums.</b></u>
     * @param newPaths How many new paths to do.
     * @param randomChanges How many edges will be changed randomly.
     * @param solutionArray Solution array to be changed as the new solution, used to test the new paths.
     * @param antenna Antenna array used to get a new path to another generated point.
     * @param random Random object used to get random points to change in the solution array.
     * @return 
     */
    public static double hillClimbAllPoints(int newPaths, int randomChanges, Solution[] solutionArray, Antenna[] antenna, Random random){
        double distance = 0;
        int qtyPoints = Options.pointsInsideAntennaRadius;
        
        Solution[] solution2 = new Solution[solutionArray.length];
        for (int i = 0; i < solution2.length; i++) {
            solution2[i] = (Solution)solutionArray[i].clone();
        }
        
        for (int i = 0; i < newPaths; i++) {
            //Solution[] solutionAux2 = solution2.clone();
            Solution[] solutionAux2 = new Solution[solution2.length];
            for (int j = 0; j < solutionAux2.length; j++) {
                solutionAux2[j] = (Solution)solution2[j].clone();
            }
            
            double newPathDistance = 0;
            for (int j = 0; j < randomChanges; j++) {
                int[] points = getTwoRandomNumbers(solutionAux2.length, random);
                int generatedPoint1 = random.nextInt(qtyPoints);
                int generatedPoint2 = random.nextInt(qtyPoints);
                newPathDistance += newPath(points[0], points[1], generatedPoint1, generatedPoint2, solutionAux2, antenna);
            }
            newPathDistance += removeIntersections(solutionAux2);
            if(newPathDistance < 0){
                for (int j = 0; j < solution2.length; j++) {
                    solution2[j] = (Solution)solutionAux2[j].clone();
                }
                distance += newPathDistance;
            }
        }
        for (int i = 0; i < solutionArray.length; i++) {
            solutionArray[i] = (Solution)solution2[i].clone();
        }
        Solve.frame.repaint();
        return distance;
    }
}
