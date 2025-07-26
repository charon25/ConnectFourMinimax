package org.minimax;

import org.minimax.player.Player;
import org.minimax.player.PlayerType;

import java.util.Random;

public final class Game {
	private Game() {}

	public static void play(final boolean shufflePlayers, final PlayerType... playerTypes) {
		if (shufflePlayers) shufflePlayers(playerTypes);

		final int playerCount = playerTypes.length;
		final Player[] players = new Player[playerCount];
		for (int i = 0; i < playerCount; i++) {
			players[i] = playerTypes[i].getNewPlayer(Cell.COLORS.get(i), i);
			players[i].init();
		}

		final Board board = new Board(Constants.WIDTH, Constants.HEIGHT);
		int currentlyPlaying = 0;
		while (true) {
			final Player player = players[currentlyPlaying];
			final Cell playerColor = Cell.COLORS.get(currentlyPlaying);

			final int column = player.play(board);
			final boolean result = board.play(playerColor, column);
			if (!result) throw new IllegalArgumentException("Player " + player + " played invalid move: " + column);

			System.out.println("Player " + player + " played in column " + (column + 1));
			System.out.println(board);

			if (board.hasWon(playerColor, column)) {
				System.out.println("##### Player " + player + " won! #####");
				break;
			}
			if (board.isFull()) {
				System.out.println("##### This game is draw! #####");
				break;
			}

			currentlyPlaying = (currentlyPlaying + 1) % playerCount;
		}
	}

	private static void shufflePlayers(final PlayerType[] players) {
		final Random random = new Random();
		for (int i = players.length - 1; i >= 0; i--) {
			final int j = random.nextInt(i + 1);
			final PlayerType temp = players[j];
			players[j] = players[i];
			players[i] = temp;
		}
	}
}
