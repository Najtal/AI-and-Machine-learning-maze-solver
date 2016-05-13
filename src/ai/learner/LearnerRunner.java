package ai.learner;

import model.GlobalLearningModel;

/**
 * Created by jvdur on 13/05/2016.
 */
public abstract class LearnerRunner implements Runnable, Learner {

    protected GlobalLearningModel glm;

    public LearnerRunner(GlobalLearningModel glm) {
        this.glm = glm;
    }

}
