package chess_game.chess_pieces;

import chess_game.MoveStates;
import chess_game.Pair;
import chess_game.PieceTypes;
import chess_game.Players;

public class Empty extends Piece{

	public Empty() {
		super(Players.EMPTY, PieceTypes.NULL);
	}

	@Override
	public MoveStates[][] getPossibleMoves(Pair pair, Piece[][] ChessBoard,
			Pair WKingPos, Pair BKingPos, boolean whiteKingCastle,
			boolean whiteQueenCastle, boolean blackKingCastle,
			boolean blackQueenCastle, boolean checkForResultingCheck) {
		
		return null;
	}
}
