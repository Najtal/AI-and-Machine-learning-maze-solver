package ai.algorithm;

import bizz.BizzFactory;
import ucc.GoalDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jvdur on 04/06/2016.
 */
public class LAGridSearch extends LearnAlgorithm {

    private GoalDTO startGoals;
    private final static int MIN = 1;
    private final static int MAX = 5;

    private int bestCost;
    private GoalDTO bestGoals;

    private List<GoalDTO> allGoals;
    private int currentGoal;

    public LAGridSearch(){
        this.bestCost = Integer.MAX_VALUE;
        this.currentGoal = 0;
        this.allGoals = new ArrayList<GoalDTO>();

        for (int a=this.MIN; a < this.MAX; a++){
            for (int b=this.MIN; b < this.MAX; b++){
                for (int c=this.MIN; c < this.MAX; c++){
                    for (int d=this.MIN; d < this.MAX; d++){
                        for (int e=this.MIN; e < this.MAX; e++){
                            this.allGoals.add(BizzFactory.INSTANCE.createGoal(a, b, c, d, e));
                        }
                    }
                }
            }
        }
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
