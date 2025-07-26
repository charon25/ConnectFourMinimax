package org.minimax.player;

import org.minimax.BoardInterface;
import org.minimax.Cell;

import java.util.List;
import java.util.Random;

public class RandomPlayer implements Player {

	private Random m_random;

	@Override
	public void init(final Cell color) {
		m_random = new Random();
	}

	@Override
	public int play(final BoardInterface board) {
		final List<Integer> playable = board.getPlayableColumns();
		return playable.get(m_random.nextInt(playable.size()));
	}

}
