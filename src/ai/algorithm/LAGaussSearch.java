package ai.algorithm;

import bizz.BizzFactory;
import ucc.GoalDTO;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jvdur on 04/06/2016.
 */
public class LAGaussSearch extends LearnAlgorithm {


    private final static int MIN = 1;
    private final static int MAX = 200;
    private final static int POOL_SIZE = 50;

    private Random r;
    private int bestCost;
    private GoalDTO bestGoals;
    private GoalDTO lastGoals;

    private GoalDTO targetGoal;
    private ArrayList<GoalDTO> poolGoals;
    private ArrayList<Integer> poolSteps;
    private int toEndOfPool;

    public LAGaussSearch() {
        this.bestCost = Integer.MAX_VALUE;
        this.toEndOfPool = 0;
        r = new Random();

        int loadDiscoverPath = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        int loadGrabKey = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        int loadOpenDoor = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        int loadReachGoal = MIN + (int)(Math.random() * ((MAX - MIN) + 1));
        int loadAction = MIN + (int)(Math.random() * ((MAX - MIN) + 1));

        targetGoal = BizzFactory.INSTANCE.createGoal(loadDiscoverPath, loadGrabKey,
                loadOpenDoor, loadReachGoal, loadAction);
    }

    @Override
    public GoalDTO getNextGoal() {

        // set last pool best values as new landmark
        if (toEndOfPool == 0) {
            if (poolGoals != null) {
                int maxi = Integer.MAX_VALUE;
                int besti = 0;
                for (int i=0; i<poolSteps.size() ; i++) {
                    if (poolSteps.get(i) < maxi) {
                        besti = i;
                    }
                }
                targetGoal = poolGoals.get(besti);
            }
            toEndOfPool = POOL_SIZE;
            poolGoals = new ArrayList<>();
            poolSteps = new ArrayList<>();
        }


        // make a guess around the last est values
        int loadDiscoverPath = Math.max(0, Math.min(200,((int) r.nextGaussian() * 40 + targetGoal.getLoadDiscoverPath())));
        int loadGrabKey = Math.max(0, Math.min(200,((int) r.nextGaussian() * 40 + targetGoal.getLoadGrabKey())));
        int loadOpenDoor = Math.max(0, Math.min(200,((int) r.nextGaussian() * 40 + targetGoal.getLoadOpenDoor())));
        int loadReachGoal = Math.max(0, Math.min(200,((int) r.nextGaussian() * 40 + targetGoal.getLoadReachGoal())));
        int loadAction = Math.max(0, Math.min(200,((int) r.nextGaussian() * 40 + targetGoal.getLoadAction())));

        lastGoals = BizzFactory.INSTANCE.createGoal(loadDiscoverPath, loadGrabKey,
                loadOpenDoor, loadReachGoal, loadAction);

        poolGoals.add(lastGoals);

        return lastGoals;
    }

    @Override
    public void setNumberStepsNeeded(int nbSteps) {
        poolSteps.add(nbSteps);
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
