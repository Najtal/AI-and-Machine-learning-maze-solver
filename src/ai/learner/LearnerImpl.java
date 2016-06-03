package ai.learner;

import ai.solver.Solver;
import ai.solver.SolverImpl;
import bizz.BizzFactory;
import constant.RunningMode;
import constant.RunningStatus;
import model.GlobalLearningModel;
import model.MazeLearningModel;
import ucc.GoalDTO;

/**
 * Created by jvdur on 13/05/2016.
 */
public class LearnerImpl extends LearnerRunner {


    private MazeLearningModel mlm;

    public LearnerImpl(GlobalLearningModel glm) {
        super(glm);
    }

    @Override
    public void run() {

        // For the # of maze we have to solve
        for(int i=0; i<glm.getNbMaze(); i++) {

            // Create a new Model (with a maze)
            mlm = new MazeLearningModel(glm);

            // Get the goals
            GoalDTO goals = getNextGoalLoad();

            // Instanciate a solver
            //Solver s = new SolverImpl(mlm.getMazeNinja(), goals);
            Solver s = new SolverImpl(mlm.getMazeOmniscient(), goals);

            // Learn from maze
            learnMaze(mlm, s);

            // TODO Analyse results from MazeLearningModel

        }

    }

    private void learnMaze(MazeLearningModel mlm, Solver s) {

        while (!glm.isDone()) {
            while(glm.getrStatus() == RunningStatus.RUNNING
                    && (glm.getrMode() != RunningMode.STEP_BY_STEP_MANUAL
                        || mlm.isGoForNextStep())) {


                if (glm.getrMode() == RunningMode.STEP_BY_STEP_AUTO) {
                    try {
                        Thread.sleep(350);
                        //Thread.sleep(glm.getAutoRunningSpeed());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.print("Next step: ");
                try {
                    s.doOneStep();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (s.isSolved()) {
                    glm.setDone(true);
                }

            }
        }


    }

    private GoalDTO getNextGoalLoad() {

        // TODO get the next try of goal load to optimize. Data can be reached in MLM

        return BizzFactory.INSTANCE.createGoal(10, 20, 30, 400, 1);
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
