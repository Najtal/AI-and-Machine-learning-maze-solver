package bizz;

/**
 * Created by jvdur on 14/05/2016.
 */
public class GoalLoadImpl extends BizzObjectImpl implements GoalLoad {

    private int loadDiscoverPath;
    private int loadGrabKey;
    private int loadOpenDoor;
    private int loadReachGoal;
    private int loadAction;


    public GoalLoadImpl(int loadDiscoverPath, int loadGrabKey, int loadOpenDoor, int loadReachGoal, int loadAction) {
        this.loadDiscoverPath = loadDiscoverPath;
        this.loadGrabKey = loadGrabKey;
        this.loadOpenDoor = loadOpenDoor;
        this.loadReachGoal = loadReachGoal;
        this.loadAction = loadAction;
    }


    /*
     * GETTERS
     */

    public int getLoadDiscoverPath() {
        return loadDiscoverPath;
    }

    public int getLoadGrabKey() {
        return loadGrabKey;
    }

    public int getLoadOpenDoor() {
        return loadOpenDoor;
    }

    public int getLoadReachGoal() {
        return loadReachGoal;
    }

    public int getLoadAction() {
        return loadAction;
    }
}
