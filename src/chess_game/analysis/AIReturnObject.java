package chess_game.analysis;
import java.util.ArrayList;

import chess_game.MoveStates;
import chess_game.Pair;
import chess_game.Players;
import chess_game.chess_pieces.Empty;
import chess_game.chess_pieces.King;
import chess_game.chess_pieces.MoveObject;
import chess_game.chess_pieces.Pawn;
import chess_game.chess_pieces.Piece;

public class AIReturnObject {

	Position position;
	Players movingColor; // person who moved (AI color)
	MoveObject mov;
	

	public AIReturnObject(Position np, MoveObject mov, Players color)
	{
		position = np;
		this.mov = mov;
		np.printPosition();
		movingColor = color;
	}
	
	// getters
	public Position getPosition(){
		return position;
	}
	public MoveObject getMove(){
		return mov;
	}
}
