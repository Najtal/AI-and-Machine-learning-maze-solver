package app;

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

		GlobalLearningModel glm = new GlobalLearningModel();

		glm.setLearningPlan(3, 2, 5, 5, 5, 5, 2, 3, RunningMode.FULL_SPEED);

	}

}
