package chess_game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import chess_game.listeners.OptionListener;

public class OptionScreen extends JPanel{

	private ArrayList<OptionButton> buttons = new ArrayList<OptionButton>();
	private int yBorder = 30;
	private final int HEIGHT, WIDTH;
	
	private ArrayList<OptionListener> listenerList = new ArrayList<OptionListener>();
	private Timer timer = new Timer();
	
	OptionScreen(int width, int height){
		this.HEIGHT = height;
		this.WIDTH = width;
		
		setSize(width, height);
		setBackground(Color.BLACK);
		
		repaint();
	}
	
	public void addListener(OptionListener ol){
		listenerList.add(ol);
	}
	
	public void removeListener(OptionListener ol){
		listenerList.remove(ol);
	}
	
	public void removeAllListeners(){
		listenerList.removeAll(listenerList);
	}
	
	public void triggerRefresh(){
		for(OptionListener ol: listenerList)
			ol.refresh();
	}
	
	public void fireSelected(int i){
		for(OptionListener ol: listenerList)
			ol.selected(i);
	}
	
	public void add(Object[] arr){
		
		int buttonHeight = (HEIGHT - 2 * yBorder - 22) / (buttons.size() + arr.length);
		
		for(int i = 0; i < arr.length; i++){
			int startX = WIDTH;
			if(i % 2 == 1){
				startX = -WIDTH;
			}
			
			OptionButton b = new OptionButton(i, startX, yBorder + i * buttonHeight, WIDTH, buttonHeight, new Color(139, 69, 19));
			
			buttons.add(b);
			b.addListener(new OptionListener(){

				@Override
				public void selected(int i) {
					fireSelected(i);
				}

				@Override
				public void refresh() {
					repaint();
				}
				
			});
			
			b.setText(arr[i].toString());
			
			addMouseListener(b);
			addMouseMotionListener(b);
			
			b.startTimer();
		}
		
		calculateButtonPositions(0, buttons.size() - arr.length);
		
		Object lock = new Object();
	
		timer.scheduleAtFixedRate(new TimerTask(){
			int[] mxArr = new int[buttons.size()];

			@Override
			public void run() {

				boolean allMoved = true;
				
				for(int i = 0; i < buttons.size(); i++){
					if(i % 2 == 0){
						if(!buttons.get(i).moveLeft(0, mxArr[i])){
							allMoved = false;
						}
					}else{
						if(!buttons.get(i).moveRight(0, mxArr[i])){
							allMoved = false;
						}
					}
					
					mxArr[i] += 2;
				}
				
				if(allMoved){				
					synchronized(lock){
						lock.notify();
					}
					
					this.cancel();
				}else{
					repaint();
				}
			}
			
		}, 0, 30);
		
		synchronized(lock){
			try {
				lock.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void removeAllButtons(){
		
		Object lock = new Object();
		
		for(OptionButton ob: buttons)
			ob.disable();

		timer.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {

				boolean allFaded = true;
				
				for(OptionButton ob: buttons){
					if(!ob.fadeAway())
						allFaded = false;
				}
				
				if(allFaded){				
					buttons.removeAll(buttons);
					
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
	}
	
	public void calculateButtonPositions(int startIndex, int endIndex){
		int buttonHeight = (HEIGHT - 2 * yBorder - 22) / buttons.size();
		
		for(int i = startIndex; i < endIndex; i++){
			buttons.get(i).setY(yBorder + i * buttonHeight);
			buttons.get(i).setHeight(buttonHeight);
		}
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		try{
			for(OptionButton ob: buttons){
				ob.draw(g);
			}
		}catch(Exception e){
			
		}
	}
}
