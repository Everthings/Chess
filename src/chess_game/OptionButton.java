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

public class OptionButton extends AbstractButton{
	
	private ArrayList<OptionListener> listenerList = new ArrayList<OptionListener>();
	
	private int index;
	final int MIN_OPAQUE = 50;
	final int MAX_OPAQUE = 200;
	final int INCREMENT = 10;
	int opaque = MIN_OPAQUE;
	
	private Timer timer = new Timer();
	private boolean mouseIsHovering = false;
	
	boolean enabled = true;
	
	public OptionButton(int index, int x, int y, int width, int height, Color c){
		super(x, y, width, height, c);
		
		this.index = index;
	}
	
	@Override
	public void disable(){
		enabled = false;
		timer.cancel();
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
	
	public void cancelTimer(){
		timer.cancel();
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
	
	public boolean fadeAway(){
		opaque = ColorUtil.min(opaque - INCREMENT, 0);
		return opaque == 0;
	}
	
	@Override
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
	void handleMousePress(int mx, int my) {
		if(enabled){
			if(mx > x && mx < x + width && my > y && my < y + height){
				fireSelected();
			}
		}
	}

	@Override
	void handleMouseMove(int mx, int my) {
		if(enabled){
			if(mx > x && mx < x + width && my > y && my < y + height){
				mouseIsHovering = true;
			}else{
				mouseIsHovering = false;
			}
		}
	}	
}
