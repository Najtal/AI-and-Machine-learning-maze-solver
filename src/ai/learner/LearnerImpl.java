package ai.learner;

import ai.solver.Solver;
import ai.solver.SolverImpl;
import constant.RunningMode;
import constant.RunningStatus;
import model.GlobalLearningModel;
import model.MazeLearningModel;

/**
 * Created by jvdur on 13/05/2016.
 */
public class LearnerImpl extends LearnerRunner {


    private MazeLearningModel mlm;

    public LearnerImpl(GlobalLearningModel glm) {
        super(glm);
        mlm = new MazeLearningModel(glm);
    }

    @Override
    public void run() {

        // For the # of maze we have to solve
        for(int i=0; i<glm.getNbMaze(); i++) {

            // Create a new Model (withe a maze)
            mlm = new MazeLearningModel(glm);
            // Instanciate a solver
            Solver s = new SolverImpl(mlm.getMazeNinja());
            // Learn from maze
            learnMaze(mlm, s);

            // TODO Analyse results from MazeLearningModel

        }

    }

    private void learnMaze(MazeLearningModel mlm, Solver s) {

        while(glm.getrStatus() == RunningStatus.RUNNING
                && ( glm.getrMode() == RunningMode.FULL_SPEED
                || glm.getrMode() == RunningMode.MAZE_BY_MAZE
                || mlm.isGoForNextStep())) {

            // TODO : must call solver over here

            s.doOneStep();

            if (s.isSolved()) {
                return;
            }

        }

    }

    @Override
    public void pause() {
        // TODO
    }

    @Override
    public void start() {
        // TODO
    }

    @Override
    public void makeOneStep() {
        // TODO
    }

    @Override
    public void stop() {
        // TODO
    }

    public MazeLearningModel getMazeLearningModel() {
        return mlm;
    }
}
