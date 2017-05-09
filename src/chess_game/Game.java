package chess_game;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import chess_game.chess_pieces.Bishop;
import chess_game.chess_pieces.Empty;
import chess_game.chess_pieces.King;
import chess_game.chess_pieces.Knight;
import chess_game.chess_pieces.Pawn;
import chess_game.chess_pieces.Piece;
import chess_game.chess_pieces.Queen;
import chess_game.chess_pieces.Rook;

@SuppressWarnings("serial")
public class Game extends JPanel implements MouseListener, MouseMotionListener{

	int response;
	final int AI = 0;
	final int HUMAN = 1;

	JTextField WhiteText = new JTextField("White's Turn");
	JTextField BlackText = new JTextField("Black's Turn");
	JTextField AITurnText = new JTextField("AI's Turn");

	final int WIDTH = 75;
	final int HEIGHT = 75;

	boolean castled = false;
	boolean kingSelected = false;

	Players player = Players.WHITE;

	final int NOT_SELECTED = 0;
	final int SELECTED = 1;
	//final int ALREADY_SELECTED = 1;
	int selected = NOT_SELECTED;

	Pair newblock;
	Pair oldblock = new Pair(0, 0);

	BufferedImage BACK;

	BufferedImage BP;
	BufferedImage BN;
	BufferedImage BB;
	BufferedImage BR;
	BufferedImage BQ;
	BufferedImage BK;
	BufferedImage WP;
	BufferedImage WN;
	BufferedImage WB;
	BufferedImage WR;
	BufferedImage WQ;
	BufferedImage WK;
	
	boolean whiteKingCastle = true;
	boolean whiteQueenCastle = true;
	boolean blackKingCastle = true;
	boolean blackQueenCastle = true;
	
	boolean dragging = false;
	int mx = 0;
	int my = 0;
	
	boolean animating = false;
	BufferedImage animatedImage;
	int animateX;
	int animateY;

	Pair BKingPos = new Pair(4, 0);
	Pair WKingPos = new Pair(4, 7);

	MoveStates[][] possibleMoves = new MoveStates[8][8];

	JFrame frame = new JFrame();

	List<Pair> BlackPos = new ArrayList<Pair>();
	List<Pair> WhitePos = new ArrayList<Pair>();
	
	Timer timer = new Timer();

	Piece[][] OldWBoard = {{new Rook(Players.BLACK), new Knight(Players.BLACK), new Bishop(Players.BLACK), new Queen(Players.BLACK), new King(Players.BLACK), new Bishop(Players.BLACK), new Knight(Players.BLACK), new Rook(Players.BLACK)},
			{new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK)},
			{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
			{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
			{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
			{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
			{new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE)}, 
			{new Rook(Players.WHITE), new Knight(Players.WHITE), new Bishop(Players.WHITE), new Queen(Players.WHITE), new King(Players.WHITE), new Bishop(Players.WHITE), new Knight(Players.WHITE), new Rook(Players.WHITE)}};

	Piece[][] OldBBoard = {{new Rook(Players.BLACK), new Knight(Players.BLACK), new Bishop(Players.BLACK), new Queen(Players.BLACK), new King(Players.BLACK), new Bishop(Players.BLACK), new Knight(Players.BLACK), new Rook(Players.BLACK)},
			{new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK)},
			{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
			{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
			{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
			{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
			{new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE)}, 
			{new Rook(Players.WHITE), new Knight(Players.WHITE), new Bishop(Players.WHITE), new Queen(Players.WHITE), new King(Players.WHITE), new Bishop(Players.WHITE), new Knight(Players.WHITE), new Rook(Players.WHITE)}};

	Piece[][] ChessBoard = {{new Rook(Players.BLACK), new Knight(Players.BLACK), new Bishop(Players.BLACK), new Queen(Players.BLACK), new King(Players.BLACK), new Bishop(Players.BLACK), new Knight(Players.BLACK), new Rook(Players.BLACK)},
			{new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK)},
			{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
			{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
			{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
			{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
			{new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE)}, 
			{new Rook(Players.WHITE), new Knight(Players.WHITE), new Bishop(Players.WHITE), new Queen(Players.WHITE), new King(Players.WHITE), new Bishop(Players.WHITE), new Knight(Players.WHITE), new Rook(Players.WHITE)}};

	public static void main(String[] args){ 		
		new Game();
	}

	Game(){
		addMouseListener(this);
		addMouseMotionListener(this);

		response = JOptionPane.showOptionDialog(null, 
				"Who would you like to play?", 
						"Welcome", 
						JOptionPane.OK_CANCEL_OPTION, 
						JOptionPane.INFORMATION_MESSAGE, 
						null, 
						new String[]{"AI", "A Human"}, // this is the array
				"default");

		if(response != AI && response != HUMAN){
			System.exit(0);
		}
		
		initWindow();
		frame.add(this);
		
		ChessUtil.initPossibleMovesArray(possibleMoves);

		for(int b = 2; b >= 1; b--){
			for(int a = 1; a <= 8; a++){
				BlackPos.add(new Pair(a, b));
			}
		}

		for(int b = 7; b <= 8; b++){
			for(int a = 1; a <= 8; a++){
				WhitePos.add(new Pair(a, b));
			}
		}

		try {

			BP = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/BlackPawn.png"));
			BN = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/BlackKnight.png"));
			BB = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/BlackBishop.png"));	
			BR = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/BlackRook.png"));
			BQ = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/BlackQueen.png"));
			BK = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/BlackKing.png"));
			WP = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/WhitePawn.png"));
			WN = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/WhiteKnight.png"));
			WB = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/WhiteBishop.png"));
			WR = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/WhiteRook.png"));
			WQ = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/WhiteQueen.png"));
			WK = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/WhiteKing.png"));

			BACK = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/Back.png"));

		} catch (IOException e) {
			System.out.println(e.getStackTrace());
		}

		frame.repaint();
	}
	
	public void initWindow(){
		//post: creates a frame and starts a thread for constant repaint; Note: the repainting thread is not ideal, but makes my life easier =)
		java.awt.EventQueue.invokeLater(new Runnable() {// uses event-dispatch thread to ensure thread safety
		    public void run() {
		    	frame.setTitle(WhiteText.getText());
				frame.setSize(600, 700); 
				frame.setLocationRelativeTo(null); 
				frame.setBackground(Color.WHITE); 
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
				frame.setVisible(true);
				frame.setResizable(false);
				frame.repaint();
				frame.revalidate();
		    }
		});
	}

	public Pair getBlock(int x, int y){
		int x1 = (int)x / WIDTH;
		int y1 = (int)y / HEIGHT;

		return new Pair(x1, y1);
	}

	public Pair getImagePixels(Pair p){
		int x = p.x;
		int y = p.y;

		x = (x-1) * WIDTH;
		y = (y-1) * HEIGHT;

		return new Pair(x, y);
	}

	public int getImagePixelsX(int i){
		int x = i;

		x = x * WIDTH;

		return x;
	}

	public int getImagePixelsY(int i){
		int y = i;

		y = y * HEIGHT;

		return y;
	}

	public boolean isPiece(Pair p){
		if(ChessBoard[p.y][p.x].equals(new Empty())){
			return false;
		}

		return true;
	}
	
	public void handleMouseClick(){
		if(response == AI){
			handleAIMove(mx, my);
		}else if(response == HUMAN){
			handleHumanMove();
		}
		
		repaint();
	}
	
	public void handleMouseRelease(){

		if(response == HUMAN){
			handleHumanSelected();
		}else if(response == AI){
			handleAISelected();
		}
		
		repaint();
	}
	
	public void handleAIMove(int mx, int my){
		if(selected == NOT_SELECTED){

			Pair pair = getBlock(mx, my);

			if(!isBack(pair)){ // if hasn't clicked back button
				if(isPiece(pair) && ChessBoard[pair.y][pair.x].getColor() == Players.WHITE){
					oldblock = pair;
					possibleMoves = ChessUtil.getPossibleMoves(oldblock, ChessBoard, WKingPos, BKingPos, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, true);
					selected = SELECTED;
					setOldBoard(Players.WHITE);
					dragging = true;
				}
			}else{
				redo(Players.WHITE);
			}

		}else{
			handleAISelected();
		}
	}
	
	public void handleAISelected(){
		
		if(selected == SELECTED){
			newblock = getBlock((int) mx, (int) my);
	
			if(!isBack(newblock)){
				if(ChessBoard[newblock.y][newblock.x].getColor() == player){
					oldblock = newblock;
					dragging = true;
					possibleMoves = ChessUtil.getPossibleMoves(oldblock, ChessBoard, WKingPos, BKingPos, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, true);
				}else if(isValidMove(newblock)){
					if(castled == true){
						castle(oldblock, newblock);
					}else{
						movePiece(oldblock, newblock);
					}
					selected = NOT_SELECTED;
					frame.setTitle(AITurnText.getText());
					eraseMoves();
					pawnQueen(newblock, Players.WHITE);
				
					castled = false;
	
					makeAIMove(Players.BLACK);
	
					frame.setTitle(WhiteText.getText());
				}else{
					selected = NOT_SELECTED;
				}
			}else{			
				selected = NOT_SELECTED;
			}
		}
	}
	
	public void handleHumanMove(){
		
		/*
		upon click
			if turn is white player's turn
				if click is on back button
					undo last move
				else
					if player has not yet selected a piece
						if square clicked on by player contains a piece of the player's color
							then reveal the possible moves the player can make, set the selected state to selected, store the square(in oldblock) for later use in moving the selected piece
					else(this means a piece has been selected already)
						if the square clicked is a castle
							castle!
						else if the square clicked is another white piece
							set that piece as the selected square
						else if the square clicked is a valid move
							either animate or move directly depending on if it is dragged or clicked
							change the turn to black
						else 
							set selected state to unselected
			if black player
				do above, but with the opposite color
								
		 */
		
		
		if(player == Players.WHITE){
			if(selected == NOT_SELECTED){
				Pair pair = getBlock(mx, my);

				if(!isBack(pair)){ // if hasn't clicked back button
					if(isPiece(pair) && ChessBoard[pair.y][pair.x].getColor() == Players.WHITE){
						oldblock = pair;
						possibleMoves = ChessUtil.getPossibleMoves(oldblock, ChessBoard, WKingPos, BKingPos, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, true);
						selected = SELECTED;
						dragging = true;
					}
				}else{
					redo(Players.BLACK);
				}
			}else{
				handleHumanSelected();
			}	
		}
		
		else if(player == Players.BLACK){
			if(selected == NOT_SELECTED){
				Pair pair = getBlock(mx, my);

				if(!isBack(pair)){ // if hasn't clicked back button
					if(isPiece(pair) && ChessBoard[pair.y][pair.x].getColor() == Players.BLACK){
						oldblock = pair;
						possibleMoves = ChessUtil.getPossibleMoves(oldblock, ChessBoard, WKingPos, BKingPos, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, true);
						selected = SELECTED;
						dragging = true;
					}
				}else{
					redo(Players.WHITE);
				}
			}else{
				handleHumanSelected();
			}
		}
	}
	
	public void handleHumanSelected(){
		if(selected == SELECTED){
			newblock = getBlock((int) mx, (int) my);
	
			if(!isBack(newblock)){
				if(possibleMoves[newblock.y][newblock.x] == MoveStates.CASTLE){
					castle(oldblock, newblock);
					selected = NOT_SELECTED;
					if(player == Players.WHITE){
						player = Players.BLACK;
						frame.setTitle(BlackText.getText());
						pawnQueen(newblock, Players.WHITE);
						setOldBoard(Players.BLACK);
					}else{
						player = Players.WHITE;
						frame.setTitle(WhiteText.getText());
						pawnQueen(newblock, Players.BLACK);
						setOldBoard(Players.WHITE);
					}
					eraseMoves();
					
					castled = false;
				}else if(ChessBoard[newblock.y][newblock.x].getColor() == player){
					oldblock = newblock;
					dragging = true;
					possibleMoves = ChessUtil.getPossibleMoves(oldblock, ChessBoard, WKingPos, BKingPos, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, true);
				}else if(isValidMove(newblock)){
					if(!dragging)
						animate(oldblock, newblock);
					else{
						movePiece(oldblock, newblock);
						switchTurn();
					}
					
					eraseMoves();
					
				}else{
					selected = NOT_SELECTED;
					handleHumanMove();
				}
			}else{			
				selected = NOT_SELECTED;
			}
		}
	}
	
	public void animate(Pair oldblock, Pair newblock){
		Piece piece = ChessBoard[oldblock.y][oldblock.x];
		double movX = (getImagePixelsX(newblock.x) - getImagePixelsX(oldblock.x))/30.0;
		double movY = (getImagePixelsX(newblock.y) - getImagePixelsX(oldblock.y))/30.0;
		animateX = getImagePixelsX(oldblock.x);
		animateY = getImagePixelsY(oldblock.y);
		animatedImage = getImage(oldblock.y, oldblock.x);
		ChessBoard[oldblock.y][oldblock.x] = new Empty();
		animating = true;
		timer.scheduleAtFixedRate(new TimerTask(){
			
			int counter = 0;

			@Override
			public void run() {
				if(counter < 30){
					animateX += movX;
					animateY += movY;
					counter++;
					repaint();
				}else{
					animating = false;
					ChessBoard[oldblock.y][oldblock.x] = piece;
					movePiece(oldblock, newblock);
					switchTurn();
					repaint();
					
					this.cancel();
				}
			}
			
		}, 0, 30);
	}
	
	public void switchTurn(){
		selected = NOT_SELECTED;
		if(player == Players.WHITE){
			player = Players.BLACK;
			frame.setTitle(BlackText.getText());
			pawnQueen(newblock, Players.WHITE);
			setOldBoard(Players.BLACK);
		}else{
			player = Players.WHITE;
			frame.setTitle(WhiteText.getText());
			pawnQueen(newblock, Players.BLACK);
			setOldBoard(Players.WHITE);
		}
	}

	public Players getOpponent(){
		if(player == Players.WHITE)
			return Players.BLACK;
		else
			return Players.WHITE;
	}
	
	public void highlight(Pair p, Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.MAGENTA);
		g2.setStroke(new BasicStroke(5));
		g2.drawRect(getImagePixelsX(p.x), getImagePixelsY(p.y), WIDTH, HEIGHT);
	}

	public void movePiece(Pair oldblock, Pair newblock){

		if(ChessBoard[oldblock.y][oldblock.x].equals(new King(Players.BLACK))){
			BKingPos = newblock;
			blackKingCastle = false;
			blackQueenCastle = false;
		}else if(ChessBoard[oldblock.y][oldblock.x].equals(new King(Players.WHITE))){
			WKingPos = newblock;
			whiteKingCastle = false;
			whiteQueenCastle = false;
		}

		ChessBoard[newblock.y][newblock.x] = ChessBoard[oldblock.y][oldblock.x];
		ChessBoard[oldblock.y][oldblock.x] = new Empty();
	}

	public boolean isValidMove(Pair newblock){//compares move with possibleMoves array
		
		if(possibleMoves[newblock.y][newblock.x] == MoveStates.OPEN){
			return true;
		}

		return false;
	}

	public  void drawPossibleMoves(Graphics g){
		for(int a = 0; a < 8; a++){
			for(int b = 0; b < 8; b++){
				if(possibleMoves[b][a] != MoveStates.CLOSED){
					Graphics2D g2 = (Graphics2D) g;
					g2.setStroke(new BasicStroke(5));
					g.setColor(Color.GREEN);
					g.drawRect(getImagePixelsX(a), getImagePixelsY(b), WIDTH, HEIGHT);
				}
			}
		}
	}

	public void makeAIMove(Players p){
		boolean PieceChosen = false;
		boolean MoveChosen = false;
		Random r = new Random();
		int moves = 0;
		int x = 0;
		int y = 0;

		while(!MoveChosen){

			int randomPiece = r.nextInt(100);
			int piecesPassed = 0;
			int a = 0;
			int b = 0;

			while(!PieceChosen){

				if(ChessBoard[b][a].getColor() == p){
					if(piecesPassed == randomPiece){
						x = a;
						y = b;
						PieceChosen = true;
					}

					piecesPassed++;
				}

				a++;

				if(a > 7){
					b++;
					a = 0;
				}

				if(b > 7){
					b = 0;
				}
			}

			possibleMoves = ChessUtil.getPossibleMoves(new Pair(x, y), ChessBoard, WKingPos, BKingPos, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, true);

			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					if(possibleMoves[j][i] != MoveStates.CLOSED){
						moves++;
					}
				}
			}

			if(moves == 0){// if there are no moves, pick another piece
				PieceChosen = false;
			}else{
				int selectMove = r.nextInt(moves);
				int movesPassed = 0;
				for(int c = 0; c < 8; c++){
					for(int d = 0; d < 8; d++){
						if(possibleMoves[d][c] == MoveStates.OPEN || possibleMoves[d][c] == MoveStates.CASTLE){
							if(selectMove == movesPassed){
								movePiece(new Pair(x, y), new Pair(c, d));
								pawnQueen(new Pair(c, d), p);
								eraseMoves();
								MoveChosen = true;
							}else{
								movesPassed++;
							}
						}
					}
				}
			}
		}
	}

	public void pawnQueen(Pair block, Players p){
		if(p == Players.WHITE){
			if(block.y == 0 && ChessBoard[block.y][block.x].getType() == PieceTypes.PAWN){
				ChessBoard[block.y][block.x] = new Queen(Players.WHITE);
			}
		}
		if(p == Players.BLACK){
			if(block.y == 7 && ChessBoard[block.y][block.x].getType() == PieceTypes.PAWN){
				ChessBoard[block.y][block.x] = new Queen(Players.BLACK);
			}
		}
	}

	public void eraseMoves(){
		for(int a = 0; a < 8; a++){
			for(int b = 0; b < 8; b++){
				possibleMoves[b][a] = MoveStates.CLOSED;
			}
		}
	}

	public void castle(Pair oldblock, Pair newblock){
		if(newblock.x == 7){
			ChessBoard[newblock.y][newblock.x - 1] = ChessBoard[oldblock.y][oldblock.x];// places king in correct sop
			ChessBoard[oldblock.y][oldblock.x] = new Empty();
			ChessBoard[newblock.y][newblock.x - 2] = ChessBoard[newblock.y][newblock.x];
			ChessBoard[newblock.y][newblock.x] = new Empty();
		}else if(newblock.x == 0){
			ChessBoard[newblock.y][newblock.x + 2] = ChessBoard[oldblock.y][oldblock.x];
			ChessBoard[oldblock.y][oldblock.x] = new Empty();
			ChessBoard[newblock.y][newblock.x + 3] = ChessBoard[newblock.y][newblock.x];
			ChessBoard[newblock.y][newblock.x] = new Empty();
		}
	}

	public  void setOldBoard(Players p){
		if(p == Players.WHITE){
			for(int a = 0; a < 8; a++){
				for(int b = 0; b < 8; b++){
					OldWBoard[b][a] = ChessBoard[b][a];
				}
			}
		}

		if(p == Players.BLACK){
			for(int a = 0; a < 8; a++){
				for(int b = 0; b < 8; b++){
					OldBBoard[b][a] = ChessBoard[b][a];
				}
			}
		}
	}

	public void redo(Players p){
		if(p == Players.WHITE){
			for(int a = 0; a < 8; a++){
				for(int b = 0; b < 8; b++){
					ChessBoard[b][a] = OldWBoard[b][a];
				}
			}
			frame.setTitle(WhiteText.getText());
			player = Players.WHITE;
		}

		if(p == Players.BLACK){
			for(int a = 0; a < 8; a++){
				for(int b = 0; b < 8; b++){
					ChessBoard[b][a] = OldBBoard[b][a];
				}
			}
			frame.setTitle(BlackText.getText());
			player = Players.BLACK;
		}
	}

	public  boolean isBack(Pair block){
		if(block.y == 8){
			return true;
		}

		return false;
	}

	@Override
	public void paintComponent(Graphics g){ 

		g.setColor(new Color(0, 160, 0));
		g.fillRect(0, 0, 600, 600); 
		g.setColor(new Color( 255, 255, 224));
		for(int i = 0; i <= 600; i+=150){ 
			for(int j = 0; j <= 450; j+=150){ 
				g.fillRect(i, j, WIDTH, HEIGHT); 
			} 
		} for(int i = 75; i <= 525; i+=150){ 
			for(int j = 75; j <= 525; j+=150){ 
				g.fillRect(i, j, WIDTH, HEIGHT); 
			} 
		} 

		if(selected == SELECTED){
			drawPossibleMoves(g);
			highlight(oldblock, g);
			if(dragging){
				BufferedImage img = getImage(oldblock.y, oldblock.x);
				g.drawImage(img, mx - img.getWidth()/2, my - img.getHeight()/2, null);
			}
		}
		
		if(animating){
			g.drawImage(animatedImage, animateX, animateY, null);
		}

		g.drawImage(BACK, getImagePixelsX(3) + WIDTH/2, getImagePixelsY(8), null);

		for(int a = 0; a <= 7; a++){
			for(int b = 0; b <= 7; b++){
				if(b != oldblock.y || a != oldblock.x || !dragging)
					g.drawImage(getImage(b, a), getImagePixelsX(a), getImagePixelsY(b), null);
			}
		}	
	}
	
	public BufferedImage getImage(int row, int col){
		if(ChessBoard[row][col] != null){
			switch(ChessBoard[row][col].getType()){
			case PAWN:
				if(ChessBoard[row][col].getColor() == Players.BLACK)
					return BP;
				else 
					return WP;
			case KNIGHT:
				if(ChessBoard[row][col].getColor() == Players.BLACK)
					return BN;
				else 
					return WN;
			case BISHOP:
				if(ChessBoard[row][col].getColor() == Players.BLACK)
					return BB;
				else 
					return WB;
			case ROOK:
				if(ChessBoard[row][col].getColor() == Players.BLACK)
					return BR;
				else 
					return WR;
			case QUEEN:
				if(ChessBoard[row][col].getColor() == Players.BLACK)
					return BQ;
				else 
					return WQ;
			case KING:
				if(ChessBoard[row][col].getColor() == Players.BLACK)
					return BK;
				else 
					return WK;
			}
		}
			
		return null;//default
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		handleMouseClick();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		handleMouseRelease();
		dragging = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		repaint();
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}


