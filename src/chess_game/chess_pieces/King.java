package chess_game.chess_pieces;

import chess_game.ChessUtil;
import chess_game.MoveStates;
import chess_game.Pair;
import chess_game.PieceTypes;
import chess_game.Players;

public class King extends Piece{

	public King(Players color) {
		super(color, PieceTypes.KING);
	}

	@Override
	public MoveStates[][] getPossibleMoves(Pair pair, Piece[][] ChessBoard,
			Pair WKingPos, Pair BKingPos, boolean whiteKingCastle,
			boolean whiteQueenCastle, boolean blackKingCastle,
			boolean blackQueenCastle, boolean checkForResultingCheck) {
		
		Players p = ChessBoard[pair.y][pair.x].getColor();
		
		MoveStates[][] possibleMoves = new MoveStates[ChessBoard.length][ChessBoard[0].length];
		ChessUtil.initPossibleMovesArray(possibleMoves);

		if(p == Players.WHITE){
			if(pair.y > 0){
				if(ChessBoard[pair.y - 1][pair.x].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x, pair.y - 1), ChessBoard), new Pair(WKingPos.x, WKingPos.y - 1), BKingPos))
						possibleMoves[pair.y - 1][pair.x] = MoveStates.OPEN;
				}
				
				if(pair.x > 0){
					if(ChessBoard[pair.y - 1][pair.x - 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y - 1), ChessBoard), new Pair(WKingPos.x - 1, WKingPos.y - 1), BKingPos))
							possibleMoves[pair.y - 1][pair.x - 1] = MoveStates.OPEN;
					}
				}
				
				if(pair.x < 7){
					if(ChessBoard[pair.y - 1][pair.x + 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y - 1), ChessBoard), new Pair(WKingPos.x + 1, WKingPos.y - 1), BKingPos))
							possibleMoves[pair.y - 1][pair.x + 1] = MoveStates.OPEN;
					}
				}
			}
			
			if(pair.y < 7){
				if(ChessBoard[pair.y + 1][pair.x].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x, pair.y + 1), ChessBoard), new Pair(WKingPos.x, WKingPos.y + 1), BKingPos))
						possibleMoves[pair.y + 1][pair.x] = MoveStates.OPEN;
				}
				
				if(pair.x < 7){
					if(ChessBoard[pair.y + 1][pair.x + 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y + 1), ChessBoard), new Pair(WKingPos.x + 1, WKingPos.y + 1), BKingPos))
							possibleMoves[pair.y + 1][pair.x + 1] = MoveStates.OPEN;
					}
				}
				
				if(pair.x > 0){
					if(ChessBoard[pair.y + 1][pair.x - 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y + 1), ChessBoard), new Pair(WKingPos.x - 1, WKingPos.y + 1), BKingPos))
							possibleMoves[pair.y + 1][pair.x - 1] = MoveStates.OPEN;
					}
				}
			}
			
			if(pair.x < 7){
				if(ChessBoard[pair.y][pair.x + 1].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y), ChessBoard), new Pair(WKingPos.x + 1, WKingPos.y), BKingPos))
						possibleMoves[pair.y][pair.x + 1] = MoveStates.OPEN;
				}
			}
			
			if(pair.x > 0){
				if(ChessBoard[pair.y][pair.x - 1].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y), ChessBoard), new Pair(WKingPos.x - 1, WKingPos.y), BKingPos))
						possibleMoves[pair.y][pair.x - 1] = MoveStates.OPEN;
				}
			}
		}else{
			if(pair.y > 0){
				if(ChessBoard[pair.y - 1][pair.x].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x, pair.y - 1), ChessBoard), WKingPos, new Pair(BKingPos.x, BKingPos.y - 1)))
						possibleMoves[pair.y - 1][pair.x] = MoveStates.OPEN;
				}
				
				if(pair.x > 0){
					if(ChessBoard[pair.y - 1][pair.x - 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y - 1), ChessBoard), WKingPos, new Pair(BKingPos.x - 1, BKingPos.y - 1)))
							possibleMoves[pair.y - 1][pair.x - 1] = MoveStates.OPEN;
					}
				}
				
				if(pair.x < 7){
					if(ChessBoard[pair.y - 1][pair.x + 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y - 1), ChessBoard), WKingPos, new Pair(BKingPos.x + 1, BKingPos.y - 1)))
							possibleMoves[pair.y - 1][pair.x + 1] = MoveStates.OPEN;
					}
				}
			}
			
			if(pair.y < 7){
				if(ChessBoard[pair.y + 1][pair.x].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x, pair.y + 1), ChessBoard), WKingPos, new Pair(BKingPos.x, BKingPos.y + 1)))
						possibleMoves[pair.y + 1][pair.x] = MoveStates.OPEN;
				}
				
				if(pair.x < 7){
					if(ChessBoard[pair.y + 1][pair.x + 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y + 1), ChessBoard), WKingPos, new Pair(BKingPos.x + 1, BKingPos.y + 1)))
							possibleMoves[pair.y + 1][pair.x + 1] = MoveStates.OPEN;
					}
				}
				
				if(pair.x > 0){
					if(ChessBoard[pair.y + 1][pair.x - 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y + 1), ChessBoard), WKingPos, new Pair(BKingPos.x - 1, BKingPos.y + 1)))
							possibleMoves[pair.y + 1][pair.x - 1] = MoveStates.OPEN;
					}
				}
			}
			
			if(pair.x < 7){
				if(ChessBoard[pair.y][pair.x + 1].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y), ChessBoard), WKingPos, new Pair(BKingPos.x + 1, BKingPos.y)))
						possibleMoves[pair.y][pair.x + 1] = MoveStates.OPEN;
				}
			}
			
			if(pair.x > 0){
				if(ChessBoard[pair.y][pair.x - 1].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y), ChessBoard), WKingPos, new Pair(BKingPos.x - 1, BKingPos.y)))
						possibleMoves[pair.y][pair.x - 1] = MoveStates.OPEN;
				}
			}
		}
		
		if(p == Players.WHITE)
			addCastleMoves(pair, ChessBoard, possibleMoves, WKingPos, BKingPos, whiteKingCastle, whiteQueenCastle, checkForResultingCheck);
		else
			addCastleMoves(pair, ChessBoard, possibleMoves, WKingPos, BKingPos, blackKingCastle, blackQueenCastle, checkForResultingCheck);
		
		return possibleMoves;
	}
	
	private void addCastleMoves(Pair pair, Piece[][] ChessBoard, MoveStates[][] possibleMoves, Pair WKingPos, Pair BKingPos, boolean kingCastle, boolean queenCastle, boolean checkForResultingCheck){
		
		Players p = ChessBoard[pair.y][pair.x].getColor();
		
		if(kingCastle){
			if(pair.x + 3 < 8){
				if(ChessBoard[pair.y][pair.x + 3].getType() == PieceTypes.ROOK){
					if(ChessBoard[pair.y][pair.x + 1].equals(new Empty())){
						if(ChessBoard[pair.y][pair.x + 2].equals(new Empty())){
							if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessBoard, WKingPos, BKingPos))
								if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 3, pair.y), ChessBoard), WKingPos, BKingPos))
									possibleMoves[pair.y][pair.x + 3] = MoveStates.CASTLE;
						}
					}
				}
			}
		}

		if(queenCastle){
			if(pair.x - 4 > -1){
				if(ChessBoard[pair.y][pair.x - 4].getType() == PieceTypes.ROOK){
					if(ChessBoard[pair.y][pair.x - 1].equals(new Empty())){
						if(ChessBoard[pair.y][pair.x - 2].equals(new Empty())){
							if(ChessBoard[pair.y][pair.x - 3].equals(new Empty())){
								if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessBoard, WKingPos, BKingPos))
									if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 4, pair.y), ChessBoard), WKingPos, BKingPos))
										possibleMoves[pair.y][pair.x - 4] = MoveStates.CASTLE;
							}	
						}
					}	
				}
			}
		}
	}
}
