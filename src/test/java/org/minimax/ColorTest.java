package org.minimax;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {
	@Test
	public void assertNoneIsFirst() {
		assertEquals(0, Color.NONE.ordinal());
	}
}