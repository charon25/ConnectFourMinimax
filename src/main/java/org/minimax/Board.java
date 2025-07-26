package org.minimax;

import java.util.*;

import static org.minimax.Constants.COUNT_TO_WIN;

public class Board implements BoardInterface {

	private final int m_width;
	private final int m_height;
	private final Cell[][] m_board;
	private final int[] m_columnHeights;

	public Board(final int width, final int height) {
		m_width = width;
		m_height = height;
		m_board = new Cell[height][width];
		m_columnHeights = new int[width];
		resetBoard();
	}

	private void resetBoard() {
		for (int x = 0; x < m_width; x++) {
			for (int y = 0; y < m_height; y++) {
				m_board[y][x] = Cell.NONE;
			}

			m_columnHeights[x] = 0;
		}
	}

	// region ===== PLAY =====

	public boolean play(final Cell cell, final int column) {
		if (m_columnHeights[column] == m_height) return false;
		final int height = m_columnHeights[column];
		m_board[m_height - 1 - height][column] = cell;
		m_columnHeights[column]++;
		return true;
	}
	
	@Override
	public boolean canPlay(final int column) {
		return m_columnHeights[column] < m_height;
	}

	@Override
	public List<Integer> getPlayableColumns() {
		final List<Integer> columns = new ArrayList<>(m_width);
		for (int x = 0; x < m_width; x++) {
			if (canPlay(x)) {
				columns.add(x);
			}
		}
		return columns;
	}

	public boolean cancel(final int column) {
		if (m_columnHeights[column] == 0) return false;
		final int height = m_columnHeights[column];
		m_board[m_height - height][column] = Cell.NONE;
		m_columnHeights[column]--;
		return true;
	}

	// endregion

	/**
	 * Returns whether the given color has won after playing in the given column.
	 * This should be called after {@link Board#play(Cell, int)} with the same arguments.
	 */
	public boolean hasWon(final Cell color, final int column) {
		final int cellX = column;
		final int cellY = m_height - m_columnHeights[column];

		// Row
		int count = 0;
		for (int x = cellX - (COUNT_TO_WIN - 1); x < cellX + COUNT_TO_WIN; x++) {
			if (x < 0 || x >= m_width) continue;
			count = (m_board[cellY][x] == color ? count + 1 : 0);
		}
		if (count >= COUNT_TO_WIN) return true;

		// Column
		count = 0;
		for (int y = cellY - (COUNT_TO_WIN - 1); y < cellX + COUNT_TO_WIN; y++) {
			if (y < 0 || y >= m_height) continue;
			count = (m_board[y][cellX] == color ? count + 1 : 0);
		}
		if (count >= COUNT_TO_WIN) return true;

		// Diagonal (top left -> bottom right)
		count = 0;
		for (int k = -(COUNT_TO_WIN - 1); k < COUNT_TO_WIN; k++) {
			final int x = cellX + k;
			final int y = cellY + k;
			if (x < 0 || x >= m_width || y < 0 || y >= m_height) continue;
			count = (m_board[y][x] == color ? count + 1 : 0);
		}
		if (count >= COUNT_TO_WIN) return true;

		// Diagonal (top right -> bottom left)
		count = 0;
		for (int k = -(COUNT_TO_WIN - 1); k < COUNT_TO_WIN; k++) {
			final int x = cellX + k;
			final int y = cellY - k;
			if (x < 0 || x >= m_width || y < 0 || y >= m_height) continue;
			count = (m_board[y][x] == color ? count + 1 : 0);
		}
		return count >= COUNT_TO_WIN;
	}

	public Optional<Cell> getWinner() {
		final int[] count = new int[Cell.COLORS.size()];

		// Rows
		for (int y = 0; y < m_height; y++) {
			Arrays.fill(count, 0);

			for (int x = 0; x < m_width; x++) {
				final Cell cell = m_board[y][x];
				for (final Cell color : Cell.COLORS) {
					if (cell == color) {
						count[cell.getId()]++;
					} else {
						count[cell.getId()] = 0;
					}
				}
			}

			for (final Cell color : Cell.COLORS) {
				if (count[color.getId()] >= COUNT_TO_WIN) return Optional.of(color);
			}
		}

		// Columns
		for (int x = 0; x < m_width; x++) {
			Arrays.fill(count, 0);

			for (int y = 0; y < m_height; y++) {
				final Cell cell = m_board[y][x];
				for (final Cell color : Cell.COLORS) {
					if (cell == color) {
						count[cell.getId()]++;
					} else {
						count[cell.getId()] = 0;
					}
				}
			}

			for (final Cell color : Cell.COLORS) {
				if (count[color.getId()] >= COUNT_TO_WIN) return Optional.of(color);
			}
		}

		// Diagonals (top left -> bottom right)
		for (int y = 0; y < m_height - (COUNT_TO_WIN - 1); y++) {
			for (int x = 0; x < m_width - (COUNT_TO_WIN - 1); x++) {
				Arrays.fill(count, 0);

				for (int k = 0; k < COUNT_TO_WIN; k++) {
					final Cell cell = m_board[y + k][x + k];
					for (final Cell color : Cell.COLORS) {
						if (cell == color) {
							count[cell.getId()]++;
						} else {
							count[cell.getId()] = 0;
						}
					}
				}

				for (final Cell color : Cell.COLORS) {
					if (count[color.getId()] >= COUNT_TO_WIN) return Optional.of(color);
				}
			}
		}

		// Diagonals (top right -> bottom left)
		for (int y = COUNT_TO_WIN - 1; y < m_height; y++) {
			for (int x = 0; x < m_width - (COUNT_TO_WIN - 1); x++) {
				Arrays.fill(count, 0);

				for (int k = 0; k < COUNT_TO_WIN; k++) {
					final Cell cell = m_board[y - k][x + k];
					for (final Cell color : Cell.COLORS) {
						if (cell == color) {
							count[cell.getId()]++;
						} else {
							count[cell.getId()] = 0;
						}
					}
				}

				for (final Cell color : Cell.COLORS) {
					if (count[color.getId()] >= COUNT_TO_WIN) return Optional.of(color);
				}
			}
		}

		return Optional.empty();
	}

	// region ===== INTERFACE =====

	@Override
	public int getWidth() {
		return m_width;
	}

	@Override
	public int getHeight() {
		return m_height;
	}

	@Override
	public Cell[][] getBoardCopy() {
		final Cell[][] copy = new Cell[m_height][m_width];
		for (int y = 0; y < m_height; y++) {
			System.arraycopy(m_board[y], 0, copy[y], 0, m_width);
		}

		return copy;
	}


	// endregion

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (int y = 0; y < m_height; y++) {
			sb.append('|');
			for (int x = 0; x < m_width; x++) {
				sb.append(m_board[y][x].getCharacter()).append('|');
			}
			sb.append('\n');
		}
		sb.append("‾".repeat(Math.max(0, 2 * m_width + 1)));
		return sb.toString();
	}
}
