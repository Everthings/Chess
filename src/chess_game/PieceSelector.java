package chess_game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import chess_game.chess_pieces.Bishop;
import chess_game.chess_pieces.Knight;
import chess_game.chess_pieces.Piece;
import chess_game.chess_pieces.Queen;
import chess_game.chess_pieces.Rook;
import chess_game.listeners.SelectionListener;

public class PieceSelector implements MouseMotionListener, MouseListener, Drawable{
	
	private ArrayList<SelectionListener> listenerList = new ArrayList<SelectionListener>();
	
	private final int OVAL_WIDTH = 370;
	private final int OVAL_HEIGHT = 100;
	private final int ARC_WIDTH = 50;
	private final int ARC_HEIGHT = 50;
	
	private int offsetX;
	private int offsetY;
	
	private Timer timer = new Timer();
	
	private Piece[] displayPieces = new Piece[4];
	private BufferedImage[] pieceImages = new BufferedImage[4];
	private ArrayList<ArrayList<Pair>> imagePixels = new ArrayList<ArrayList<Pair>>();
	
	private int red = 255;
	private int green = 215;
	private int blue = 0;
	private double dr = red / 15;
	private double dg = green / 15;
	private double db = blue / 15;
	
	private int hoveredPieceIndex = -1;
	
	public PieceSelector(int offsetX, int offsetY, Players p){
		
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		
		initImages(p);	

		displayPieces[0] = new Queen(p);
		displayPieces[1] = new Rook(p);
		displayPieces[2] = new Knight(p);
		displayPieces[3] = new Bishop(p);
		
		for(int i = 0; i < pieceImages.length; i++)
			imagePixels.add(new ArrayList<Pair>());
		
		for(int i = 0; i < pieceImages.length; i++){	
			for(int a = 0; a < pieceImages[i].getWidth(); a++){
				for(int b = 0; b < pieceImages[i].getHeight(); b++){
	
					int imageColor = pieceImages[i].getRGB(a, b); 
					int imageRed = (imageColor & 0x00ff0000) >> 16;
					int imageGreen = (imageColor & 0x0000ff00) >> 8;
					int imageBlue = imageColor & 0x000000ff;
				
					if(imageRed + imageGreen + imageBlue < 600 && !isTransparent(a, b, pieceImages[i])){
						imagePixels.get(i).add(new Pair(a, b));
					}
				}
			}
		}
		
		timer.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
			
				for(int i = 0; i < pieceImages.length; i++){
					for(int j = 0; j < imagePixels.get(i).size(); j++){
						
						Pair p = imagePixels.get(i).get(j);
						
						int imageColor = pieceImages[i].getRGB(p.x, p.y); 
						int imageRed = (imageColor & 0x00ff0000) >> 16;
						int imageGreen = (imageColor & 0x0000ff00) >> 8;
						int imageBlue = imageColor & 0x000000ff;
						
						if(hoveredPieceIndex == i){
							Color newColor = new Color(ColorUtil.max((int)(imageRed + dr), red), ColorUtil.max((int)(imageGreen + dg), green), ColorUtil.max((int)(imageBlue + db), blue));
							pieceImages[i].setRGB(p.x, p.y, newColor.getRGB());
						}else{
							Color newColor = new Color(ColorUtil.min((int)(imageRed - dr), 0), ColorUtil.min((int)(imageGreen - dg), 0), ColorUtil.min((int)(imageBlue - db), 0));
							pieceImages[i].setRGB(p.x, p.y, newColor.getRGB());
						}
					}
				}
	
				triggerRefresh();
			}
			
		}, 0, 50);
	}
	
	public void initImages(Players p){
		try {
			if(p == Players.WHITE){
				pieceImages[0] = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/WhiteQueen.png"));	
				pieceImages[1] = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/WhiteRook.png"));	
				pieceImages[2] = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/WhiteKnight.png"));	
				pieceImages[3] = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/WhiteBishop.png"));	
			}else{
				pieceImages[0] = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/BlackQueen.png"));	
				pieceImages[1] = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/BlackRook.png"));	
				pieceImages[2] = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/BlackKnight.png"));	
				pieceImages[3] = ImageIO.read(new File("/Users/XuMan/Documents/ChessGame/BlackBishop.png"));	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isTransparent(int x, int y, BufferedImage bf) {
		int alpha = (bf.getRGB(x, y) >> 24) & 0xff;

		if(alpha < 100) {//100 is mostly transparent but not all
			return true;
		}
	  
	  return false;
	}
	
	public void addDiceModifierListener(SelectionListener c) {
		//adds event listener
		listenerList.add(c);
	}
		
	public void removeDiceModifierListener(SelectionListener c) {
		//removes event listener
		 listenerList.remove(c);
	}
		
	private void fireSelected(int index) {
		//propagates event upwards
	    for(SelectionListener cl: listenerList){
	    	cl.selected(displayPieces[index]);
	    }
	}
	
	private void triggerRefresh() {
		//propagates event upwards
	    for(SelectionListener cl: listenerList){
	    	cl.refresh();
	    }
	}
	
	@Override
	public void draw(Graphics g){
			
		g.setColor(Color.WHITE);
		g.fillRoundRect(offsetX, offsetY, OVAL_WIDTH, OVAL_HEIGHT, ARC_WIDTH, ARC_HEIGHT);
		
		for(int i = 0; i < pieceImages.length; i++)
			g.drawImage(pieceImages[i], offsetX + i * 90 + 12, offsetY + 12, null);
		
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		int mx = e.getX();
		int my = e.getY();
		
		if(my > offsetY + 12 && my < offsetY + 87){
			for(int i = 0; i < displayPieces.length; i++){
				if(mx > offsetX + i * 90 + 12 && mx < offsetX + i * 90 + 87){
					fireSelected(i);
					break;
				}
			}
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {

		int mx = e.getX();
		int my = e.getY();
		
		if(my > offsetY + 12 && my < offsetY + 87){
			for(int i = 0; i < displayPieces.length; i++){
				if(mx > offsetX + i * 90 + 12 && mx < offsetX + i * 90 + 87){
					hoveredPieceIndex = i;
					break;
				}else if(i == displayPieces.length){
					hoveredPieceIndex = -1;
				}
			}
		}else{
			hoveredPieceIndex = -1;
		}
	}
}