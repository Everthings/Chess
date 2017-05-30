package chess_game.analysis;
import java.util.ArrayList;
import java.util.Arrays;

import chess_game.ChessUtil;
import chess_game.MoveStates;
import chess_game.Pair;
import chess_game.PieceTypes;
import chess_game.Players;
import chess_game.Results;
import chess_game.chess_pieces.Bishop;
import chess_game.chess_pieces.Empty;
import chess_game.chess_pieces.King;
import chess_game.chess_pieces.Knight;
import chess_game.chess_pieces.MoveObject;
import chess_game.chess_pieces.Pawn;
import chess_game.chess_pieces.Piece;
import chess_game.chess_pieces.Queen;
import chess_game.chess_pieces.Rook;



public class Evaluator {

	Piece[][] board;

	Position position;

	Players color;

	public void setValues(Position pos, Players color)

	{

		position = pos;

		board = pos.getBoard();

		this.color = color; // color that has just moved

	}



	public double evalPosition(){

		//System.out.println(currentBasicEval());

		//System.out.println(centralAdvantage());

		//System.out.println(countWeakPawns());

		//System.out.println(kingSafety());

		//System.out.println(spacing());
		
		if(ChessUtil.getKingPosition(Players.WHITE, board) == null){
			return -10000;
		}else if(ChessUtil.getKingPosition(Players.BLACK, board) == null){
			return 10000;
		}
		
		double turn = 0;
		
		if (color.equals(Players.WHITE)){
			turn += .3;
		}
		else {	
			turn += -.3;
		}

		return currentBasicEval() + centralAdvantage()/4 + (double) countWeakPawns() / 2 + kingSafety() * 2 + spacing()/30 + turn;
	}



	private double currentBasicEval(){

		double white = 0;

		double black = 0;

		for (int i = 0; i < 8; i++){

			for (int j = 0; j < 8; j++){

				Piece a = board[i][j];

				if(a.getColor() == Players.WHITE){
					if (a.getType() == PieceTypes.QUEEN)
						
						white += 9;
					
					else if (a.getType() == PieceTypes.ROOK)
	
						white += 5;
	
					else if (a.getType() == PieceTypes.BISHOP)
	
						white += 3.2;
	
					else if (a.getType() == PieceTypes.KNIGHT)
	
						white += 3.1;
	
					else if (a.getType() == PieceTypes.PAWN)
	
						white += 1;
					
				}if(a.getColor() == Players.BLACK){

					if (a.getType() == PieceTypes.QUEEN)
	
						black += 9;
					
					else if (a.getType() == PieceTypes.ROOK)
	
						black += 5;
	
					else if (a.getType() == PieceTypes.BISHOP)
	
						black += 3.2;
	
					else if (a.getType() == PieceTypes.KNIGHT)
	
						black += 3.1;
	
					else if (a.getType() == PieceTypes.PAWN)
	
						black += 1;
				}

			}

		}

		return white - black;

	}

	private double centralAdvantage(){

		boolean occupiedW33 = false;

		boolean occupiedB33 = false;

		int knightProtW33 = 0;

		int knightProtB33 = 0;

		int pawnProtW33 = 0;

		int pawnProtB33 = 0;

		int protW33 = 0;

		int protB33 = 0;

		boolean occupiedW34 = false;

		boolean occupiedB34 = false;

		int knightProtW34 = 0;

		int knightProtB34 = 0;

		int pawnProtW34 = 0;

		int pawnProtB34 = 0;

		int protW34 = 0;

		int protB34 = 0;

		boolean occupiedW43 = false;

		boolean occupiedB43 = false;

		int knightProtW43 = 0;

		int knightProtB43 = 0;

		int pawnProtW43 = 0;

		int pawnProtB43 = 0;

		int protW43 = 0;

		int protB43 = 0;

		boolean occupiedW44 = false;

		boolean occupiedB44 = false;

		int knightProtW44 = 0;

		int knightProtB44 = 0;

		int pawnProtW44 = 0;

		int pawnProtB44 = 0;

		int protW44 = 0;

		int protB44 = 0;
		
		if (occ(3,3)[0]){

			if (occ(3,3)[1])

				occupiedW33 = true;

			else

				occupiedB33 = true;

		}

		if (occ(3,4)[0]){

			if (occ(3,4)[1])

				occupiedW34 = true;

			else

				occupiedB34 = true;

		}

		if (occ(4, 3)[0]){

			if (occ(4, 3)[1])

				occupiedW43 = true;

			else

				occupiedB43 = true;



		}

		if (occ(4, 4)[0]){

			if (occ(4,4)[1])

				occupiedW44 = true;

			else

				occupiedB44 = true;

		}

		for (int i = 0; i < 8; i++){

			for (int j = 0; j < 8; j++){

				if (board[i][j].getType() != PieceTypes.NULL){

					if (board[i][j].getType() != PieceTypes.PAWN){

						Piece store = board[3][3];

						board[3][3] = new Empty();

						MoveStates[][] poss = ChessUtil.getPossibleMoves(new Pair(j,i), board, null, null, position.getTurnNum(), false, false, false, false, false);

						board[3][3] = store;

						if (poss[3][3] != MoveStates.CLOSED){

						if (board[i][j].equals(new Knight(Players.WHITE)))

						knightProtW33 += 1;

						else if (board[i][j].equals(new Knight(Players.BLACK)))

						knightProtB33 += 1;

						else if (board[i][j].getColor().equals(Players.WHITE))

						protW33 += 1;

						else

						protB33 += 1;

						}

						store = board[3][4];

						board[3][4] = new Empty();

						poss = ChessUtil.getPossibleMoves(new Pair(j,i), board, null, null, position.getTurnNum(), false, false, false, false, false);

						board[3][4] = store;

						if (poss[3][4] != MoveStates.CLOSED){

						if (board[i][j].equals(new Knight(Players.WHITE)))

						knightProtW34 += 1;

						else if (board[i][j].equals(new Knight(Players.BLACK)))

						knightProtB34 += 1;

						else if (board[i][j].getColor().equals(Players.WHITE))

						protW34 += 1;

						else

						protB34 += 1;

						}

						store = board[4][3];

						board[4][3] = new Empty();

						poss = ChessUtil.getPossibleMoves(new Pair(j,i), board, null, null, position.getTurnNum(), false, false, false, false, false);

						board[4][3] = store;

						if (poss[4][3] != MoveStates.CLOSED){

						if (board[i][j].equals(new Knight(Players.WHITE)))

						knightProtW43 += 1;

						else if (board[i][j].equals(new Knight(Players.BLACK)))

						knightProtB43 += 1;

						else if (board[i][j].getColor().equals(Players.WHITE))

						protW43 += 1;

						else

						protB43 += 1;

						}

						store = board[4][4];

						board[4][4] = new Empty();

						poss = ChessUtil.getPossibleMoves(new Pair(j, i), board, null, null, position.getTurnNum(), false, false, false, false, false);

						board[4][4] = store;

						if (poss[4][4] != MoveStates.CLOSED){


							if (board[i][j].equals(new Knight(Players.WHITE)))

								knightProtW44 += 1;

							else if (board[i][j].equals(new Knight(Players.BLACK)))

								knightProtB44 += 1;

							else if (board[i][j].getColor().equals(Players.WHITE))

								protW44 += 1;

							else

								protB44 += 1;

						}



					}



				}

			}}





		if (board[2][2].equals(new Pawn (Players.WHITE))){

			pawnProtW33 += 1;

		}

		if (board[2][4].equals(new Pawn (Players.WHITE))){

			pawnProtW33 += 1;

		}

		if (board[2][3].equals(new Pawn (Players.WHITE))){

			pawnProtW34 += 1;

		}

		if (board[2][5].equals(new Pawn (Players.WHITE))){

			pawnProtW34 += 1;

		}

		if (board[3][2].equals(new Pawn (Players.WHITE))){

			pawnProtW43 += 1;

		}

		if (board[3][4].equals(new Pawn (Players.WHITE))){

			pawnProtW43 += 1;

		}

		if (board[3][3].equals(new Pawn (Players.WHITE))){

			pawnProtW44 += 1;

		}

		if (board[3][5].equals(new Pawn (Players.WHITE))){

			pawnProtW44 += 1;

		}

		if (board[5][2].equals(new Pawn (Players.BLACK))){

			pawnProtB43 += 1;

		}

		if (board[5][4].equals(new Pawn (Players.BLACK))){

			pawnProtB43 += 1;

		}

		if (board[5][3].equals(new Pawn (Players.BLACK))){

			pawnProtB44 += 1;

		}

		if (board[5][5].equals(new Pawn (Players.BLACK))){

			pawnProtB44 += 1;

		}

		if (board[4][2].equals(new Pawn (Players.BLACK))){

			pawnProtB33 += 1;

		}

		if (board[4][4].equals(new Pawn (Players.BLACK))){

			pawnProtB33 += 1;

		}

		if (board[4][3].equals(new Pawn (Players.BLACK))){

			pawnProtB34 += 1;

		}

		if (board[4][5].equals(new Pawn (Players.BLACK))){

			pawnProtB34 += 1;

		}



		double a33 = (double) (pawnProtW33 - pawnProtB33 + ((double)(knightProtW33 - knightProtB33))/2 + ((double) (protW33 - protB33))/3);

		if (occupiedW33)

			a33 += 1;

		if (occupiedB33)

			a33 -= 1;

		double a34 = (double) (pawnProtW34 - pawnProtB34 + ((double)(knightProtW34 - knightProtB34))/2 + ((double) (protW34 - protB34))/3);



		if (occupiedW34)

			a34 += 1;



		if (occupiedB34)

			a34 -= 1;



		double a43 = (double) (pawnProtW43 - pawnProtB43 + ((double)(knightProtW43 - knightProtB43))/2 + ((double) (protW43 - protB43))/3);



		if (occupiedW43)

			a43 += 1;



		if (occupiedB43)

			a43 -= 1;

		double a44 = (double) (pawnProtW44 - pawnProtB44 + ((double)(knightProtW44 - knightProtB44))/2 + ((double) (protW44 - protB44))/3);



		if (occupiedW44)

			a44 += 1;



		if (occupiedB44)

			a44 -= 1;

		//System.out.println(((double) (protW44 - protB44))/3);

		//System.out.println(protB44);

		//System.out.println(a33 + " " + a34 + " " + a43 + " " + a44);

		return a44 + a33 + a43 + a34;

	}

	private boolean[] occ(int i, int j){

		boolean[] occ = new boolean[2];

		if (!board[i][j].equals(new Empty())){

			occ[0] = true;

			if (board[i][j].getColor()==Players.WHITE)

				occ[1] = true;

			else

				occ[1] = false;

		}

		else

			occ[0] = false;

		return occ;





	}

	private double countWeakPawns()

	{

		int total = 0;





		for (int r = 1; r < 7; r++){ // no pawns on 0 or 7

			for (int f = 0; f < 8; f++)

			{

				if (board[r][f].equals((new Pawn (Players.WHITE))))

					total ++;

			}}





		for (int r = 1; r < 7; r++){ // no pawns on 0 or 7

			for (int f = 0; f < 8; f++)

			{

				if (board[r][f].equals((new Pawn (Players.BLACK))))

					total --;

			}}



		return total;

	}

	private double kingSafety()

	// evals king safety

	{

		int kingRow;

		int kingFile;

		double white = 0;

		double black = 0;

		for (int a = 0; a < 8; a++) // searches for king

		{

			for (int b = 0; b < 8; b++)

			{

				if (color==Players.WHITE && board[a][b].equals((new King(Players.WHITE))))

				{

					kingRow = a;

					kingFile = b;

					a+=8;

					b+=8;

					double wall = checkPawnWall(kingRow, kingFile);

					double attackPawns = attackingPawns(kingRow, kingFile);

					white = wall + attackPawns;

				}

				if (color==Players.BLACK && board[a][b].equals((new King(Players.BLACK))))

				{

					kingRow = a;

					kingFile = b;

					a+=8;

					b+=8;

					double wall = checkPawnWall(kingRow, kingFile);

					double attackPawns = attackingPawns(kingRow, kingFile);

					black = wall + attackPawns;

				}

			}



		}

		return white - black;

	}

	private double checkPawnWall(int row, int file)

	// sees if 3 pawns are in front of it. returns 0 if not, returns .3 if so

	{

		if (color==Players.WHITE)

		{

			if (row == 7)

				return 0;

			for (int r = 0; r < 8; r ++)

			{

				if (r == row+1 || r == row || r == row-1)

					if(file < 7){
					
						if (!board[r][file +1].equals((new Pawn (Players.WHITE))))
	
						{
	
							return 0;
	
						}
				}

			}

			return 0.3;

		}

		else

		{

			{

				if (row == 0)

					return 0;

				for (int r = 0; r < 8; r ++)

				{

					if (r == row+1 || r == row || r == row-1)
						
						if(file - 1 > -1){
							if (!board[r][file-1].equals((new Pawn (Players.BLACK))))
	
							{

								return 0;

							}
						}

				}

				return 0.3;

			}

		}

	}

	private double attackingPawns(int row, int file)

	// returns -.7 for each attacking pawn

	{

		double total = 0;

		if (color == Players.WHITE)

		{

			if (row > 4)

				return 0;
			
			for (int r = 0; r < 8; r ++)

			{

				for (int f = file + 1; f < file + 3; f++ ){

					if (r == row+1 || r == row || r == row-1){

						if(f < 8){
							if (!board[r][f].equals((new Pawn (Players.BLACK))))
	
							{

							total -= 0.7;

							}
						}
					}
				}

			}

		}

		else

		{

			{



				if (row > 4)

					return 0;

				for (int r = 0; r < 8; r ++)

				{

					for (int f = file - 1; f < file - 3; f++){

						if (r == row+1 || r == row || r == row-1)
							
							if(f > -1){

							if (!board[r][f].equals((new Pawn (Players.WHITE))))

							{

								total -= 0.7;

							}
						}
						
					}

				}

			}

		}

		return total;

	}



	private double spacing(){

		whoControls[][] who = who();

		double blackScore = 0;

		double whiteScore = 0;

		for (int i = 0; i < 8; i++){



			for (int j = 0; j < 8; j++){

				if (who[i][j] == whoControls.white){
					
					whiteScore += 1;

				}

				if (who[i][j] == whoControls.WHITE){

					whiteScore += 2;

				}



				if (who[i][j] == whoControls.black){

					blackScore += 1;
				}



				if (who[i][j] == whoControls.BLACK){
					blackScore+=2;
				}



				if (who[i][j] == whoControls.both){


					if (color==Players.WHITE)

						whiteScore += 0.1;

					else

						blackScore += 0.1;

				}

			}

		}

		return whiteScore - blackScore;

	}


	private whoControls[][] who(){



		whoControls[][] who = new whoControls[8][8];

		boolean [][] fought = new boolean[8][8];

		int[][] control = new int[8][8];

		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){

				if (board[i][j].getType() != PieceTypes.NULL){

					if (board[i][j].getType() != PieceTypes.PAWN){

						//MoveStates[][] poss = ChessUtil.getPossibleMoves(new Pair(j,i), board, null, null, position.getTurnNum(), false, false, false, false, false);
						ArrayList<MoveObject> poss = board[i][j].getOnlyPossibleMoves(new Pair(j, i), board, null, null, position.getTurnNum(), false, false, false, false, false);
						
						//boardFlipper(poss);

						for (int a = 0; a < poss.size(); a++){

								
							MoveObject move = poss.get(a);
							
							fought[move.getNewPair().y][move.getNewPair().x] = true;
							
							if (board[i][j].getColor() == Players.WHITE)
								control[move.getNewPair().y][move.getNewPair().x] += 1;
							else
								control[move.getNewPair().y][move.getNewPair().x] -= 1;


							
						}

					}

					if (board[i][j].equals(new Pawn(Players.WHITE)) ){

						if(i > 0){
							if (j == 0)
	
								control[i - 1][j + 1] += 1;
	
							else if (j == 7)
	
								control[i - 1][j - 1] += 1;
	
							else{
	
								control[i - 1][j + 1] += 1;
	
								control[i - 1][j - 1] += 1;
	
							}
						}


					}

					if (board[i][j].equals(new Pawn(Players.BLACK))){

						if(i < 7){
							if (j == 0)
	
								control[i + 1][j + 1] -= 1;
	
							else if (j == 7)
	
								control[i + 1][j - 1] -= 1;
	
							else{
	
								control[i + 1][j + 1] -= 1;
	
								control[i + 1][j - 1] -= 1;
	
							}
						}
					
					}
				}
			}
		}

		for (int i = 0; i < 8; i++){

			for (int j = 0; j < 8; j++){

				if (control[i][j] == 0){

					if (fought[i][j])

						who[i][j] = whoControls.both;
					else
						who[i][j] = whoControls.neither;
				}

				else if (control[i][j] > 0){
					if (control[i][j] == 1)

						who[i][j] = whoControls.white;
					else
						who[i][j] = whoControls.WHITE;
				}

				else{
					if (control[i][j] == -1)

						who[i][j] = whoControls.black;

					else
						who[i][j] = whoControls.BLACK;

				}
			}

		}
		return who;

	}


	private void boardFlipper(MoveStates[][] moves){

		MoveStates[][] extra = moves.clone();

		for (int i = 0; i < 8; i++){

			for (int j = 0; j < 8; j++){

				moves[7 - i][j] = extra[i][j];

			}

		}

	}
}