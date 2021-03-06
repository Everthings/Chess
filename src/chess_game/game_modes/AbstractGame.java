package chess_game.game_modes;

import chess_game.ChessUtil;
import chess_game.MoveStates;
import chess_game.Pair;
import chess_game.Players;
import chess_game.Results;
import chess_game.chess_pieces.Empty;
import chess_game.chess_pieces.King;
import chess_game.chess_pieces.Piece;
import chess_game.chess_pieces.Rook;

public abstract class AbstractGame {
	
	Pair WKingPos;
	Pair BKingPos;
	
	private volatile Piece[][] ChessBoard;
	private int numWhiteChecks = 0;//number of checks white has made on opponent
	private int numBlackChecks = 0;//and vice versa
	private int numHalfMoves = 0;
	
	private boolean whiteKingCastle = true;
	private boolean whiteQueenCastle = true;
	private boolean blackKingCastle = true;
	private boolean blackQueenCastle = true;
	
	public void initVariables(){
		ChessBoard = initChessBoard();
		
		WKingPos = ChessUtil.getKingPosition(Players.WHITE, ChessBoard);
		BKingPos = ChessUtil.getKingPosition(Players.BLACK, ChessBoard);
	}
	
	public void incrementHalfMoves(){
		numHalfMoves++;
	}
	
	public void decrementHalfMoves(){
		numHalfMoves--;
	}
	
	public synchronized Piece[][] getChessBoard(){
		Piece[][] newChessBoard = ChessBoard.clone();
		for(int i = 0; i < ChessBoard.length; i++)
			newChessBoard[i] = ChessBoard[i].clone();

		return newChessBoard;
	}
	
	public int getHalfMoves(){
		return numHalfMoves;
	}
	
	public int getTimesChecked(Players p){
		if(p == Players.WHITE)
			return numBlackChecks;
		else
			return numWhiteChecks;
	}
	
	public void incrementNumChecks(Players p){
		if(p == Players.WHITE)
			numWhiteChecks++;
		else
			numBlackChecks++;
	}
	
	public void decrementNumChecks(Players p){
		if(p == Players.WHITE)
			numWhiteChecks--;
		else
			numBlackChecks--;
	}
	
	public void resetChecks(){
		numWhiteChecks = 0;
		numBlackChecks = 0;
	}
	
	public Pair getWhiteKingPos(){
		return WKingPos;
	}
	
	public Pair getBlackKingPos(){
		return BKingPos;
	}
	
	public void setWhiteKingPos(Pair newPos){
		WKingPos = newPos;
	}
	
	public void setBlackKingPos(Pair newPos){
		BKingPos = newPos;
	}
	
	public boolean getKingCastle(Players p){
		if(p == Players.WHITE){
			return whiteKingCastle;
		}else{
			return blackKingCastle;
		}
	}
	
	public boolean getQueenCastle(Players p){
		if(p == Players.WHITE){
			return whiteQueenCastle;
		}else{
			return blackQueenCastle;
		}
	}
	
	public synchronized void setChessBoard(Piece[][] ChessBoard){
		Piece[][] newChessBoard = ChessBoard.clone();
		for(int i = 0; i < ChessBoard.length; i++)
			newChessBoard[i] = ChessBoard[i].clone();
		
		this.ChessBoard = newChessBoard;
	}
	
	public boolean whiteKingCastle(){
		return whiteKingCastle;
	}
	
	public boolean whiteQueenCastle(){
		return whiteQueenCastle;
	}
	
	public boolean blackKingCastle(){
		return blackKingCastle;
	}
	
	public boolean blackQueenCastle(){
		return blackQueenCastle;
	}
	
	public void setHalfMoves(int num){
		numHalfMoves = num;
	}
	
	public void movePiece(Pair oldblock, Pair newblock, MoveStates type){
		//checks to stop future castling
		
		if(type == MoveStates.CASTLE){
			castle(oldblock, newblock);
		}else if(type == MoveStates.PASSANT){
			passant(oldblock, newblock);
		}else if(type == MoveStates.OPEN || type == MoveStates.TAKE){
			if(ChessBoard[oldblock.y][oldblock.x].equals(new King(Players.BLACK))){
				BKingPos = newblock;
				blackKingCastle = false;
				blackQueenCastle = false;
			}else if(ChessBoard[oldblock.y][oldblock.x].equals(new King(Players.WHITE))){
				WKingPos = newblock;
				whiteKingCastle = false;
				whiteQueenCastle = false;
			}else if(ChessBoard[oldblock.y][oldblock.x].equals(new Rook(Players.BLACK))){
				if(oldblock.x > BKingPos.x)
					blackKingCastle = false;
				else
					blackQueenCastle = false;
			}else if(ChessBoard[oldblock.y][oldblock.x].equals(new Rook(Players.WHITE))){
				if(oldblock.x > WKingPos.x)
					whiteKingCastle = false;
				else
					whiteQueenCastle = false;
			}
	
			Piece[][] newChessBoard = getChessBoard();
			
			newChessBoard[oldblock.y][oldblock.x].incrementMoves();
			newChessBoard[oldblock.y][oldblock.x].setLastMoved(numHalfMoves);
			newChessBoard[newblock.y][newblock.x] = newChessBoard[oldblock.y][oldblock.x];
			newChessBoard[oldblock.y][oldblock.x] = new Empty();
			
			setChessBoard(newChessBoard);

			numHalfMoves++;
		}
	}
	
	public void passant(Pair oldblock, Pair newblock){
		Players p = ChessBoard[oldblock.y][oldblock.x].getColor();
		
		Piece[][] newChessBoard = getChessBoard();
		
		if(p == Players.WHITE){
			newChessBoard[newblock.y + 1][newblock.x] = new Empty();
		}if(p == Players.BLACK){
			newChessBoard[newblock.y - 1][newblock.x] = new Empty();
		}
		
		newChessBoard[oldblock.y][oldblock.x].incrementMoves();
		newChessBoard[oldblock.y][oldblock.x].setLastMoved(numHalfMoves);
		newChessBoard[newblock.y][newblock.x] = newChessBoard[oldblock.y][oldblock.x];
		newChessBoard[oldblock.y][oldblock.x] = new Empty();
		
		setChessBoard(newChessBoard);
		
		numHalfMoves++;
	}
	
	public void castle(Pair oldblock, Pair newblock){
		
		Players p = ChessBoard[oldblock.y][oldblock.x].getColor();
		
		Piece[][] newChessBoard = getChessBoard();
		
		newChessBoard[oldblock.y][oldblock.x] = new Empty();
		newChessBoard[newblock.y][newblock.x] = new Empty();
		
		if(newblock.x > oldblock.x){
			newChessBoard[newblock.y][6] = new King(p);// places king in correct spot
			newChessBoard[newblock.y][5] = new Rook(p);
			
			newChessBoard[newblock.y][6].incrementMoves();
			newChessBoard[newblock.y][6].setLastMoved(numHalfMoves);
			newChessBoard[newblock.y][5].incrementMoves();
			newChessBoard[newblock.y][5].setLastMoved(numHalfMoves);
			
			if(p == Players.WHITE){
				WKingPos = new Pair(6, newblock.y);
				whiteKingCastle = false;
				whiteQueenCastle = false;
			}else{
				BKingPos = new Pair(6, newblock.y);
				blackKingCastle = false;
				blackQueenCastle = false;
			}
		}else{
			newChessBoard[newblock.y][2] = new King(p);
			newChessBoard[newblock.y][3] = new Rook(p);
			
			newChessBoard[newblock.y][2].incrementMoves();
			newChessBoard[newblock.y][2].setLastMoved(numHalfMoves);
			newChessBoard[newblock.y][3].incrementMoves();
			newChessBoard[newblock.y][3].setLastMoved(numHalfMoves);
			
			if(p == Players.WHITE){
				WKingPos = new Pair(2, newblock.y);
				whiteKingCastle = false;
				whiteQueenCastle = false;
			}else{
				BKingPos = new Pair(2, newblock.y);
				blackKingCastle = false;
				blackQueenCastle = false;
			}
		}
		
		setChessBoard(newChessBoard);
		
		numHalfMoves++;
	}

	abstract public Results isGameEnd(Players p, Piece[][] ChessBoard, Pair WKingPos, Pair BKingPos, int numHalfMoves, boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle);
	
	abstract public Piece[][] initChessBoard();
	
}
