package chess_game;

import chess_game.analysis.Position;
import chess_game.chess_pieces.Empty;
import chess_game.chess_pieces.King;
import chess_game.chess_pieces.Piece;
import chess_game.chess_pieces.Queen;
import chess_game.chess_pieces.Rook;

public class ChessUtil {
	
	public static long movePiece(long hash, Players colorTurn, Position p, Pair oldblock, Pair newblock, MoveStates type, int numHalfMoves){

		Piece[][] newChessBoard = p.getBoard();
		
		if(type == MoveStates.CASTLE){
			hash = castle(hash, newChessBoard, oldblock, newblock, numHalfMoves);
		}else if(type == MoveStates.PASSANT){
			hash = passant(hash, newChessBoard, oldblock, newblock, numHalfMoves);
		}else if(type == MoveStates.OPEN || type == MoveStates.TAKE){	
			newChessBoard[oldblock.y][oldblock.x].incrementMoves();
			newChessBoard[oldblock.y][oldblock.x].setLastMoved(numHalfMoves);
			
			if(newChessBoard[newblock.y][newblock.x].getType() == PieceTypes.KING)
				p.kingsAlive = false;

			hash = Position.addXOR(hash, newblock.y, newblock.x, newChessBoard[newblock.y][newblock.x].getIndex());
			newChessBoard[newblock.y][newblock.x] = newChessBoard[oldblock.y][oldblock.x];
			hash = Position.addXOR(hash, newblock.y, newblock.x, newChessBoard[newblock.y][newblock.x].getIndex());
			hash = Position.addXOR(hash, oldblock.y, oldblock.x, newChessBoard[oldblock.y][oldblock.x].getIndex());
			newChessBoard[oldblock.y][oldblock.x] = new Empty();
			
			if(colorTurn == Players.WHITE){
				if(newblock.y == 0 && newChessBoard[newblock.y][newblock.x].getType() == PieceTypes.PAWN){
					newChessBoard[newblock.y][newblock.x] = new Queen(Players.WHITE);
					
					hash = Position.addXOR(hash, newblock.y, newblock.x, newChessBoard[newblock.y][newblock.x].getIndex());
				}
			}else if(colorTurn == Players.BLACK){
				if(newblock.y == 7 && newChessBoard[newblock.y][newblock.x].getType() == PieceTypes.PAWN){
					newChessBoard[newblock.y][newblock.x] = new Queen(Players.BLACK);
					
					hash = Position.addXOR(hash, newblock.y, newblock.x, newChessBoard[newblock.y][newblock.x].getIndex());
				}
			}
			
		}
		
		return hash;
	}
	
	private static long castle(long hash, Piece[][] newChessBoard, Pair oldblock, Pair newblock, int numHalfMoves){
	
		Players p = newChessBoard[oldblock.y][oldblock.x].getColor();
		
		hash = Position.addXOR(hash, newblock.y, newblock.x, newChessBoard[newblock.y][newblock.x].getIndex());
		hash = Position.addXOR(hash, oldblock.y, oldblock.x, newChessBoard[oldblock.y][oldblock.x].getIndex());
		newChessBoard[oldblock.y][oldblock.x] = new Empty();
		newChessBoard[newblock.y][newblock.x] = new Empty();
		
		if(newblock.x > oldblock.x){
			newChessBoard[newblock.y][6] = new King(p);// places king in correct spot
			hash = Position.addXOR(hash, newblock.y, 6, newChessBoard[newblock.y][6].getIndex());
			newChessBoard[newblock.y][5] = new Rook(p);
			hash = Position.addXOR(hash, newblock.y, 5, newChessBoard[newblock.y][5].getIndex());
			
			newChessBoard[newblock.y][6].incrementMoves();
			newChessBoard[newblock.y][6].setLastMoved(numHalfMoves);
			newChessBoard[newblock.y][5].incrementMoves();
			newChessBoard[newblock.y][5].setLastMoved(numHalfMoves);
		}else{
			newChessBoard[newblock.y][2] = new King(p);
			hash = Position.addXOR(hash, newblock.y, 2, newChessBoard[newblock.y][2].getIndex());
			newChessBoard[newblock.y][3] = new Rook(p);
			hash = Position.addXOR(hash, newblock.y, 3, newChessBoard[newblock.y][3].getIndex());
			
			newChessBoard[newblock.y][2].incrementMoves();
			newChessBoard[newblock.y][2].setLastMoved(numHalfMoves);
			newChessBoard[newblock.y][3].incrementMoves();
			newChessBoard[newblock.y][3].setLastMoved(numHalfMoves);
		}
		
		return hash;
	}
	
	private static long passant(long hash, Piece[][] newChessBoard, Pair oldblock, Pair newblock, int numHalfMoves){
		
		Players p = newChessBoard[oldblock.y][oldblock.x].getColor();
		
		if(p == Players.WHITE){
			hash = Position.addXOR(hash, newblock.y + 1, newblock.x, newChessBoard[newblock.y + 1][newblock.x].getIndex());
			newChessBoard[newblock.y + 1][newblock.x] = new Empty();
		}if(p == Players.BLACK){
			hash = Position.addXOR(hash, newblock.y - 1, newblock.x, newChessBoard[newblock.y + 1][newblock.x].getIndex());
			newChessBoard[newblock.y - 1][newblock.x] = new Empty();
		}
		
		newChessBoard[oldblock.y][oldblock.x].incrementMoves();
		newChessBoard[oldblock.y][oldblock.x].setLastMoved(numHalfMoves);
		newChessBoard[newblock.y][newblock.x] = newChessBoard[oldblock.y][oldblock.x];
		hash = Position.addXOR(hash, newblock.y, newblock.x, newChessBoard[newblock.y][newblock.x].getIndex());
		newChessBoard[oldblock.y][oldblock.x] = new Empty();
		hash = Position.addXOR(hash, oldblock.y, oldblock.x, newChessBoard[oldblock.y][oldblock.x].getIndex());

		return hash;
	}
	
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
							if(possibleMoves[j][i] == MoveStates.OPEN || possibleMoves[j][i] == MoveStates.TAKE){
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
				else if(pos2[a][b] == MoveStates.TAKE)
					possibleMoves[a][b] = MoveStates.TAKE;
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
							if(possibleMoves[BKingPos.y][BKingPos.x] == MoveStates.TAKE){
								return true;
							}
						}else if(p == Players.WHITE){
							if(possibleMoves[WKingPos.y][WKingPos.x] == MoveStates.TAKE){
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
