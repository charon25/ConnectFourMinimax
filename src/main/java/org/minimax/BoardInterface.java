package org.minimax;

import java.util.List;

public interface BoardInterface {

	int getWidth();
	int getHeight();

	Cell[][] getBoardCopy();

	boolean canPlay(final int column);
	List<Integer> getPlayableColumns();

}
