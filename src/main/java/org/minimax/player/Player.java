package org.minimax.player;

import org.minimax.Board;
import org.minimax.Color;

import java.util.List;

public abstract class Player {

	private final Color m_color;
	private final int m_turnOrder;
	private final List<Color> m_players;

	/**
	 * @param color color this player will play with
	 * @param players list of all players in order of play
	 */
	protected Player(final Color color, final List<Color> players) {
		m_color = color;
		m_turnOrder = players.indexOf(color);
		m_players = players;
	}

	protected Color getColor() {
		return m_color;
	}

	public int getTurnOrder() {
		return m_turnOrder;
	}

	public int getPlayerCount() {
		return m_players.size();
	}

	public abstract void init();

	// Use interface so players cannot alter the board directly
	public abstract int play(final Board board);

	protected abstract String name();

	@Override
	public String toString() {
		return name() + '[' + m_color.getCharacter() + ']';
	}
}
