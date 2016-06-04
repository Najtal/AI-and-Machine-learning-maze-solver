package ai.algorithm;

import ucc.GoalDTO;

/**
 * Created by jvdur on 04/06/2016.
 */
public interface LearnAlgorithmInt {

    GoalDTO getNextGoal();

    void setNumberStepsNeeded(int nbSteps);

    GoalDTO getBestGoals();

}
