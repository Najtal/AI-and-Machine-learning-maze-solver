package app;

import ai.learner.LearnerImpl;
import ai.learner.LearnerRunner;
import constant.RunningMode;
import gui.swing.MazeFrame;
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


		// Create and Init a Global learning model
		GlobalLearningModel glm = new GlobalLearningModel(3, 2, 20, 20, 20, 20, 7, 7, RunningMode.FULL_SPEED);

		// Create a Learner and give it the context (the GlobalLearningModel)
		LearnerRunner lr = new LearnerImpl(glm);

		// Show mazes
		new MazeFrame(glm.getRunningThread().getMazeLearningModel().getMazeNinja(), "maze", false);
		new MazeFrame(glm.getRunningThread().getMazeLearningModel().getMazeOmniscient(), "maze", true);

		// Run it !
		lr.run();





	}

}
