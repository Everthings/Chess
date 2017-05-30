package chess_game.analysis;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import chess_game.ChessUtil;
import chess_game.MoveStates;
import chess_game.Pair;
import chess_game.PieceTypes;
import chess_game.Players;
import chess_game.chess_pieces.Empty;
import chess_game.chess_pieces.King;
import chess_game.chess_pieces.Piece;
import chess_game.chess_pieces.Queen;
import chess_game.chess_pieces.Rook;
import chess_game.chess_pieces.MoveObject;
public class Position {
	
	private static long[][] zTable = new long[64][12];
	private Piece[][] board;
	private int whosTurn;  //computer to move (player just moved) = -1. player = 1;
	private int turnNum; // white and black turns have different numbers
	private long hash;
	public boolean whiteKingCastle;
	public boolean whiteQueenCastle;
	public boolean blackKingCastle;
	public boolean blackQueenCastle;
	private Players colorTurn;
	public MoveObject mov;
	public boolean kingsAlive = true;
	
	//constructor
	public Position(MoveObject mov, Piece[][] b, int wt, int halfMoves, boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle)
	{
		this.mov = mov;
		board = b;
		whosTurn = wt;
		turnNum = halfMoves;
		this.whiteKingCastle = whiteKingCastle;
		this.whiteQueenCastle = whiteQueenCastle;
		this.blackKingCastle = blackKingCastle;
		this.blackQueenCastle = blackQueenCastle;
		
//		System.out.println("In Position constructor. printing board");
//		this.printPosition();
		if (turnNum%2==0)
			colorTurn = Players.WHITE;
		else
			colorTurn = Players.BLACK;
	}
	
	public static void initZobristTable(){
	
		Random rand = new Random();
		Random rand2 = new Random();
		
		for(int a = 0; a < 64; a++){
			for(int b = 0; b < 12; b++){
				//taken from stack overflow
				zTable[a][b] = rand.nextLong() ^ rand2.nextLong() << 1;
			}
		}
	}
	
	public static long addXOR(long hash, int row, int col, int pieceIndex){
		if(pieceIndex >= 0){
			hash = hash ^ zTable[row * 8 + col][pieceIndex];
		}
		
		return hash;
	}
	
	public static long hashPosition(Position p){
		long hash = 0;
		Piece[][] board = p.getBoard();
		
		for(int a = 0; a < 8; a++){
			for(int b = 0; b < 8; b++){
				int index = board[a][b].getIndex();
				if(index >= 0){
					hash = hash ^ zTable[a * 8 + b][index];
				}
			}
		}
		
		//System.out.println("Hash: " + hash);
		
		return hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Position){
			
			Position p = (Position) o;
			Piece[][] thisBoard = this.getBoard();
			Piece[][] otherBoard = p.getBoard();
			
			for(int a = 0; a < 8; a++){
				for(int b = 0; b < 8; b++){
					if(thisBoard[a][b].getIndex() != otherBoard[a][b].getIndex())
						return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	// getters
	public Piece[][] getBoard()
	{
		return board;
	}
	public int getTurn()
	{
		return whosTurn;
	}
	public int getTurnNum()
	{
		return turnNum;
	}
	public Players getColor()
	{
		if (turnNum % 2 == 0)
			return Players.WHITE;
		else
			return Players.BLACK;
	}
	public void setHash(long hash){
		this.hash = hash;
	}
	public long getHash(){
		return hash;
	}
	
	protected ArrayList<Position> getPossiblePositions(MoveObject[] killerMoves)
	// returns an array of all possible new positions one turn away
	{
		ArrayList<Position> newPosList = new ArrayList<Position>();
		
		if(ChessUtil.getKingPosition(colorTurn, board) == null){
			return newPosList;
		}
		
		for(int a = 0; a < 8; a++){
			for(int b = 0; b < 8; b++){
				if (!board[a][b].equals(new Empty()) && board[a][b].getColor() == colorTurn)
				{

					ArrayList<MoveObject> pieceMoves = board[a][b].getOnlyPossibleMoves(new Pair(b, a), board, null, null, turnNum, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, false);
					sortMoves(pieceMoves, killerMoves);
					
					for (MoveObject o: pieceMoves)
					{
						
						Piece[][] tempBoard = new Piece[8][8];
						
						for (int i = 0; i < 8; i++)
							for (int j = 0; j < 8; j++)
								tempBoard[i][j] = board[i][j];
						
						Position newPos = new Position(o, tempBoard, whosTurn * -1, turnNum + 1, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle);
	
						long newhash = ChessUtil.movePiece(hash, colorTurn, newPos, o.getOldPair(), o.getNewPair(), o.getMoveType(), turnNum);
						
						newPos.setHash(newhash);
						newPosList.add(newPos);
				
					}
				}
			}
		}

		return newPosList;
	}
	
	public void sortMoves(ArrayList<MoveObject> pieceMoves, MoveObject[] killerMoves){
		int index = 0;
		for(int i = 0; i < pieceMoves.size(); i++){
			if(pieceMoves.get(i).getMoveType() == MoveStates.TAKE){
				MoveObject temp = pieceMoves.set(index, pieceMoves.get(i));
				pieceMoves.set(i, temp);
				index++;
			}
		}

		for (int slot = 0; slot < killerMoves.length; slot++) {
		    MoveObject killerMove = killerMoves[slot];

		    for (int i = 0; i < pieceMoves.size(); i++){
		        if (pieceMoves.get(i).equals(killerMove)) {
		           MoveObject goodMove = pieceMoves.remove(i);
		           pieceMoves.add(0, goodMove);
		           //System.out.println("RIP player - killed");
		           break;
		        }
		    }
		}
	}

	public void printPosition()
	{
		for (int a = 0; a < 8; a++)
		{
			for (int b = 0; b < 8; b++)
			{
				if (board[a][b].equals(new Empty()))
					System.out.print(" __ ");
				else
					System.out.print(" " + board[a][b].getColor().toString().substring(0,1) + board[a][b].getType().toString().substring(0,1) + " ");
			}
			System.out.println();
		}
	}
}
