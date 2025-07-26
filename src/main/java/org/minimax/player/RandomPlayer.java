package org.minimax.player;

import org.minimax.Board;
import org.minimax.Color;

import java.util.List;
import java.util.Random;

public class RandomPlayer extends Player {

	private final Random m_random;

	public RandomPlayer(final Color color, final List<Color> players) {
		super(color, players);
		m_random = new Random();
	}

	@Override
	public void init() {
	}

	@Override
	public int play(final Board board) {
		final List<Integer> playable = board.getPlayableColumns();
		return playable.get(m_random.nextInt(playable.size()));
	}

	@Override
	protected String name() {
		return "Random";
	}
}
