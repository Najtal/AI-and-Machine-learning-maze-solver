package model;

/**
 * Created by jvdur on 09/05/2016.
 */
public class MazeLearningModel {

    private static MazeLearningModel instance;

    /**
     * Singleton getter
     * @return
     */
    public static MazeLearningModel getInstance() {
        if (instance == null)
            instance = new MazeLearningModel();
        return instance;
    }

    /**
     * private constructor
     */
    private MazeLearningModel() {

        // TODO: 10/05/2016

    }
}
