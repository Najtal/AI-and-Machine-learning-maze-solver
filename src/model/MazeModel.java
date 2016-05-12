package model;

import bizz.BizzFactory;
import bizz.Maze;
import ucc.MazeUCC;

/**
 * Created by jvdur on 09/05/2016.
 */
public class MazeModel {

    // Model
    private Maze mazeNinja; // Know as the Ninja eveolves through the maze.
    private Maze mazeGod;   // Omniscient about the maze.

    // Action
    private MazeUCC mazeUcc;


    /**
     * private constructor
     */
    public MazeModel(Maze generatedMaze) {
        mazeUcc = MazeUCC.INSTANCE;

        mazeGod = generatedMaze;
        mazeNinja = BizzFactory.INSTANCE.createNinjaMaze(
                generatedMaze.getStartNode(),
                generatedMaze.getSizex(),
                generatedMaze.getSizey());
    }


    public void startNinja() {

    }

}
