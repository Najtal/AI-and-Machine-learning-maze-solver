package ucc;

/**
 * Interface de gestion des Use Case concernants les tomes
 * 
 * @author BOUREZ Philippe, LINS Sébastien, REYNERS Gaetan, STREEL Xavier
 * @version 1.1
 */
public interface MazeUCC {

	MazeUCC INSTANCE = MazeUCCImpl.getInstance();

	MazeDTO getNinjaMazeFromOmniscientMaze(MazeDTO maze);

}
