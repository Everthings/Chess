package chess_game.listeners;

import chess_game.chess_pieces.Piece;

public interface SelectionListener {
	void selected(Piece p);
	
	void refresh();
}
