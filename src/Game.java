/**
 * @author Timothy McWatters
 * @version 1.0
 * 
 * COP 4027 Advanced Computer Programming
 * Project 5
 * File Name: Player.java
 * 
 * This Program: Is a networked tic-tac-toe game. This game involves a server that
 * waits for 2 players to join. As soon as the server receives a pair of players,
 * it will assign them to a game. Then these 2 players will play the game to 
 * a win, loss, or a draw. 
 */

public class Game {
	private boolean gameFull;
	private int player;
	private int[][] grid;
	private PlayerService otherPlayer;
	private final int NUMBER_OF_GRID_ROWS = 3;
	private final int NUMBER_OF_GRID_COLUMNS = 3;
	
	public Game() {
		this.player = 1;
		this.grid = new int[NUMBER_OF_GRID_ROWS][NUMBER_OF_GRID_COLUMNS];
	}

	/**
	 * @return the gameFull
	 */
	public boolean isGameFull() {
		return gameFull;
	}

	/**
	 * @param gameFull the gameFull to set
	 */
	public void setGameFull(boolean gameFull) {
		this.gameFull = gameFull;
	}

	/**
	 * @return the player
	 */
	public int getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(int playerToSet) {
		if (playerToSet == 1) {
			this.player = playerToSet;
		} else {
			this.player++;
		}
	}

	/**
	 * @return the otherPlayer
	 */
	public PlayerService getOtherPlayer() {
		return otherPlayer;
	}

	/**
	 * @param otherPlayer the otherPlayer to set
	 */
	public void setOtherPlayer(PlayerService otherPlayer) {
		this.otherPlayer = otherPlayer;
	}
	
	public boolean gameOverWithWinner() {
        return
                (grid[0][0] != 0 && grid[0][0] == grid[0][1] && grid[0][0] == grid[0][2])
              ||(grid[1][0] != 0 && grid[1][0] == grid[1][1] && grid[1][0] == grid[1][2])
              ||(grid[2][0] != 0 && grid[2][0] == grid[2][1] && grid[2][0] == grid[2][2])
              ||(grid[0][0] != 0 && grid[0][0] == grid[1][0] && grid[0][0] == grid[2][0])
              ||(grid[0][1] != 0 && grid[0][1] == grid[1][1] && grid[0][1] == grid[2][1])
              ||(grid[0][2] != 0 && grid[0][2] == grid[1][2] && grid[0][2] == grid[2][2])
              ||(grid[0][0] != 0 && grid[0][0] == grid[1][1] && grid[0][0] == grid[2][2])
              ||(grid[0][2] != 0 && grid[0][2] == grid[1][1] && grid[0][2] == grid[2][0]);
	}
	
	public boolean gameOverWithDraw() {
        for (int i = 0; i < NUMBER_OF_GRID_ROWS; i++) {
        	for (int j = 0; j < NUMBER_OF_GRID_COLUMNS; j++)
            if (grid[i][j] == 0) {
                return false;
            }
        }
        return true;
	}
	
	public boolean moveIsOk(int row, int column) {
        if (grid[row][column] == 0) {
        	return true;
        } else {
            return false;
        }	
	}
	
	public boolean locationExists(int row, int column) {
		if ((row >= 0 && row <=2) && (column >= 0 && column <= 2)) {
			return true;
		} else {
        	return false;
		}
	}
	
	public void makeMove(int playerNumber, int row, int column) {
		grid[row][column] = playerNumber;
	}
	
	public String printGame() {
		String game = grid[0][0] + " " + grid[0][1] + " " + grid[0][2] + "\n" + grid[1][0] + " " + grid[1][1] + " "
				+ grid[1][2] + "\n" + grid[2][0] + " " + grid[2][1] + " " + grid[2][2];
		return game;
	}
	
	public int otherPlayerMark(int playerNumber) {
		int otherPlayerNumber;
		if (playerNumber == 1) {
			otherPlayerNumber = 2;
		} else {
			otherPlayerNumber = 1;
		}
		return otherPlayerNumber;
	}
	
}
