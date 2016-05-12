package model;

/**
 * Created by jvdur on 09/05/2016.
 */
public class GlobalLearningModel {

    private static GlobalLearningModel instance;

    /**
     * Singleton getter
     * @return
     */
    public static GlobalLearningModel getInstance() {
        if (instance == null)
            instance = new GlobalLearningModel();
        return instance;
    }

    /**
     * private constructor
     */
    private GlobalLearningModel() {

        // TODO: 10/05/2016

    }
}
