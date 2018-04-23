
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

		System.out.println("Welcome to Tim's Wonderful Wacky Tic-Tac-Toe game!");
		System.out.print("To join a game, please enter your name: ");
		String playerName = inputStreamFromSystem.next();
		String action = "join " + playerName + "\n";
		System.out.println("Joining " + playerName + "... ");
		outputStreamToSocket.print(action);
		outputStreamToSocket.flush();
		String responseFromSocket = inputStreamFromSocket.nextLine();
		int playerNum = inputStreamFromSocket.nextInt();
		System.out.println(responseFromSocket);
		if (playerNum == 2)
			System.out.println("Player 1's turn, please wait...");
		inputStreamFromSocket.nextLine();
		
		String top = "";
		String middle = "";
		String bottom = "";
		boolean illegalMove = false;
		try {
			while (true) {
				if (!illegalMove) {
					responseFromSocket = inputStreamFromSocket.nextLine();
				}
				if (!responseFromSocket.startsWith("Other player moved") && !responseFromSocket.startsWith("PLAYER")
						&& !responseFromSocket.startsWith("Game ")) {
					illegalMove = false;
					System.out.print("Enter your move <player number> <row> <column>: ");
					
					int number = inputStreamFromSystem.nextInt();
					int row = inputStreamFromSystem.nextInt();
					int column = inputStreamFromSystem.nextInt();
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
				}
			}
		} finally {
			socket.close();
			inputStreamFromSystem.close();
			inputStreamFromSocket.close();
		}
	}
}
