package chess_game.chess_pieces;

import chess_game.MoveStates;
import chess_game.Pair;

public class MoveObject {
	Pair newblock;
	Pair oldblock;
	MoveStates move;
	
	public MoveObject(Pair oldPair, Pair newPair, MoveStates move){
		this.oldblock = oldPair;
		this.newblock = newPair;
		this.move = move;
	}
	
	public Pair getNewPair(){
		return newblock;
	}
	
	public Pair getOldPair(){
		return oldblock;
	}
	
	public MoveStates getMoveType(){
		return move;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof MoveObject){
			if(newblock.equals(((MoveObject) o).newblock)){
				if(oldblock.equals(((MoveObject) o).oldblock)){
					if(move == ((MoveObject) o).move){
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
