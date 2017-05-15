package chess_game;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import chess_game.listeners.OptionListener;
import chess_game.listeners.SelectionListener;

@SuppressWarnings("serial")
public class Game extends JPanel implements MouseListener, MouseMotionListener{

	int gameType;
	int response;
	final int HUMAN = 0;
	final int AI = 1;

	JTextField WhiteText = new JTextField("White's Turn");
	JTextField BlackText = new JTextField("Black's Turn");

	final int BLOCK_WIDTH = 75;
	final int BLOCK_HEIGHT = 75;

	Players player = Players.WHITE;

	final int NOT_SELECTED = 0;
	final int SELECTED = 1;
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
	
	volatile boolean animating = false;
	BufferedImage animatedImage;
	double animateX;
	double animateY;

	Pair BKingPos = new Pair(4, 0);
	Pair WKingPos = new Pair(4, 7);

	MoveStates[][] possibleMoves = new MoveStates[8][8];

	JFrame frame = new JFrame();
	
	Timer timer = new Timer();

	ArrayList<Piece[][]> previousMoves = new ArrayList<Piece[][]>();
	ArrayList<Pair> blackPosHistory = new ArrayList<Pair>();
	ArrayList<Pair> whitePosHistory = new ArrayList<Pair>();
	
	int numChecks = 0;
	int numHalfMoves = 0;
	
	Random rand;
	Object selectorLock = new Object();
	Object animateLock = new Object();
	Object optionScreenLock = new Object();
	
	PieceSelector pSelect = null;
	
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
		
		
		initWindow();
		OptionScreen optionScreen = new OptionScreen(600, 700);
		frame.add(optionScreen);
		optionScreen.addListener(new OptionListener(){

			@Override
			public void selected(int i) {
	
				gameType = i;
			
				synchronized(optionScreenLock){
					optionScreenLock.notify();
				}
			}

			@Override
			public void refresh() {
				// TODO Auto-generated method stub
			}
			
		});
		optionScreen.add(new String[]{"Regular", "960", "3-Check", "Settings"});
		frame.repaint();
		
		synchronized(optionScreenLock){
			try {
				optionScreenLock.wait();
				
				final int REGULAR = 0;
				final int NINESIXTEE = 1;
				if(gameType == NINESIXTEE){
					initNineSixTeeBoard();
				}
				
				optionScreen.removeAll();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		optionScreen.removeAllListeners();
		optionScreen.addListener(new OptionListener(){

			@Override
			public void selected(int i) {
				response = i;
				
				synchronized(optionScreenLock){
					optionScreenLock.notify();
				}
			}

			@Override
			public void refresh() {
				// TODO Auto-generated method stub
			}
			
		});
		optionScreen.add(new String[]{"A Human", "AI"});
		
		synchronized(optionScreenLock){
			try {
				optionScreenLock.wait();
				
				optionScreen.removeAll();
				
				frame.remove(optionScreen);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		setSize(600, 700);
		setVisible(true);
		frame.add(this);
		frame.repaint();
		
		Piece[][] clone = new Piece[8][8];
		for(int i = 0; i < 8; i++){
			clone[i] = ChessBoard[i].clone();
		}
		
		previousMoves.add(clone);
		whitePosHistory.add(WKingPos);
		blackPosHistory.add(BKingPos);
		
		ChessUtil.initPossibleMovesArray(possibleMoves);
		
		addMouseListener(this);
		addMouseMotionListener(this);
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
	
	public int getSeedValue(){
	
		Object lock = new Object();
		JFrame window = new JFrame("Seed");
		JTextField textField = new JTextField(10);
		
		window.setLocationRelativeTo(null); 
		window.setBackground(Color.WHITE); 
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		window.setResizable(false);
		
		JPanel panel = new JPanel();
		panel.setVisible(true);
		panel.setSize(300, 70);
		
		JLabel label = new JLabel("Seed Number: ");
		label.setToolTipText("Long Value");
		
		textField.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isInt(textField.getText())){
					textField.setText("Not An Integer");
					textField.setEnabled(false);
					timer.schedule(new TimerTask(){
						@Override
						public void run() {
							textField.setEnabled(true);
							textField.setText("");	
						}	
					}, 2000);
				}else{
					synchronized(lock){
						lock.notify();
					}
				}
				
			}
		});
		
		panel.add(label);
		panel.add(textField);
		window.add(panel);
		window.pack();
		window.setVisible(true);
		
		try {
			synchronized(lock){
				lock.wait();
			}
			window.dispose();
			return Integer.parseInt(textField.getText());
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return -1;
	}
	
	public boolean isInt(String str){
		try{
			int i = Integer.parseInt(str);
		}catch(NumberFormatException nfe){
			return false;
		}
		
		return true;
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

	public void initNineSixTeeBoard(){
		rand = new Random(getSeedValue());
		
		for(int a = 0; a < 8; a++){
			ChessBoard[0][a] = null;
		}
		
		ChessBoard[0][rand.nextInt(4) * 2] = new Bishop(Players.BLACK);
		ChessBoard[0][rand.nextInt(4) * 2 + 1] = new Bishop(Players.BLACK);
		ChessBoard[0][getNthEmptySpot(ChessBoard, 6)] = new Queen(Players.BLACK);
		ChessBoard[0][getNthEmptySpot(ChessBoard, 5)] = new Knight(Players.BLACK);
		ChessBoard[0][getNthEmptySpot(ChessBoard, 4)] = new Knight(Players.BLACK);
		ChessBoard[0][getNthEmptySpot(ChessBoard, 1)] = new Rook(Players.BLACK);
		int kingSpot = getNthEmptySpot(ChessBoard, 1);
		ChessBoard[0][kingSpot] = new King(Players.BLACK);
		ChessBoard[0][getNthEmptySpot(ChessBoard, 1)] = new Rook(Players.BLACK);
		
		
		for(int a = 0; a < 8; a++){
			Piece whitePiece = (Piece)ChessBoard[0][a].clone();
			whitePiece.setColor(Players.WHITE);
			ChessBoard[7][a] = whitePiece;
		}
		
		BKingPos = new Pair(kingSpot, 0);
		WKingPos = new Pair(kingSpot, 7);
	}
	
	public Pair getBlock(int x, int y){
		int x1 = (int)x / BLOCK_WIDTH;
		int y1 = (int)y / BLOCK_HEIGHT;

		return new Pair(x1, y1);
	}

	public Pair getImagePixels(Pair p){
		int x = p.x;
		int y = p.y;

		x = (x-1) * BLOCK_WIDTH;
		y = (y-1) * BLOCK_HEIGHT;

		return new Pair(x, y);
	}

	public int getImagePixelsX(int i){
		int x = i;

		x = x * BLOCK_WIDTH;

		return x;
	}

	public int getImagePixelsY(int i){
		int y = i;

		y = y * BLOCK_HEIGHT;

		return y;
	}

	public void handleMouseClick(){
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				if(response == AI){
					handleAIMove(mx, my);
				}else if(response == HUMAN){
					handleHumanMove();
				}
				
				repaint();
			}
			
		}).start();
	}
	
	public void handleMouseRelease(){
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				if(isOnBoard(getBlock((int) mx, (int) my))){
					if(response == HUMAN){
						handleHumanSelected();
					}else if(response == AI){
						handleAISelected();
					}
				}
				
				dragging = false;
				repaint();
			}
		}).start();

	}
	
	public void handleAIMove(int mx, int my){
		if(selected == NOT_SELECTED){

			Pair pair = getBlock(mx, my);

			if(!isBack(pair)){ // if hasn't clicked back button
				if(isPiece(pair) && ChessBoard[pair.y][pair.x].getColor() == Players.WHITE){
					oldblock = pair;
					possibleMoves = ChessUtil.getPossibleMoves(oldblock, ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, true);
					selected = SELECTED;
					dragging = true;
				}
			}else{
				undo();//undo twice because you want it to revert black's last turn as well
				undo();
			}

		}else{
			handleAISelected();
		}
	}
	
	public void handleAISelected(){
		
		if(selected == SELECTED){
			newblock = getBlock((int) mx, (int) my);
	
			if(!isBack(newblock)){
				if(possibleMoves[newblock.y][newblock.x] == MoveStates.CASTLE){
					selected = NOT_SELECTED;
					castle(oldblock, newblock);
					checkPromote(newblock, player, false);
					switchTurn();
					saveCurrentBoard();
					makeAIMove(Players.BLACK);
					saveCurrentBoard();
					switchTurn();
				}else if(possibleMoves[newblock.y][newblock.x] == MoveStates.PASSANT){
					selected = NOT_SELECTED;
					passant(oldblock, newblock);
					checkPromote(newblock, player, false);
					switchTurn();
					saveCurrentBoard();
					makeAIMove(Players.BLACK);
					saveCurrentBoard();
					switchTurn();
				}else if(ChessBoard[newblock.y][newblock.x].getColor() == player){
					oldblock = newblock;
					dragging = true;
					possibleMoves = ChessUtil.getPossibleMoves(oldblock, ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, true);
				}else if(isValidMove(newblock)){
					selected = NOT_SELECTED;
					if(!dragging){	
						animate(oldblock, newblock);
					}else{
						movePiece(oldblock, newblock);
					}
					checkPromote(newblock, player, false);
					saveCurrentBoard();
					switchTurn();
					makeAIMove(Players.BLACK);
					saveCurrentBoard();
					switchTurn();
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

		if(selected == NOT_SELECTED){
			Pair pair = getBlock(mx, my);

			if(!isBack(pair)){ // if hasn't clicked back button
				if(isPiece(pair) && ChessBoard[pair.y][pair.x].getColor() == player){
					oldblock = pair;
					possibleMoves = ChessUtil.getPossibleMoves(oldblock, ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, true);
					selected = SELECTED;
					dragging = true;
				}
			}else{
				undo();
			}
		}else{
			handleHumanSelected();
		}	
	}
	
	public void handleHumanSelected(){
		if(selected == SELECTED){
			newblock = getBlock((int) mx, (int) my);
	
			if(!isBack(newblock)){
				if(possibleMoves[newblock.y][newblock.x] == MoveStates.CASTLE){
					selected = NOT_SELECTED;
					castle(oldblock, newblock);
					checkPromote(newblock, player, false);
					switchTurn();
					saveCurrentBoard();
				}else if(possibleMoves[newblock.y][newblock.x] == MoveStates.PASSANT){
					selected = NOT_SELECTED;
					passant(oldblock, newblock);
					checkPromote(newblock, player, false);
					switchTurn();
					saveCurrentBoard();
				}else if(ChessBoard[newblock.y][newblock.x].getColor() == player){
					oldblock = newblock;
					dragging = true;
					possibleMoves = ChessUtil.getPossibleMoves(oldblock, ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, true);
				}else if(isValidMove(newblock)){
					selected = NOT_SELECTED;
					if(!dragging){
						animate(oldblock, newblock);
					}else{
						movePiece(oldblock, newblock);					
					}
					checkPromote(newblock, player, false);
					saveCurrentBoard();
					switchTurn();
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
		animateX = getImagePixelsX(oldblock.x);
		animateY = getImagePixelsY(oldblock.y);
		animatedImage = getImage(oldblock.y, oldblock.x);
		ChessBoard[oldblock.y][oldblock.x] = new Empty();//removes piece so it isn't draw(re-added later)
		animating = true;
		
		timer.scheduleAtFixedRate(new TimerTask(){
			
			int counter = 0;
			
			double movRate = 5;//pixels per 10 milliseconds
			double movX = (getImagePixelsX(newblock.x) - getImagePixelsX(oldblock.x));
			double movY = (getImagePixelsY(newblock.y) - getImagePixelsY(oldblock.y));
			double mag = Math.sqrt(movX * movX + movY * movY);
			int numIterations = (int)(mag / movRate);

			@Override
			public void run() {
				if(counter < numIterations){
					animateX += movX / mag * movRate;
					animateY += movY / mag * movRate;
					counter++;
					repaint();
				}else{
					
					synchronized(animateLock){
						animateLock.notify();
					}
					
					animating = false;
					ChessBoard[oldblock.y][oldblock.x] = piece;
					movePiece(oldblock, newblock);
					repaint();
					this.cancel();
				}
				
			}
			
		}, 0, 10);
		
		synchronized(animateLock){
			try {
				animateLock.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void switchTurn(){
		if(player == Players.WHITE){
			frame.setTitle(BlackText.getText());
		}else{
			frame.setTitle(WhiteText.getText());
		}
		player = getOpponent(player);
	}
	
	public void saveCurrentBoard(){
		//always call before switch turn
		Piece[][] clone = new Piece[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++)
				clone[i][j] = (Piece) ChessBoard[i][j].clone();
		}
		
		whitePosHistory.add(WKingPos);
		blackPosHistory.add(BKingPos);
		
		previousMoves.add(clone);
	}

	public Players getOpponent(Players p){
		if(p == Players.WHITE)
			return Players.BLACK;
		else if(p == Players.BLACK)
			return Players.WHITE;
		
		return Players.EMPTY;
	}
	
	public void highlight(Pair p, Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.MAGENTA);
		g2.setStroke(new BasicStroke(5));
		g2.drawRect(getImagePixelsX(p.x), getImagePixelsY(p.y), BLOCK_WIDTH, BLOCK_HEIGHT);
	}

	public void movePiece(Pair oldblock, Pair newblock){

		//checks to stop future castling
		if(ChessBoard[oldblock.y][oldblock.x].equals(new King(Players.BLACK))){
			BKingPos = newblock;
			blackKingCastle = false;
			blackQueenCastle = false;
		}else if(ChessBoard[oldblock.y][oldblock.x].equals(new King(Players.WHITE))){
			WKingPos = newblock;
			whiteKingCastle = false;
			whiteQueenCastle = false;
		}else if(ChessBoard[oldblock.y][oldblock.x].equals(new Rook(Players.BLACK))){
			if(oldblock.equals(new Pair(7, 0)))
				blackKingCastle = false;
			else if(oldblock.equals(new Pair(0, 0)))
				blackQueenCastle = false;
		}else if(ChessBoard[oldblock.y][oldblock.x].equals(new Rook(Players.WHITE))){
			if(oldblock.equals(new Pair(7, 7)))
				whiteKingCastle = false;
			else if(oldblock.equals(new Pair(0, 7)))
				whiteQueenCastle = false;
		}

		ChessBoard[oldblock.y][oldblock.x].incrementMoves();
		ChessBoard[oldblock.y][oldblock.x].setLastMoved(numHalfMoves);
		ChessBoard[newblock.y][newblock.x] = ChessBoard[oldblock.y][oldblock.x];
		ChessBoard[oldblock.y][oldblock.x] = new Empty();
		
		numHalfMoves++;
		
		if(ChessUtil.isCheck(getOpponent(ChessBoard[newblock.y][newblock.x].getColor()), ChessBoard, WKingPos, BKingPos))
			numChecks++;// won't work with undo(FIX!!!)
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
					g.drawRect(getImagePixelsX(a), getImagePixelsY(b), BLOCK_WIDTH, BLOCK_HEIGHT);
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
			
			possibleMoves = ChessUtil.getPossibleMoves(new Pair(x, y), ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, true);

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
								oldblock = new Pair(x, y);
								newblock = new Pair(c, d);
								if(possibleMoves[d][c] == MoveStates.CASTLE){
									castle(oldblock, newblock);
								}else if(possibleMoves[d][c] == MoveStates.PASSANT){
									passant(oldblock, newblock);
								}else{
									animate(oldblock, newblock);
									checkPromote(newblock, p, true);
								}
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

	public void checkPromote(Pair block, Players p, boolean autoQueen){
		if(p == Players.WHITE){
			if(block.y == 0 && ChessBoard[block.y][block.x].getType() == PieceTypes.PAWN){
				if(autoQueen){
					ChessBoard[block.y][block.x] = new Queen(Players.WHITE);
				}else{
					openSelector(Players.WHITE, block);
				}
				
			}
		}else if(p == Players.BLACK){
			if(block.y == 7 && ChessBoard[block.y][block.x].getType() == PieceTypes.PAWN){
				if(autoQueen){
					ChessBoard[block.y][block.x] = new Queen(Players.BLACK);
				}else{
					openSelector(Players.BLACK, block);
				}
			}
		}
	}
	
	public void openSelector(Players p, Pair block){
		pSelect = new PieceSelector(115, 263, p);
		pSelect.addDiceModifierListener(new SelectionListener(){
	
			@Override
			public void selected(Piece p) {
				
				ChessBoard[block.y][block.x] = p;
				removeMouseListener(pSelect);
				removeMouseMotionListener(pSelect);
				synchronized(selectorLock){
					selectorLock.notify();
				}
				pSelect = null;
	
			}

			@Override
			public void refresh() {
				repaint();
			}
			
		});
		addMouseListener(pSelect);
		addMouseMotionListener(pSelect);
		
		synchronized(selectorLock){
			try {
				selectorLock.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		
		Players p = ChessBoard[oldblock.y][oldblock.x].getColor();
		
		ChessBoard[oldblock.y][oldblock.x] = new Empty();
		ChessBoard[newblock.y][newblock.x] = new Empty();
		
		if(newblock.x > oldblock.x){
			ChessBoard[newblock.y][6] = new King(p);// places king in correct spot
			ChessBoard[newblock.y][5] = new Rook(p);
			
			ChessBoard[newblock.y][6].incrementMoves();
			ChessBoard[newblock.y][6].setLastMoved(numHalfMoves);
			ChessBoard[newblock.y][5].incrementMoves();
			ChessBoard[newblock.y][5].setLastMoved(numHalfMoves);
			
			if(p == Players.WHITE){
				WKingPos = new Pair(6, newblock.y);
				whiteKingCastle = false;
				whiteQueenCastle = false;
			}else{
				BKingPos = new Pair(6, newblock.y);
				blackKingCastle = false;
				blackQueenCastle = false;
			}
		}else{
			ChessBoard[newblock.y][2] = new King(p);
			ChessBoard[newblock.y][3] = new Rook(p);
			
			ChessBoard[newblock.y][2].incrementMoves();
			ChessBoard[newblock.y][2].setLastMoved(numHalfMoves);
			ChessBoard[newblock.y][3].incrementMoves();
			ChessBoard[newblock.y][3].setLastMoved(numHalfMoves);
			
			if(p == Players.WHITE){
				WKingPos = new Pair(2, newblock.y);
				whiteKingCastle = false;
				whiteQueenCastle = false;
			}else{
				BKingPos = new Pair(2, newblock.y);
				blackKingCastle = false;
				blackQueenCastle = false;
			}
		}
		
		numHalfMoves++;
	}
	
	public void passant(Pair oldblock, Pair newblock){
		Players p = ChessBoard[oldblock.y][oldblock.x].getColor();
		
		if(p == Players.WHITE){
			ChessBoard[newblock.y + 1][newblock.x] = new Empty();
		}if(p == Players.BLACK){
			ChessBoard[newblock.y - 1][newblock.x] = new Empty();
		}
		
		ChessBoard[oldblock.y][oldblock.x].incrementMoves();
		ChessBoard[oldblock.y][oldblock.x].setLastMoved(numHalfMoves);
		ChessBoard[newblock.y][newblock.x] = ChessBoard[oldblock.y][oldblock.x];
		ChessBoard[oldblock.y][oldblock.x] = new Empty();
		
		numHalfMoves++;
	}
	
	public void undo(){
		
		if(previousMoves.size() > 1){
			Piece[][] restoredBoard = previousMoves.get(previousMoves.size() - 2);
			for(int i = 0; i < 8; i++)
				ChessBoard[i] = restoredBoard[i].clone();
			previousMoves.remove(previousMoves.size() - 1);
			
			if(player == Players.WHITE){
				WKingPos = whitePosHistory.get(whitePosHistory.size() - 2);
				whitePosHistory.remove(whitePosHistory.size() - 1);
			}else{
				BKingPos = blackPosHistory.get(blackPosHistory.size() - 2);
				blackPosHistory.remove(blackPosHistory.size() - 1);
			}
			
			numHalfMoves--;
			
			switchTurn();
		}else{
			Piece[][] restoredBoard = previousMoves.get(0);
			for(int i = 0; i < 8; i++)
				ChessBoard[i] = restoredBoard[i].clone();
			
			ChessBoard = restoredBoard;
			player = Players.WHITE;
			frame.setTitle(WhiteText.getText());
			
			WKingPos = whitePosHistory.get(0);
			BKingPos = blackPosHistory.get(0);
			
			numHalfMoves = 0;
		}
		
		repaint();
	}

	public  boolean isBack(Pair block){
		if(block.y == 8){
			return true;
		}

		return false;
	}
	
	public boolean isPiece(Pair p){
		if(ChessBoard[p.y][p.x].equals(new Empty())){
			return false;
		}

		return true;
	}
	
	public boolean isOnBoard(Pair block){
		if(block.x >= 0 && block.x <= 7 && block.y >= 0 && block.y <= 7){
			return true;
		}
		
		return false;
	}

	@Override
	public void paintComponent(Graphics g){ 

		g.setColor(new Color(255, 222, 173));
		g.fillRect(0, 0, 600, 600); 
		g.setColor(new Color(160, 82, 45));
		for(int i = 0; i <= 600; i+=150){ 
			for(int j = 0; j <= 450; j+=150){ 
				g.fillRect(i, j, BLOCK_WIDTH, BLOCK_HEIGHT); 
			} 
		} for(int i = 75; i <= 525; i+=150){ 
			for(int j = 75; j <= 525; j+=150){ 
				g.fillRect(i, j, BLOCK_WIDTH, BLOCK_HEIGHT); 
			} 
		}

		g.drawImage(BACK, getImagePixelsX(3) + BLOCK_WIDTH/2, getImagePixelsY(8), null);

		for(int a = 0; a <= 7; a++){
			for(int b = 0; b <= 7; b++){
				if(b != oldblock.y || a != oldblock.x || !dragging)
					g.drawImage(getImage(b, a), getImagePixelsX(a), getImagePixelsY(b), null);
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
		
		if(pSelect != null){
			pSelect.draw(g);
		}
		
		if(animating){
			g.drawImage(animatedImage, (int)animateX, (int)animateY, null);
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
		if(pSelect == null && !animating)
			handleMouseClick();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		if(!animating)
			handleMouseRelease();
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


