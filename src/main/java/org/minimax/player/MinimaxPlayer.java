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
		final List<Integer> playableColumns = board.getPlayableColumns();
		if (playableColumns.size() == 1) return playableColumns.get(0);

		final Result result = minimax(board, null, 0, true);
		return result.bestColumns.get(m_random.nextInt(result.bestColumns.size()));
	}

	private Result minimax(final Board board, final Integer lastPlayedColumn, final int depth, final boolean maximizing) {
		if (lastPlayedColumn != null && board.hasWon(lastPlayedColumn)) {
			return new Result(maximizing ? Score.worst() : Score.best(), List.of(), depth);
		} else if (depth >= getMaxDepth()) {
			return new Result(new Score(computeHeuristic(board), 0), List.of(), null);
		} else {
			final Color color = getColor(maximizing);
			final List<Integer> playableColumns = board.getPlayableColumns();
			Score value;
			final List<Integer> bestColumns = new ArrayList<>();
			if (maximizing) {
				value = Score.worst();
				for (final int column : playableColumns) {
					board.play(color, column);
					final Result result = minimax(board, column, depth + 1, false);
					if (result.score.greaterThan(value)){
						value = result.score;
						bestColumns.clear();
						bestColumns.add(column);
					} else if (result.score.equals(value)) {
						bestColumns.add(column);
					}
					board.cancel(column);
				}
			} else {
				value = Score.best();
				for (final int column : playableColumns) {
					board.play(color, column);
					final Result result = minimax(board, column, depth + 1, true);
					if (value.greaterThan(result.score)) {
						value = result.score;
						bestColumns.clear();
						bestColumns.add(column);
					} else if (result.score.equals(value)) {
						bestColumns.add(column);
					}
					board.cancel(column);
				}
			}
			return new Result(value.increaseTurnsBeforeEnd(), bestColumns, null);
		}
	}

	private Color getColor(final boolean maximizing) {
		if (maximizing) return getColor();
		return getPlayers().get(1 - getTurnOrder());
	}

	private record Result(Score score, List<Integer> bestColumns, Integer depthWin) {}

	/**
	 * Represent a score will a different comparison as a normal integer :<br>
	 * (_, -1) < (_, -2) < (_, -3) < ... < (-x, 0) < (0, 0) < (x, 0) < ... < (_, 3) < (_, 2) < (_, 1)
	 * @param value	if turnsBeforeEnd == 0, heuristic of the position
	 * @param turnsBeforeEnd if 0 means there is no end ;
	 *                       if positive, means this player will win in X turns ;
	 *                       if negative, means the opponent will win in X turns
	 */
	record Score(int value, int turnsBeforeEnd) {
		boolean greaterThan(final Score other) {
			if (turnsBeforeEnd == 0 && other.turnsBeforeEnd == 0) {
				return value > other.value;
			} else if (turnsBeforeEnd == 0) { // && other.turnsBeforeEnd != 0
				return other.turnsBeforeEnd <= 0;
			} else if (other.turnsBeforeEnd == 0) { // && turnsBeforeEnd != 0
				return turnsBeforeEnd >= 0;
			} else {
				if (turnsBeforeEnd < 0 && other.turnsBeforeEnd < 0) {
					return turnsBeforeEnd < other.turnsBeforeEnd;
				} else if (turnsBeforeEnd < 0) { // && other.turnsBeforeEnd > 0
					return false;
				} else if (other.turnsBeforeEnd < 0) { // && turnsBeforeEnd > 0
					return true;
				} else {
					return turnsBeforeEnd < other.turnsBeforeEnd;
				}
			}
		}

		private static Score worst() {
			return new Score(0, -1);
		}

		private static Score best() {
			return new Score(0, 1);
		}

		private Score increaseTurnsBeforeEnd() {
			if (turnsBeforeEnd == 0) return this;
			if (turnsBeforeEnd > 0) return new Score(value, turnsBeforeEnd + 1);
			return new Score(value, turnsBeforeEnd - 1);
		}
	}
}
