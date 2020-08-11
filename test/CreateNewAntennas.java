
import java.io.FileWriter;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Creates a new antenna file with randomized antennas and randomized radius.
 * @author Gabriel Marinello Moura Leite
 */
public class CreateNewAntennas {
  public static void main(String[] args) {
    try {
        
        int num_antennas = 100000;
        
        String filename = "solution_"+num_antennas+".csv";
        FileWriter myWriter = new FileWriter("solution_.txt");
        myWriter.write("Files in Java might be tricky, but it is fun enough!");
        myWriter.close();
        System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}
