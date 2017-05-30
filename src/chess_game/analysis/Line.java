package chess_game.analysis;

import java.util.ArrayList;

import chess_game.Players;
import chess_game.chess_pieces.Bishop;
import chess_game.chess_pieces.Empty;
import chess_game.chess_pieces.King;
import chess_game.chess_pieces.Knight;
import chess_game.chess_pieces.MoveObject;
import chess_game.chess_pieces.Pawn;
import chess_game.chess_pieces.Piece;
import chess_game.chess_pieces.Queen;
import chess_game.chess_pieces.Rook;

public class Line {

	Position currentPosition;
	int whosTurn; //computer to move (player just moved) = -1. player = 1;
	Players AIColor; // AI's color
	double evaluation;
	
	// constructors
	public Line (Position cp, Players color)
	{
		currentPosition = cp;
		whosTurn = currentPosition.getTurn();
		AIColor = color;
	}
	public Line (Players AIColor)
	// used for when first line is created
	{
		if (AIColor == Players.WHITE)
			whosTurn = -1;
		else
			whosTurn = 1;

		this.AIColor = AIColor;
	}
	
	// getters
	public Position getPosition()
	{
		return currentPosition;
	}
	public int getTurn()
	{
		return whosTurn;
	}
	
	protected Line[] expandAllLines(MoveObject[] killerMoves)
	// fills all moves for next line
	{
		Line[] possibleLines;
		ArrayList<Position> moves = currentPosition.getPossiblePositions(killerMoves);
		possibleLines = new Line[moves.size()];
		
		for (int j = 0; j < moves.size(); j++)
		{
			possibleLines[j] = new Line(moves.get(j), this.AIColor);
		}
		
		return possibleLines;
	}
	
	
}
