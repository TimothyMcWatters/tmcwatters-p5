import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

public class TicTacToeServer {
	
	public static void main(String[] args) throws IOException {
		ServerSocket server = null;
		final int PORT = 8888;
		try {
			server = new ServerSocket(PORT);
			System.out.println("Waiting for players to connect...");
			Game game = new Game();
			while (true) {
				Socket s = server.accept();
				System.out.println("Player connected...");
				PlayerService playerService = new PlayerService(s, game);
				Thread t = new Thread(playerService);
				t.start();
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		} finally {
			server.close();
		}
	}
}