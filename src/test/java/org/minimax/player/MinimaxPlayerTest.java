package org.minimax.player;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MinimaxPlayerTest {
	@Test
	public void testScore() {
		final List<MinimaxPlayer.Score> scores = List.of(
				new MinimaxPlayer.Score(45, -1),
				new MinimaxPlayer.Score(23, -2),
				new MinimaxPlayer.Score(12, -3),
				new MinimaxPlayer.Score(89, -4),
				new MinimaxPlayer.Score(-23, 0),
				new MinimaxPlayer.Score(0, 0),
				new MinimaxPlayer.Score(76, 0),
				new MinimaxPlayer.Score(32, 4),
				new MinimaxPlayer.Score(59, 3),
				new MinimaxPlayer.Score(73, 2),
				new MinimaxPlayer.Score(12, 1)
		);

		for (int i = 0; i < scores.size(); i++) {
			for (int j = 0; j < scores.size(); j++) {
				if (i > j) {
					assertTrue(scores.get(i).greaterThan(scores.get(j)));
					assertNotEquals(scores.get(i), scores.get(j));
					assertFalse(scores.get(j).greaterThan(scores.get(i)));
				} else if (i < j) {
					assertFalse(scores.get(i).greaterThan(scores.get(j)));
					assertNotEquals(scores.get(i), scores.get(j));
					assertTrue(scores.get(j).greaterThan(scores.get(i)));
				} else {
					assertFalse(scores.get(i).greaterThan(scores.get(j)));
					assertEquals(scores.get(i), scores.get(j));
					assertFalse(scores.get(j).greaterThan(scores.get(i)));
				}
			}
		}
	}
}