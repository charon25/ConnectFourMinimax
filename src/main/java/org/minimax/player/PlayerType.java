package org.minimax.player;

import org.minimax.Color;

import java.util.List;

public enum PlayerType {
	HUMAN {
		@Override
		public Player instantiate(final Color color, final List<Color> players) {
			return new HumanPlayer(color, players);
		}
	},
	RANDOM {
		@Override
		public Player instantiate(final Color color, final List<Color> players) {
			return new RandomPlayer(color, players);
		}
	},

	SIMPLE_MINIMAX {
		@Override
		public Player instantiate(final Color color, final List<Color> players) {
			return new SimpleMinimaxPlayer(color, players);
		}
	}
	;

	public abstract Player instantiate(final Color color, final List<Color> players);
}
