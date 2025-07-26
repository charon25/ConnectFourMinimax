package org.minimax;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Board {

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

	public boolean play(final Cell cell, final int column) {
		if (m_columnHeights[column] == m_height) return false;
		final int height = m_columnHeights[column];
		m_board[m_height - 1 - height][column] = cell;
		m_columnHeights[column]++;
		return true;
	}
	
	public boolean canPlay(final int column) {
		return m_columnHeights[column] < m_height;
	}

	public List<Integer> playableColumns() {
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

	public Optional<Cell> getWinner() {
		// Rows
		for (int y = 0; y < m_height; y++) {
			int yellow = 0;
			int red = 0;
			for (int x = 0; x < m_width; x++) {
				final Cell cell = m_board[y][x];
				red = (cell == Cell.RED ? red + 1 : 0);
				yellow = (cell == Cell.YELLOW ? yellow + 1 : 0);
			}
			if (red >= Constants.COUNT_TO_WIN) return Optional.of(Cell.RED);
			if (yellow >= Constants.COUNT_TO_WIN) return Optional.of(Cell.YELLOW);
		}

		// Columns
		for (int x = 0; x < m_width; x++) {
			int yellow = 0;
			int red = 0;
			for (int y = 0; y < m_height; y++) {
				final Cell cell = m_board[y][x];
				red = (cell == Cell.RED ? red + 1 : 0);
				yellow = (cell == Cell.YELLOW ? yellow + 1 : 0);
			}
			if (red >= Constants.COUNT_TO_WIN) return Optional.of(Cell.RED);
			if (yellow >= Constants.COUNT_TO_WIN) return Optional.of(Cell.YELLOW);
		}

		// Diagonals (top left -> bottom right)
		for (int y = 0; y < m_height - (Constants.COUNT_TO_WIN - 1); y++) {
			for (int x = 0; x < m_width - (Constants.COUNT_TO_WIN - 1); x++) {
				int yellow = 0;
				int red = 0;
				for (int k = 0; k < Constants.COUNT_TO_WIN; k++) {
					final Cell cell = m_board[y + k][x + k];
					red = (cell == Cell.RED ? red + 1 : 0);
					yellow = (cell == Cell.YELLOW ? yellow + 1 : 0);
				}
				if (red >= Constants.COUNT_TO_WIN) return Optional.of(Cell.RED);
				if (yellow >= Constants.COUNT_TO_WIN) return Optional.of(Cell.YELLOW);
			}
		}

		// Diagonals (top right -> bottom left)
		for (int y = Constants.COUNT_TO_WIN - 1; y < m_height; y++) {
			for (int x = 0; x < m_width - (Constants.COUNT_TO_WIN - 1); x++) {
				int yellow = 0;
				int red = 0;
				for (int k = 0; k < Constants.COUNT_TO_WIN; k++) {
					final Cell cell = m_board[y - k][x + k];
					red = (cell == Cell.RED ? red + 1 : 0);
					yellow = (cell == Cell.YELLOW ? yellow + 1 : 0);
				}
				if (red >= Constants.COUNT_TO_WIN) return Optional.of(Cell.RED);
				if (yellow >= Constants.COUNT_TO_WIN) return Optional.of(Cell.YELLOW);
			}
		}

		return Optional.empty();
	}

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
