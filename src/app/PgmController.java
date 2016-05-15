package app;

import constant.RunningMode;
import constant.RunningStatus;
import gui.maze.MazeFrame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.GlobalLearningModel;



public class PgmController {

    @FXML
    private TextField setMinsize = new TextField();
    @FXML
    private TextField setMaxsize = new TextField();
    @FXML
    private TextField setMindoor = new TextField();
    @FXML
    private TextField setMaxDoor = new TextField();

    @FXML
    private Button startButton = new Button("ExStart");
    @FXML
    private Button omnMazeButton = new Button("ExOmnimaze");
    @FXML
    private Button ninMazeButton = new Button("ExSolvermaze");

    private boolean startButtonIsStart = false;
    private static GlobalLearningModel glm;

    /**
     * Controller : called before linking fx element
     */
    public PgmController() {
    }

    /**
     * Called after event and variables linked
     */
    @FXML
    public void initialize() {
    }

    /**
     * Start the execution
     * @param event
     */
    @FXML private void butStart(final ActionEvent event)
    {
        startButtonIsStart = !startButtonIsStart;

        if(!startButtonIsStart) {
            startButton.setText("Start");

            glm.setrStatus(RunningStatus.PAUSED);

        } else {

            startButton.setText("Generating...");

            new Thread() {

                // runnable for that thread
                public void run() {

                    // Create and Init a Global learning model
                    glm = new GlobalLearningModel(1, 1, 12, 12, 12, 12, 1, 1, RunningMode.STEP_BY_STEP_AUTO);

                    // Create a Learner
                    glm.createLearner();

                    // Run it !
                    glm.start();

                }
            }.start();

            //while(glm == null) {
            //}

        startButton.setText("Stop");

        omnMazeButton.setDisable(false);
        ninMazeButton.setDisable(false);


        }

    }

    /**
     * Open omniscient maze window
     * @param event
     */
    @FXML
    private void butOmniscient(final ActionEvent event)
    {
        new MazeFrame(glm.getRunningThread().getMazeLearningModel().getMazeOmniscient(), "maze", true);
    }

    /**
     * Open solver maze window
     * @param event
     */
    @FXML
    private void butExSolver(final ActionEvent event)
    {
        new MazeFrame(glm.getRunningThread().getMazeLearningModel().getMazeNinja(), "maze", false);
    }

    /**
     * Open data ML window
     * @param event
     */
    @FXML
    private void butExData(final ActionEvent event)
    {
        System.out.println("couocou ! 4");
    }










}
