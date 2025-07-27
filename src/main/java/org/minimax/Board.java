package org.minimax;

import java.util.*;

public class Board {

	private final int m_width;
	private final int m_height;
	private final int m_countToWin;

	private final int m_bitsByColor;
	private final Color[][] m_board;
	private final BitSet m_boardBitset;
	private final int[] m_columnHeights;
	private final int[] m_playedCount;

	private final List<Color> m_colors;

	public Board(final int width, final int height, final int countToWin, final int playerCount) {
		m_width = width;
		m_height = height;
		m_countToWin = countToWin;

		m_bitsByColor = Integer.SIZE - Integer.numberOfLeadingZeros(playerCount);
		m_board = new Color[height][width];
		m_boardBitset = new BitSet(m_bitsByColor * m_width * m_height);
		m_columnHeights = new int[width];
		m_playedCount = new int[playerCount];

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

		m_boardBitset.clear();
	}

	/**
	 * The copy must share no reference with the original.
	 */
	public Board copy() {
		final Board copy = new Board(m_width, m_height, m_countToWin, m_colors.size());
		System.arraycopy(m_columnHeights, 0, copy.m_columnHeights, 0, m_columnHeights.length);
		for (int y = 0; y < m_height; y++) {
			System.arraycopy(m_board[y], 0, copy.m_board[y], 0, m_width);
		}
		System.arraycopy(m_playedCount, 0, copy.m_playedCount, 0, m_playedCount.length);
		copy.m_boardBitset.clear();
		copy.m_boardBitset.or(m_boardBitset);
		return copy;
	}

	// region ===== PLAY =====

	public boolean play(final Color color, final int column) {
		if (m_columnHeights[column] == m_height) return false;
		final int height = m_columnHeights[column];
		m_board[m_height - 1 - height][column] = color;
		m_columnHeights[column]++;
		m_playedCount[color.getId()]++;
		updateBitSet(column, m_height - 1 - height);
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
		final Color color = m_board[m_height - height][column];
		m_board[m_height - height][column] = Color.NONE;
		m_columnHeights[column]--;
		m_playedCount[color.getId()]--;
		updateBitSet(column, m_height - height);
		return true;
	}

	public boolean isFull() {
		for (int x = 0; x < m_width; x++) {
			if (canPlay(x)) return false;
		}
		return true;
	}

	// endregion

	// region ===== BIT SET =====

	private void updateBitSet(final int x, final int y) {
		final int value = m_board[y][x].ordinal();
		final int p = y * m_width + x;
		for (int k = 0; k < m_bitsByColor; k++) {
			m_boardBitset.set(m_bitsByColor * p + k, ((value >> k) & 1) > 0);
		}
	}

	public BitSet getBoardBitset() {
		return (BitSet) m_boardBitset.clone();
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
		if (m_playedCount[color.getId()] < m_countToWin) return false;

		// Row
		int count = 0;
		for (int x = cellX - (m_countToWin - 1); x < cellX + m_countToWin; x++) {
			if (x < 0 || x >= m_width) continue;
			count = (m_board[cellY][x] == color ? count + 1 : 0);
			if (count >= m_countToWin) return true;
		}

		// Column
		count = 0;
		for (int y = cellY - (m_countToWin - 1); y < cellY + m_countToWin; y++) {
			if (y < 0 || y >= m_height) continue;
			count = (m_board[y][cellX] == color ? count + 1 : 0);
			if (count >= m_countToWin) return true;
		}

		// Diagonal (top left -> bottom right)
		count = 0;
		for (int k = -(m_countToWin - 1); k < m_countToWin; k++) {
			final int x = cellX + k;
			final int y = cellY + k;
			if (x < 0 || x >= m_width || y < 0 || y >= m_height) continue;
			count = (m_board[y][x] == color ? count + 1 : 0);
			if (count >= m_countToWin) return true;
		}

		// Diagonal (top right -> bottom left)
		count = 0;
		for (int k = -(m_countToWin - 1); k < m_countToWin; k++) {
			final int x = cellX + k;
			final int y = cellY - k;
			if (x < 0 || x >= m_width || y < 0 || y >= m_height) continue;
			count = (m_board[y][x] == color ? count + 1 : 0);
			if (count >= m_countToWin) return true;
		}

		return false;
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

	public int getWidth() {
		return m_width;
	}

	public int getHeight() {
		return m_height;
	}

	public String serialize() {
		final StringBuilder sb = new StringBuilder();
		for (int y = 0; y < m_height; y++) {
			for (int x = 0; x < m_width; x++) {
				final Color color = m_board[y][x];
				sb.append(color == Color.NONE ? "" : color);
				if (x <= m_width - 1) sb.append(',');
			}
		}
		return sb.toString();
	}

	public void unserialize(final String serialized) {
		int x = 0;
		int y = 0;
		Arrays.fill(m_playedCount, 0);
		for (final String colorString : serialized.split(",")) {
			final Color color = colorString.isBlank() ? Color.NONE : Color.valueOf(colorString);
			m_board[y][x] = color;
			x++;
			if (x == m_width) {
				y++;
				x = 0;
			}
			if (color != Color.NONE) {
				m_playedCount[color.getId()]++;
			}
		}

		for (x = 0; x < m_width; x++) {
			m_columnHeights[x] = 0;
			for (y = 0; y < m_height; y++) {
				if (m_board[y][x] != Color.NONE) {
					m_columnHeights[x] = m_height - y;
					break;
				}
			}
		}
	}

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
