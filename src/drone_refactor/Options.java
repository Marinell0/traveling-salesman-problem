/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drone_refactor;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enumeration class for the options received in the commandUse line arguments (args) from the program.
 * @author Gabriel Marinello Moura Leite
 */
public enum Options {
    NoOptions(){
        @Override
        public String toString() {
            String manual = "\nUsage: java -jar 'file'.jar ";
            String everything = "Options:\n";
            for (int i = 1; i < options.length; i++) {
                manual += options[i].commandUse() + " ";
                everything += options[i].toString();
            }
            return manual + "file_name\n" + everything;
        }

        @Override
        public String commandUse() {
            return String.format("", super.option);
        }

        @Override
        public int apply(String next) {
            logger.log(Level.SEVERE, next+this.toString());
            //throw new UnsupportedOperationException(next+this.toString()); //To change body of generated methods, choose Tools | Templates.
            return 0;
        }
        
    },NumAntennas("-a") {
        @Override
        public String toString() {
            return String.format("\t%s antenna-quantity\tSpecifies how many antennas to look for minimum path.\n\t\t\t\t"
                                                        + "Default is 30.\n", super.option);
        }

        @Override
        public String commandUse() {
            return String.format("[%s antenna-quantity]", super.option);
        }

        @Override
        public int apply(String next) {
            try{
                antennaQty = Integer.parseInt(next);
            }catch(NumberFormatException ex){
                Options.NoOptions.apply("-q needs an int input next to it.\n" + next);
                throw new UnsupportedOperationException();
            }
            return 0;
        }
    },ShuffleAntennas("-s"){
        @Override
        public String toString() {
            return String.format("\t%s [seed]\t\tShuffle the antennas used instead of taking the same antennas\n\t\t\t\t"
                                                + "on order from the file. If seed is present, uses the seed as the random factor.\n", super.option);
        }

        @Override
        public String commandUse() {
            return String.format("[%s [seed]]", super.option);
        }

        @Override
        public int apply(String next) {
            randomize = true;
            try{
                seed = Integer.parseInt(next);
            }catch(NumberFormatException ex){
                return -1;
            }
            return 0;
        }
    },Repetitions("-r") {
        @Override
        public String toString() {
            return String.format("\t%s repetitions\t\tNumber of times to do a Hillclimb with removed intersections.\n\t\t\t\t"
                                                    + "Default is 10 repetitions.\n", super.option);
        }

        @Override
        public String commandUse() {
            return String.format("[%s repetitions]", super.option);
        }

        @Override
        public int apply(String next) {
            try{
                repetitions = Integer.parseInt(next);
                if(repetitions <= 0){
                    Options.NoOptions.apply("-r parameter needs to be > 0.\n");
                    throw new UnsupportedOperationException();
                }
            }catch(NumberFormatException ex){
                Options.NoOptions.apply("-r needs an int input next to it.\n");
                throw new UnsupportedOperationException();
            }
            return 0;
        }
    },NumPointsInsideRadius("-n"){
        @Override
        public String toString() {
            return String.format("\t%s num-points\t\tNumber of points > 1 inside antenna radius to fit the path.\n\t\t\t\t"
                                                + "If not present, fit the path only to the antenna origin.\n\t\t\t\t"
                                                + "Default is 10.\n", super.option);
        }

        @Override
        public String commandUse() {
            return String.format("[%s num_points]", super.option);
        }

        @Override
        public int apply(String next) {
            try{
                pointsInsideAntennaRadius = Integer.parseInt(next);
                if(pointsInsideAntennaRadius <= 0){
                    Options.NoOptions.apply("-n parameter needs to be > 0.\n");
                    throw new UnsupportedOperationException();
                }
            }catch(NumberFormatException ex){
                Options.NoOptions.apply("-n needs an int input next to it.\n");
                throw new UnsupportedOperationException();
            }
            return 0;
        }
    },HillClimbRandomPoint("-h"){
        @Override
        public String toString() {
            return String.format("\t%s\t\t\tIf present, uses hillclimb looking for random points inside the radius.\n\t\t\t\t"
                                        + "Can only be used if -r is present.\n", super.option);
        }

        @Override
        public String commandUse() {
            return String.format("[%s]", super.option);
        }

        @Override
        public int apply(String next) {
            hillClimbAllPoints = true;
            return -1;
        }
        
    },NumberOfCores("-c") {
        @Override
        public String toString() {
            return String.format("\t%s count\t\tNumber of cores to use.\n\t\t\t\t"
                                            + "Default is total cores of your system - 2.\n", super.option);
        }

        @Override
        public String commandUse() {
            return String.format("[%s count]", super.option);
        }

        @Override
        public int apply(String next) {
            try{
                CORE_COUNT = Integer.parseInt(next);
                if(CORE_COUNT <= 0){
                    Options.NoOptions.apply("-c parameter needs to be > 0.\n");
                    throw new UnsupportedOperationException();
                }
            }catch(NumberFormatException ex){
                Options.NoOptions.apply("-c needs an int input next to it.\n");
                throw new UnsupportedOperationException();
            }
            return 0;
        }
    };
    
    private final static Logger logger = Logger.getLogger(Options.class.getName());
    
    public static int antennaQty = 30;
    public static int pointsInsideAntennaRadius = 0;
    public static boolean randomize = false;
    public static int seed = -1;
    public static int repetitions = 10;
    public static int CORE_COUNT = Runtime.getRuntime().availableProcessors()-2;
    public static boolean hillClimbAllPoints = false;
    public static String filename;
    
    public static final Options[] options = Options.values();
    
    private String option = "";
    
    private Options(){}
    
    private Options(String option){
        this.option = option;
    }
    
    public String getOption(){
        return option;
    }
    
    @Override
    public abstract String toString();
    
    public abstract String commandUse();
    
    public abstract int apply(String next);
}
