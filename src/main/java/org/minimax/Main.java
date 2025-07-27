package org.minimax;

import org.minimax.player.PlayerType;

import static org.minimax.Constants.IN;

public class Main {

	public static void main(String[] args) {
		if (args.length >= 1 && Constants.PROFILE_ARG.equals(args[0])) {
			RandomHelper.setSeed(3141592653589793238L);
			final long start = System.nanoTime();
			Game.play(
					Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT, Constants.DEFAULT_COUNT_TO_WIN, false,
					PlayerType.SIMPLE_MINIMAX_12, PlayerType.SIMPLE_MINIMAX_12
			);
			final long end = System.nanoTime();
			System.out.println("Duration: " + (float) (end - start) / Constants.ONE_BILLION + " s");
			return;
		}

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

		Game.s_verbose = true;
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