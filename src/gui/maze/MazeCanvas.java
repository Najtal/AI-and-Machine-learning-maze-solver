package gui.maze;

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
    private boolean isOmniscient;

    public MazeCanvas(MazeDTO maze, MazeFrame frame, boolean isOmniscient) {
        this.maze = maze;
        this.frame = frame;
        this.isOmniscient = isOmniscient;
    }

    public Dimension getPreferredSize() {
        return new Dimension(frame.getWidth(), frame.getHeight());
    }

    public void setMaze(MazeDTO maze) {
        this.maze = maze;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // compute box size
        int boxWidth = frame.getContentPane().getWidth()/maze.getSizex();
        int boxHeight = frame.getContentPane().getHeight()/maze.getSizey();

        if(!isOmniscient) {
            frame.setTitle("Has key: " + maze.getSolverCarriedKey());
        }

            g.setColor(Color.BLACK);

        // Draw the lines
        for(int i=0; i<maze.getSizex(); i++){
            for(int j=0; j<maze.getSizey(); j++) {

               /* if(!isOmniscient && maze.getMazeStructure()[j][i] == 0) {
                    if (j != maze.getStartNode().getPosy() || i != maze.getStartNode().getPosx()) {
                        g.fillRect(j * boxWidth, i * boxHeight, boxWidth, boxHeight);
                    }
                } else {*/

                    int val = maze.getMazeStructure()[j][i];
                    // Draw left line
                    if (val < 8 && j!=0) {
                        g.drawLine(j*boxWidth, i*boxHeight, j*boxWidth, ((i+1)*boxHeight));
                    }
                    // Draw top neighbour
                    if ((val%2) == 0 && i!=0) {
                        g.drawLine(j*boxWidth, i*boxHeight, (j+1)*boxWidth, i*boxHeight);
                    }

                //}
            }
        }

        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));

        // Draw the doors
        for (NodeDTO door : maze.getDoorPosition().values()) {
            String nbDoor = door.getIsDoor()+"";

            g.setColor(Color.BLUE);
            g.fillOval(door.getPosy()*boxWidth, door.getPosx()*boxHeight,boxWidth, boxHeight);

            g.setColor(Color.WHITE);
            g.drawString(nbDoor, (int) door.getPosy()*boxWidth+45, (int) door.getPosx()*boxHeight+45);

        }

        // Draw the keys
        if(isOmniscient) {
            for (NodeDTO key : maze.getKeyPosition().values()) {
                String nbDoor = key.getHasKey()+"";

                g.setColor(Color.GREEN);
                g.fillOval(key.getPosy()*boxWidth, key.getPosx()*boxHeight,boxWidth, boxHeight);

                g.setColor(Color.WHITE);
                g.drawString(nbDoor, (int) key.getPosy()*boxWidth+45, (int) key.getPosx()*boxHeight+45);
            }
        } else {
            for (NodeDTO key : maze.getSolverkeys()) {
                if(key == null)
                    continue;
                String nbDoor = key.getHasKey()+"";

                g.setColor(Color.GREEN);
                g.fillOval(key.getPosy()*boxWidth, key.getPosx()*boxHeight,boxWidth, boxHeight);

                g.setColor(Color.WHITE);
                g.drawString(nbDoor, (int) key.getPosy()*boxWidth+45, (int) key.getPosx()*boxHeight+45);
            }
        }

        // Draw Position
        if(isOmniscient) {
            g.setColor(Color.RED);
            g.fillOval(maze.getStartNode().getPosy()*boxWidth, maze.getStartNode().getPosx()*boxHeight,boxWidth, boxHeight);
        } else {
            g.setColor(Color.RED);
            g.fillOval(maze.getSolverPosition().getPosy()*boxWidth, maze.getSolverPosition().getPosx()*boxHeight,boxWidth, boxHeight);
        }

        // Draw the Goal
        if(maze.getGoalNode() != null) {
            g.setColor(Color.BLACK);
            g.fillOval(maze.getGoalNode().getPosy()*boxWidth, maze.getGoalNode().getPosx()*boxHeight,boxWidth, boxHeight);
        }

    }
}