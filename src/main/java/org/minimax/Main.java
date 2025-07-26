package org.minimax;

import org.minimax.player.PlayerType;

import static org.minimax.Constants.IN;

public class Main {

	public static void main(String[] args) {
		System.out.println("Player types: ");
		for (final PlayerType playerType : PlayerType.values()) {
			System.out.println("- " + playerType.ordinal() + " : " + playerType);
		}

		System.out.print("\nPlayer count: ");
		final int playerCount = Integer.parseInt(IN.nextLine());

		final PlayerType[] players = new PlayerType[playerCount];
		for (int i = 0; i < playerCount; i++) {
			System.out.print("Player " + (i + 1) + ": ");
			players[i] = getPlayerType(IN.nextLine());
		}

		Game.play(false, players);
	}

	private static PlayerType getPlayerType(final String input) {
		try {
			return PlayerType.values()[Integer.parseInt(input)];
		} catch (final Exception e) {
			return PlayerType.valueOf(input.strip().toUpperCase());
		}
	}
}