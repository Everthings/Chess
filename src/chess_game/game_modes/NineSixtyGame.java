package chess_game.game_modes;

import java.util.Random;

import chess_game.ChessUtil;
import chess_game.Pair;
import chess_game.Players;
import chess_game.Results;
import chess_game.chess_pieces.Bishop;
import chess_game.chess_pieces.Empty;
import chess_game.chess_pieces.King;
import chess_game.chess_pieces.Knight;
import chess_game.chess_pieces.Pawn;
import chess_game.chess_pieces.Piece;
import chess_game.chess_pieces.Queen;
import chess_game.chess_pieces.Rook;

public class NineSixtyGame extends AbstractGame{
	
	private Random rand;
	
	public NineSixtyGame(Random r){
		rand = r;
	}

	@Override
	public Results isGameEnd(Players p, Piece[][] ChessBoard, Pair WKingPos,
			Pair BKingPos, int numHalfMoves, boolean whiteKingCastle,
			boolean whiteQueenCastle, boolean blackKingCastle,
			boolean blackQueenCastle) {

		return ChessUtil.isMateOrDraw(p, ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle);

	}

	@Override
	public Piece[][] initChessBoard() {
		
		Piece[][] newChessBoard = {{null, null, null, null, null, null, null, null},
				{new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK)},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE)}, 
				{null, null, null, null, null, null, null, null}};
		
		newChessBoard[0][rand.nextInt(4) * 2] = new Bishop(Players.BLACK);
		newChessBoard[0][rand.nextInt(4) * 2 + 1] = new Bishop(Players.BLACK);
		newChessBoard[0][getNthEmptySpot(newChessBoard, 6)] = new Queen(Players.BLACK);
		newChessBoard[0][getNthEmptySpot(newChessBoard, 5)] = new Knight(Players.BLACK);
		newChessBoard[0][getNthEmptySpot(newChessBoard, 4)] = new Knight(Players.BLACK);
		newChessBoard[0][getNthEmptySpot(newChessBoard, 1)] = new Rook(Players.BLACK);
		int kingSpot = getNthEmptySpot(newChessBoard, 1);
		newChessBoard[0][kingSpot] = new King(Players.BLACK);
		newChessBoard[0][getNthEmptySpot(newChessBoard, 1)] = new Rook(Players.BLACK);
		
		
		for(int a = 0; a < 8; a++){
			Piece whitePiece = (Piece)newChessBoard[0][a].clone();
			whitePiece.setColor(Players.WHITE);
			newChessBoard[7][a] = whitePiece;
		}
		
		return newChessBoard;
		
	}
	
	public int getNthEmptySpot(Piece[][] cBoard, int n){
		int r = rand.nextInt(n);
		int counter = 0;
		for(int i = 0; i < 8; i++){
			if(cBoard[0][i] == null){
				if(counter == r){
					return i;
				}
				
				counter++;
			}
		}
		
		return -1;
	}
}
