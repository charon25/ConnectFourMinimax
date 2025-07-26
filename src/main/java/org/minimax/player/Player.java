package org.minimax.player;

import org.minimax.Board;
import org.minimax.BoardInterface;
import org.minimax.Cell;

public abstract class Player {

	private final Cell m_color;
	private final int m_turnOrder;

	/**
	 * @param turnOrder between 0 and playerCount - 1
	 * @param color color this player will play with
	 */
	protected Player(final Cell color, final int turnOrder) {
		m_color = color;
		m_turnOrder = turnOrder;
	}

	protected Cell getColor() {
		return m_color;
	}

	protected int getTurnOrder() {
		return m_turnOrder;
	}

	public abstract void init();

	// Use interface so players cannot alter the board directly
	public abstract int play(final BoardInterface board);

	protected abstract String name();

	@Override
	public String toString() {
		return name() + '[' + m_color + ',' + m_turnOrder + ']';
	}
}
