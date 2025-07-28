package org.minimax.player;

import org.minimax.Board;
import org.minimax.Color;
import org.minimax.RandomHelper;

import java.util.*;

public abstract class MinimaxPlayer extends Player {

	private final Map<BitSet, Result> m_cache = new HashMap<>();

	protected MinimaxPlayer(final Color color, final List<Color> players) {
		super(color, players);
		if (players.size() != 2) throw new IllegalArgumentException("Minimax player can only play versus one other player");
	}

	protected abstract int getMaxDepth();

	protected abstract int computeHeuristic(final Board board);

	@Override
	public int play(final Board board) {
		final List<Integer> playableColumns = board.getPlayableColumns();
		if (playableColumns.size() == 1) return playableColumns.get(0);

		final List<Integer> bestMoves = getBestMoves(board);
		return bestMoves.get(RandomHelper.nextInt(bestMoves.size()));
	}

	private List<Integer> getBestMoves(final Board board) {
		m_cache.clear();
		return minimax(board, null, 0, true, Score.WORST, Score.BEST).bestColumns;
	}

	private Result minimax(final Board board, final Integer lastPlayedColumn,
						   final int depth, final boolean maximizing,
						   Score alpha, Score beta) {
		final BitSet boardBitset = board.getBoardBitset();
		final Result cachedResult = m_cache.get(boardBitset);
		if (cachedResult != null) {
			return cachedResult;
		}

		final Result iterationResult;
		if (lastPlayedColumn != null && board.hasWon(lastPlayedColumn)) {
			iterationResult = new Result(maximizing ? Score.WORST : Score.BEST, List.of());
		} else if (depth >= getMaxDepth()) {
			iterationResult = new Result(new Score(computeHeuristic(board), 0), List.of());
		} else {
			final Color color = getColor(maximizing);
			final List<Integer> playableColumns = board.getPlayableColumns();
			Score value;
			final List<Integer> bestColumns = new ArrayList<>(board.getWidth());
			if (maximizing) {
				value = Score.WORST;
				for (final int column : playableColumns) {
					board.play(color, column);
					final Result result = minimax(board, column, depth + 1, false, alpha, beta);

					if (result.score.greaterThan(value)){
						value = result.score;
						bestColumns.clear();
						bestColumns.add(column);
					} else if (result.score.equals(value)) {
						bestColumns.add(column);
					}

					if (value.greaterThanOrEqualsTo(beta)) {
						board.cancel(column);
						break;
					}

					alpha = Score.max(alpha, value);

					board.cancel(column);
				}
			} else {
				value = Score.BEST;
				for (final int column : playableColumns) {
					board.play(color, column);
					final Result result = minimax(board, column, depth + 1, true, alpha, beta);

					if (value.greaterThan(result.score)) {
						value = result.score;
						bestColumns.clear();
						bestColumns.add(column);
					} else if (result.score.equals(value)) {
						bestColumns.add(column);
					}

					if (alpha.greaterThan(value)) {
						board.cancel(column);
						break;
					}

					beta = Score.min(beta, value);

					board.cancel(column);
				}
			}
			iterationResult = new Result(value.increaseTurnsBeforeEnd(), bestColumns);
		}

		m_cache.put(boardBitset, iterationResult);
		return iterationResult;
	}

	private Color getColor(final boolean maximizing) {
		if (maximizing) return getColor();
		return getPlayers().get(1 - getTurnOrder());
	}

	private record Result(Score score, List<Integer> bestColumns) {}

	/**
	 * Represent a score will a different comparison as a normal integer :<br>
	 * (_, -1) < (_, -2) < (_, -3) < ... < (-x, 0) < (0, 0) < (x, 0) < ... < (_, 3) < (_, 2) < (_, 1)
	 * @param value	if turnsBeforeEnd == 0, heuristic of the position
	 * @param turnsBeforeEnd if 0 means there is no end ;
	 *                       if positive, means this player will win in X turns ;
	 *                       if negative, means the opponent will win in X turns
	 */
	record Score(int value, int turnsBeforeEnd) {
		private static final Score WORST = new Score(0, -1);
		private static final Score BEST = new Score(0, 1);
		
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

		boolean greaterThanOrEqualsTo(final Score other) {
			return greaterThan(other) || equals(other);
		}

		private Score increaseTurnsBeforeEnd() {
			if (turnsBeforeEnd == 0) return this;
			if (turnsBeforeEnd > 0) return new Score(value, turnsBeforeEnd + 1);
			return new Score(value, turnsBeforeEnd - 1);
		}

		private static Score max(final Score a, final Score b) {
			return a.greaterThan(b) ? a : b;
		}

		private static Score min(final Score a, final Score b) {
			return a.greaterThan(b) ? b : a;
		}
	}
}
