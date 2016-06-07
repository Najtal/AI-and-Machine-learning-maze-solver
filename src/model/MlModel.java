package model;

import ucc.GoalDTO;

import java.util.ArrayList;

/**
 * Created by jvdur on 07/06/2016.
 */
public class MlModel {


    private ArrayList<MLRunPSave> allRuns;
    private int maxNbSteps;

    public MlModel() {
        this.allRuns = new ArrayList();
        this.maxNbSteps = 0;
    }


    public void addRun(GoalDTO goals, int steps) {
        maxNbSteps = Math.max(steps, maxNbSteps);
        allRuns.add(new MLRunPSave(goals, steps, allRuns.size()));
    }

    public int getNbRuns() {
        return allRuns.size();
    }

    public GoalDTO getRunGoals(int i) {
        return allRuns.get(i).goals;
    }

    public int getRunSteps(int i) {
        return allRuns.get(i).steps;
    }

    public int getMaxNbSteps() {
        return maxNbSteps;
    }

    class MLRunPSave {

        private GoalDTO goals;
        private int steps;
        private int id;

        public MLRunPSave(GoalDTO goals, int steps, int id) {
            this.goals = goals;
            this.steps = steps;
        }
    }

}
