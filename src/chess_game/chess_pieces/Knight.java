package chess_game.chess_pieces;

import chess_game.ChessUtil;
import chess_game.MoveStates;
import chess_game.Pair;
import chess_game.PieceTypes;
import chess_game.Players;

public class Knight extends Piece{

	public Knight(Players color) {
		super(color, PieceTypes.KNIGHT);
	}

	@Override
	public MoveStates[][] getPossibleMoves(Pair pair, Piece[][] ChessBoard,
			Pair WKingPos, Pair BKingPos, int numHalfMoves, boolean whiteKingCastle,
			boolean whiteQueenCastle, boolean blackKingCastle,
			boolean blackQueenCastle, boolean checkForResultingCheck) {
		

		Players p = ChessBoard[pair.y][pair.x].getColor();
		
		MoveStates[][] possibleMoves = new MoveStates[ChessBoard.length][ChessBoard[0].length];
		ChessUtil.initPossibleMovesArray(possibleMoves);

		if(pair.y > 1 && pair.x > 0){	
			if(ChessBoard[pair.y - 2][pair.x - 1].getColor() != p){
				if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y - 2), ChessBoard), WKingPos, BKingPos))
					possibleMoves[pair.y - 2][pair.x - 1] = MoveStates.OPEN;
			}
		}
		if(pair.y < 6 && pair.x > 0){
			if(ChessBoard[pair.y + 2][pair.x - 1].getColor() != p){
				if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y + 2), ChessBoard), WKingPos, BKingPos))
					possibleMoves[pair.y + 2][pair.x - 1] = MoveStates.OPEN;
			}
		}
		if(pair.y > 1 && pair.x < 7){
			if(ChessBoard[pair.y - 2][pair.x + 1].getColor() != p){
				if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y - 2), ChessBoard), WKingPos, BKingPos))
					possibleMoves[pair.y - 2][pair.x + 1] = MoveStates.OPEN;
			}	
		}
		if(pair.y > 0 && pair.x < 6){
			if(ChessBoard[pair.y - 1][pair.x + 2].getColor() != p){
				if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 2, pair.y - 1), ChessBoard), WKingPos, BKingPos))
					possibleMoves[pair.y - 1][pair.x + 2] = MoveStates.OPEN;
			}	
		}
		if(pair.y < 6 && pair.x < 7){
			if(ChessBoard[pair.y + 2][pair.x + 1].getColor() != p){
				if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y + 2), ChessBoard), WKingPos, BKingPos))
					possibleMoves[pair.y + 2][pair.x + 1] = MoveStates.OPEN;
			}
		}
		if(pair.y > 0 && pair.x > 1){
			if(ChessBoard[pair.y - 1][pair.x - 2].getColor() != p){
				if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 2, pair.y - 1), ChessBoard), WKingPos, BKingPos))
					possibleMoves[pair.y - 1][pair.x - 2] = MoveStates.OPEN;
			}
		}
		if(pair.y < 7 && pair.x > 1){
			if(ChessBoard[pair.y + 1][pair.x - 2].getColor() != p){
				if(!ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 2, pair.y + 1), ChessBoard), WKingPos, BKingPos))
					possibleMoves[pair.y + 1][pair.x - 2] = MoveStates.OPEN;
			}
		}
		if(pair.y < 7 && pair.x < 6){
			if(ChessBoard[pair.y + 1][pair.x + 2].getColor() != p){
				if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 2, pair.y + 1), ChessBoard), WKingPos, BKingPos))
					possibleMoves[pair.y + 1][pair.x + 2] = MoveStates.OPEN;
			}
		}
		
		return possibleMoves;
	}
}
