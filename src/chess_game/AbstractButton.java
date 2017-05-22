package chess_game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import chess_game.listeners.OptionListener;

abstract class AbstractButton implements MouseListener, MouseMotionListener, Drawable{

	int x, y, height, width;
	Color c;
	
	String text = "";
	Color textColor = Color.WHITE;
	

	
	boolean enabled = true;
	
	AbstractButton(int x, int y, int width, int height, Color c){
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		
		this.c = c;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setWidth(int width){
		this.width = width;
	}
	
	public void setHeight(int height){
		this.height = height;
	}
	
	public void setText(String str){
		this.text = str;
	}
	
	public boolean moveLeft(int x, int mx){
		
		this.x = ColorUtil.min(this.x - mx, 0);
		
		if(this.x == x)
			return true;
		
		return false;
	}
	
	public boolean moveRight(int x, int mx){
		
		this.x = ColorUtil.max(this.x + mx, 0);
		
		if(this.x == x)
			return true;
		
		return false;
	}
	
	public boolean moveDown(int y, int my){
		
		this.y = ColorUtil.max(this.y + my, y);
		
		if(this.y == y)
			return true;
		
		return false;
	}
	
	public void disable(){
		enabled = false;
	}
	
	public abstract void draw(Graphics g);
	
	abstract void handleMousePress(int mx, int my);
	
	abstract void handleMouseMove(int mx, int my);
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		handleMouseMove(e.getX(), e.getY());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		handleMousePress(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
