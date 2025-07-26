package org.minimax;

public final class Constants {
	private Constants() {}

	public static final String ANSI_COLOR_RESET = "\033[0m";

	public static final int MAX_PLAYER_COUNT = Cell.COLORS.size();
	public static final int WIDTH = 7;
	public static final int HEIGHT = 6;
	public static final int COUNT_TO_WIN = 4;
}
