package ai.algorithm;

import bizz.BizzFactory;
import ucc.GoalDTO;

/**
 * Created by jvdur on 04/06/2016.
 */
public class LARandomSearch extends LearnAlgorithm {


    private final static int MIN = 1;
    private final static int MAX = 200;

    private int bestCost;
    private GoalDTO bestGoals;
    private GoalDTO lastGoals;

    public LARandomSearch() {
        this.bestCost = Integer.MAX_VALUE;
    }

    @Override
    public GoalDTO getNextGoal() {
        int loadDiscoverPath = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        int loadGrabKey = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        int loadOpenDoor = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        int loadReachGoal = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        int loadAction = MIN + (int)(Math.random() * ((MAX - MIN) + 1));

        lastGoals = BizzFactory.INSTANCE.createGoal(loadDiscoverPath, loadGrabKey,
                loadOpenDoor, loadReachGoal, loadAction);

        return lastGoals;
    }

    @Override
    public void setNumberStepsNeeded(int nbSteps) {
        if (nbSteps < bestCost) {
            bestCost = nbSteps;
            bestGoals = lastGoals;
        }
    }

    @Override
    public GoalDTO getBestGoals() {
        return bestGoals;
    }

}
