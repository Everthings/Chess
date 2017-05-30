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

public class TestGame extends AbstractGame{

	@Override
	public Results isGameEnd(Players p, Piece[][] ChessBoard, Pair WKingPos,
			Pair BKingPos, int numHalfMoves, boolean whiteKingCastle,
			boolean whiteQueenCastle, boolean blackKingCastle,
			boolean blackQueenCastle) {
		
		return ChessUtil.isMateOrDraw(p, ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle);
	}

	@Override
	public Piece[][] initChessBoard() {
		
		Piece[][] newChessBoard = {{new Empty(), new Empty(), new Empty(), new Knight(Players.BLACK), new Empty(), new Rook(Players.BLACK), new Empty(), new Empty()},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Empty(), new Empty(), new Empty(), new King(Players.BLACK), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Empty(), new Empty(), new Queen(Players.WHITE), new King(Players.WHITE), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Empty(), new Pawn(Players.WHITE), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()}};
		
		
		/*
		Piece[][] newChessBoard = {{new King(Players.BLACK), new Queen(Players.BLACK), new Empty(), new Queen(Players.WHITE), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Pawn(Players.BLACK), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Bishop(Players.WHITE)},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Pawn(Players.WHITE), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
				{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new King(Players.WHITE), new Empty()}};
		*/
		
		return newChessBoard;
	}

}
