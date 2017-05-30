package chess_game.chess_pieces;

import java.util.ArrayList;

import chess_game.MoveStates;
import chess_game.Pair;
import chess_game.PieceTypes;
import chess_game.Players;

public class Empty extends Piece{

	public Empty() {
		super(Players.EMPTY, PieceTypes.NULL, -100);
	}

	@Override
	public MoveStates[][] getPossibleMoves(Pair pair, Piece[][] ChessBoard,
			Pair WKingPos, Pair BKingPos, int numHalfMoves, boolean whiteKingCastle,
			boolean whiteQueenCastle, boolean blackKingCastle,
			boolean blackQueenCastle, boolean checkForResultingCheck) {
		
		return null;
	}

	@Override
	public ArrayList<MoveObject> getOnlyPossibleMoves(Pair pair,
			Piece[][] ChessBoard, Pair WKingPos, Pair BKingPos,
			int numHalfMoves, boolean whiteKingCastle,
			boolean whiteQueenCastle, boolean blackKingCastle,
			boolean blackQueenCastle, boolean checkForResultingCheck) {
		// TODO Auto-generated method stub
		return null;
	}
}
