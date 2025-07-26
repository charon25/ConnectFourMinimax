package org.minimax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Board {

	private final int m_width;
	private final int m_height;
	private final int m_countToWin;

	private final Color[][] m_board;
	private final int[] m_columnHeights;

	private final List<Color> m_colors;

	public Board(final int width, final int height, final int countToWin, final int playerCount) {
		m_width = width;
		m_height = height;
		m_countToWin = countToWin;

		m_board = new Color[height][width];
		m_columnHeights = new int[width];

		m_colors = Color.COLORS.subList(0, playerCount);

		resetBoard();
	}

	private void resetBoard() {
		for (int x = 0; x < m_width; x++) {
			for (int y = 0; y < m_height; y++) {
				m_board[y][x] = Color.NONE;
			}

			m_columnHeights[x] = 0;
		}
	}

	/**
	 * The copy must share no reference with the original.
	 */
	public Board copy() {
		final Board copy = new Board(m_width, m_height, m_countToWin, m_colors.size());
		System.arraycopy(m_columnHeights, 0, copy.m_columnHeights, 0, copy.m_columnHeights.length);
		for (int y = 0; y < m_height; y++) {
			System.arraycopy(m_board[y], 0, copy.m_board[y], 0, m_width);
		}
		return copy;
	}

	// region ===== PLAY =====

	public boolean play(final Color color, final int column) {
		if (m_columnHeights[column] == m_height) return false;
		final int height = m_columnHeights[column];
		m_board[m_height - 1 - height][column] = color;
		m_columnHeights[column]++;
		return true;
	}
	
	public boolean canPlay(final int column) {
		return m_columnHeights[column] < m_height;
	}

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
		m_board[m_height - height][column] = Color.NONE;
		m_columnHeights[column]--;
		return true;
	}

	public boolean isFull() {
		for (int x = 0; x < m_width; x++) {
			if (canPlay(x)) return false;
		}
		return true;
	}

	// endregion

	/**
	 * Returns whether the last move of the given column resulted in a win.
	 */
	public boolean hasWon(final int column) {
		if (m_columnHeights[column] == 0) return false;

		final int cellX = column;
		final int cellY = m_height - m_columnHeights[column];
		final Color color = m_board[cellY][cellX];

		// Row
		int count = 0;
		for (int x = cellX - (m_countToWin - 1); x < cellX + m_countToWin; x++) {
			if (x < 0 || x >= m_width) continue;
			count = (m_board[cellY][x] == color ? count + 1 : 0);
		}
		if (count >= m_countToWin) return true;

		// Column
		count = 0;
		for (int y = cellY - (m_countToWin - 1); y < cellY + m_countToWin; y++) {
			if (y < 0 || y >= m_height) continue;
			count = (m_board[y][cellX] == color ? count + 1 : 0);
		}
		if (count >= m_countToWin) return true;

		// Diagonal (top left -> bottom right)
		count = 0;
		for (int k = -(m_countToWin - 1); k < m_countToWin; k++) {
			final int x = cellX + k;
			final int y = cellY + k;
			if (x < 0 || x >= m_width || y < 0 || y >= m_height) continue;
			count = (m_board[y][x] == color ? count + 1 : 0);
		}
		if (count >= m_countToWin) return true;

		// Diagonal (top right -> bottom left)
		count = 0;
		for (int k = -(m_countToWin - 1); k < m_countToWin; k++) {
			final int x = cellX + k;
			final int y = cellY - k;
			if (x < 0 || x >= m_width || y < 0 || y >= m_height) continue;
			count = (m_board[y][x] == color ? count + 1 : 0);
		}
		return count >= m_countToWin;
	}

	public Optional<Color> getWinner() {
		final int[] count = new int[m_colors.size()];

		// Rows
		for (int y = 0; y < m_height; y++) {
			Arrays.fill(count, 0);

			for (int x = 0; x < m_width; x++) {
				final Color cell = m_board[y][x];
				for (final Color color : m_colors) {
					if (cell == color) {
						count[cell.getId()]++;
					} else {
						count[cell.getId()] = 0;
					}
				}
			}

			for (final Color color : m_colors) {
				if (count[color.getId()] >= m_countToWin) return Optional.of(color);
			}
		}

		// Columns
		for (int x = 0; x < m_width; x++) {
			Arrays.fill(count, 0);

			for (int y = 0; y < m_height; y++) {
				final Color cell = m_board[y][x];
				for (final Color color : m_colors) {
					if (cell == color) {
						count[cell.getId()]++;
					} else {
						count[cell.getId()] = 0;
					}
				}
			}

			for (final Color color : m_colors) {
				if (count[color.getId()] >= m_countToWin) return Optional.of(color);
			}
		}

		// Diagonals (top left -> bottom right)
		for (int y = 0; y < m_height - (m_countToWin - 1); y++) {
			for (int x = 0; x < m_width - (m_countToWin - 1); x++) {
				Arrays.fill(count, 0);

				for (int k = 0; k < m_countToWin; k++) {
					final Color cell = m_board[y + k][x + k];
					for (final Color color : m_colors) {
						if (cell == color) {
							count[cell.getId()]++;
						} else {
							count[cell.getId()] = 0;
						}
					}
				}

				for (final Color color : m_colors) {
					if (count[color.getId()] >= m_countToWin) return Optional.of(color);
				}
			}
		}

		// Diagonals (top right -> bottom left)
		for (int y = m_countToWin - 1; y < m_height; y++) {
			for (int x = 0; x < m_width - (m_countToWin - 1); x++) {
				Arrays.fill(count, 0);

				for (int k = 0; k < m_countToWin; k++) {
					final Color cell = m_board[y - k][x + k];
					for (final Color color : m_colors) {
						if (cell == color) {
							count[cell.getId()]++;
						} else {
							count[cell.getId()] = 0;
						}
					}
				}

				for (final Color color : m_colors) {
					if (count[color.getId()] >= m_countToWin) return Optional.of(color);
				}
			}
		}

		return Optional.empty();
	}

	// region ===== INTERFACE =====

	public int getWidth() {
		return m_width;
	}

	public int getHeight() {
		return m_height;
	}

	// endregion

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(' ');
		for (int x = 0; x < m_width; x++) {
			sb.append(x + 1).append(' ');
		}
		sb.append('\n');
		for (int y = 0; y < m_height; y++) {
			sb.append('|');
			for (int x = 0; x < m_width; x++) {
				sb.append("\u001B[31m").append(m_board[y][x].getCharacter()).append("\u001B[0m").append('|');
			}
			sb.append('\n');
		}
		sb.append((Constants.UTF8 ? "‾" : "-").repeat(Math.max(0, 2 * m_width + 1)));
		return sb.toString();
	}
}
