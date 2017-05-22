package chess_game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class DrawableImage implements Drawable{
	
	private BufferedImage image;
	private double x, y;
	
	DrawableImage(BufferedImage image, int x, int y){
		this.image = image;
		this.x = x;
		this.y = y;
	}
	
	public void setX(double x){
		this.x = x;
	}

	public void setY(double y){
		this.y = y;
	}
	
	public double getX(){
		return x;
	}

	public double getY(){
		return y;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(image, (int)x, (int)y, null);
	}
}
