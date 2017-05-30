package chess_game.chess_pieces;

import java.util.ArrayList;

import chess_game.ChessUtil;
import chess_game.MoveStates;
import chess_game.Pair;
import chess_game.PieceTypes;
import chess_game.Players;

public class King extends Piece{

	public King(Players color) {
		super(color, PieceTypes.KING, 10);
	}

	@Override
	public MoveStates[][] getPossibleMoves(Pair pair, Piece[][] ChessBoard,
			Pair WKingPos, Pair BKingPos, int numHalfMoves, boolean whiteKingCastle,
			boolean whiteQueenCastle, boolean blackKingCastle,
			boolean blackQueenCastle, boolean checkForResultingCheck) {
		
		Players p = ChessBoard[pair.y][pair.x].getColor();
		
		MoveStates[][] possibleMoves = new MoveStates[ChessBoard.length][ChessBoard[0].length];
		ChessUtil.initPossibleMovesArray(possibleMoves);

		if(p == Players.WHITE){
			if(pair.y > 0){
				if(ChessBoard[pair.y - 1][pair.x].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x, pair.y - 1), ChessBoard), new Pair(WKingPos.x, WKingPos.y - 1), BKingPos)){
						if(ChessBoard[pair.y - 1][pair.x].getColor() != Players.EMPTY)
							possibleMoves[pair.y - 1][pair.x] = MoveStates.TAKE;
						else
							possibleMoves[pair.y - 1][pair.x] = MoveStates.OPEN;
					}
				}
				
				if(pair.x > 0){
					if(ChessBoard[pair.y - 1][pair.x - 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y - 1), ChessBoard), new Pair(WKingPos.x - 1, WKingPos.y - 1), BKingPos)){
							if(ChessBoard[pair.y - 1][pair.x - 1].getColor() != Players.EMPTY)
								possibleMoves[pair.y - 1][pair.x - 1] = MoveStates.TAKE;
							else
								possibleMoves[pair.y - 1][pair.x - 1] = MoveStates.OPEN;
						}
					}
				}
				
				if(pair.x < 7){
					if(ChessBoard[pair.y - 1][pair.x + 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y - 1), ChessBoard), new Pair(WKingPos.x + 1, WKingPos.y - 1), BKingPos)){
							if(ChessBoard[pair.y - 1][pair.x + 1].getColor() != Players.EMPTY)
								possibleMoves[pair.y - 1][pair.x + 1] = MoveStates.TAKE;
							else
								possibleMoves[pair.y - 1][pair.x + 1] = MoveStates.OPEN;
						}
					}
				}
			}
			
			if(pair.y < 7){
				if(ChessBoard[pair.y + 1][pair.x].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x, pair.y + 1), ChessBoard), new Pair(WKingPos.x, WKingPos.y + 1), BKingPos)){
						if(ChessBoard[pair.y + 1][pair.x].getColor() != Players.EMPTY)
							possibleMoves[pair.y + 1][pair.x] = MoveStates.TAKE;
						else
							possibleMoves[pair.y + 1][pair.x] = MoveStates.OPEN;
					}
				}
				
				if(pair.x < 7){
					if(ChessBoard[pair.y + 1][pair.x + 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y + 1), ChessBoard), new Pair(WKingPos.x + 1, WKingPos.y + 1), BKingPos)){
							if(ChessBoard[pair.y + 1][pair.x + 1].getColor() != Players.EMPTY)
								possibleMoves[pair.y + 1][pair.x + 1] = MoveStates.TAKE;
							else
								possibleMoves[pair.y + 1][pair.x + 1] = MoveStates.OPEN;
						}
					}
				}
				
				if(pair.x > 0){
					if(ChessBoard[pair.y + 1][pair.x - 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y + 1), ChessBoard), new Pair(WKingPos.x - 1, WKingPos.y + 1), BKingPos)){
							if(ChessBoard[pair.y + 1][pair.x - 1].getColor() != Players.EMPTY)
								possibleMoves[pair.y + 1][pair.x - 1] = MoveStates.TAKE;
							else
								possibleMoves[pair.y + 1][pair.x - 1] = MoveStates.OPEN;
						}
					}
				}
			}
			
			if(pair.x < 7){
				if(ChessBoard[pair.y][pair.x + 1].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y), ChessBoard), new Pair(WKingPos.x + 1, WKingPos.y), BKingPos)){
						if(ChessBoard[pair.y][pair.x + 1].getColor() != Players.EMPTY)
							possibleMoves[pair.y][pair.x + 1] = MoveStates.TAKE;
						else
							possibleMoves[pair.y][pair.x + 1] = MoveStates.OPEN;
					}
				}
			}
			
			if(pair.x > 0){
				if(ChessBoard[pair.y][pair.x - 1].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y), ChessBoard), new Pair(WKingPos.x - 1, WKingPos.y), BKingPos)){
						if(ChessBoard[pair.y][pair.x - 1].getColor() != Players.EMPTY)
							possibleMoves[pair.y][pair.x - 1] = MoveStates.TAKE;
						else
							possibleMoves[pair.y][pair.x - 1] = MoveStates.OPEN;
					}
				}
			}
		}else{
			if(pair.y > 0){
				if(ChessBoard[pair.y - 1][pair.x].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x, pair.y - 1), ChessBoard), WKingPos, new Pair(BKingPos.x, BKingPos.y - 1))){
						if(ChessBoard[pair.y - 1][pair.x].getColor() != Players.EMPTY)
							possibleMoves[pair.y - 1][pair.x] = MoveStates.TAKE;
						else
							possibleMoves[pair.y - 1][pair.x] = MoveStates.OPEN;
					}
				}
				
				if(pair.x > 0){
					if(ChessBoard[pair.y - 1][pair.x - 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y - 1), ChessBoard), WKingPos, new Pair(BKingPos.x - 1, BKingPos.y - 1))){
							if(ChessBoard[pair.y - 1][pair.x - 1].getColor() != Players.EMPTY)
								possibleMoves[pair.y - 1][pair.x - 1] = MoveStates.TAKE;
							else
								possibleMoves[pair.y - 1][pair.x - 1] = MoveStates.OPEN;
						}
					}
				}
				
				if(pair.x < 7){
					if(ChessBoard[pair.y - 1][pair.x + 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y - 1), ChessBoard), WKingPos, new Pair(BKingPos.x + 1, BKingPos.y - 1))){
							if(ChessBoard[pair.y - 1][pair.x + 1].getColor() != Players.EMPTY)
								possibleMoves[pair.y - 1][pair.x + 1] = MoveStates.TAKE;
							else
								possibleMoves[pair.y - 1][pair.x + 1] = MoveStates.OPEN;
						}
					}
				}
			}
			
			if(pair.y < 7){
				if(ChessBoard[pair.y + 1][pair.x].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x, pair.y + 1), ChessBoard), WKingPos, new Pair(BKingPos.x, BKingPos.y + 1))){
						if(ChessBoard[pair.y + 1][pair.x].getColor() != Players.EMPTY)
							possibleMoves[pair.y + 1][pair.x] = MoveStates.TAKE;
						else
							possibleMoves[pair.y + 1][pair.x] = MoveStates.OPEN;
					}
				}
				
				if(pair.x < 7){
					if(ChessBoard[pair.y + 1][pair.x + 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y + 1), ChessBoard), WKingPos, new Pair(BKingPos.x + 1, BKingPos.y + 1))){
							if(ChessBoard[pair.y + 1][pair.x + 1].getColor() != Players.EMPTY)
								possibleMoves[pair.y + 1][pair.x + 1] = MoveStates.TAKE;
							else
								possibleMoves[pair.y + 1][pair.x + 1] = MoveStates.OPEN;
						}
					}
				}
				
				if(pair.x > 0){
					if(ChessBoard[pair.y + 1][pair.x - 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y + 1), ChessBoard), WKingPos, new Pair(BKingPos.x - 1, BKingPos.y + 1))){
							if(ChessBoard[pair.y + 1][pair.x - 1].getColor() != Players.EMPTY)
								possibleMoves[pair.y + 1][pair.x - 1] = MoveStates.TAKE;
							else
								possibleMoves[pair.y + 1][pair.x - 1] = MoveStates.OPEN;
						}
					}
				}
			}
			
			if(pair.x < 7){
				if(ChessBoard[pair.y][pair.x + 1].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y), ChessBoard), WKingPos, new Pair(BKingPos.x + 1, BKingPos.y))){
						if(ChessBoard[pair.y][pair.x + 1].getColor() != Players.EMPTY)
							possibleMoves[pair.y][pair.x + 1] = MoveStates.TAKE;
						else
							possibleMoves[pair.y][pair.x + 1] = MoveStates.OPEN;
					}
				}
			}
			
			if(pair.x > 0){
				if(ChessBoard[pair.y][pair.x - 1].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y), ChessBoard), WKingPos, new Pair(BKingPos.x - 1, BKingPos.y))){
						if(ChessBoard[pair.y][pair.x - 1].getColor() != Players.EMPTY)
							possibleMoves[pair.y][pair.x - 1] = MoveStates.TAKE;
						else
							possibleMoves[pair.y][pair.x - 1] = MoveStates.OPEN;
					}
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
		
		int firstRook = -1;
		int secondRook = -1;
		for(int i = 0; i < 8; i++){
			if(ChessBoard[pair.y][i].equals(new Rook(p))){
				if(firstRook == -1)
					firstRook = i;
				else{
					secondRook = i;
					break;
				}
			}
		}
		
		if(kingCastle && secondRook != -1){
			
			boolean isEmpty = true;
			int startCheckPos = pair.x + 1;
			if(startCheckPos > 5){
				startCheckPos = 5;
			}
			int endCheckPos = secondRook - 1;
			if(endCheckPos < 6){
				endCheckPos = 6;
			}
			
			for(int i = startCheckPos; i <= endCheckPos; i++){
				if(!ChessBoard[pair.y][i].equals(new Empty()) && i != secondRook && i != pair.x){
					isEmpty = false;
					break;
				}
			}
			
			
			if(isEmpty){
				if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessBoard, WKingPos, BKingPos)){
					if(p == Players.WHITE){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(6, pair.y), ChessBoard), new Pair(6, pair.y), BKingPos))
							possibleMoves[pair.y][secondRook] = MoveStates.CASTLE;
					}else{
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(6, pair.y), ChessBoard), WKingPos, new Pair(6, pair.y)))
							possibleMoves[pair.y][secondRook] = MoveStates.CASTLE;
					}
				}
			}

		}

		if(queenCastle && firstRook != -1){
			
			boolean isEmpty = true;
			int startCheckPos = firstRook + 1;
			if(startCheckPos > 2){
				startCheckPos = 2;
			}
			int endCheckPos = pair.x - 1;
			if(endCheckPos < 3){
				endCheckPos = 3;
			}
			
			for(int i = startCheckPos; i <= endCheckPos; i++){
				if(!ChessBoard[pair.y][i].equals(new Empty()) && i != firstRook && i != pair.x){
					isEmpty = false;
					break;
				}
			}
			
			if(isEmpty){
				if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessBoard, WKingPos, BKingPos)){
					if(p == Players.WHITE){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(2, pair.y), ChessBoard), new Pair(2, pair.y), BKingPos)){
							possibleMoves[pair.y][firstRook] = MoveStates.CASTLE;
						}
					}else{
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(2, pair.y), ChessBoard), WKingPos, new Pair(2, pair.y))){
							possibleMoves[pair.y][firstRook] = MoveStates.CASTLE;
						}
					}
				}	
			}
		}
	}

	@Override
	public ArrayList<MoveObject> getOnlyPossibleMoves(Pair pair,
			Piece[][] ChessBoard, Pair WKingPos, Pair BKingPos,
			int numHalfMoves, boolean whiteKingCastle,
			boolean whiteQueenCastle, boolean blackKingCastle,
			boolean blackQueenCastle, boolean checkForResultingCheck) {
		
		ArrayList<MoveObject> moves = new ArrayList<MoveObject>();
		
		Players p = ChessBoard[pair.y][pair.x].getColor();
		
		if(p == Players.WHITE){
			if(pair.y > 0){
				if(ChessBoard[pair.y - 1][pair.x].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x, pair.y - 1), ChessBoard), new Pair(WKingPos.x, WKingPos.y - 1), BKingPos)){
						if(ChessBoard[pair.y - 1][pair.x].getColor() != Players.EMPTY)
							moves.add(new MoveObject(pair, new Pair(pair.x, pair.y - 1), MoveStates.TAKE));
						else
							moves.add(new MoveObject(pair, new Pair(pair.x, pair.y - 1), MoveStates.OPEN));
					}
				}
				
				if(pair.x > 0){
					if(ChessBoard[pair.y - 1][pair.x - 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y - 1), ChessBoard), new Pair(WKingPos.x - 1, WKingPos.y - 1), BKingPos)){
							if(ChessBoard[pair.y - 1][pair.x - 1].getColor() != Players.EMPTY)
								moves.add(new MoveObject(pair, new Pair(pair.x - 1, pair.y - 1), MoveStates.TAKE));
							else
								moves.add(new MoveObject(pair, new Pair(pair.x - 1, pair.y - 1), MoveStates.OPEN));
						}
					}
				}
				
				if(pair.x < 7){
					if(ChessBoard[pair.y - 1][pair.x + 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y - 1), ChessBoard), new Pair(WKingPos.x + 1, WKingPos.y - 1), BKingPos)){
							if(ChessBoard[pair.y - 1][pair.x + 1].getColor() != Players.EMPTY)
								moves.add(new MoveObject(pair, new Pair(pair.x + 1, pair.y - 1), MoveStates.TAKE));
							else
								moves.add(new MoveObject(pair, new Pair(pair.x + 1, pair.y - 1), MoveStates.OPEN));
						}
					}
				}
			}
			
			if(pair.y < 7){
				if(ChessBoard[pair.y + 1][pair.x].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x, pair.y + 1), ChessBoard), new Pair(WKingPos.x, WKingPos.y + 1), BKingPos)){
						if(ChessBoard[pair.y + 1][pair.x].getColor() != Players.EMPTY)
							moves.add(new MoveObject(pair, new Pair(pair.x, pair.y + 1), MoveStates.TAKE));
						else
							moves.add(new MoveObject(pair, new Pair(pair.x, pair.y + 1), MoveStates.OPEN));
					}
				}
				
				if(pair.x < 7){
					if(ChessBoard[pair.y + 1][pair.x + 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y + 1), ChessBoard), new Pair(WKingPos.x + 1, WKingPos.y + 1), BKingPos)){
							if(ChessBoard[pair.y + 1][pair.x + 1].getColor() != Players.EMPTY)
								moves.add(new MoveObject(pair, new Pair(pair.x + 1, pair.y + 1), MoveStates.TAKE));
							else
								moves.add(new MoveObject(pair, new Pair(pair.x + 1, pair.y + 1), MoveStates.OPEN));
						}
					}
				}
				
				if(pair.x > 0){
					if(ChessBoard[pair.y + 1][pair.x - 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y + 1), ChessBoard), new Pair(WKingPos.x - 1, WKingPos.y + 1), BKingPos)){
							if(ChessBoard[pair.y + 1][pair.x - 1].getColor() != Players.EMPTY)
								moves.add(new MoveObject(pair, new Pair(pair.x - 1, pair.y + 1), MoveStates.TAKE));
							else
								moves.add(new MoveObject(pair, new Pair(pair.x - 1, pair.y + 1), MoveStates.OPEN));
						}
					}
				}
			}
			
			if(pair.x < 7){
				if(ChessBoard[pair.y][pair.x + 1].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y), ChessBoard), new Pair(WKingPos.x + 1, WKingPos.y), BKingPos)){
						if(ChessBoard[pair.y][pair.x + 1].getColor() != Players.EMPTY)
							moves.add(new MoveObject(pair, new Pair(pair.x + 1, pair.y), MoveStates.TAKE));
						else
							moves.add(new MoveObject(pair, new Pair(pair.x + 1, pair.y), MoveStates.OPEN));
					}
				}
			}
			
			if(pair.x > 0){
				if(ChessBoard[pair.y][pair.x - 1].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y), ChessBoard), new Pair(WKingPos.x - 1, WKingPos.y), BKingPos)){
						if(ChessBoard[pair.y][pair.x - 1].getColor() != Players.EMPTY)
							moves.add(new MoveObject(pair, new Pair(pair.x - 1, pair.y), MoveStates.TAKE));
						else
							moves.add(new MoveObject(pair, new Pair(pair.x - 1, pair.y), MoveStates.OPEN));
					}
				}
			}
		}else{
			if(pair.y > 0){
				if(ChessBoard[pair.y - 1][pair.x].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x, pair.y - 1), ChessBoard), WKingPos, new Pair(BKingPos.x, BKingPos.y - 1))){
						if(ChessBoard[pair.y - 1][pair.x].getColor() != Players.EMPTY)
							moves.add(new MoveObject(pair, new Pair(pair.x, pair.y - 1), MoveStates.TAKE));
						else
							moves.add(new MoveObject(pair, new Pair(pair.x, pair.y - 1), MoveStates.OPEN));
					}
				}
				
				if(pair.x > 0){
					if(ChessBoard[pair.y - 1][pair.x - 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y - 1), ChessBoard), WKingPos, new Pair(BKingPos.x - 1, BKingPos.y - 1))){
							if(ChessBoard[pair.y - 1][pair.x - 1].getColor() != Players.EMPTY)
								moves.add(new MoveObject(pair, new Pair(pair.x - 1, pair.y - 1), MoveStates.TAKE));
							else
								moves.add(new MoveObject(pair, new Pair(pair.x - 1, pair.y - 1), MoveStates.OPEN));
						}
					}
				}
				
				if(pair.x < 7){
					if(ChessBoard[pair.y - 1][pair.x + 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y - 1), ChessBoard), WKingPos, new Pair(BKingPos.x + 1, BKingPos.y - 1))){
							if(ChessBoard[pair.y - 1][pair.x + 1].getColor() != Players.EMPTY)
								moves.add(new MoveObject(pair, new Pair(pair.x + 1, pair.y - 1), MoveStates.TAKE));
							else
								moves.add(new MoveObject(pair, new Pair(pair.x + 1, pair.y - 1), MoveStates.OPEN));
						}
					}
				}
			}
			
			if(pair.y < 7){
				if(ChessBoard[pair.y + 1][pair.x].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x, pair.y + 1), ChessBoard), WKingPos, new Pair(BKingPos.x, BKingPos.y + 1))){
						if(ChessBoard[pair.y + 1][pair.x].getColor() != Players.EMPTY)
							moves.add(new MoveObject(pair, new Pair(pair.x, pair.y + 1), MoveStates.TAKE));
						else
							moves.add(new MoveObject(pair, new Pair(pair.x, pair.y + 1), MoveStates.OPEN));
					}
				}
				
				if(pair.x < 7){
					if(ChessBoard[pair.y + 1][pair.x + 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y + 1), ChessBoard), WKingPos, new Pair(BKingPos.x + 1, BKingPos.y + 1))){
							if(ChessBoard[pair.y + 1][pair.x + 1].getColor() != Players.EMPTY)
								moves.add(new MoveObject(pair, new Pair(pair.x + 1, pair.y + 1), MoveStates.TAKE));
							else
								moves.add(new MoveObject(pair, new Pair(pair.x + 1, pair.y + 1), MoveStates.OPEN));
						}
					}
				}
				
				if(pair.x > 0){
					if(ChessBoard[pair.y + 1][pair.x - 1].getColor() != p){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y + 1), ChessBoard), WKingPos, new Pair(BKingPos.x - 1, BKingPos.y + 1))){
							if(ChessBoard[pair.y + 1][pair.x - 1].getColor() != Players.EMPTY)
								moves.add(new MoveObject(pair, new Pair(pair.x - 1, pair.y + 1), MoveStates.TAKE));
							else
								moves.add(new MoveObject(pair, new Pair(pair.x - 1, pair.y + 1), MoveStates.OPEN));
						}
					}
				}
			}
			
			if(pair.x < 7){
				if(ChessBoard[pair.y][pair.x + 1].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x + 1, pair.y), ChessBoard), WKingPos, new Pair(BKingPos.x + 1, BKingPos.y))){
						if(ChessBoard[pair.y][pair.x + 1].getColor() != Players.EMPTY)
							moves.add(new MoveObject(pair, new Pair(pair.x + 1, pair.y), MoveStates.TAKE));
						else
							moves.add(new MoveObject(pair, new Pair(pair.x + 1, pair.y), MoveStates.OPEN));
					}
				}
			}
			
			if(pair.x > 0){
				if(ChessBoard[pair.y][pair.x - 1].getColor() != p){
					if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(pair.x - 1, pair.y), ChessBoard), WKingPos, new Pair(BKingPos.x - 1, BKingPos.y))){
						if(ChessBoard[pair.y][pair.x - 1].getColor() != Players.EMPTY)
							moves.add(new MoveObject(pair, new Pair(pair.x - 1, pair.y), MoveStates.TAKE));
						else
							moves.add(new MoveObject(pair, new Pair(pair.x - 1, pair.y), MoveStates.OPEN));
					}
				}
			}
		}
		if(p == Players.WHITE)
			addOnlyCastleMoves(moves, pair, ChessBoard, WKingPos, BKingPos, whiteKingCastle, whiteQueenCastle, checkForResultingCheck);
		else
			addOnlyCastleMoves(moves, pair, ChessBoard, WKingPos, BKingPos, blackKingCastle, blackQueenCastle, checkForResultingCheck);
		
		return moves;
	}
	
	private void addOnlyCastleMoves(ArrayList<MoveObject> moves, Pair pair, Piece[][] ChessBoard, Pair WKingPos, Pair BKingPos, boolean kingCastle, boolean queenCastle, boolean checkForResultingCheck){
		
		Players p = ChessBoard[pair.y][pair.x].getColor();
		
		int firstRook = -1;
		int secondRook = -1;
		for(int i = 0; i < 8; i++){
			if(ChessBoard[pair.y][i].equals(new Rook(p))){
				if(firstRook == -1)
					firstRook = i;
				else{
					secondRook = i;
					break;
				}
			}
		}
		
		if(kingCastle && secondRook != -1){
			
			boolean isEmpty = true;
			int startCheckPos = pair.x + 1;
			if(startCheckPos > 5){
				startCheckPos = 5;
			}
			int endCheckPos = secondRook - 1;
			if(endCheckPos < 6){
				endCheckPos = 6;
			}
			
			for(int i = startCheckPos; i <= endCheckPos; i++){
				if(!ChessBoard[pair.y][i].equals(new Empty()) && i != secondRook && i != pair.x){
					isEmpty = false;
					break;
				}
			}
			
			
			if(isEmpty){
				if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessBoard, WKingPos, BKingPos)){
					if(p == Players.WHITE){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(6, pair.y), ChessBoard), new Pair(6, pair.y), BKingPos))
							moves.add(new MoveObject(pair, new Pair(secondRook, pair.y), MoveStates.CASTLE));
					}else{
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(6, pair.y), ChessBoard), WKingPos, new Pair(6, pair.y)))
							moves.add(new MoveObject(pair, new Pair(secondRook, pair.y), MoveStates.CASTLE));
					}
				}
			}

		}

		if(queenCastle && firstRook != -1){
			
			boolean isEmpty = true;
			int startCheckPos = firstRook + 1;
			if(startCheckPos > 2){
				startCheckPos = 2;
			}
			int endCheckPos = pair.x - 1;
			if(endCheckPos < 3){
				endCheckPos = 3;
			}
			
			for(int i = startCheckPos; i <= endCheckPos; i++){
				if(!ChessBoard[pair.y][i].equals(new Empty()) && i != firstRook && i != pair.x){
					isEmpty = false;
					break;
				}
			}
			
			if(isEmpty){
				if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessBoard, WKingPos, BKingPos)){
					if(p == Players.WHITE){
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(2, pair.y), ChessBoard), new Pair(2, pair.y), BKingPos)){
							moves.add(new MoveObject(pair, new Pair(firstRook, pair.y), MoveStates.CASTLE));
						}
					}else{
						if(!checkForResultingCheck || !ChessUtil.isCheck(p, ChessUtil.getChessBoardAfterMove(pair, new Pair(2, pair.y), ChessBoard), WKingPos, new Pair(2, pair.y))){
							moves.add(new MoveObject(pair, new Pair(firstRook, pair.y), MoveStates.CASTLE));
						}
					}
				}	
			}
		}
	}
}
