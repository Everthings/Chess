package chess_game.chess_pieces;

import java.util.ArrayList;

import chess_game.ChessUtil;
import chess_game.MoveStates;
import chess_game.Pair;
import chess_game.PieceTypes;
import chess_game.Players;

public class Bishop extends Piece{

	public Bishop(Players color) {
		super(color, PieceTypes.BISHOP, 4);
	}

	@Override
	public MoveStates[][] getPossibleMoves(Pair pair, Piece[][] ChessBoard,
			Pair WKingPos, Pair BKingPos, int numHalfMoves, boolean whiteKingCastle,
			boolean whiteQueenCastle, boolean blackKingCastle,
			boolean blackQueenCastle, boolean checkForResultingCheck) {
		
		Players p = ChessBoard[pair.y][pair.x].getColor();
		
		MoveStates[][] possibleMoves = new MoveStates[ChessBoard.length][ChessBoard[0].length];
		ChessUtil.initPossibleMovesArray(possibleMoves);
		
		for(int a = 1; a < 8; a++){
			if(pair.y + a <= 7 && pair.x + a <= 7){
				if(ChessBoard[pair.y + a][pair.x + a].equals(new Empty())){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + a, pair.y + a), ChessBoard), WKingPos, BKingPos))
						possibleMoves[pair.y + a][pair.x + a] = MoveStates.OPEN;
				}else if(ChessBoard[pair.y + a][pair.x + a].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + a, pair.y + a), ChessBoard), WKingPos, BKingPos))
						possibleMoves[pair.y + a][pair.x + a] = MoveStates.TAKE;
					break;
				}else{
					break;
				}
			}
		}
		for(int a = 1; a < 8; a++){
			if(pair.y + a <= 7 && pair.x - a >= 0){
				if(ChessBoard[pair.y + a][pair.x - a].equals(new Empty())){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - a, pair.y + a), ChessBoard), WKingPos, BKingPos))
						possibleMoves[pair.y + a][pair.x - a] = MoveStates.OPEN;
				}else if(ChessBoard[pair.y + a][pair.x - a].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - a, pair.y + a), ChessBoard), WKingPos, BKingPos))
						possibleMoves[pair.y + a][pair.x - a] = MoveStates.TAKE;
					break;
				}else{
					
					break;
				}
			}
		}
		for(int a = 1; a < 8; a++){
			if(pair.y - a >= 0 && pair.x + a <= 7){
				if(ChessBoard[pair.y - a][pair.x + a].equals(new Empty())){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + a, pair.y - a), ChessBoard), WKingPos, BKingPos))
						possibleMoves[pair.y - a][pair.x + a] = MoveStates.OPEN;
				}else if(ChessBoard[pair.y - a][pair.x + a].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + a, pair.y - a), ChessBoard), WKingPos, BKingPos))
						possibleMoves[pair.y - a][pair.x + a] = MoveStates.TAKE;
						break;
				}else{
					break;
				}
			}
		}
		for(int a = 1; a < 8; a++){
			if(pair.y - a >= 0 && pair.x - a >= 0){
				if(ChessBoard[pair.y - a][pair.x - a].equals(new Empty())){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - a, pair.y - a), ChessBoard), WKingPos, BKingPos))
						possibleMoves[pair.y - a][pair.x - a] = MoveStates.OPEN;
				}else if(ChessBoard[pair.y - a][pair.x - a].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - a, pair.y - a), ChessBoard), WKingPos, BKingPos))
						possibleMoves[pair.y - a][pair.x - a] = MoveStates.TAKE;
					break;
				}else{
					break;
				}
			}
		}
		
		return possibleMoves;
	}

	@Override
	public ArrayList<MoveObject> getOnlyPossibleMoves(Pair pair,
			Piece[][] ChessBoard, Pair WKingPos, Pair BKingPos,
			int numHalfMoves, boolean whiteKingCastle,
			boolean whiteQueenCastle, boolean blackKingCastle,
			boolean blackQueenCastle, boolean checkForResultingCheck) {
		
		ArrayList<MoveObject> moves = new ArrayList<MoveObject>();
		
		Players p = ChessBoard[pair.y][pair.x].getColor();
		
		for(int a = 1; a < 8; a++){
			if(pair.y + a <= 7 && pair.x + a <= 7){
				if(ChessBoard[pair.y + a][pair.x + a].equals(new Empty())){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + a, pair.y + a), ChessBoard), WKingPos, BKingPos))
						moves.add(new MoveObject(pair, new Pair(pair.x + a, pair.y + a), MoveStates.OPEN));
				}else if(ChessBoard[pair.y + a][pair.x + a].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + a, pair.y + a), ChessBoard), WKingPos, BKingPos))
						moves.add(new MoveObject(pair, new Pair(pair.x + a, pair.y + a), MoveStates.TAKE));
					break;
				}else{
					break;
				}
			}
		}
		for(int a = 1; a < 8; a++){
			if(pair.y + a <= 7 && pair.x - a >= 0){
				if(ChessBoard[pair.y + a][pair.x - a].equals(new Empty())){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - a, pair.y + a), ChessBoard), WKingPos, BKingPos))
						moves.add(new MoveObject(pair, new Pair(pair.x - a, pair.y + a), MoveStates.OPEN));
				}else if(ChessBoard[pair.y + a][pair.x - a].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - a, pair.y + a), ChessBoard), WKingPos, BKingPos))
						moves.add(new MoveObject(pair, new Pair(pair.x - a, pair.y + a), MoveStates.TAKE));
					break;
				}else{
					
					break;
				}
			}
		}
		for(int a = 1; a < 8; a++){
			if(pair.y - a >= 0 && pair.x + a <= 7){
				if(ChessBoard[pair.y - a][pair.x + a].equals(new Empty())){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + a, pair.y - a), ChessBoard), WKingPos, BKingPos))
						moves.add(new MoveObject(pair, new Pair(pair.x + a, pair.y - a), MoveStates.OPEN));
				}else if(ChessBoard[pair.y - a][pair.x + a].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + a, pair.y - a), ChessBoard), WKingPos, BKingPos))
						moves.add(new MoveObject(pair, new Pair(pair.x + a, pair.y - a), MoveStates.TAKE));
					break;
				}else{
					break;
				}
			}
		}
		for(int a = 1; a < 8; a++){
			if(pair.y - a >= 0 && pair.x - a >= 0){
				if(ChessBoard[pair.y - a][pair.x - a].equals(new Empty())){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - a, pair.y - a), ChessBoard), WKingPos, BKingPos))
						moves.add(new MoveObject(pair, new Pair(pair.x - a, pair.y - a), MoveStates.OPEN));
				}else if(ChessBoard[pair.y - a][pair.x - a].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - a, pair.y - a), ChessBoard), WKingPos, BKingPos))
						moves.add(new MoveObject(pair, new Pair(pair.x - a, pair.y - a), MoveStates.TAKE));
					break;
				}else{
					break;
				}
			}
		}
		
		return moves;
	}
}
