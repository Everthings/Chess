package chess_game.chess_pieces;

import chess_game.MoveStates;
import chess_game.Pair;
import chess_game.PieceTypes;
import chess_game.Players;

public abstract class Piece implements Cloneable{
	private Players color;
	private PieceTypes type;
	
	public Piece(Players color, PieceTypes type){
		this.color = color;
		this.type = type;
	}
	
	public Players getColor(){
		return color;
	}
	
	public PieceTypes getType(){
		return type;
	}
	
	public void setColor(Players p){
		this.color = p;
	}
	
	@Override 
	public boolean equals(Object o){
		if(o instanceof Piece){
			Piece p = (Piece) o;
			
			if(color == p.getColor() && type == p.getType())
				return true;
		}
		
		return false;
	}
	
	public abstract MoveStates[][] getPossibleMoves(Pair pair, Piece[][] ChessBoard,  Pair WKingPos, Pair BKingPos,
			boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle, boolean checkForResultingCheck);

	@Override
	public Object clone() {
		try{  
			return super.clone(); 
		}catch(Exception e){ 
			return null; 
		}
	}
}
