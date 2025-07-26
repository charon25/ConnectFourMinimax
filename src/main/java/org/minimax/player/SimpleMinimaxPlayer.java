package org.minimax.player;

import org.minimax.Board;
import org.minimax.Color;

import java.util.List;

public class SimpleMinimaxPlayer extends MinimaxPlayer {
	public SimpleMinimaxPlayer(final Color color, final List<Color> players) {
		super(color, players);
	}

	@Override
	public void init() {
	}

	@Override
	protected int getMaxDepth() {
		return 3;
	}

	@Override
	protected int computeHeuristic(final Board board) {
		return 0;
	}

	@Override
	protected String name() {
		return "SimpleMinimax";
	}
}
