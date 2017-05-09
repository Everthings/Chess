package chess_game;

public class Pair {
	public int x;
	public int y;
	
	public Pair(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o){
		
		if(o instanceof Pair){
			Pair p = (Pair) o;
			if(this.x == p.x && this.y == p.y)
				return true;
		}
		
		return false;
	}
}
