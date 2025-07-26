package org.minimax;

public enum Cell {
	NONE(' '),
	RED('x'),
	YELLOW('o'),
	;

	private final char m_character;

	Cell(final char character) {
		m_character = character;
	}

	public char getCharacter() {
		return m_character;
	}
}
