package ai.learner;

import model.MazeLearningModel;

/**
 * Created by jvdur on 13/05/2016.
 */
public interface LearnerInt {

    void pause();

    void start();

    void makeOneStep();

    void stop();

    MazeLearningModel getMazeLearningModel();

}
