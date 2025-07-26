package org.minimax.player;

import org.minimax.Cell;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public enum PlayerType {
	RANDOM(RandomPlayer.class),
	;

	private final Class<? extends Player> m_class;

	PlayerType(final Class<? extends Player> aClass) {
		m_class = aClass;
	}

	public Player getNewPlayer(final Cell color, final int turnOrder) {
		try {
			return (Player) m_class.getDeclaredConstructors()[0].newInstance(color, turnOrder);
		} catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Could not instantiate player of type " + this,e);
		}
	}
}
