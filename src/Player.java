
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

	public static void main(String[] args) throws IOException {
		final int PORT = 8888;
		Socket socket = new Socket("localhost", PORT);
		InputStream socketInput = socket.getInputStream();
		OutputStream socketOutput = socket.getOutputStream();
		Scanner inputStreamFromSocket = new Scanner(socketInput);
		PrintWriter outputStreamToSocket = new PrintWriter(socketOutput);
		Scanner inputStreamFromSystem = new Scanner(System.in);
		String top = "";
		String middle = "";
		String bottom = "";

		// Game Welcome and join
		System.out.println("Welcome to Tim's Wonderful Wacky Tic-Tac-Toe game!");
		System.out.print("To join a game, please enter your name: ");
		String playerName = inputStreamFromSystem.next();
		String action = "join " + playerName + "\n";
		System.out.println("Joining " + playerName + "... ");
		outputStreamToSocket.print(action);
		outputStreamToSocket.flush();
		
		//Establishing Player 1 as first player
		String responseFromSocket = inputStreamFromSocket.nextLine();
		int playerNum = inputStreamFromSocket.nextInt();
		System.out.println(responseFromSocket);
		if (playerNum == 2)
			System.out.println("Player 1's turn, please wait...");
		
		
		System.out.print(inputStreamFromSocket.nextLine());
		boolean illegalMove = false; // establishing a baseline for illegalMove
		try {
			while (true) {
				if (!illegalMove) {
					responseFromSocket = inputStreamFromSocket.nextLine();
				}
				//confirming illegalMove's status after input
				if (!responseFromSocket.startsWith("Other player moved") && !responseFromSocket.startsWith("PLAYER")
						&& !responseFromSocket.startsWith("Game ")) {
					illegalMove = false;
					
					//requesting a move 
					System.out.println("To quit the game type <quit> otherwise,");
					System.out.println("Enter your move, type <choose> <player number> <row> <column>: ");
					String selection = inputStreamFromSystem.next();
					if (selection.equalsIgnoreCase("quit")) {
						String quitMessage = ("Player " + playerNum + " has quit the game.");
						System.out.println(quitMessage);
						action = "quit\n";
						outputStreamToSocket.print(action);
						outputStreamToSocket.flush();
						break;
					}
					int number = inputStreamFromSystem.nextInt();
					int row = inputStreamFromSystem.nextInt();
					int column = inputStreamFromSystem.nextInt();
					
					//looping if wrong player is trying to play (or entering the wrong players number on accident)
					while (number != playerNum) {
						System.out.println("Illegal player number");
						System.out.print("Please reenter <player number> <row> <column>: ");
						number = inputStreamFromSystem.nextInt();
						row = inputStreamFromSystem.nextInt();
						column = inputStreamFromSystem.nextInt();
					}
					action = "choose " + playerNum + " " + row + " " + column + "\n";
					System.out.print(action);
					outputStreamToSocket.println(action);
					outputStreamToSocket.flush();
					responseFromSocket = inputStreamFromSocket.nextLine();
				}
				if (responseFromSocket.startsWith("Player")) {
					top = inputStreamFromSocket.nextLine();
					middle = inputStreamFromSocket.nextLine();
					bottom = inputStreamFromSocket.nextLine();
					System.out.println(top + "\n" + middle + "\n" + bottom);
				} else if (responseFromSocket.startsWith("Game ")) {
					System.out.println(responseFromSocket);
					action = "quit\n";
					outputStreamToSocket.print(action);
					outputStreamToSocket.flush();
					break;
				} else if (responseFromSocket.startsWith("Other player moved")) {
					System.out.println(inputStreamFromSocket.nextLine());
					top = inputStreamFromSocket.nextLine();
					middle = inputStreamFromSocket.nextLine();
					bottom = inputStreamFromSocket.nextLine();
					System.out.println(top + "\n" + middle + "\n" + bottom);
				} else if (responseFromSocket.startsWith("We")) {
					System.out.println("We do not have two players yet");
					illegalMove = true;
				} else if (responseFromSocket.startsWith("Position")) {
					System.out.println(responseFromSocket);
					illegalMove = true;
				} else if (responseFromSocket.startsWith("PLAYER")) {
					System.out.println(responseFromSocket);
					action = "quit\n";
					outputStreamToSocket.print(action);
					outputStreamToSocket.flush();
					break;
				} else if (responseFromSocket.startsWith("Illegal")) {
					System.out.println(responseFromSocket);
					illegalMove = true;
				} else if (responseFromSocket.startsWith("quit")) {
					String quitMessage = ("Player " + playerNum + " has quit the game.");
					System.out.println(quitMessage);
					action = "quit\n";
					outputStreamToSocket.print(action);
					outputStreamToSocket.flush();
					break;
				}
			}
		} finally {
			socket.close();
			inputStreamFromSystem.close();
			inputStreamFromSocket.close();
		}
	}
}
