package chess_game;

import chess_game.chess_pieces.Empty;
import chess_game.chess_pieces.Piece;

public class ChessUtil {
	
	public static MoveStates[][] getPossibleMoves(Pair pair, Piece[][] ChessBoard,  Pair WKingPos, Pair BKingPos,
			boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle, boolean checkForResultingCheck){
		
		MoveStates[][] possibleMoves = new MoveStates[ChessBoard.length][ChessBoard[0].length];
		Piece piece = ChessBoard[pair.y][pair.x];
		
		if(!piece.equals(new Empty()))
			possibleMoves = piece.getPossibleMoves(pair, ChessBoard, WKingPos, BKingPos, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, checkForResultingCheck);
		
		return possibleMoves;
	}
	
	public static MoveStates[][] getAllPossibleMoves(Piece[][] ChessBoard,  Pair WKingPos, Pair BKingPos,
			boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle, boolean checkForResultingCheck){
		
		MoveStates[][] possibleMoves = new MoveStates[ChessBoard.length][ChessBoard[0].length];
		
		for(int a = 0; a < 8; a++){
			for(int b = 0; b < 8; b++){
				Piece piece = ChessBoard[b][a];
				
				if(!piece.equals(new Empty())){
					MoveStates[][] temp = piece.getPossibleMoves(new Pair(a, b), ChessBoard, WKingPos, BKingPos, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, checkForResultingCheck);
					
					for(int i = 0; i < 8; i++){
						for(int j = 0; j < 8; j++){
							if(temp[i][j] == MoveStates.CASTLE)
								possibleMoves[i][j] = MoveStates.CASTLE;
							else if(temp[i][j] == MoveStates.OPEN){
								possibleMoves[i][j] = MoveStates.OPEN;
							}
						}
					}
				}

			}
		}
		
		return possibleMoves;
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

	
	//public static boolean isMate(){

	//}

	public static boolean isCheck(Players p, Piece[][] ChessBoard, Pair WKingPos, Pair BKingPos){
		
		MoveStates[][] possibleMoves = new MoveStates[ChessBoard.length][ChessBoard[0].length];
		
		for(int a = 0; a < 8; a++){
			for(int b = 0; b < 8; b++){
				if(!ChessBoard[b][a].equals(new Empty())){
					if(ChessBoard[b][a].getColor() != p){
						
						if(p == Players.BLACK){
							possibleMoves = getPossibleMoves(new Pair(a, b), ChessBoard, WKingPos, BKingPos, false, false, false, false, false);//all false because king can't castle and shouldn't check for check(unless you like infinite loops :))
						}else{
							possibleMoves = getPossibleMoves(new Pair(a, b), ChessBoard, WKingPos, BKingPos, false, false, false, false, false);
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
			for(int b = 0; b < 8; b++){
				newBoard[a][b] = ChessBoard[a][b];
			}
		}
		
		newBoard[moveTo.y][moveTo.x] = newBoard[initial.y][initial.x];
		newBoard[initial.y][initial.x] = new Empty();
		
		return newBoard;
	}
}
