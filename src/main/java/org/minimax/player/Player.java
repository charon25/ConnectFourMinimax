package org.minimax.player;

import org.minimax.Board;
import org.minimax.BoardInterface;
import org.minimax.Cell;

public interface Player {

	void init(final Cell color);

	// Use interface so players cannot alter the board directly
	int play(final BoardInterface board);

}
