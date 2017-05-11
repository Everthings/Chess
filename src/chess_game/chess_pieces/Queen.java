package chess_game.chess_pieces;

import chess_game.ChessUtil;
import chess_game.MoveStates;
import chess_game.Pair;
import chess_game.PieceTypes;
import chess_game.Players;

public class Queen extends Piece{

	public Queen(Players color) {
		super(color, PieceTypes.QUEEN);
	}

	@Override
	public MoveStates[][] getPossibleMoves(Pair pair, Piece[][] ChessBoard,
			Pair WKingPos, Pair BKingPos, boolean whiteKingCastle,
			boolean whiteQueenCastle, boolean blackKingCastle,
			boolean blackQueenCastle, boolean checkForResultingCheck) {
		
		Players p = ChessBoard[pair.y][pair.x].getColor();
		
		MoveStates[][] possibleMoves = new MoveStates[ChessBoard.length][ChessBoard[0].length];
		ChessUtil.initPossibleMovesArray(possibleMoves);
		
		possibleMoves = new Rook(getColor()).getPossibleMoves(pair, ChessBoard, WKingPos, BKingPos, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, checkForResultingCheck);
		possibleMoves = ChessUtil.addMoveStatesArray(possibleMoves, new Bishop(getColor()).getPossibleMoves(pair, ChessBoard, WKingPos, BKingPos, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, checkForResultingCheck));
		
		return possibleMoves;
	}
}
