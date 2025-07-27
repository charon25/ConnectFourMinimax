package org.minimax;

import org.minimax.player.Player;
import org.minimax.player.PlayerType;

import java.util.Collections;
import java.util.List;

public final class Game {
	private Game() {}

	public static boolean s_verbose = false;

	private static final String TURN_DELIMITER = "========================================";

	public static void play(final int width, final int height, final int countToWin, final boolean shufflePlayers, final PlayerType... playerTypes) {
		final int playerCount = playerTypes.length;
		if (playerCount > Constants.MAX_PLAYER_COUNT) {
			throw new IllegalArgumentException("Too many players: max is " + Constants.MAX_PLAYER_COUNT + " but got " + playerCount);
		}

		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("Invalid board size: " + width + 'x' + height);
		}

		if (countToWin <= 0) {
			throw new IllegalArgumentException("Invalid count to win: " + countToWin);
		}

		if (shufflePlayers) shufflePlayers(playerTypes);

		final Player[] players = new Player[playerCount];
		final List<Color> playerColors = Collections.unmodifiableList(Color.COLORS.subList(0, playerCount));
		for (int i = 0; i < playerCount; i++) {
			players[i] = playerTypes[i].instantiate(Color.COLORS.get(i), playerColors);
			players[i].init();
		}

		final Board board = new Board(width, height, countToWin, playerCount);
		print(TURN_DELIMITER);
		print("Game started on " + board.getWidth() + 'x' + board.getHeight() + " board:");
		print(board);

		int currentlyPlaying = 0;
		while (true) {
			print(TURN_DELIMITER);
			final Player player = players[currentlyPlaying];
			final Color playerColor = Color.COLORS.get(currentlyPlaying);

			final int column = player.play(board.copy());
			final boolean result = board.play(playerColor, column);
			if (!result) throw new IllegalArgumentException("Player " + player + " played invalid move: " + column);

			print("Player " + player + " played in column " + (column + 1) + '.');
			print(board);
			print(board.serialize());

			if (board.hasWon(column)) {
				print("##### Player " + player + " won! #####");
				break;
			}
			if (board.isFull()) {
				print("##### This game is a draw! #####");
				break;
			}

			currentlyPlaying = (currentlyPlaying + 1) % playerCount;
		}
	}

	private static void shufflePlayers(final PlayerType[] players) {
		for (int i = players.length - 1; i >= 0; i--) {
			final int j = RandomHelper.nextInt(i + 1);
			final PlayerType temp = players[j];
			players[j] = players[i];
			players[i] = temp;
		}
	}

	private static void print(final Object object) {
		if (s_verbose) System.out.println(object);
	}
}
