package org.minimax;

import org.minimax.player.Player;
import org.minimax.player.PlayerType;

import java.util.Random;

public final class Game {
	private Game() {}

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
		for (int i = 0; i < playerCount; i++) {
			players[i] = playerTypes[i].getNewPlayer(Cell.COLORS.get(i), i);
			players[i].init();
		}

		final Board board = new Board(width, height, countToWin, playerCount);
		System.out.println(TURN_DELIMITER);
		System.out.println("Game started on " + board.getWidth() + 'x' + board.getHeight() + " board:");
		System.out.println(board);

		int currentlyPlaying = 0;
		while (true) {
			System.out.println(TURN_DELIMITER);
			final Player player = players[currentlyPlaying];
			final Cell playerColor = Cell.COLORS.get(currentlyPlaying);

			final int column = player.play(board.copy());
			final boolean result = board.play(playerColor, column);
			if (!result) throw new IllegalArgumentException("Player " + player + " played invalid move: " + column);

			System.out.println("Player " + player + " played in column " + (column + 1) + '.');
			System.out.println(board);

			if (board.hasWon(column)) {
				System.out.println("##### Player " + player + " won! #####");
				break;
			}
			if (board.isFull()) {
				System.out.println("##### This game is a draw! #####");
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
