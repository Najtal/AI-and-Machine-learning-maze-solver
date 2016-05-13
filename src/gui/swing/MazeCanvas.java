package gui.swing;

import ucc.MazeDTO;
import ucc.NodeDTO;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jvdur on 13/05/2016.
 */
public class MazeCanvas  extends JPanel {

    private MazeDTO maze;
    private MazeFrame frame;

    public MazeCanvas(MazeDTO maze, MazeFrame frame) {
        this.maze = maze;
        this.frame = frame;
    }

    public Dimension getPreferredSize() {
        return new Dimension(frame.getWidth(), frame.getHeight());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // compute box size
        int boxWidth = frame.getContentPane().getWidth()/maze.getSizex();
        int boxHeight = frame.getContentPane().getHeight()/maze.getSizey();

        // Draw the lines
        for(int i=0; i<maze.getSizex(); i++){
            for(int j=0; j<maze.getSizey(); j++) {

                int val = maze.getMazeStructure()[j][i];
                // Draw left line
                if (val < 8 && j!=0) {
                    g.drawLine(j*boxWidth, i*boxHeight, j*boxWidth, ((i+1)*boxHeight));
                }
                // Draw top neighbour
                if ((val%2) == 0 && i!=0) {
                    g.drawLine(j*boxWidth, i*boxHeight, (j+1)*boxWidth, i*boxHeight);
                }

            }
        }

        // Draw the doors
        for (NodeDTO door : maze.getDoorPosition().values()) {
            int nbDoor = door.getIsDoor();

            g.setColor(Color.BLUE);
            g.fillRect(door.getPosx()*boxWidth, door.getPosy()*boxHeight,
                    (door.getPosx()+1)*boxWidth, (door.getPosy()+1)*boxHeight);
            //g.drawChars(new char[]{(char) nbDoor}, door.getPosx()*boxWidth+boxWidth/3, door.getPosy()*boxHeight+boxWidth/3,
            //        (door.getPosx()+1)*boxWidth+boxWidth/3, (door.getPosy()+1)*boxHeight+boxWidth/3);
        }


        // Draw the Keys

    }
}