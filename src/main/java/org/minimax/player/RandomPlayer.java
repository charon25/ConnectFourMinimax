package org.minimax.player;

import org.minimax.BoardInterface;
import org.minimax.Cell;

import java.util.List;
import java.util.Random;

public class RandomPlayer implements Player {

	private int m_turnOrder;
	private Random m_random;

	@Override
	public void init(final int turnOrder, final Cell color) {
		m_turnOrder = turnOrder;
		m_random = new Random();
	}

	@Override
	public int play(final BoardInterface board) {
		final List<Integer> playable = board.getPlayableColumns();
		return playable.get(m_random.nextInt(playable.size()));
	}

	@Override
	public String toString() {
		return "Random[" + m_turnOrder + ']';
	}
}
