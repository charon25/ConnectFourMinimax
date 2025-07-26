package org.minimax.player;

import org.minimax.Board;
import org.minimax.BoardInterface;
import org.minimax.Cell;

public interface Player {

	/**
	 * @param turnOrder between 0 and playerCount - 1
	 * @param color color this player will play with
	 */
	void init(final int turnOrder, final Cell color);

	// Use interface so players cannot alter the board directly
	int play(final BoardInterface board);

}
