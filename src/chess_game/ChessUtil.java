package chess_game;

import chess_game.chess_pieces.Empty;
import chess_game.chess_pieces.King;
import chess_game.chess_pieces.Piece;

public class ChessUtil {
	
	public static int getNumberOfPieces(Piece[][] ChessBoard){
		int num = 0;
		
		for(int a  = 0; a < 8; a++){
			for(int b = 0; b < 8; b++){
				if(!ChessBoard[a][b].equals(new Empty())){
					num++;
				}
			}
		}
		
		return num;
	}
	
	public static MoveStates[][] getPossibleMoves(Pair pair, Piece[][] ChessBoard,  Pair WKingPos, Pair BKingPos, int numHalfMoves,
			boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle, boolean checkForResultingCheck){
		
		MoveStates[][] possibleMoves = new MoveStates[ChessBoard.length][ChessBoard[0].length];
		Piece piece = ChessBoard[pair.y][pair.x];
		
		if(!piece.equals(new Empty()))
			possibleMoves = piece.getPossibleMoves(pair, ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, checkForResultingCheck);
		
		return possibleMoves;
	}
	
	public static boolean arePossibleMoves(Players p, Piece[][] ChessBoard,  Pair WKingPos, Pair BKingPos, int numHalfMoves,
			boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle, boolean checkForResultingCheck){
		
		for(int a = 0; a < ChessBoard.length; a++){
			for(int b = 0; b < ChessBoard[a].length; b++){
				Piece piece = ChessBoard[b][a];
				
				if(piece.getColor() == p){
					MoveStates[][] possibleMoves = piece.getPossibleMoves(new Pair(a, b), ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, checkForResultingCheck);
					
					for(int i = 0; i < possibleMoves.length; i++){
						for(int j = 0; j < possibleMoves[i].length; j++){
							if(possibleMoves[j][i] == MoveStates.OPEN){
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}
	
	public static Pair getKingPosition(Players p, Piece[][] ChessBoard){
		for(int a = 0; a < ChessBoard.length; a++){
			for(int b = 0; b < ChessBoard[a].length; b++){
				if(ChessBoard[b][a].equals(new King(p))){
					return new Pair(a, b);
				}
			}
		}
		
		return null;
	}
	
	public static void initPossibleMovesArray(MoveStates[][] possibleMoves){
		for(int a = 0; a < 8; a++){
			for(int b = 0; b < 8; b++){
				possibleMoves[a][b] = MoveStates.CLOSED;
			}
		}
	}
	
	public static MoveStates[][] addMoveStatesArray(MoveStates[][] pos1, MoveStates[][] pos2){
		MoveStates[][] possibleMoves = pos1;
		
		for(int a = 0; a < pos2.length; a++){
			for(int b = 0; b < pos2[0].length; b++){
				if(pos2[a][b] == MoveStates.OPEN)
					possibleMoves[a][b] = MoveStates.OPEN;
			}
		}
		
		return possibleMoves;
	}

	
	public static Results isMateOrDraw(Players p, Piece[][] ChessBoard, Pair WKingPos, Pair BKingPos, int numHalfMoves,
			boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle){
		
		if(!arePossibleMoves(p, ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, true)){
			if(isCheck(p, ChessBoard, WKingPos, BKingPos)){
				return Results.WIN;
			}else{
				return Results.DRAW;
			}
		}
		
		if(getNumberOfPieces(ChessBoard) <= 2){
			return Results.DRAW;
		}
		
		return Results.NONE;
	}
	
	public static boolean isCheck(Players p, Piece[][] ChessBoard, Pair WKingPos, Pair BKingPos){
		
		MoveStates[][] possibleMoves = new MoveStates[ChessBoard.length][ChessBoard[0].length];
		
		for(int a = 0; a < 8; a++){
			for(int b = 0; b < 8; b++){
				if(!ChessBoard[b][a].equals(new Empty())){
					if(ChessBoard[b][a].getColor() != p){
						
						if(p == Players.BLACK){
							possibleMoves = getPossibleMoves(new Pair(a, b), ChessBoard, WKingPos, BKingPos, 0, false, false, false, false, false);//all false because king can't castle and shouldn't check for check(unless you like infinite loops :))
						}else{
							possibleMoves = getPossibleMoves(new Pair(a, b), ChessBoard, WKingPos, BKingPos, 0, false, false, false, false, false);
						}
						
						if(p == Players.BLACK){
							if(possibleMoves[BKingPos.y][BKingPos.x] == MoveStates.OPEN){
								return true;
							}
						}else if(p == Players.WHITE){
							if(possibleMoves[WKingPos.y][WKingPos.x] == MoveStates.OPEN){
								return true;
							}
						}
					}
				}
			}
		}
	
		return false;
	}

	public static Piece[][] getChessBoardAfterMove(Pair initial, Pair moveTo, Piece[][] ChessBoard){
		Piece[][] newBoard =  new Piece[ChessBoard.length][ChessBoard[0].length];
		
		for(int a = 0; a < 8; a++){
			newBoard[a] = ChessBoard[a].clone();
		}
		
		newBoard[moveTo.y][moveTo.x] = newBoard[initial.y][initial.x];
		newBoard[initial.y][initial.x] = new Empty();
		
		return newBoard;
	}
}
