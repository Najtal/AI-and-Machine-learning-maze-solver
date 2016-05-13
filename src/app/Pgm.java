package app;

import ai.learner.LearnerImpl;
import ai.learner.LearnerRunner;
import constant.RunningMode;
import model.GlobalLearningModel;
import util.Log;



/**
 * Classe app.Pgm : Main program, main controller
 * @author Jean-Vital Durieu
 * @version 0.01
 */
public class Pgm {

	/**
	 * Constructeur. Crée le modele et la vue, par défaut sur la page de connection
	 */
	public Pgm() {

		Log.logInfo("Program started all good");

		// Create a Global learning model
		GlobalLearningModel glm = new GlobalLearningModel();

		// Init the context of the maze execution
		glm.setLearningPlan(3, 2, 5, 5, 5, 5, 2, 3, RunningMode.FULL_SPEED);

		// Create a Learner and give it the context (the GlobalLearningModel)
		LearnerRunner lr = new LearnerImpl(glm);

		// Run it !
		lr.run();

	}

}
