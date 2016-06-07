package ai.learner;

import ai.solver.Solver;
import ai.solver.SolverImpl;
import constant.RunningMode;
import constant.RunningStatus;
import model.GlobalLearningModel;
import model.MazeLearningModel;
import ucc.GoalDTO;
import ucc.GoalUCC;
import ucc.MazeDTO;
import ucc.MazeUCC;
import util.Log;

/**
 * Created by jvdur on 13/05/2016.
 */
public class Learner implements Runnable, LearnerInt {

    private static GlobalLearningModel glm;
    private MazeLearningModel mlm;

    public Learner(GlobalLearningModel glmv) {
        glm = glmv;
    }

    /**
     * Run the Learner. Will LEARN_MAZE for the amount of mazes defined
     */
    @Override
    public void run() {

        // For the # of maze we have to solve
        for(int i=0; i<glm.getNbMaze(); i++) {

            // Create a new Model (with a maze)
            this.mlm = new MazeLearningModel(glm);

            // learn maze
            learnMaze();
            System.out.println("Best number of step: " + mlm.getBestSteps());
            System.out.println("Goals are : " + mlm.getBestGoals().toString());
        }
    }

    public MazeLearningModel getMazeLearningModel() {
        return mlm;
    }


    /**
     * Will execute and solve the maze the amount of time defined
     */
    private void learnMaze(){

        // For the # of time to execute the maze
        for(int i=0; i<glm.getNbIterationPerMaze() ; i++) {

            // Get the goals
            GoalDTO goals = GoalUCC.INSTANCE.clone(mlm.getNextGoalLoad());

            // Instantiate a solver
            // Solver s = new SolverImpl(mlm.getMazeNinja(), goals);
            // TODO : The solver should take a ninja maze and not a ominshient maze

            MazeDTO maze;
            maze = mlm.getMazeOmniscient();
            mlm.setMazeNinja(maze);
            MazeUCC.INSTANCE.clean(maze);

            Solver s = new SolverImpl(maze, goals);

            // Learn from maze
            int nbStepsNeeded = executeMaze(s);
            glm.getRuns().addRun(goals, nbStepsNeeded);

            //System.out.println("Maze " + i + ": GOALS: "+goals.toString()+" STEPS: "+nbStepsNeeded);

            mlm.setNewBestMvmt(nbStepsNeeded, goals);
        }


    }

    private int executeMaze(Solver s) {

        int nbSteps = 0;
        System.out.print("Maze execution:");

        while(glm.getrStatus() == RunningStatus.RUNNING) {

            if (glm.getrMode() == RunningMode.STEP_BY_STEP_AUTO) {
                try {
                    Thread.sleep(glm.getAutoRunningSpeed());
                } catch (InterruptedException e) {
                    Log.logWarning("Maze execution core could not SLEEP in STEP_BY_STEP_AUTO");
                }
            }

            try {
                s.doOneStep();
                nbSteps ++;
                System.out.print('.');
            } catch (Exception e) {
                Log.logSevere("Error while executing maze solver next step : " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }

            if (glm.getNinjaFrame() != null) {
                glm.getNinjaFrame().actionPerformed(null);
            }

            if (s.isSolved()) {
                System.out.println();
                Log.logFine("Maze solved");
                return nbSteps;
            }

            glm.setSolverProcessingState(false);

        }
        return 0;
    }

}

