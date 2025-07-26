package org.minimax;

import java.util.List;

public enum Color {
	NONE(-1,  null),
	RED(0, "\033[0;91m"),
	YELLOW(1, "\033[0;93m"),
	GREEN(2, "\033[0;92m"),
	BLUE(3, "\033[0;94m"),
	PURPLE(4, "\033[0;95m"),
	CYAN(5, "\033[0;96m"),
	;

	public static final List<Color> COLORS = List.of(
			RED, YELLOW, GREEN, BLUE, PURPLE, CYAN
	);

	private final int m_id;
	private final String m_ansiColor;

	Color(final int id, final String ansiColor) {
		m_id = id;
		m_ansiColor = ansiColor;
	}

	public int getId() {
		return m_id;
	}

	public String getCharacter() {
		if (m_ansiColor == null) return " ";
		return m_ansiColor + (Constants.UTF8 ? '●' : 'O') + Constants.ANSI_COLOR_RESET;
	}
}
