package org.minimax.player;

import org.minimax.Board;
import org.minimax.Color;
import org.minimax.RandomHelper;

import java.util.List;

public class RandomPlayer extends Player {

	public RandomPlayer(final Color color, final List<Color> players) {
		super(color, players);
	}

	@Override
	public void init() {
	}

	@Override
	public int play(final Board board) {
		final List<Integer> playable = board.getPlayableColumns();
		return playable.get(RandomHelper.nextInt(playable.size()));
	}

	@Override
	protected String name() {
		return "Random";
	}
}
