package model;

import ai.learner.LearnerImpl;
import ai.learner.LearnerRunner;
import constant.RunningMode;
import constant.RunningStatus;

/**
 * Created by jvdur on 09/05/2016.
 */
public class GlobalLearningModel {

    private int nbMaze;
    private int nbIterationPerMaze;
    private int mXmin;
    private int mYmin;
    private int mXmax;
    private int mYmax;
    private int mMinLevel;
    private int mMaxLevel;

    private RunningMode rMode;
    private RunningStatus rStatus;

    private LearnerRunner runningThread;
    private boolean done;
    private int autoRunningSpeed;

    public GlobalLearningModel(int nbMaze, int nbIterationPerMaze,
                                int mXmin, int mYmin, int mXmax, int mYmax,
                                int mMinLevel, int mMaxLevel, RunningMode rMode) {
        this.nbMaze = nbMaze;
        this.nbIterationPerMaze = nbIterationPerMaze;
        this.mXmin = mXmin;
        this.mYmin = mYmin;
        this.mXmax = mXmax;
        this.mYmax = mYmax;
        this.mMinLevel = mMinLevel;
        this.mMaxLevel = mMaxLevel;
        this.rMode = rMode;

    }


    public void createLearner() {
        runningThread = new LearnerImpl(this);
    }

    public void start() {
        rStatus = RunningStatus.RUNNING;
        runningThread.run();
    }


    public int getNbMaze() {
        return nbMaze;
    }

    public int getNbIterationPerMaze() {
        return nbIterationPerMaze;
    }

    public int getmXmin() {
        return mXmin;
    }

    public int getmYmin() {
        return mYmin;
    }

    public int getmXmax() {
        return mXmax;
    }

    public int getmYmax() {
        return mYmax;
    }

    public int getmMinLevel() {
        return mMinLevel;
    }

    public int getmMaxLevel() {
        return mMaxLevel;
    }

    public RunningMode getrMode() {
        return rMode;
    }

    public RunningStatus getrStatus() {
        return rStatus;
    }

    public LearnerRunner getRunningThread() {
        return runningThread;
    }

    public void setrMode(RunningMode rMode) {
        this.rMode = rMode;
    }

    public void setrStatus(RunningStatus rStatus) {
        this.rStatus = rStatus;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean isDone) {
        this.done = done;
    }

    public int getAutoRunningSpeed() {
        return autoRunningSpeed;
    }

    public void setAutoRunningSpeed(int autoRunningSpeed) {
        this.autoRunningSpeed = autoRunningSpeed;
    }
}
