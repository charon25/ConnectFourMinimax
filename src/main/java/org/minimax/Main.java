package org.minimax;

import org.minimax.player.PlayerType;

import static org.minimax.Constants.IN;

public class Main {

	public static void main(String[] args) {

		System.out.print("Game width [" + Constants.DEFAULT_WIDTH + "]: ");
		final int width = getIntValue(Constants.DEFAULT_WIDTH);

		System.out.print("Game height [" + Constants.DEFAULT_HEIGHT + "]: ");
		final int height = getIntValue(Constants.DEFAULT_HEIGHT);

		System.out.print("Count to win [" + Constants.DEFAULT_COUNT_TO_WIN + "]: ");
		final int countToWin = getIntValue(Constants.DEFAULT_COUNT_TO_WIN);

		System.out.print("\nPlayer count [" + Constants.DEFAULT_PLAYER_COUNT + "]: ");
		final int playerCount = getIntValue(Constants.DEFAULT_PLAYER_COUNT);

		System.out.println("Player types: ");
		for (final PlayerType playerType : PlayerType.values()) {
			System.out.println("- " + playerType.ordinal() + " : " + playerType);
		}
		System.out.println();

		final PlayerType[] players = new PlayerType[playerCount];
		for (int i = 0; i < playerCount; i++) {
			System.out.print("Player " + (i + 1) + ": ");
			players[i] = getPlayerType(IN.nextLine());
		}

		Game.play(width, height, countToWin, true, players);
	}

	private static int getIntValue(final int defaultValue) {
		final String input = IN.nextLine();
		if (input.isBlank()) return defaultValue;
		try {
			return Integer.parseInt(input);
		} catch (final NumberFormatException e) {
			return defaultValue;
		}
	}

	private static PlayerType getPlayerType(final String input) {
		try {
			return PlayerType.values()[Integer.parseInt(input)];
		} catch (final Exception e) {
			return PlayerType.valueOf(input.strip().toUpperCase());
		}
	}
}