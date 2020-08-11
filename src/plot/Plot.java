/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plot;

import Objects.Antenna;
import Objects.Solution;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.*;

/**
 *
 * @author a16028
 */
public class Plot extends JPanel {

    //private final int verticeInicial;
    private final Antenna[] Antennas;
    private final Solution[][] solutions;
    private final double[] totalDistance;


    public Plot(Antenna[] antennas, Solution[][] solutions, double[] totalDistance) {
        setOpaque(true);
        setBackground(Color.WHITE);
        this.Antennas = antennas;
        this.solutions = solutions;
        this.totalDistance = totalDistance;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        //g2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        //g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        
        final double tamX = (double)super.getWidth() / 10000;
        final double tamY = (double)super.getHeight() / 10000;
        
        for (int i = 0; i < solutions.length; i++) {
            Colors.byInteger(i % Colors.COLORS.length).setColor(g2D);
            g2D.drawString(String.format("Thread %d - Total distance: %.2f", i, totalDistance[i]), (float)tamX+5, (float)tamY+(20*(i+1)));
        }
        
        
        for (Antenna Antenna : Antennas) {
            final String coordenada = String.format("%d", Antenna.ID /*"[%.2f, %.2f]", ponto[0], ponto[1]*/ );
            final double x = Antenna.origin.x * tamX;
            final double y = Antenna.origin.y * tamY;
            final double radiusX = Antenna.radius * tamX;
            final double radiusY = Antenna.radius * tamY;
            g2D.setColor(Color.BLACK);
            Ellipse2D.Double shape = new Ellipse2D.Double(x - tamX*20, y - tamY*20, tamX*40, tamY*40);
            g2D.draw(shape);
            final Ellipse2D.Double raio = new Ellipse2D.Double(x - radiusX, y - radiusY, radiusX * 2, radiusY * 2);
            g2D.draw(raio);
            g2D.setColor(Color.RED);
            g2D.drawString(coordenada, (float) (x + 10), (float) (y - 10));
        }
        
        //g2D.setColor(Color.BLUE);
        
        int changeColor = 0;
        for (Solution[] solution : solutions) {
            Colors.byInteger(changeColor++).setColor(g2D);
            for (int i = 0; i < solution.length-1; i++) {
                drawLine(solution, g2D, tamX, tamY, i, i+1);
            }
            drawLine(solution, g2D, tamX, tamY, solution.length-1, 0);
            changeColor %= Colors.COLORS.length;
        }
    }
    
    /**
     * Draw line function made just for removing repetition of code after the loop printing lines.
     * @param solution Solution to print the lines.
     * @param g2D Frame to be printed.
     * @param tamX Size of the frame on the X axis.
     * @param tamY Size of the frame on the Y axis.
     * @param index Index of solution to be printed.
     * @param indexNext {@code index}+1.
     */
    private void drawLine(Solution[] solution, Graphics2D g2D, double tamX, double tamY, int index, int indexNext){
        final double x1 = solution[index].currentPoint.x * tamX;
        final double y1 = solution[index].currentPoint.y * tamY;
        final double x2 = solution[indexNext].currentPoint.x * tamX;
        final double y2 = solution[indexNext].currentPoint.y * tamY;
        final Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);
        g2D.draw(line);
    }
}
