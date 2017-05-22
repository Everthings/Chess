package chess_game;

import java.awt.Color;

import javax.swing.JFrame;

import chess_game.listeners.GameListener;
import chess_game.listeners.OptionListener;

public class Runner {
	
	JFrame frame = new JFrame();
	int gameType = 0;
	int gamePlayer = 2;
	
	public static void main(String args[]){	
		new Runner();
	}
	
	Runner(){
		
		initWindow();
		OptionScreen optionScreen = new OptionScreen(600, 700);
		frame.add(optionScreen);
		frame.repaint();
		
		getGameType(optionScreen);
		getGamePlayer(optionScreen);
		
		frame.remove(optionScreen);
		
		Game game = new Game(gameType, gamePlayer);
		game.addListener(new GameListener(){

			@Override
			public void finished(Results results) {
				frame.remove(game);
				addEndScreen(results);
			}
			
		});
		
		frame.add(game);
		frame.repaint();
		
	}
	
	public void getGameType(OptionScreen screen){
		
		Object optionScreenLock = new Object();
		
		screen.addListener(new OptionListener(){

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
		screen.add(new String[]{"Regular", "960", "3-Check", "King of the Hill", "Test"});
		frame.repaint();
		
		synchronized(optionScreenLock){
			try {
				optionScreenLock.wait();	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		screen.removeAllButtons();
		screen.removeAllListeners();
	}

	public void getGamePlayer(OptionScreen screen){
		
		Object optionScreenLock = new Object();
		
		screen.addListener(new OptionListener(){

			@Override
			public void selected(int i) {
				gamePlayer = i;
				
				synchronized(optionScreenLock){
					optionScreenLock.notify();
				}
			}

			@Override
			public void refresh() {
				// TODO Auto-generated method stub
			}
			
		});
		screen.add(new String[]{"Human vs Human", "Human vs AI", "AI vs AI"});
		
		synchronized(optionScreenLock){
			try {
				optionScreenLock.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		screen.removeAllButtons();
		screen.removeAllListeners();
	}
	
	public void initWindow(){
		//post: creates a frame and starts a thread for constant repaint; Note: the repainting thread is not ideal, but makes my life easier =)
		java.awt.EventQueue.invokeLater(new Runnable() {// uses event-dispatch thread to ensure thread safety
		    public void run() {
		    	frame.setTitle("Chess");
				frame.setSize(600, 700); 
				frame.setLocationRelativeTo(null); 
				frame.setBackground(Color.BLACK); 
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
				frame.setVisible(true);
				frame.setResizable(false);
				frame.repaint();
				frame.revalidate();
		    }
		});
	}
	
	public void addEndScreen(Results r){
		
		Object lock = new Object();
		
		OptionScreen optionScreen = new OptionScreen(600, 700);
		frame.add(optionScreen);
		
		optionScreen.addListener(new OptionListener(){

			@Override
			public void selected(int i) {

				if(i == 0){
					new Thread(new Runnable(){

						@Override
						public void run() {
						
							optionScreen.removeAllListeners();
							optionScreen.removeAllButtons();
							
							getGameType(optionScreen);
							getGamePlayer(optionScreen);
							
							frame.remove(optionScreen);
							Game game = new Game(gameType, gamePlayer);
							game.addListener(new GameListener(){

								@Override
								public void finished(Results results) {
									frame.remove(game);
									addEndScreen(results);
								}
								
							});
							
							frame.add(game);
							frame.repaint();
							
						}
						
					}).start();
				}else if(i == 1){
					System.exit(0);
				}
			
				synchronized(lock){
					lock.notify();
				}
			}

			@Override
			public void refresh() {
				// TODO Auto-generated method stub
			}
			
		});
		optionScreen.add(new String[]{"Play Again", "Exit"});
		frame.repaint();
		
		synchronized(lock){
			try {
				lock.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
