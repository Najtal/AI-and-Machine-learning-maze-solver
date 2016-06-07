package ai.algorithm;

import bizz.BizzFactory;
import ucc.GoalDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marion on 07/06/16.
 */
public class LABayes extends LearnAlgorithm {

    private GoalDTO startGoals;
    private final static int MIN = 1;
    private final static int MAX = 5;

    private int bestCost;
    private GoalDTO bestGoals;

    private List<GoalDTO> allGoals;
    private int currentGoal;

    public LABayes(){

    }

    @Override
    public GoalDTO getNextGoal() {
        currentGoal++;
        return allGoals.get(currentGoal);
    }

    @Override
    public void setNumberStepsNeeded(int nbSteps) {
        if (nbSteps < bestCost) {
            bestCost = nbSteps;
            bestGoals = allGoals.get(currentGoal);
        }
    }

    @Override
    public GoalDTO getBestGoals() {
        return bestGoals;
    }

}
