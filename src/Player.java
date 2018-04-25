import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

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

public class Player {
	private static final int PORT = 8888;
	private static Socket socket;
	private static InputStream socketInput;
	private static OutputStream socketOutput;
	private static Scanner inputStreamFromSocket;
	private static PrintWriter outputStreamToSocket;
	private static Scanner inputStreamFromSystem;
	private static String responseFromSocket;
	private static int playerNum;
	
	
	public static void main(String[] args) throws IOException {
		socket = new Socket("localhost", PORT);
		socketInput = socket.getInputStream();
		socketOutput = socket.getOutputStream();
		inputStreamFromSocket = new Scanner(socketInput);
		outputStreamToSocket = new PrintWriter(socketOutput);
		inputStreamFromSystem = new Scanner(System.in);

		String action = joinGame();
		establishPlayerOrder();
		
		try {
			processResponse();
		} finally {
			socket.close();
			inputStreamFromSystem.close();
			inputStreamFromSocket.close();
		}
	}

	/**
	 * @return
	 */
	public static String joinGame() {
		// Game Welcome and join
		System.out.println("Welcome to Tim's Wonderful Wacky Tic-Tac-Toe game!");
		System.out.print("To join a game, please type <join> <your name> and hit enter: ");
		String command = inputStreamFromSystem.next();
		String playerName = inputStreamFromSystem.next();
		String action = command + " " + playerName + "\n";
		System.out.println("Joining a game, please wait... ");
		outputStreamToSocket.print(action);
		outputStreamToSocket.flush();
		return action;
	}
	
	public static void establishPlayerOrder() {
		//Establishing Player 1 as first player
		String responseFromSocket = inputStreamFromSocket.nextLine(); // Hello, you are player #
		playerNum = inputStreamFromSocket.nextInt();
		System.out.println(responseFromSocket);
		if (playerNum == 2)
			System.out.println("Player 1's turn, please wait...");
	}

	/**
	 * 
	 */
	public static void processGamePlayCommands() {
		String action;
		String selection = "";
		//check to make sure player properly types choose or quit, if not ask again until they do
		boolean inputOk = false;
		while (!inputOk) {
			//requesting a move or quit 
			System.out.println("To quit the game type <quit> otherwise,");
			System.out.println("Enter your move, type <choose> <player number> <row> <column>: ");
			selection = inputStreamFromSystem.next();
			inputOk = (selection.equalsIgnoreCase("choose") || selection.equalsIgnoreCase("quit"));
			if (!selection.equalsIgnoreCase("choose") && !selection.equalsIgnoreCase("quit")) {
				if (inputStreamFromSystem.hasNextInt()) {
					inputStreamFromSystem.nextInt();
					inputStreamFromSystem.nextInt();
					inputStreamFromSystem.nextInt();
				}
			}
		}
		// QUIT
		if (selection.equalsIgnoreCase("quit")) {
			action = "quit";
			outputStreamToSocket.println(action);
			outputStreamToSocket.flush();
		// CHOOSE	
		} else if (selection.equalsIgnoreCase("choose")) {
			int number = inputStreamFromSystem.nextInt();
			int row = inputStreamFromSystem.nextInt();
			int column = inputStreamFromSystem.nextInt();
			//looping if wrong player is trying to play (or entering the wrong players number on accident)
			while (number != playerNum) {
				System.out.println("Illegal player number");
				System.out.print("Please reenter <choose> <player number> <row> <column>: ");
				inputStreamFromSystem.next();
				number = inputStreamFromSystem.nextInt();
				row = inputStreamFromSystem.nextInt();
				column = inputStreamFromSystem.nextInt();
			}
			action = "choose " + playerNum + " " + row + " " + column + "\n";
			outputStreamToSocket.println(action);
			outputStreamToSocket.flush();
		}
	}
	
	/**
	 * 
	 */
	public static void processResponse() {
		String action;
		System.out.print(inputStreamFromSocket.nextLine()); // player
		boolean illegalMove = false; // ensures we complete this step at least once
		while (true) {
			if (!illegalMove) {
				responseFromSocket = inputStreamFromSocket.nextLine();
			}
			//confirming illegalMove's status after input
			if (!responseFromSocket.startsWith("Other player moved") && !responseFromSocket.startsWith("PLAYER")
					&& !responseFromSocket.startsWith("Game ")) {
				illegalMove = false;
				processGamePlayCommands();
				responseFromSocket = inputStreamFromSocket.nextLine();
			}
			if (responseFromSocket.startsWith("Player")) {
				displayCurrentGrid();
			} else if (responseFromSocket.startsWith("Game ")) {
				System.out.println(responseFromSocket);
				action = "quit\n";
				outputStreamToSocket.print(action);
				outputStreamToSocket.flush();
				break;
			} else if (responseFromSocket.startsWith("Other player moved")) {
				System.out.println(inputStreamFromSocket.nextLine());
				displayCurrentGrid();
			} else if (responseFromSocket.startsWith("We")) { 
				System.out.println("We do not have two players yet");
				illegalMove = true;
			} else if (responseFromSocket.startsWith("Position")) {
				System.out.println(responseFromSocket);
				illegalMove = true;
			} else if (responseFromSocket.startsWith("PLAYER")) { // Response to quit
				System.out.println("TESTTESTTESTTEST>>>>>>>>>>>>>>>>");
				System.out.println(responseFromSocket);
				break;
			} else if (responseFromSocket.startsWith("Illegal")) {
				System.out.println(responseFromSocket);
				illegalMove = true;
			}
		}
	}
	
	/**
	 * 
	 */
	public static void displayCurrentGrid() {
		String top = inputStreamFromSocket.nextLine();
		String middle = inputStreamFromSocket.nextLine();
		String bottom = inputStreamFromSocket.nextLine();
		System.out.println(top + "\n" + middle + "\n" + bottom);
	}
}
