/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drone_refactor;

import Solution.Solve;
import Objects.Antenna;
import Solution.Utilities;
import java.util.Random;

/**
 *
 * @author gabri
 */
public class Drone_Refactor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean solve = true;
        
        for (String arg : args) {
            if (arg.equals("-?")) {
                System.out.println("Entrou");
                solve = false;
            }
        }
        
        if(args.length == 0 || !solve){
            Options.NoOptions.apply("");
        }else{
            for (int i = 0; i < args.length-1; i+=2) {
                int sumLater = 0;
                for (Options option : Options.options) {
                    if(option.getOption().equals(args[i])){
                        sumLater += option.apply(args[i+1]);
                    }
                }
                i += sumLater;
            }
            
            String fileName = args[args.length-1];

            Read_File read = new Read_File(fileName, Options.pointsInsideAntennaRadius);
            
            Antenna[] antennas = new Antenna[Options.antennaQty];
            //System.setProperty("sun.java2d.opengl", "true");
            if(!Options.randomize)
                read.getAntennas().subList(0, Options.antennaQty > 1000 ? 1000 : Options.antennaQty).toArray(antennas);
            else{
                Random random;
                if(Options.seed >= 0)
                    random = new Random(Options.seed);
                else
                    random = new Random();
                
                int[] randomPoints = Utilities.getRandomNumbers(Options.antennaQty, 1000, random);
                
                for (int i = 0; i < randomPoints.length; i++) {
                    antennas[i] = read.getAntennas().get(randomPoints[i]);
                }
            }

            Solve solution = new Solve(antennas, Options.repetitions, Options.CORE_COUNT);
            long start = System.nanoTime();

            System.out.println("Solving...");
            solution.plot();
            solution.solve();
            long finish = System.nanoTime();
            System.out.println("Time: "+formatTime(start, finish));
        }
    }
    
    public static String formatTime(long start, long finish){
        long finalTime = finish-start;
        
        long milliseconds = (finalTime - ((finalTime)/1_000_000_000)*1_000_000_000)/100_000;
        long seconds = (finalTime)/1_000_000_000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        seconds %= 60;
        minutes %= 60;
        hours %= 24;
        
        return String.format("%02d days, %02d:%02d:%02d %04d ms", days, hours, minutes, seconds, milliseconds);
    }
    
}
