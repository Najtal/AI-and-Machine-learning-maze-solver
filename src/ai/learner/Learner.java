package ai.learner;

import model.MazeLearningModel;

/**
 * Created by jvdur on 13/05/2016.
 */
public interface Learner {

    void pause();

    void start();

    void makeOneStep();

    void stop();

    MazeLearningModel getMazeLearningModel();

}
