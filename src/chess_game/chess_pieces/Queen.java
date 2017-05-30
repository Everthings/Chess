package chess_game.chess_pieces;

import java.util.ArrayList;

import chess_game.ChessUtil;
import chess_game.MoveStates;
import chess_game.Pair;
import chess_game.PieceTypes;
import chess_game.Players;

public class Queen extends Piece{

	public Queen(Players color) {
		super(color, PieceTypes.QUEEN, 8);
	}

	@Override
	public MoveStates[][] getPossibleMoves(Pair pair, Piece[][] ChessBoard,
			Pair WKingPos, Pair BKingPos, int numHalfMoves, boolean whiteKingCastle,
			boolean whiteQueenCastle, boolean blackKingCastle,
			boolean blackQueenCastle, boolean checkForResultingCheck) {
		
		Players p = ChessBoard[pair.y][pair.x].getColor();
		
		MoveStates[][] possibleMoves = new MoveStates[ChessBoard.length][ChessBoard[0].length];
		ChessUtil.initPossibleMovesArray(possibleMoves);
		
		possibleMoves = new Rook(getColor()).getPossibleMoves(pair, ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, checkForResultingCheck);
		possibleMoves = ChessUtil.addMoveStatesArray(possibleMoves, new Bishop(getColor()).getPossibleMoves(pair, ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, checkForResultingCheck));
		
		return possibleMoves;
	}

	@Override
	public ArrayList<MoveObject> getOnlyPossibleMoves(Pair pair,
			Piece[][] ChessBoard, Pair WKingPos, Pair BKingPos,
			int numHalfMoves, boolean whiteKingCastle,
			boolean whiteQueenCastle, boolean blackKingCastle,
			boolean blackQueenCastle, boolean checkForResultingCheck) {
		
		ArrayList<MoveObject> moves = new ArrayList<MoveObject>();
		
		moves = new Rook(getColor()).getOnlyPossibleMoves(pair, ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, checkForResultingCheck);
		moves.addAll(new Bishop(getColor()).getOnlyPossibleMoves(pair, ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, checkForResultingCheck));
		
		return moves;
	}
}
