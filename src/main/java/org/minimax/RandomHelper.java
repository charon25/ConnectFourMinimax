package org.minimax;

import java.util.Random;

public final class RandomHelper {
	private RandomHelper() {}

	private static final Random RANDOM = new Random();

	public static void setSeed(final long seed) {
		RANDOM.setSeed(seed);
	}

	public static int nextInt(final int bound) {
		return RANDOM.nextInt(bound);
	}

}
