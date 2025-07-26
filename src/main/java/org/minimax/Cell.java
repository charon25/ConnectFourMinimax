package org.minimax;

import java.util.List;

public enum Cell {
	NONE(-1,  ' '),
	RED(0, 'x'),
	YELLOW(1, 'o'),
	;

	public static final List<Cell> COLORS = List.of(
			RED, YELLOW
	);

	private final int m_id;
	private final char m_character;

	Cell(final int id, final char character) {
		m_id = id;
		m_character = character;
	}

	public int getId() {
		return m_id;
	}

	public char getCharacter() {
		return m_character;
	}
}
