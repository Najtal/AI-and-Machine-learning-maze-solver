package ucc;

public interface MazeUCC {

	MazeUCC INSTANCE = MazeUCCImpl.getInstance();

	MazeDTO getNinjaMazeFromOmniscientMaze(MazeDTO maze);

}
