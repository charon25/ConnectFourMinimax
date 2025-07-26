package org.minimax;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public final class Constants {
	private Constants() {}

	public static final Scanner IN = new Scanner(System.in);
	public static final boolean UTF8 = System.out.charset() == StandardCharsets.UTF_8;

	public static final String ANSI_COLOR_RESET = "\033[0m";

	public static final int MAX_PLAYER_COUNT = Color.COLORS.size();
	public static final int DEFAULT_PLAYER_COUNT = 2;
	public static final int DEFAULT_WIDTH = 7;
	public static final int DEFAULT_HEIGHT = 6;
	public static final int DEFAULT_COUNT_TO_WIN = 4;
}
