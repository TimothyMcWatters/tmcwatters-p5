import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Timothy McWatters
 * @version 1.0
 * 
 * COP 4027 Advanced Computer Programming
 * Project 5
 * File Name: PlayerService.java
 * 
 * This Program: Is a networked tic-tac-toe game. This game involves a server that
 * waits for 2 players to join. As soon as the server receives a pair of players,
 * it will assign them to a game. Then these 2 players will play the game to 
 * a win, loss, or a draw. 
 */

public class PlayerService implements Runnable {
	private Socket socket;
	private Scanner inputStream;
	private PrintWriter outputStream;
	private Game game;
	private int thisPlayerNumber; 

	public PlayerService(Socket socket, Game game) {
		this.socket = socket;
		this.game = game;
	}

	@Override
	public void run() {
		try {
			try {
				inputStream = new Scanner(socket.getInputStream());
				outputStream = new PrintWriter(socket.getOutputStream());
				playGame();
			} finally {
				socket.close();
			}
		} catch(IOException exception) {
			exception.printStackTrace();
		}
	}

	private void playGame() {
		while (true) {
			if (inputStream.hasNext()) {
				String action = inputStream.next();
				startTheGame(action);
			} else {
				return;
			}	
		}
	}
	
	private void startTheGame(String action) {
		if (action.equalsIgnoreCase("join")) {
			joinGame();
		} else if (action.equalsIgnoreCase("choose")) {
			checkIfMoveIsLegal();
		} else if (action.equalsIgnoreCase("quit")) {
			quitGame();
		}	
	}

	/**
	 * 
	 */
	public void joinGame() {
		String name = inputStream.next();
		if (game.getPlayer() == 1) {
			setThisPlayerNumber(1);
			outputStream.println("Hello " + name + " you are player " + game.getPlayer());
			outputStream.println(game.getPlayer());
			outputStream.println("Player 1, you go first");
			game.setPlayer(0);
		} else {
			game.setGameFull(true);
			setThisPlayerNumber(2);
			outputStream.println("Hello " + name + " you are player " + game.getPlayer() + ".  " + "Let the game begin!");
			outputStream.println(game.getPlayer());
			game.setPlayer(1);
			game.setOtherPlayer(this);
		}
		outputStream.flush();
	}

	private void checkIfMoveIsLegal() {
		int playerNum = inputStream.nextInt();
		int row = inputStream.nextInt();
		int column = inputStream.nextInt();
		
		if (game.isGameFull()) {
			if (game.locationExists(row, column)) {
				if (game.moveIsOk(row, column)) {
					processPlayerMove(playerNum, row, column);
				} else {
					outputStream.println("Position " + row + " " + column + " is taken, try again");
					outputStream.flush();
				}
			} else {
				outputStream.println("Illegal Board Position");
				outputStream.flush();
			}
		} else {
			outputStream.println("We do not have two players yet");
			outputStream.flush();
		} 
		
	}

	/**
	 * @param playerNum
	 * @param row
	 * @param column
	 */
	private void processPlayerMove(int playerNum, int row, int column) {
		game.makeMove(playerNum, row, column);
		outputStream.println("Player " + playerNum + " has chosen " + row + " " + column);
		outputStream.println(game.printGame());
		game.getOtherPlayer().otherPlayerMoved(playerNum, row, column);
		
		if (game.gameOverWithWinner()) {
			outputStream.println("PLAYER " + getThisPlayerNumber() + " WINS!!!");
			game.getOtherPlayer().outputStream.println("PLAYER " + getThisPlayerNumber() + " WINS!!!");
			
			game.getOtherPlayer().outputStream.flush();		
			outputStream.flush();
			
			
			
			//outputStream.println("PLAYER " + playerNum + " WINS!!!");
			//game.getOtherPlayer().outputStream.println("PLAYER " + playerNum + " WINS!!!");
		}
		
		if (game.gameOverWithDraw()) {
			outputStream.println("Game over, it is a draw!");
			game.getOtherPlayer().outputStream.println("Game over, it is a draw!");
			
			game.getOtherPlayer().outputStream.flush();		
			outputStream.flush();
		}
		outputStream.flush();
		game.getOtherPlayer().outputStream.flush();
		game.setOtherPlayer(this);
	}
	
	private void otherPlayerMoved(int playerNum, int row, int column) {
		String otherPlayersMove = ("Player " + playerNum + " has chosen " + row + " " + column);
		game.getOtherPlayer().outputStream.println("Other player moved" + " \n" + otherPlayersMove + "\n" + game.printGame());
		game.getOtherPlayer().outputStream.println("Player " + game.getPlayer());
	}
	
	private void quitGame() {
		outputStream.println("PLAYER " + getThisPlayerNumber() + " HAS QUIT");
		game.getOtherPlayer().outputStream.println("PLAYER " + getThisPlayerNumber() + " HAS QUIT");
		
		game.getOtherPlayer().outputStream.flush();		
		outputStream.flush();
	}

	/**
	 * @return the thisPlayerNumber
	 */
	public int getThisPlayerNumber() {
		return thisPlayerNumber;
	}

	/**
	 * @param thisPlayerNumber the thisPlayerNumber to set
	 */
	public void setThisPlayerNumber(int thisPlayerNumber) {
		this.thisPlayerNumber = thisPlayerNumber;
	}
}
