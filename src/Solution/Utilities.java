/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Solution;

import Objects.Antenna;
import Objects.Point;
import Objects.Solution;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides utilities to the {@link Solve} class.
 * @author Gabriel Marinello Moura Leite
 */
public final class Utilities {
    private Utilities(){}
    
    /**
     * Returns an array with the same size as qty, containing distinct numbers between 0 and (exclusive) upperBound. upperBound - qty has to be higher than  0.
     * @param qty Quantity of distinct numbers.
     * @param upperBound Exclusive upperBound, meaning the function will return an array filled with numbers between 0 and (upperBound-1).
     * @param random Random object used to generate random points.
     * @return {@link int[]} array containing qty distinct numbers between 0 and upperBound.
     */
    public static int[] getRandomNumbers(int qty, int upperBound, Random random){
        int[] points = new int[qty];
        int[] everyPoint = new int[upperBound];
        
        for (int i = 0; i < everyPoint.length; i++) 
            everyPoint[i] = i;
        
        for (int i = 0; i < qty; i++) {
            int usedPoint = random.nextInt(everyPoint.length-i);
            points[i] = everyPoint[usedPoint];
            everyPoint[usedPoint] = everyPoint[everyPoint.length-1-i];
        }
        
        return points;
    }

    /**
     * Returns 2 distinct random numbers from 0 (inclusive) to upperBound (exclusive).
     * @param upperBound Max number (exclusive) to be generated.
     * @param random {@link Random} object used to generate the random number.
     * @return Int[] array containing the 2 random numbers.
     */
    public static int[] getTwoRandomNumbers(int upperBound, Random random){
        int[] points = new int[2]; //Creates the Int[] array to be returned.
        
        points[0] = random.nextInt(upperBound); //Gives the first number to the first position.

        do{ 
            points[1] = random.nextInt(upperBound);
        }while(points[0] == points[1]); //Try until both numbers are different.
        
        return points;
    }
    
    /**
     * Uses the current {@link solution} to measure current path distance.
     * @param solutionArray Solution array to utilize as the path.
     * @param distances Distances between every antenna or point in the solution array.
     * @return Total path distance.
     */
    public static double pathDistanceFromArray(int[] solutionArray, double[][] distances){
        //Total distance
        double distance = 0;
        //First point in the solution array.
        int first = solutionArray[0];
        //Last point in the solution array.
        int last = solutionArray[solutionArray.length-1];
        
        //For every point, gets the next one and measure it's distance.
        for (int i = 0; i < solutionArray.length-1; i++) {
            int current = solutionArray[i];
            int next = solutionArray[i+1];
            
            distance += distances[current][next];
        }
        //Gets distance from last point to the first, finishing the path cicle.
        distance += distances[last][first];
        
        return distance;
    }
 
    /**
     * Uses the current {@link solution} to measure current path distance.
     * @param solutionArray Solution array to utilize as the path.
     * @return Total path distance.
     */
    public static double pathDistance(Solution[] solutionArray, Antenna[] antennas){
        //Total distance
        double distance = 0;
        //First point in the solution array.
        Point first = solutionArray[0].currentPoint;
        //Last point in the solution array.
        Point last = solutionArray[solutionArray.length-1].currentPoint;
        
        //For every point, gets the next one and measure it's distance.
        for (int i = 0; i < solutionArray.length-1; i++) {
            Point current = solutionArray[i].currentPoint;
            Point next = solutionArray[i+1].currentPoint;
            
            distance += getDistance(current, next);
        }
        //Gets distance from last point to the first, finishing the path cicle.
        distance += getDistance(last, first);
        
        return distance;
    }
 
    /**
     * Measure the distances between points before and after index q1 and p2 on the solution array.
     * @param q1 Index of point or antenna q1.
     * @param p2 Index of point or antenna p2.
     * @param solutionArray Solution array to get the coordinates from.
     * @param distances Distance from every point to another.
     * @return Distance from points before q1 and p2 and to points after q1 and p2, summed.
     */
    public static double measureDistanceFromArray(int q1, int p2, Solution[] solutionArray, double[][] distances){
        double distance = 0;
        
        int beforeQ1 = q1 == 0 ? solutionArray.length-1 : q1 - 1;
        int afterQ1 = q1 == solutionArray.length-1 ? 0 : q1 + 1;
        
        int beforeP2 = p2 == 0 ? solutionArray.length-1 : p2 - 1;
        int afterP2 = p2 == solutionArray.length-1 ? 0 : p2 + 1;
        
        int antennaBeforeQ1 = solutionArray[beforeQ1].antennaID;
        int antennaQ1 = solutionArray[q1].antennaID;
        int antennaAfterQ1 = solutionArray[afterQ1].antennaID;
        
        int antennaBeforeP2 = solutionArray[beforeP2].antennaID;
        int antennaP2 = solutionArray[p2].antennaID;
        int antennaAfterP2 = solutionArray[afterP2].antennaID;
        
        if(beforeQ1 == p2){
            distance += distances[antennaBeforeP2][antennaP2];
            distance += distances[antennaP2][antennaQ1];
            distance += distances[antennaQ1][antennaAfterQ1];
            return distance;
        }
        if(beforeP2 == q1){
            distance += distances[antennaBeforeQ1][antennaQ1];
            distance += distances[antennaQ1][antennaP2];
            distance += distances[antennaP2][antennaAfterP2];
            return distance;
        }
        
        distance += distances[antennaBeforeQ1][antennaQ1];
        distance += distances[antennaQ1][antennaAfterQ1];
        distance += distances[antennaBeforeP2][antennaP2];
        distance += distances[antennaP2][antennaAfterP2];
        
        return distance;
    }

    /**
     * Measure the distances between points before and after index q1 and p2 on the solution array.
     * @param q1 Index of point or antenna q1.
     * @param p2 Index of point or antenna p2.
     * @param solutionArray Solution array to get the coordinates from.
     * @return Distance from points before q1 and p2 and to points after q1 and p2, summed.
     */
    public static double measureDistance(int q1, int p2, Solution[] solutionArray){
        double distance = 0;
        
        int beforeQ1 = q1 == 0 ? solutionArray.length-1 : q1 - 1;
        int afterQ1 = q1 == solutionArray.length-1 ? 0 : q1 + 1;
        
        int beforeP2 = p2 == 0 ? solutionArray.length-1 : p2 - 1;
        int afterP2 = p2 == solutionArray.length-1 ? 0 : p2 + 1;
        
        if(beforeQ1 == p2){
            distance += getDistance(solutionArray[beforeP2].currentPoint, solutionArray[p2].currentPoint);
            distance += getDistance(solutionArray[p2].currentPoint, solutionArray[q1].currentPoint);
            distance += getDistance(solutionArray[q1].currentPoint, solutionArray[afterQ1].currentPoint);
            return distance;
        }
        if(beforeP2 == q1){
            distance += getDistance(solutionArray[beforeQ1].currentPoint, solutionArray[q1].currentPoint);
            distance += getDistance(solutionArray[q1].currentPoint, solutionArray[p2].currentPoint);
            distance += getDistance(solutionArray[p2].currentPoint, solutionArray[afterP2].currentPoint);
            return distance;
        }
        
        distance += getDistance(solutionArray[beforeQ1].currentPoint, solutionArray[q1].currentPoint);
        distance += getDistance(solutionArray[q1].currentPoint, solutionArray[afterQ1].currentPoint);
        distance += getDistance(solutionArray[beforeP2].currentPoint, solutionArray[p2].currentPoint);
        distance += getDistance(solutionArray[p2].currentPoint, solutionArray[afterP2].currentPoint);
        
        return distance;
    }
    
    /**
     * Measure distance between two objects: (Point or Antenna).
     * @param a Point 1
     * @param b Point 2
     * @return Distance between Objects.
     */
    public static double getDistance(Point a, Point b){
        return sqrt(pow(a.x - b.x, 2) + pow(a.y - b.y, 2));
    }
    
    /**
     * Makes the thread sleep for a certain amount of time in milliseconds.
     * @param time Time in milliseconds.
     */
    public static void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Logger.getLogger(Solve.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Receive the index of the points on the {@link solution} array to change.
     * Measure the distance from points to be changed and then returns the difference
     * between the new path and the old one.
     * @param q1 Index of the first point to be changed.
     * @param p2 Index of the second point to be changed.
     * @param solutionArray The array to change.
     * @param distances Cached distances from and to every point.
     * @return The difference between the new path distance and the old one.
     */
    public static double newPath(int q1, int p2, Solution[] solutionArray, double[][] distances){
        double oldPath = measureDistanceFromArray(q1, p2, solutionArray, distances);
        
        Solution aux = (Solution)solutionArray[q1].clone();
        solutionArray[q1] = (Solution)solutionArray[p2].clone();
        solutionArray[p2] = aux;
        
        double newPath = measureDistanceFromArray(q1, p2, solutionArray, distances);

        return newPath - oldPath;
    }
    
    /**
     * Receive the index of the points on the {@link solution} array to change.
     * Measure the distance from points to be changed and then returns the difference
     * between the new path and the old one.
     * @param q1 Index of the first point to be changed.
     * @param p2 Index of the second point to be changed.
     * @param solutionArray Solution array containing the antennas in order of path.
     * @return The difference between the new path distance and the old one.
     */
    public static double newPath(int q1, int p2, Solution[] solutionArray){
        double oldPath = measureDistance(q1, p2, solutionArray);
        
        Solution aux = (Solution)solutionArray[q1].clone();
        solutionArray[q1] = (Solution)solutionArray[p2].clone();
        solutionArray[p2] = aux;
        
        double newPath = measureDistance(q1, p2, solutionArray);

        return newPath - oldPath;
    }
    
    /**
     * Receive the index of the points on the {@link solution} array to change.Measure the distance from points to be changed and then returns the difference
 between the new path and the old one.
     * @param q1 Index of the first point to be changed.
     * @param p2 Index of the second point to be changed.
     * @param point1 Change path from any point to the point in array Point[] position point1 on antenna q1
     * @param point2 Change path from any point to the point in array Point[] position point1 on antenna p2
     * @param solutionArray Solution array containing the antennas in order of path.
     * @return The difference between the new path distance and the old one.
     */
    public static double newPath(int q1, int p2, int point1, int point2, Solution[] solutionArray, Antenna[] antenna){
        double oldPath = measureDistance(q1, p2, solutionArray);
        //System.out.println(String.format("Current points: %s - %s | %.2f", solutionArray[q1].antenna.currentPoint, solutionArray[p2].antenna.currentPoint, oldPath));
        
        solutionArray[q1].currentPoint = (Point)antenna[solutionArray[q1].antennaID].generatedPoints[point1].clone();
        solutionArray[p2].currentPoint = (Point)antenna[solutionArray[p2].antennaID].generatedPoints[point2].clone();

        Solution aux = (Solution)solutionArray[q1].clone();
        solutionArray[q1] = (Solution)solutionArray[p2].clone();
        solutionArray[p2] = aux;
        
        double newPath = measureDistance(q1, p2, solutionArray);
        //System.out.println(String.format("After points: %s - %s", solutionArray[q1].antenna.currentPoint, solutionArray[p2].antenna.currentPoint, newPath));

        return newPath - oldPath;
    }
    
    /**
     * Prints an array.
     * @param array
     * @return String with array printed.
     */
    public static String printArray(int[] array){
        String start = "[";
        for (int i = 0; i < array.length-1; i++) {
            start += array[i] + ", ";
        }
        start += array[array.length-1] + "]";
        
        return start;
    }
    
    /**
     * Finds minimum value in an array and returns the index from it.
     * @param array Array used to find the minimum value.
     * @return The minimum value inside the array.
     */
    public static int min(double[] array){
        double min = array[0];
        int index = 0;
        for (int i = 1; i < array.length; i++) {
            if(min > array[i]){
                min = array[i];
                index = i;
            }
        }
        return index;
    }
}
