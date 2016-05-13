package ai.learner;

import model.GlobalLearningModel;

/**
 * Created by jvdur on 13/05/2016.
 */
public abstract class MainExecutionRunner implements Runnable {


    protected GlobalLearningModel glm;

    public MainExecutionRunner(GlobalLearningModel glm) {
        this.glm = glm;
    }

}
