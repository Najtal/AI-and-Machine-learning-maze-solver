package ai.solver;

import ucc.MazeDTO;

/**
 * Created by jvdur on 13/05/2016.
 */
public class SolverImpl implements Solver {

    /**
     *
     * @param maze
     */
    public SolverImpl(MazeDTO maze) {

        /**
         * The MazeDTO received only contains the
         *          maze.getStartNode()
         *          maze.getSizex()
         *          maze.getSizey()
         *
         *          Important, must update know position in mazeStructure !
         *          maze.mazeStructure[npX][npY] = 1;
         */

    }

    @Override
    public boolean isSolved() {
        return false;
    }

    @Override
    public void doOneStep() {
    }
}
