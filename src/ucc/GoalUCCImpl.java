package ucc;

import bizz.BizzFactory;

/**
 * Created by jvdur on 07/06/2016.
 */
public class GoalUCCImpl implements GoalUCC {

    static GoalUCCImpl instance;

    public static GoalUCC getInstance() {
        if (instance == null)
            instance = new GoalUCCImpl();
        return instance;
    }

    @Override
    public GoalDTO clone(GoalDTO goals) {
        return BizzFactory.INSTANCE.createGoal(goals.getLoadDiscoverPath(), goals.getLoadGrabKey(), goals.getLoadOpenDoor(), goals.getLoadReachGoal(), goals.getLoadAction());
    }
}
