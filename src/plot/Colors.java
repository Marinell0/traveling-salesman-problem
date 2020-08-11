/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plot;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Sets colors from {@link Graphics2D} by enumerating them.
 * @author Gabriel Marinello Moura Leite
 */
public enum Colors {
    /**
     * Set color to Blue using {@link setColor}
     */
    BLUE("BLUE") {
        @Override
        public void setColor(Graphics2D g2D) {
            g2D.setColor(Color.BLUE);
        }
    }, CYAN("CYAN") {
        @Override
        public void setColor(Graphics2D g2D) {
            g2D.setColor(Color.CYAN);
        }
    }, DARK_GRAY("DARK_GRAY"){
        @Override
        public void setColor(Graphics2D g2D) {
            g2D.setColor(Color.DARK_GRAY);
        }
    }, GRAY("GRAY"){
        @Override
        public void setColor(Graphics2D g2D) {
            g2D.setColor(Color.GRAY);
        }
    }, GREEN("GREEN") {
        @Override
        public void setColor(Graphics2D g2D) {
            g2D.setColor(Color.GREEN);
        }
    }, LIGHT_GRAY("LIGHT_GRAY"){
        @Override
        public void setColor(Graphics2D g2D) {
            g2D.setColor(Color.LIGHT_GRAY);
        }
    }, MAGENTA("MAGENTA") {
        @Override
        public void setColor(Graphics2D g2D) {
            g2D.setColor(Color.MAGENTA);
        }
    }, ORANGE("ORANGE") {
        @Override
        public void setColor(Graphics2D g2D) {
            g2D.setColor(Color.ORANGE);
        }
    }, PINK("PINK") {
        @Override
        public void setColor(Graphics2D g2D) {
            g2D.setColor(Color.PINK);
        }
    }, RED("RED") {
        @Override
        public void setColor(Graphics2D g2D) {
            g2D.setColor(Color.RED);
        }
    }, YELLOW("YELLOW") {
        @Override
        public void setColor(Graphics2D g2D) {
            g2D.setColor(Color.YELLOW);
        }
    };
    
    /**
    * Sets the color provided by the enumeration.
     * @param g2D The current {@link Graphics2D} utilized.
    */
    public abstract void setColor(Graphics2D g2D);
    /**
     * Color from enumeration as {@link String}.
     */
    public final String color;
        
    /**
     * Array of colors set at compile time.
     */
    public static final Colors[] COLORS = Colors.values();
    
    /**
     * Get the color by using an int.
     * @param i Index of the color.
     * @return The color desired.
     */
    public static Colors byInteger(int i){
        return COLORS[i];
    }
    
    /**
     * Private constructor to get the value of the enumeration as {@link String}
     * @param color {@link String} value of {@link color}
     */
    private Colors(String color){
        this.color = color;
    }
    
    /**
     * Converts the enumeration to {@link String}
     * @return Color in {@link String}
     */
    @Override
    public String toString(){
        return color;
    }
}
