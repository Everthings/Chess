package chess_game;
import java.awt.AlphaComposite;
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

import chess_game.analysis.AIReturnObject;
import chess_game.chess_pieces.Empty;
import chess_game.chess_pieces.Piece;
import chess_game.chess_pieces.Queen;
import chess_game.game_modes.AbstractGame;
import chess_game.game_modes.KingOfHillGame;
import chess_game.game_modes.NineSixtyGame;
import chess_game.game_modes.RegularGame;
import chess_game.game_modes.TestGame;
import chess_game.game_modes.ThreeCheckGame;
import chess_game.listeners.ClickListener;
import chess_game.listeners.GameListener;
import chess_game.listeners.SelectionListener;

@SuppressWarnings("serial")
public class Game extends JPanel implements MouseListener, MouseMotionListener{

	ArrayList<GameListener> listenerList = new ArrayList<GameListener>();
	
	int gameType;
	int response;
	final int HUMAN = 0;
	final int AI = 1;
	Players AI_COLOR = Players.BLACK;

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
	
	boolean dragging = false;
	int mx = 0;
	int my = 0;
	
	boolean animating = false;
	volatile Pair animatedPiece = null;

	MoveStates[][] possibleMoves = new MoveStates[8][8];
	
	Timer timer = new Timer();

	ArrayList<Piece[][]> previousMoves = new ArrayList<Piece[][]>();
	ArrayList<Pair> blackPosHistory = new ArrayList<Pair>();
	ArrayList<Pair> whitePosHistory = new ArrayList<Pair>();
	
	volatile ArrayList<Drawable> drawables = new ArrayList<Drawable>();
	
	int opacity = 255;
	AbstractGame game;
	SOAPClient client = new SOAPClient();
	boolean AIRunning = true;
	int depth = 4;
	chess_game.analysis.AI ai = new chess_game.analysis.AI(depth);
	
	Game(int gameType, int gamePlayer){
		
		response = gamePlayer;
		this.gameType = gameType;
		
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
		
		final int REGULAR = 0;
		final int NINE_SIXTY = 1;
		final int THREE_CHECK = 2;
		final int KING_OF_HILL = 3;
		final int END_GAME = 4;
		if(gameType == REGULAR){
			game = new RegularGame();
		}else if(gameType == NINE_SIXTY){
			game = new NineSixtyGame(new Random(getSeedValue()));
		}else if(gameType == THREE_CHECK){
			game = new ThreeCheckGame();
		}else if(gameType == KING_OF_HILL){
			game = new KingOfHillGame();
		}else if(gameType == END_GAME){
			game = new TestGame();
		}
		
		game.initVariables();
		
		setSize(600, 700);
		setVisible(true);
		setBackground(Color.BLACK);
		
		Piece[][] ChessBoard = game.getChessBoard();
		
		Piece[][] clone = new Piece[8][8];
		for(int i = 0; i < 8; i++){
			clone[i] = ChessBoard[i].clone();
		}
		previousMoves.add(clone);
		whitePosHistory.add(game.getWhiteKingPos());
		blackPosHistory.add(game.getBlackKingPos());
		
		ChessUtil.initPossibleMovesArray(possibleMoves);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		if(gamePlayer == 2){
			new Thread(new Runnable(){

				@Override
				public void run() {
					runAIvsAI(Players.WHITE);
				}
				
			}).start();
				
		}
	}
	
	public void runAIvsAI(Players p){
		if(AIRunning){
			makeAIMove2(p);
			switchTurn();
			if(ChessUtil.isCheck(Players.BLACK, game.getChessBoard(), game.getWhiteKingPos(), game.getBlackKingPos())){
				game.incrementNumChecks(getOpponent(getOpponent(p)));
			}
			checkForGameEnd(getOpponent(p));
			runAIvsAI(getOpponent(p));
		}
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
	
	public void setAIColor(Players aiColor){//for Human vs AI
		this.AI_COLOR = aiColor;

		if(aiColor == Players.WHITE){
			
			Timer timer2 = new Timer();
			
			timer2.schedule(new TimerTask(){

				@Override
				public void run() {
					makeAIMove2(aiColor);
					saveCurrentBoard();
					switchTurn();
				}
				
			}, 200);
		}
	}
	
	public boolean isInt(String str){
		try{
			int i = Integer.parseInt(str);
		}catch(NumberFormatException nfe){
			return false;
		}
		
		return true;
	}

	public void addListener(GameListener gl){
		listenerList.add(gl);
	}
	
	public void removeListener(GameListener gl){
		listenerList.remove(gl);
	}
	
	public void fireFinished(Results r){
		for(GameListener gl: listenerList){
			gl.finished(r);
		}
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
			
			Piece[][] ChessBoard = game.getChessBoard();

			Pair pair = getBlock(mx, my);

			if(!isBack(pair)){ // if hasn't clicked back button
				if(isPiece(pair) && ChessBoard[pair.y][pair.x].getColor() == getOpponent(AI_COLOR)){
					oldblock = pair;
					possibleMoves = ChessUtil.getPossibleMoves(oldblock, ChessBoard, game.getWhiteKingPos(), game.getBlackKingPos(), game.getHalfMoves(), game.whiteKingCastle(), game.whiteQueenCastle(), game.blackKingCastle(), game.blackQueenCastle(), true);
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
			
			Piece[][] ChessBoard = game.getChessBoard();
			
			newblock = getBlock((int) mx, (int) my);
			
			MoveStates moveType = MoveStates.CLOSED;
			
			if(!isBack(newblock)){
				
				selected = NOT_SELECTED;
				
				if(possibleMoves[newblock.y][newblock.x] == MoveStates.CASTLE){
					
					moveType = MoveStates.CASTLE;
					
				}else if(possibleMoves[newblock.y][newblock.x] == MoveStates.PASSANT){
					
					moveType = MoveStates.PASSANT;
					
				}else if(ChessBoard[newblock.y][newblock.x].getColor() == player){
					selected = SELECTED;
					oldblock = newblock;
					dragging = true;
					possibleMoves = ChessUtil.getPossibleMoves(oldblock, ChessBoard, game.getWhiteKingPos(), game.getBlackKingPos(), game.getHalfMoves(), game.whiteKingCastle(), game.whiteQueenCastle(), game.blackKingCastle(), game.blackQueenCastle(), true);
				}else if(isValidMove(newblock)){
					
					if(!dragging){
						animate(oldblock, newblock, MoveStates.OPEN);
						checkPromote(newblock, player, false);
						saveCurrentBoard();
						switchTurn();
						
						if(ChessUtil.isCheck(player, ChessBoard, game.getWhiteKingPos(), game.getBlackKingPos())){
							game.incrementNumChecks(getOpponent(player));
						}
						checkForGameEnd(player);
						makeAIMove2(AI_COLOR);
						saveCurrentBoard();
						switchTurn();
						
						if(ChessUtil.isCheck(player, ChessBoard, game.getWhiteKingPos(), game.getBlackKingPos())){
							game.incrementNumChecks(getOpponent(player));
						}
						checkForGameEnd(player);
					}else{
						moveType = MoveStates.OPEN;		
					}
					
				}else{
					handleHumanMove();
				}
				
				if(moveType != MoveStates.CLOSED){
					game.movePiece(oldblock, newblock, moveType);	
					checkPromote(newblock, player, false);
					saveCurrentBoard();
					switchTurn();
					
					if(ChessUtil.isCheck(player, ChessBoard, game.getWhiteKingPos(), game.getBlackKingPos())){
						game.incrementNumChecks(player);
					}
					checkForGameEnd(player);
					repaint();
					makeAIMove2(AI_COLOR);
					saveCurrentBoard();
					switchTurn();
					
					if(ChessUtil.isCheck(player, ChessBoard, game.getWhiteKingPos(), game.getBlackKingPos())){
						game.incrementNumChecks(player);
					}
					checkForGameEnd(player);
				}
			}else{			
				selected = NOT_SELECTED;
			}
		}else{			
			selected = NOT_SELECTED;
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
			
			Piece[][] ChessBoard = game.getChessBoard();
			
			Pair pair = getBlock(mx, my);

			if(!isBack(pair)){ // if hasn't clicked back button
				if(isPiece(pair) && ChessBoard[pair.y][pair.x].getColor() == player){
					oldblock = pair;
					possibleMoves = ChessUtil.getPossibleMoves(oldblock, ChessBoard, game.getWhiteKingPos(), game.getBlackKingPos(), game.getHalfMoves(), game.whiteKingCastle(), game.whiteQueenCastle(), game.blackKingCastle(), game.blackQueenCastle(), true);
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
			
			Piece[][] ChessBoard = game.getChessBoard();
			
			newblock = getBlock((int) mx, (int) my);
			
			MoveStates moveType = MoveStates.CLOSED;
	
			if(!isBack(newblock)){
				
				selected = NOT_SELECTED;
				
				if(possibleMoves[newblock.y][newblock.x] == MoveStates.CASTLE){
					
					moveType = MoveStates.CASTLE;
					
				}else if(possibleMoves[newblock.y][newblock.x] == MoveStates.PASSANT){
					
					moveType = MoveStates.PASSANT;
					
				}else if(ChessBoard[newblock.y][newblock.x].getColor() == player){
					selected = SELECTED;
					oldblock = newblock;
					dragging = true;
					possibleMoves = ChessUtil.getPossibleMoves(oldblock, ChessBoard, game.getWhiteKingPos(), game.getBlackKingPos(), game.getHalfMoves(), game.whiteKingCastle(), game.whiteQueenCastle(), game.blackKingCastle(), game.blackQueenCastle(), true);
				}else if(isValidMove(newblock)){
					
					if(!dragging){
						animate(oldblock, newblock, MoveStates.OPEN);
						checkPromote(newblock, player, false);
						saveCurrentBoard();
						switchTurn();
						
						if(ChessUtil.isCheck(player, ChessBoard, game.getWhiteKingPos(), game.getBlackKingPos())){
							game.incrementNumChecks(getOpponent(player));
						}
						checkForGameEnd(player);
					}else{
						moveType = MoveStates.OPEN;		
					}
					
				}else{
					handleHumanMove();
				}
				
				if(moveType != MoveStates.CLOSED){
					game.movePiece(oldblock, newblock, moveType);	
					checkPromote(newblock, player, false);
					saveCurrentBoard();
					switchTurn();
					
					if(ChessUtil.isCheck(player, ChessBoard, game.getWhiteKingPos(), game.getBlackKingPos())){
						game.incrementNumChecks(getOpponent(player));
					}
					checkForGameEnd(player);
				}
			}else{			
				selected = NOT_SELECTED;
			}
		}
	}
	
	public void animate(Pair oldblock, Pair newblock, MoveStates type){
		
		Object animateLock = new Object();
	
		Piece[][] ChessBoard = game.getChessBoard();
		
		DrawableImage image = new DrawableImage(getImage(oldblock.y, oldblock.x, ChessBoard), getImagePixelsX(oldblock.x), getImagePixelsY(oldblock.y));
		drawables.add(image);
		animatedPiece = oldblock;
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
					image.setX(image.getX() + movX / mag * movRate);
					image.setY(image.getY() + movY / mag * movRate);
					counter++;
				}else{
					
					synchronized(animateLock){
						animateLock.notify();
					}

					this.cancel();
				}
				
				repaint();
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

		animating = false;
		animatedPiece = null;
		drawables.remove(image);
		game.movePiece(oldblock, newblock, type);
	}
	
	public void switchTurn(){
		player = getOpponent(player);
	}
	
	public void saveCurrentBoard(){
		//always call before switch turn
		
		Piece[][] ChessBoard = game.getChessBoard();
		Piece[][] clone = new Piece[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++)
				clone[i][j] = (Piece) ChessBoard[i][j].clone();
		}
		
		whitePosHistory.add(game.getWhiteKingPos());
		blackPosHistory.add(game.getBlackKingPos());
		
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

	public boolean isValidMove(Pair newblock){//compares move with possibleMoves array
		
		if(possibleMoves[newblock.y][newblock.x] != MoveStates.CLOSED){
			return true;
		}

		return false;
	}

	public void drawPossibleMoves(Graphics g){
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

	public void makeAIMove2(Players p){
		
		boolean moved = false;
		String opening = client.getOppeningMove(game.getChessBoard(), p, game.whiteKingCastle(), game.whiteQueenCastle(), game.blackKingCastle(), game.blackQueenCastle(), null, game.getHalfMoves());
		if(opening != null){
			System.out.println("Running Opening");
			System.out.println(opening);

			Pair[] pairs = client.decodeLocationsFromOpeningString(opening);

			oldblock = pairs[0];
			newblock = pairs[1];

			if(game.getChessBoard()[oldblock.y][oldblock.x].getType() == PieceTypes.KING){
				if(game.getKingCastle(p)){
					if(oldblock.x == 4 && newblock.x == 6){
						newblock.x = 7;
						game.castle(oldblock, newblock);
						moved = true;
					}
				}

				if(game.getQueenCastle(p)){
					if(oldblock.x == 4 && newblock.x == 2){
						newblock.x = 0;
						game.castle(oldblock, newblock);
						moved = true;
					}
				}	   
			}
		}else if(ChessUtil.getNumberOfPieces(game.getChessBoard()) > 6){
			
			System.out.println("Running Mid Game");
			ai.updateLine(p, game.getChessBoard(), game.getHalfMoves(), game.whiteKingCastle(), game.whiteQueenCastle(), game.blackKingCastle(), game.blackQueenCastle());
			AIReturnObject retObject = ai.makeMove(game.getWhiteKingPos(), game.getBlackKingPos());
			if(!ChessUtil.isCheck(p, retObject.getPosition().getBoard(), ChessUtil.getKingPosition(Players.WHITE, retObject.getPosition().getBoard()), ChessUtil.getKingPosition(Players.BLACK, retObject.getPosition().getBoard()))){
				oldblock = retObject.getMove().getOldPair();
				newblock = retObject.getMove().getNewPair();
				
				animate(oldblock, newblock, retObject.getMove().getMoveType());
				checkPromote(newblock, p, true);
				
				System.out.println("oldblock: " + oldblock.x + " " + oldblock.y);
				System.out.println("newblock: " + newblock.x + " " + newblock.y +"\n\n");
				
				moved = true;
				
			}else{
				 String move = client.getMidMove(game.getChessBoard(), p, game.whiteKingCastle(), game.whiteQueenCastle(), game.blackKingCastle(), game.blackQueenCastle(), null, game.getHalfMoves());
			    System.out.println(move);	

			    Pair[] pairs = client.decodeLocationsFromMidString(move);

			    oldblock = pairs[0];
			    newblock = pairs[1];

			    if(game.getChessBoard()[oldblock.y][oldblock.x].getType() == PieceTypes.KING){
				    if(game.getKingCastle(p)){
				    	if(oldblock.x == 4 && newblock.x == 6){
				    		newblock.x = 7;
				    		game.castle(oldblock, newblock);
				    		moved = true;
				    	}
				    }

				    if(game.getQueenCastle(p)){
				    	if(oldblock.x == 4 && newblock.x == 2){
				    		newblock.x = 0;
				    		game.castle(oldblock, newblock);
				    		moved = true;
				    	}
				    }	   
				}
			}
			
		}else{
			System.out.println("Running End Game");
			String move = client.getEndMove(game.getChessBoard(), p, game.whiteKingCastle(), game.whiteQueenCastle(), game.blackKingCastle(), game.blackQueenCastle(), null, game.getHalfMoves());
			System.out.println(move + "\n");	

			oldblock = client.decodeOpeningLocationFromString(move, p, game.getChessBoard(), game.getWhiteKingPos(), game.getBlackKingPos(), game.whiteKingCastle(), game.whiteQueenCastle(), game.blackKingCastle(), game.blackQueenCastle(), game.getHalfMoves());
			newblock = client.decodeEndingLocationFromString(move);
		}

		if(!moved){
			animate(oldblock, newblock, MoveStates.OPEN);
			checkPromote(newblock, p, true);
		}
	}
	
	public void makeAIMove(Players p){
	
		Piece[][] ChessBoard = game.getChessBoard();
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
			
			possibleMoves = ChessUtil.getPossibleMoves(new Pair(x, y), ChessBoard, game.getWhiteKingPos(), game.getBlackKingPos(), game.getHalfMoves(), game.whiteKingCastle(), game.whiteQueenCastle(), game.blackKingCastle(), game.blackQueenCastle(), true);

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
						if(possibleMoves[d][c] != MoveStates.CLOSED){
							if(selectMove == movesPassed){
								oldblock = new Pair(x, y);
								newblock = new Pair(c, d);
								
								if(possibleMoves[d][c] == MoveStates.OPEN || possibleMoves[d][c] == MoveStates.TAKE){
									animate(oldblock, newblock, MoveStates.OPEN);
									checkPromote(newblock, p, true);
								}else{
									game.movePiece(oldblock, newblock, possibleMoves[d][c]);
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
		
		Piece[][] ChessBoard = game.getChessBoard();
		
		if(p == Players.WHITE){
			if(block.y == 0 && ChessBoard[block.y][block.x].getType() == PieceTypes.PAWN){
				if(autoQueen){
					ChessBoard[block.y][block.x] = new Queen(Players.WHITE);
					game.setChessBoard(ChessBoard);
				}else{
					openSelector(Players.WHITE, block);
				}
				
			}
		}else if(p == Players.BLACK){
			if(block.y == 7 && ChessBoard[block.y][block.x].getType() == PieceTypes.PAWN){
				if(autoQueen){
					ChessBoard[block.y][block.x] = new Queen(Players.BLACK);
					game.setChessBoard(ChessBoard);
				}else{
					openSelector(Players.BLACK, block);
				}
			}
		}
	}
	
	public void openSelector(Players p, Pair block){
		
		Piece[][] ChessBoard = game.getChessBoard();
		Object selectorLock = new Object();
		
		PieceSelector pSelect = new PieceSelector(115, 263, p);
		pSelect.addDiceModifierListener(new SelectionListener(){
	
			@Override
			public void selected(Piece p) {
				ChessBoard[block.y][block.x] = p;
				game.setChessBoard(ChessBoard);
				removeMouseListener(pSelect);
				removeMouseMotionListener(pSelect);
				synchronized(selectorLock){
					selectorLock.notify();
				}
				
			}

			@Override
			public void refresh() {
				repaint();
			}
			
		});
		addMouseListener(pSelect);
		addMouseMotionListener(pSelect);
		drawables.add(pSelect);
		animating = true;
		
		synchronized(selectorLock){
			try {
				selectorLock.wait();
				drawables.remove(pSelect);
				removeMouseListener(pSelect);
				removeMouseMotionListener(pSelect);
				animating = false;
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
	
	public void checkForGameEnd(Players p){
		
		Piece[][] ChessBoard = game.getChessBoard();
		
		Results r = game.isGameEnd(p, ChessBoard, game.getWhiteKingPos(), game.getBlackKingPos(), game.getHalfMoves(), game.whiteKingCastle(), game.whiteQueenCastle(), game.blackKingCastle(), game.blackQueenCastle());
		
		if(r == Results.WIN || r == Results.DRAW){
			AIRunning = false;
			Object lock = new Object();
			
			String text = "Draw...";
			
			if(r == Results.WIN){
				text = getOpponent(p).toString() + " wins!";
			}
			
			BannerButton bannerButton = new BannerButton(0, -250, getWidth(), 100, Color.DARK_GRAY, text);
			bannerButton.addListener(new ClickListener(){

				@Override
				public void clicked() {
					synchronized(lock){
						lock.notify();
					}
				}

				@Override
				public void refresh() {
					repaint();
				}
				
			});
			repaint();
			addMouseListener(bannerButton);
			addMouseMotionListener(bannerButton);
			drawables.add(bannerButton);
			animating = true;
			
			Object timerLock = new Object();
			
			timer.scheduleAtFixedRate(new TimerTask(){

				int my = 1;
				
				@Override
				public void run() {
					if(bannerButton.moveDown(250, my += 2)){
						
						synchronized(timerLock){
							timerLock.notify();
						}
						
						this.cancel();

					}
					repaint();
				}
			}, 0, 30);
			
			synchronized(timerLock){
				try {
					timerLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			synchronized(lock){
				try {
					lock.wait();
					drawables.remove(bannerButton);
					removeMouseListener(bannerButton);
					removeMouseMotionListener(bannerButton);
					animating = false;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			timer.scheduleAtFixedRate(new TimerTask(){

				@Override
				public void run() {
					opacity = ColorUtil.min(opacity - 10, 0);
					
					if(opacity == 0){
						
						synchronized(lock){
							lock.notify();
						}
						
						this.cancel();
					}else{
						repaint();
					}
				}
				
			}, 0, 50);
			
			synchronized(lock){
				try {
					lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			fireFinished(r);
		}
	}
	
	public void undo(){
		
		Piece[][] ChessBoard = game.getChessBoard();
		
		if(previousMoves.size() > 1){
		
			if(ChessUtil.isCheck(player, ChessBoard, game.getWhiteKingPos(), game.getBlackKingPos())){
				game.decrementNumChecks(getOpponent(player));
			}
			
			Piece[][] restoredBoard = previousMoves.get(previousMoves.size() - 2);
			for(int i = 0; i < 8; i++)
				ChessBoard[i] = restoredBoard[i].clone();
			previousMoves.remove(previousMoves.size() - 1);
			
			if(player == Players.WHITE){
				game.setBlackKingPos(blackPosHistory.get(blackPosHistory.size() - 2));
			}else{
				game.setWhiteKingPos(whitePosHistory.get(whitePosHistory.size() - 2));
			}
			whitePosHistory.remove(whitePosHistory.size() - 1);
			blackPosHistory.remove(blackPosHistory.size() - 1);
			
			game.setChessBoard(ChessBoard);
			
			game.decrementHalfMoves();
			
			switchTurn();
		}else{
			Piece[][] restoredBoard = previousMoves.get(0);
			for(int i = 0; i < 8; i++)
				ChessBoard[i] = restoredBoard[i].clone();
			
			ChessBoard = restoredBoard;
			player = Players.WHITE;
			
			game.setWhiteKingPos(whitePosHistory.get(0));
			game.setBlackKingPos(blackPosHistory.get(0));
			
			game.setHalfMoves(0);
			game.resetChecks();
			
			game.setChessBoard(ChessBoard);
			
			if(AI_COLOR == Players.WHITE && response == 1){
				makeAIMove2(AI_COLOR);
				saveCurrentBoard();
				switchTurn();
			}
		}

		repaint();
	}

	public boolean isBack(Pair block){
		if(block.y == 8){
			return true;
		}

		return false;
	}
	
	public boolean isPiece(Pair p){
		
		Piece[][] ChessBoard = game.getChessBoard();
		
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
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)opacity/255));

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

		g2.drawImage(BACK, getImagePixelsX(3) + BLOCK_WIDTH/2, getImagePixelsY(8), null);

		Piece[][] ChessBoard = game.getChessBoard();
		
		if(animatedPiece != null)
			ChessBoard[animatedPiece.y][animatedPiece.x] = new Empty();
		
		for(int a = 0; a <= 7; a++){
			for(int b = 0; b <= 7; b++){
				if(b != oldblock.y || a != oldblock.x || !dragging){
					g2.drawImage(getImage(b, a, ChessBoard), getImagePixelsX(a), getImagePixelsY(b), null);
				}
			}
		}	
		
		if(selected == SELECTED){
			drawPossibleMoves(g);
			highlight(oldblock, g);
			if(dragging){
				BufferedImage img = getImage(oldblock.y, oldblock.x, ChessBoard);
				g2.drawImage(img, mx - img.getWidth()/2, my - img.getHeight()/2, null);
			}
		}
		
		try{
			for(Drawable d: drawables){
				d.draw(g);
			}
		}catch(Exception e){
			
		}
	}
	
	public BufferedImage getImage(int row, int col, Piece[][] ChessBoard){
		
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
		if(!animating && (response != AI || (player == getOpponent(AI_COLOR))))
			handleMouseClick();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		if(!animating && (response != AI || (player == getOpponent(AI_COLOR))))
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


