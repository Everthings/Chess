package chess_game.game_modes;

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

public class RegularGame extends AbstractGame{

	@Override
	public Results isGameEnd(Players p, Piece[][] ChessBoard, Pair WKingPos,
			Pair BKingPos, int numHalfMoves, boolean whiteKingCastle,
			boolean whiteQueenCastle, boolean blackKingCastle,
			boolean blackQueenCastle) {
		
		return ChessUtil.isMateOrDraw(p, ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle);
		
	}
	
	@Override
	public Piece[][] initChessBoard() {
		Piece[][] newChessBoard = {{new Rook(Players.BLACK), new Knight(Players.BLACK), new Bishop(Players.BLACK), new Queen(Players.BLACK), new King(Players.BLACK), new Bishop(Players.BLACK), new Knight(Players.BLACK), new Rook(Players.BLACK)},
				{new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK)},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE)}, 
				{new Rook(Players.WHITE), new Knight(Players.WHITE), new Bishop(Players.WHITE), new Queen(Players.WHITE), new King(Players.WHITE), new Bishop(Players.WHITE), new Knight(Players.WHITE), new Rook(Players.WHITE)}};
	
		return newChessBoard;
	}

}
