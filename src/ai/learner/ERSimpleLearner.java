package ai.learner;

import model.GlobalLearningModel;
import model.MazeLearningModel;

/**
 * Created by jvdur on 13/05/2016.
 */
public class ERSimpleLearner extends MainExecutionRunner {


    public ERSimpleLearner(GlobalLearningModel glm) {
        super(glm);
    }

    @Override
    public void run() {

        // For the # of maze we have to solve
        for(int i=0; i<glm.getNbMaze(); i++) {

            MazeLearningModel mlm = new MazeLearningModel(glm, this);

            // TODO Analyse results from MazeLearningModel

        }

    }

}
