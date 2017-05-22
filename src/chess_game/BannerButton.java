package chess_game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

import chess_game.listeners.ClickListener;
import chess_game.listeners.OptionListener;

public class BannerButton extends AbstractButton{
	
	private ArrayList<ClickListener> listenerList = new ArrayList<ClickListener>();
	Color highlight = Color.GREEN;
	Color regular;

	BannerButton(int x, int y, int width, int height, Color c, String info) {
		super(x, y, width, height, c);

		this.regular = c;
		
		setText(info);
	}
	
	public void addListener(ClickListener cl){
		listenerList.add(cl);
	}
	
	public void removeListener(ClickListener cl){
		listenerList.remove(cl);
	}
	
	public void triggerRefresh(){
		for(ClickListener cl: listenerList)
			cl.refresh();
	}
	
	public void fireClicked(){
		for(ClickListener cl: listenerList)
			cl.clicked();
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue()));
		g.fillRect(x, y, width, height);
		g.setColor(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue()));
		Font newFont = new Font("Some Font", Font.ITALIC, 50);
		g.setFont(newFont);
		FontMetrics metrics = g.getFontMetrics(newFont);
		g.drawString(text, x + (width - metrics.stringWidth(text))/2, y + ((height - metrics.getHeight()) / 2) + metrics.getAscent());
	}

	@Override
	void handleMousePress(int mx, int my) {
		if(enabled){
			if(mx > x && mx < x + width && my > y && my < y + height){
				fireClicked();
			}
		}
	}

	@Override
	void handleMouseMove(int mx, int my) {
		if(enabled){
			if(mx > x && mx < x + width && my > y && my < y + height){
				c = highlight;
			}else{
				c = regular;
			}
			
			triggerRefresh();
		}
	}
}
