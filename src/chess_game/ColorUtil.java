package chess_game;

public class ColorUtil {
	public static int max(int i, int max){
		if(i > max)
			return max;
		
		return i;
	}
	
	public static int min(int i, int min){
		if(i < min)
			return min;
		
		return i;
	}
}
