package ucc;

public interface GoalUCC {

	GoalUCC INSTANCE = GoalUCCImpl.getInstance();

	GoalDTO clone(GoalDTO goals);

}
