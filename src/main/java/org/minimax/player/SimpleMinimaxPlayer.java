package org.minimax.player;

import org.minimax.Board;
import org.minimax.Color;

import java.util.List;

public class SimpleMinimaxPlayer extends MinimaxPlayer {
	private final int m_maxDepth;

	public SimpleMinimaxPlayer(final Color color, final List<Color> players, final int maxDepth) {
		super(color, players);
		m_maxDepth = maxDepth;
	}

	@Override
	public void init() {
	}

	@Override
	protected int getMaxDepth() {
		return m_maxDepth;
	}

	@Override
	protected int computeHeuristic(final Board board) {
		return 0;
	}

	@Override
	protected String name() {
		return "SimpleMinimax-" + m_maxDepth;
	}
}
