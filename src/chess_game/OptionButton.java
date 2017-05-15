package chess_game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import chess_game.listeners.OptionListener;

public class OptionButton implements MouseListener, MouseMotionListener{
	
	private ArrayList<OptionListener> listenerList = new ArrayList<OptionListener>();
	
	private int index;
	private final int MIN_OPAQUE = 50;
	private final int MAX_OPAQUE = 200;
	private final int INCREMENT = 10;
	private int opaque = MIN_OPAQUE;
	
	private int x, y, height, width;
	private Color c;
	
	private Timer timer = new Timer();
	private boolean mouseIsHovering = false;
	
	private String text = "";
	private Color textColor = Color.WHITE;
	
	boolean enabled = true;
	
	public OptionButton(int index, int x, int y, int width, int height, Color c){
		this.index = index;
		
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
	
	public void disable(){
		enabled = false;
		timer.cancel();
	}
	
	public boolean fadeAway(){
		opaque = ColorUtil.min(opaque - INCREMENT, 0);
		return opaque == 0;
	}
	
	public void startTimer(){
		timer.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				opaquify(!mouseIsHovering);
				triggerRefresh();
			}
			
		}, 0, 50);
	}
	
	public void setText(String str){
		this.text = str;
	}
	
	public void addListener(OptionListener ol){
		listenerList.add(ol);
	}
	
	public void removeListener(OptionListener ol){
		listenerList.remove(ol);
	}
	
	public void triggerRefresh(){
		for(OptionListener ol: listenerList)
			ol.refresh();
	}
	
	public void fireSelected(){
		for(OptionListener ol: listenerList)
			ol.selected(index);
	}
	
	public void opaquify(boolean moreOpaque){
		if(moreOpaque){
			opaque = ColorUtil.min(opaque - INCREMENT, MIN_OPAQUE);
		}else{
			opaque = ColorUtil.max(opaque + INCREMENT, MAX_OPAQUE);
		}
	}
	
	public void draw(Graphics g){
		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), opaque));
		g.fillRect(x, y, width, height);
		g.setColor(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), opaque));
		Font newFont = new Font("Some Font", Font.ITALIC, 50);
		g.setFont(newFont);
		FontMetrics metrics = g.getFontMetrics(newFont);
		g.drawString(text, x + (width - metrics.stringWidth(text))/2, y + ((height - metrics.getHeight()) / 2) + metrics.getAscent());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if(enabled){
			if(mx > x && mx < x + width && my > y && my < y + height){
				mouseIsHovering = true;
			}else{
				mouseIsHovering = false;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if(enabled){
			if(mx > x && mx < x + width && my > y && my < y + height){
				fireSelected();
			}
		}
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
