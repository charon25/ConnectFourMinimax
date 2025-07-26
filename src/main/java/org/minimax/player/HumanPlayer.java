package org.minimax.player;

import org.minimax.BoardInterface;
import org.minimax.Cell;

import java.util.List;

import static org.minimax.Constants.IN;

public class HumanPlayer extends Player {

	public HumanPlayer(final Cell color, final int turnOrder) {
		super(color, turnOrder);
	}

	@Override
	public void init() {
	}

	@Override
	public int play(final BoardInterface board) {
		final List<Integer> playableColumns = board.getPlayableColumns();
		int column;
		do {
			System.out.print("Your turn " + this + " ! Play in column: ");
			column = IN.nextInt() - 1; // To compensate the 1-indexing of display
		} while (!playableColumns.contains(column));

		return column;
	}

	@Override
	protected String name() {
		return "Human";
	}
}
