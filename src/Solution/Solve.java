/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Solution;

import Objects.Antenna;
import Objects.Point;
import Objects.Solution;
import static Solution.Hillclimb.hillClimb;
import static Solution.Hillclimb.hillClimbAllPoints;
import static Solution.PullPoints.pull;
import static Solution.RemoveCrosspath.removeIntersections;
import static Solution.Utilities.*;
import drone_refactor.Options;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import plot.Plot;

/**
 *
 * @author gabri
 */
public class Solve {
    private final int REPETITIONS;
    /**
     * Number of threads to use in the solution.
     */
    private static int CORE_COUNT;
    /**
     * Fixed thread pool with the same size as {@link CORE_COUNT}.
     */
    ThreadPoolExecutor threads;
    /**
     * Every antenna to get the solution
     */
    private final Antenna[] ANTENNAS;
    /**
     * Solution in order. Index 0 is linked to Index 1, 1 to 2 ... {@link solution#length}-1 to 0.
     */
    private final Solution[][] solution;
    /**
     * Cached distance between antenna origins. It is not used on the {@link Hillclimb#hillClimbAllPoints}
     */
    private final double[][] DISTANCE_ORIGIN;
    /**
     * Path distance from current solution.
     */
    public static double[] totalDistance;
    /**
     * All the random variables will come from this {@link Random}.
     */
    private static Random[] RANDOM;
    /**
     * Global frame variable used on functions to repaint the frame.
     */
    public static JFrame frame;
    
    /**
     * Constructor for the Solve class.It'll receive an array of antennas and the number of repetitions to be used in the {@link SolveWorker}
     * @param antennas Antenna array to be used in the solution
     * @param repetitions Number of time to do a HillClimb and a removeIntersection.
     * @param CORE_COUNT Number of cores to be used.
     */
    public Solve(Antenna[] antennas, int repetitions, int CORE_COUNT) {
        this.ANTENNAS = antennas;
        this.REPETITIONS = repetitions;
        this.CORE_COUNT = CORE_COUNT;
        
        solution = new Solution[CORE_COUNT][];
        totalDistance = new double[CORE_COUNT];
        RANDOM = new Random[CORE_COUNT];
        threads = (ThreadPoolExecutor) Executors.newFixedThreadPool(CORE_COUNT);
        
        DISTANCE_ORIGIN = new double[ANTENNAS.length][ANTENNAS.length];
        
        for (int i = 0; i < DISTANCE_ORIGIN.length; i++) {
            DISTANCE_ORIGIN[i][i] = Double.MAX_VALUE;
            for (int j = i+1; j < DISTANCE_ORIGIN[i].length; j++) {
                DISTANCE_ORIGIN[i][j] = DISTANCE_ORIGIN[j][i] = getDistance(ANTENNAS[i].origin, ANTENNAS[j].origin);
            }
        }
        
        for (int i = 0; i < CORE_COUNT; i++) {
            RANDOM[i] = new Random(i+122);
            this.solution[i] = new Solution[antennas.length];
        }
        
    }
    
    /**
     * Calls {@link SolveWorker} as threads to solve the problem. It will call {@link CORE_COUNT} number of threads.
     */
    public void solve(){
        CountDownLatch latch = new CountDownLatch(CORE_COUNT);
        
        for (int i = 0; i < CORE_COUNT; i++) {
            threads.execute(new SolveWorker(i, this.REPETITIONS, latch));
        }
        
        try{
            latch.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(Solve.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //this.totalDistance = pathDistance();
        System.out.println("Final distance: " + totalDistance[min(totalDistance)]);
        frame.repaint();
        plotEveryThread();
    }
    
    /**
     * Class created to be called as threads in the {@link solve} function.
     */
    private class SolveWorker implements Runnable{
        final int threadID;
        final int repetitions;
        final CountDownLatch latch;

        public SolveWorker(int threadID, int repetitions, CountDownLatch latch) {
            this.threadID = threadID;
            this.repetitions = repetitions;
            this.latch = latch;
        }

        @Override
        public void run() {
            //Initial solution
            for (int i = 0; i < solution[threadID].length; i++) {
                solution[threadID][i] = new Solution(i, (Point)ANTENNAS[i].origin.clone());
            }
            
            totalDistance[threadID] = pathDistance(solution[threadID], ANTENNAS);
            
            System.out.println("Thread "+threadID+" - Initial distance: "+totalDistance[threadID]);
            
            frame.repaint();
            for (int i = 0; i < this.repetitions; i++) {
                long start = System.nanoTime();
                if(!Options.hillClimbAllPoints && Options.pointsInsideAntennaRadius == 0)
                    totalDistance[threadID] += hillClimb(10000, solution[threadID].length*5, solution[threadID], DISTANCE_ORIGIN, RANDOM[threadID]);
                else{
                    totalDistance[threadID] += hillClimb(10000, solution[threadID].length*5, solution[threadID], RANDOM[threadID]);
                    totalDistance[threadID] += hillClimbAllPoints(10000, solution[threadID].length*5, solution[threadID], ANTENNAS, RANDOM[threadID]);
                }
                
                totalDistance[threadID] += removeIntersections(solution[threadID]);
                long end = System.nanoTime();
                System.out.println(i + " - Distance thread " +threadID+ ": " +totalDistance[threadID] + " " + (end-start)/1000000 + " ms");
                frame.repaint();
            }
            if(Options.pointsInsideAntennaRadius > 0)
                totalDistance[threadID] += pull(solution[threadID], ANTENNAS, 1000, RANDOM[threadID]);
            System.out.println(String.format("Thread %d finished...", threadID));
            latch.countDown();
        }
    }
    
    /**
     * Creates the plot frame and gives pointer of {@link ANTENNAS} and {@link solution} to {@link Plot} class.
     */
    public void plot(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("Plot dos pontos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(800, 800);
        Plot plot = new Plot(ANTENNAS, solution, totalDistance);
        frame.add(plot);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /**
     * Plots every thread in different frames.
     */
    public void plotEveryThread(){
        for (int i = 0; i < CORE_COUNT; i++) {
            JFrame.setDefaultLookAndFeelDecorated(true);
            frame = new JFrame("Plot dos pontos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setSize(400, 400);

            int indexMin = i;//min(totalDistance);

            Solution[][] solutionAux = new Solution[1][];
            solutionAux[0] = solution[indexMin];

            double[] totalDistanceAux = new double[1];
            totalDistanceAux[0] = totalDistance[indexMin];

            Plot plot = new Plot(ANTENNAS, solutionAux, totalDistanceAux);
            frame.add(plot);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}
