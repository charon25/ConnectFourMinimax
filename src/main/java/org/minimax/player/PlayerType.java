package org.minimax.player;

import java.util.function.Supplier;

public enum PlayerType {
	RANDOM(RandomPlayer::new),
	;

	private final Supplier<Player> m_playerSupplier;

	PlayerType(final Supplier<Player> playerSupplier) {
		m_playerSupplier = playerSupplier;
	}

	public Player getNewPlayer() {
		return m_playerSupplier.get();
	}
}
