package ai.solver;

import ucc.MazeDTO;

/**
 * Created by jvdur on 13/05/2016.
 */
public class MSSimpleSolver implements Solver {


    /**
     *
     * @param maze
     */
    public MSSimpleSolver(MazeDTO maze) {

        /**
         * The MazeDTO received only contains the
         *          maze.getStartNode()
         *          maze.getSizex()
         *          maze.getSizey()
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
