package org.minimax.player;

import org.minimax.BoardInterface;
import org.minimax.Cell;

import java.util.List;
import java.util.Random;

public class RandomPlayer extends Player {

	private final Random m_random;

	public RandomPlayer(final Cell color, final int turnOrder) {
		super(color, turnOrder);
		m_random = new Random();
	}

	@Override
	public void init() {
	}

	@Override
	public int play(final BoardInterface board) {
		final List<Integer> playable = board.getPlayableColumns();
		return playable.get(m_random.nextInt(playable.size()));
	}

	@Override
	protected String name() {
		return "Random";
	}
}
