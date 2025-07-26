package org.minimax.player;

import org.minimax.Board;
import org.minimax.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class MinimaxPlayer extends Player {

	private final Random m_random;

	protected MinimaxPlayer(final Color color, final List<Color> players) {
		super(color, players);
		if (players.size() != 2) throw new IllegalArgumentException("Minimax player can only play versus one other player");
		m_random = new Random();
	}

	protected abstract int getMaxDepth();

	protected abstract int computeHeuristic(final Board board);

	@Override
	public int play(final Board board) {
		final Result result = minimax(board, null, 0, true);
		return result.bestColumns.get(m_random.nextInt(result.bestColumns.size()));
	}

	private Result minimax(final Board board, final Integer lastPlayedColumn, final int depth, final boolean maximizing) {
		if (lastPlayedColumn != null && board.hasWon(lastPlayedColumn)) {
			return new Result(maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE, List.of());
		} else if (depth >= getMaxDepth()) {
			return new Result(computeHeuristic(board), List.of());
		} else {
			final Color color = getColor(maximizing);
			final List<Integer> playableColumns = board.getPlayableColumns();
			int value;
			final List<Integer> bestColumns = new ArrayList<>();
			if (maximizing) {
				value = Integer.MIN_VALUE;
				for (final int column : playableColumns) {
					board.play(color, column);
					final Result result = minimax(board, column, depth + 1, false);
					if (result.score > value){
						value = result.score;
						bestColumns.clear();
						bestColumns.add(column);
					} else if (result.score == value) {
						bestColumns.add(column);
					}
					board.cancel(column);
				}
			} else {
				value = Integer.MAX_VALUE;
				for (final int column : playableColumns) {
					board.play(color, column);
					final Result result = minimax(board, column, depth + 1, true);
					if (result.score < value) {
						value = result.score;
						bestColumns.clear();
						bestColumns.add(column);
					} else if (result.score == value) {
						bestColumns.add(column);
					}
					board.cancel(column);
				}
			}
			return new Result(value, bestColumns);
		}
	}

	private Color getColor(final boolean maximizing) {
		if (maximizing) return getColor();
		return getPlayers().get(1 - getTurnOrder());
	}

	private record Result(int score, List<Integer> bestColumns) {}
}
