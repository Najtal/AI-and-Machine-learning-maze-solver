package app;

import ai.learner.Learner;
import constant.LearningAlgorithm;
import constant.RunningMode;
import constant.RunningStatus;
import gui.maze.MazeFrame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import model.GlobalLearningModel;



public class PgmController {

    // SETTINGS TAB
    @FXML private TextField setMinsize;
    @FXML private TextField setMaxsize;
    @FXML private TextField setMindoor;
    @FXML private TextField setMaxDoor;

    @FXML private TextField setNbmaze;
    @FXML private TextField setItpermaze;
    @FXML private ChoiceBox setMLalgo;

    // EXECUTION TAB
    @FXML private Button startButton;
    @FXML private Button omnMazeButton;
    @FXML private Button ninMazeButton;
    @FXML private Slider exSpeed;
    @FXML private Button btdata;
    @FXML private ChoiceBox exMode;



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
        setMLalgo.getSelectionModel().selectFirst();
        exMode.getSelectionModel().selectFirst();

        exSpeed.setMin(100);
        exSpeed.setMax(1000);
        exSpeed.setValue(500);
        exSpeed.setShowTickLabels(true);
        exSpeed.setShowTickMarks(true);
        exSpeed.setMajorTickUnit(100);
        exSpeed.setMinorTickCount(100);
        exSpeed.setBlockIncrement(100);
    }

    /**
     * Start the execution
     * @param event
     */
    @FXML private void butStart(final ActionEvent event)
    {

        int minSize = Integer.parseInt(setMinsize.getText());
        int maxSize = Integer.parseInt(setMaxsize.getText());
        int minDoor = Integer.parseInt(setMindoor.getText());
        int maxDoor = Integer.parseInt(setMaxDoor.getText());

        int nbmaze = Integer.parseInt(setNbmaze.getText());
        int itPerMaze = Integer.parseInt(setItpermaze.getText());
        int speed = (int)exSpeed.getValue();

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(actionEvent -> System.exit(1));

        // SET ALGO
        String algo = (String) setMLalgo.getValue();
        LearningAlgorithm la = null;
        switch (algo) {
            case "Random search" :
                la = LearningAlgorithm.RANDOM_SEARCH;
                break;
            case "Grid search" :
                la = LearningAlgorithm.GRID_SEARCH;
                break;
        }
        final LearningAlgorithm laCopy = la;

        // SET MODE
        String mode = (String) exMode.getValue();
        RunningMode rMode = null;
        switch (mode) {
            case "Full speed" :
                rMode = RunningMode.FULL_SPEED;
                break;
            case "Step by step auto" :
                rMode = RunningMode.STEP_BY_STEP_AUTO;
                break;
        }
        final RunningMode rModeCopy = rMode;

        // PRINT CONFIG
        System.out.println("CONFIG:"
                +"\n\tminSize: "+minSize+"\n\tmaxSize: "+maxSize
                +"\n\tminDoor: "+minDoor+"\n\tmaxDoor: "+maxDoor
                +"\n\tnbmaze: "+nbmaze+"\n\titPerMaze: "+itPerMaze
                +"\n\talgo: "+algo+"\n\tSpeed: "+speed+"\n\tRunning mode: "+rMode);

        startButtonIsStart = !startButtonIsStart;
        glm.setrMode(rMode);
        glm.setAutoRunningSpeed(speed);

        startButton.setText("Generating...");
        new Thread() {
            // runnable for that thread
            public void run() {

                // Create and Init a Global learning model
                glm = new GlobalLearningModel(
                        nbmaze, itPerMaze,
                        minSize, minSize, maxSize, maxSize,
                        minDoor, maxDoor, rModeCopy, laCopy);

                // Create Learner
                Learner learner = new Learner(glm);

                // Create a Learner
                glm.setRunningThread(learner);

                // Run it !
                glm.setrStatus(RunningStatus.RUNNING);
                learner.run();

            }
        }.start();

        while(glm == null) {
        }

        // ACTIVATE BUTTONS
        omnMazeButton.setDisable(false);

        if (rMode == RunningMode.STEP_BY_STEP_AUTO)
            ninMazeButton.setDisable(false);

        startButton.setText("Stop");



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
        glm.setNinjaFrame(new MazeFrame(glm.getRunningThread().getMazeLearningModel().getMazeNinja(), "maze", false));
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
