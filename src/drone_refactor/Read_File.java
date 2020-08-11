/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drone_refactor;

import Objects.Antenna;
import Objects.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Read the {@link file} and get coordinates and radius to populate {@link antennas}.
 * 
 * @author Gabriel Marinello
 **/
public class Read_File {
    /**
     * Path of the file containing the antenna parameters.
     */
    private final String file;
    /**
     * 
     */
    private String header;
    private int pointsInsideAntennaRadius;
    private final LinkedList<Antenna> antennas = new LinkedList<Antenna>();

    public Read_File(String file, int pointsInsideAntennaRadius) {
        this.file = file;
        this.pointsInsideAntennaRadius = pointsInsideAntennaRadius;
        this.read();
    }
    
    
    /*
    @deprecated
    public void read(){
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            header = br.readLine();
            
            br.lines().forEachOrdered((String s) -> {
                String[] each = s.split(",");
                double x = Double.parseDouble(each[0]);
                double y = Double.parseDouble(each[1]);
                double radius = Double.parseDouble(each[2])*100;
                antennas.add(new Antenna(x, y, radius));
            });
            
            //lineCount = (int)allLines.count()-1;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Read_File.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Read_File.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    */
    
    public void read(){
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            header = br.readLine();
            
            int countAntennas = 0;

            Random random = new Random(1);
            
            String line;
            
            while((line = br.readLine()) != null){
                String[] each = line.split(",");
                double x = Double.parseDouble(each[0]);
                double y = Double.parseDouble(each[1]);
                double radius = Double.parseDouble(each[2])*100;
                
                Antenna antenna = new Antenna(countAntennas++, new Point(x, y), radius);
                if(pointsInsideAntennaRadius > 0)
                    antenna.generatePointsInRadius(pointsInsideAntennaRadius, random);
                
                antennas.add(antenna);
            }
            
            //lineCount = (int)allLines.count()-1;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Read_File.class.getName()).log(Level.SEVERE, "File not found.", ex);
        } catch (IOException ex) {
            Logger.getLogger(Read_File.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    


    public String getCabecalho() {
        return header;
    }

    public int getNumAntennas() {
        return antennas.size();
    }

    public LinkedList<Antenna> getAntennas() {
        return antennas;
    }
    
    
    
}
