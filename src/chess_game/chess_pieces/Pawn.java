package chess_game.chess_pieces;

import chess_game.ChessUtil;
import chess_game.MoveStates;
import chess_game.Pair;
import chess_game.PieceTypes;
import chess_game.Players;

public class Pawn extends Piece{

	public Pawn(Players color) {
		super(color, PieceTypes.PAWN);
	}

	@Override
	public MoveStates[][] getPossibleMoves(Pair pair, Piece[][] ChessBoard,
			Pair WKingPos, Pair BKingPos, boolean whiteKingCastle,
			boolean whiteQueenCastle, boolean blackKingCastle,
			boolean blackQueenCastle, boolean checkForResultingCheck) {
	
		Players p = ChessBoard[pair.y][pair.x].getColor();
		
		MoveStates[][] possibleMoves = new MoveStates[ChessBoard.length][ChessBoard[0].length];
		ChessUtil.initPossibleMovesArray(possibleMoves);
		
		if(p == Players.WHITE){
			if(pair.y > 0 && ChessBoard[pair.y - 1][pair.x].equals(new Empty())){
				if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x, pair.y - 1), ChessBoard), WKingPos, BKingPos))
					possibleMoves[pair.y - 1][pair.x] = MoveStates.OPEN;
			}
			if(pair.y == 6 && ChessBoard[pair.y - 2][pair.x].equals(new Empty()) && ChessBoard[pair.y - 1][pair.x].equals(new Empty())){
				if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x, 4), ChessBoard), WKingPos, BKingPos))
					possibleMoves[4][pair.x] = MoveStates.OPEN;
			}
			if(pair.y > 0 && pair.x < 7){
				if(ChessBoard[pair.y - 1][pair.x + 1].getColor() == Players.BLACK){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y - 1), ChessBoard), WKingPos, BKingPos))
						possibleMoves[pair.y - 1][pair.x + 1] = MoveStates.OPEN;
				}
			}
			if(pair.y > 0 && pair.x > 0){
				if(ChessBoard[pair.y - 1][pair.x - 1].getColor() == Players.BLACK){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y - 1), ChessBoard), WKingPos, BKingPos))
						possibleMoves[pair.y - 1][pair.x - 1] = MoveStates.OPEN;
				}
			}
		}

		if(p == Players.BLACK){
			if(pair.y == 1 && ChessBoard[pair.y + 2][pair.x].equals(new Empty()) && ChessBoard[pair.y + 1][pair.x].equals(new Empty())){
				if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x, 3), ChessBoard), WKingPos, BKingPos))
					possibleMoves[3][pair.x] = MoveStates.OPEN;
			}
			if(pair.y < 7 && ChessBoard[pair.y + 1][pair.x].equals(new Empty())){
				if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x, pair.y + 1), ChessBoard), WKingPos, BKingPos))
					possibleMoves[pair.y + 1][pair.x] = MoveStates.OPEN;
			}
			if(pair.y > 0 && pair.x < 7){
				if(ChessBoard[pair.y + 1][pair.x + 1].getColor() == Players.WHITE){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y + 1), ChessBoard), WKingPos, BKingPos))
						possibleMoves[pair.y + 1][pair.x + 1] = MoveStates.OPEN;
				}
			}
			if( pair.y > 0 && pair.x > 0){
				if(ChessBoard[pair.y + 1][pair.x - 1].getColor() == Players.WHITE){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y + 1), ChessBoard), WKingPos, BKingPos))
						possibleMoves[pair.y + 1][pair.x - 1] = MoveStates.OPEN;
				}
			}
		}	
		
		return possibleMoves;
	}
}
