package app;

import ai.maze.Generator;
import gui.swing.MazeFrame;
import ucc.MazeDTO;
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


		Generator g = new Generator(7, 7, 2);
		MazeDTO m = g.generate();

		// Show Maze Frame
		new MazeFrame(m, "maze");

		/*

		// Create a Global learning model
		GlobalLearningModel glm = new GlobalLearningModel();

		// Init the context of the maze execution
		glm.setLearningPlan(3, 2, 5, 5, 5, 5, 2, 3, RunningMode.FULL_SPEED);

		// Create a Learner and give it the context (the GlobalLearningModel)
		LearnerRunner lr = new LearnerImpl(glm);

		// Run it !
		lr.run();


		*/


	}

}
