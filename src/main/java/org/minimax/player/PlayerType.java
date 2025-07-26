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

	SIMPLE_MINIMAX_3 {
		@Override
		public Player instantiate(final Color color, final List<Color> players) {
			return new SimpleMinimaxPlayer(color, players, 3);
		}
	},

	SIMPLE_MINIMAX_5 {
		@Override
		public Player instantiate(final Color color, final List<Color> players) {
			return new SimpleMinimaxPlayer(color, players, 5);
		}
	},
	;

	public abstract Player instantiate(final Color color, final List<Color> players);
}
