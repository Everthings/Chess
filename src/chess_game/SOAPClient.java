package chess_game;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.soap.*;

import chess_game.chess_pieces.Bishop;
import chess_game.chess_pieces.Empty;
import chess_game.chess_pieces.King;
import chess_game.chess_pieces.Knight;
import chess_game.chess_pieces.Pawn;
import chess_game.chess_pieces.Piece;
import chess_game.chess_pieces.Queen;
import chess_game.chess_pieces.Rook;

public class SOAPClient {
	MessageFactory messageFactory;
	SOAPMessage message;
	
	SOAPConnectionFactory connectionFactory;
	SOAPConnection connection;
	
	static Piece[][] ChessBoard = {{new Rook(Players.BLACK), new Knight(Players.BLACK), new Bishop(Players.BLACK), new Queen(Players.BLACK), new King(Players.BLACK), new Bishop(Players.BLACK), new Knight(Players.BLACK), new Rook(Players.BLACK)},
			{new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK), new Pawn(Players.BLACK)},
			{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
			{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
			{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
			{new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
			{new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE), new Pawn(Players.WHITE)}, 
			{new Rook(Players.WHITE), new Knight(Players.WHITE), new Bishop(Players.WHITE), new Queen(Players.WHITE), new King(Players.WHITE), new Bishop(Players.WHITE), new Knight(Players.WHITE), new Rook(Players.WHITE)}};

	SOAPClient(){
		 try {
			messageFactory = MessageFactory.newInstance();
			connectionFactory = SOAPConnectionFactory.newInstance();
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	public static void main(String[] args){
		//for testing
		Pair[] locs = new SOAPClient().decodeLocationsFromStartString("2291");
		System.out.println("old: " + locs[0].x + " " + locs[0].y);
		System.out.println("new: " + locs[1].x + " " + locs[1].y);
	}
	*/
	
	public PieceTypes decodeTypeFromEndString(String move){
		String type = move.substring(0, 1);

		if(!type.toUpperCase().equals(type)){
			return PieceTypes.PAWN;
		}else{
			if(type.equals("N")){
				return PieceTypes.KNIGHT;
			}else if(type.equals("B")){
				return PieceTypes.BISHOP;
			}else if(type.equals("R")){
				return PieceTypes.ROOK;
			}else if(type.equals("Q")){
				return PieceTypes.QUEEN;
			}else if(type.equals("K")){
				return PieceTypes.KING;
			}
		}
		
		return PieceTypes.NULL;
	}
	
	public Pair decodeEndingLocationFromString(String move){
		String strLocation = null;
		
		for(int i = 1; i < move.length(); i++){
			if(isInt(move.substring(i, i + 1))){
				strLocation = move.substring(i - 1, i + 1);
				break;
			}
		}
		
		Pair endLoc = new Pair(strLocation.charAt(0) - 'a', 7 - (Integer.parseInt(strLocation.substring(1)) - 1));
	
		return endLoc;
	}
	
	public boolean isInt(String str){
		try{
			Integer.parseInt(str);
		}catch(NumberFormatException e){
			return false;
		}
		
		return true;
	}
	
 	public Pair decodeOpeningLocationFromString(String move, Players p, Piece[][] ChessBoard, Pair WKingPos, Pair BKingPos, 
			boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle, 
			int numHalfMoves){
		
		Pair endLoc = decodeEndingLocationFromString(move);
		
		PieceTypes type = decodeTypeFromEndString(move);
		
		for(int a = 0; a < 8; a++){
			for(int b = 0; b < 8; b++){
				if(ChessBoard[b][a].getColor() == p && ChessBoard[b][a].getType() == type){
					MoveStates[][] possibleMoves = ChessBoard[b][a].getPossibleMoves(new Pair(a, b), ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, true);
					if(possibleMoves[endLoc.y][endLoc.x] != MoveStates.CLOSED){
						return new Pair(a, b);
					}
				}
			}
		}
		
		return null;
	}
	
	public Pair[] decodeLocationsFromMidString(String move){
		
		int moveInt = Integer.parseInt(move);
		
		int from = moveInt & 63;
	    int fromrank = from >> 3;
	    int fromfile = from & 7;
	    int to = (moveInt >> 6) & 63;
	    int torank = to >> 3;
	    int tofile = to & 7;
		
		Pair[] locations = new Pair[2];
		
		locations[0] = new Pair(fromfile, 7 - fromrank);
		locations[1] = new Pair(tofile, 7 - torank);

		return locations;
	}
	
	public Pair[] decodeLocationsFromOpeningString(String move){
		
		String strLocation;
		Pair[] locations = new Pair[2];
		
		strLocation = move.substring(0, 2);
		locations[0] = new Pair(strLocation.charAt(0) - 'a', 7 - (Integer.parseInt(strLocation.substring(1)) - 1));

		strLocation = move.substring(2, 4);
		locations[1] = new Pair(strLocation.charAt(0) - 'a', 7 - (Integer.parseInt(strLocation.substring(1)) - 1));
	
		return locations;
	}
	
	public String getEndMove(Piece[][] ChessBoard, Players p, boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle, 
			Pair enPassant, int numHalfMoves){
		
		try {
			final String USER_AGENT = "Mozilla/5.0";
	
	        String url = "http://tb7.chessok.com/probe/branch";
	       
	        URL obj = new URL(url);
	
	        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	
	        //add reuqest header
	
	        con.setRequestMethod("POST");
	        //con.setRequestMethod("GET");
	       
	        con.setRequestProperty("User-Agent", USER_AGENT);
	
	        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

	        String FEN = convertToFEDString(ChessBoard, p, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, 
		    		 enPassant, numHalfMoves);
	        String urlParameters = "fen=" + FEN;
	   
	        System.out.println(FEN);
	
	        // Send post request
	
	        con.setDoOutput(true);
	
	        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	
	        wr.writeBytes(urlParameters);
	
	        wr.flush();
	
	        wr.close();
	
	        int responseCode = con.getResponseCode();
	
	        //System.out.println("\nSending 'POST' request to URL : " + url);
	
	        //System.out.println("Post parameters : " + urlParameters);
	
	        //System.out.println("Response Code : " + responseCode);
	
	        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	
	        String inputLine;
	
	        StringBuffer response = new StringBuffer();

	        while ((inputLine = in.readLine()) != null) {
	
	                response.append(inputLine);
	
	        }
	
	        in.close();
	        
	        //print result
	        System.out.println(response.toString());
	    	
	        
	    	Scanner sc = new Scanner(response.toString());
	    	
	    	String text = sc.next();
	    	while(text.indexOf("1.") == -1){
	    		text = sc.next();
	    	}
	    	
	    	int startIndex;
	    	if(text.indexOf("...") != -1){
	    		startIndex = text.indexOf("...") + 3;
	    	}else{
	    		 startIndex = text.indexOf(".") + 1;
	    	}
	    	
	    	return text.substring(startIndex);
	
		}catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}

	public String getOppeningMove(Piece[][] ChessBoard, Players p, boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle, 
			Pair enPassant, int numHalfMoves){
		
		try {
			final String USER_AGENT = "Mozilla/5.0";
	
	        String url = "http://chessok.com/onlineserv/opening/connection.php";
	       
	        URL obj = new URL(url);
	
	        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	
	        //add reuqest header
	
	        con.setRequestMethod("POST");
	        //con.setRequestMethod("GET");
	       
	        con.setRequestProperty("User-Agent", USER_AGENT);
	
	        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

	        String FEN = convertToFEDString(ChessBoard, p, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, 
		    		 enPassant, numHalfMoves);
	        
	        String urlParameters = "fen=" + FEN;
	     
	        // Send post request
	
	        con.setDoOutput(true);
	
	        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	
	        wr.writeBytes(urlParameters);
	
	        wr.flush();
	
	        wr.close();
	
	        int responseCode = con.getResponseCode();
	
	        //System.out.println("\nSending 'POST' request to URL : " + url);
	
	        //System.out.println("Post parameters : " + urlParameters);
	
	        //System.out.println("Response Code : " + responseCode);
	
	        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	
	        String inputLine;
	
	        StringBuffer response = new StringBuffer();

	        while ((inputLine = in.readLine()) != null) {
	
	                response.append(inputLine);
	
	        }
	
	        in.close();
	        
	        String result = response.toString();
	        ArrayList<String> choices = new ArrayList<String>();
	        int counter = 0;
	        int MAX_CHOICES = 1;
	    	
	        while(result.indexOf("<WhiteWins>") != -1 && counter < MAX_CHOICES){
	        	
		    	Scanner sc = new Scanner(result);
		    	
		    	String text = sc.next();
		    	while(text.indexOf("<WhiteWins>") == -1){
		    		text = sc.next();
		    	}
		    	
		    	int startIndex = text.indexOf("<WhiteWins>") + 11;
		    	int endIndex = text.indexOf("</WhiteWins>");

		    	if(Integer.parseInt(text.substring(startIndex, endIndex)) > 500){
		    	
			    	sc = new Scanner(result);
			    	text = sc.next();
			    	while(text.indexOf("<Move>") == -1){
			    		text = sc.next();
			    	}
			    	
			    	startIndex = text.indexOf("<Move>") + 6;
			    	
			    	counter++;
			    	result = result.substring(result.indexOf("</WhiteWins>") + 12);
			    	choices.add(text.substring(startIndex, startIndex + 4));
		    	}else{
		    		break;
		    	}
	        }
	        
	        if(choices.size() > 0){
	        	return choices.get((int)(Math.random() * choices.size()));
	        }
	
		}catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}
	
	public String getMidMove(Piece[][] ChessBoard, Players p, boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle, 
			Pair enPassant, int numHalfMoves){
		
		try {
			final String USER_AGENT = "Mozilla/5.0";
	
			String FEN = convertToFEDString(ChessBoard, p, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, 
			    		 enPassant, numHalfMoves);
	        String url = "http://play2.shredderchess.com/online/playshredder/fetch.php?obid=eng10.8596537920306933&reqid=" + Math.random() + "&hook=null&action=engine&level=2&fen=" + FEN;
	        
	        URL obj = new URL(url);
	
	        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	
	        //add reuqest header
	
	        //con.setRequestMethod("POST");
	        con.setRequestMethod("GET");
	       
	        con.setRequestProperty("User-Agent", USER_AGENT);
	
	        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

	      
	        String urlParameters = "level=hard&fen=" + FEN;
	        
	        // Send get request
	
	        con.setDoOutput(true);
	
	        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	
	        wr.writeBytes(urlParameters);
	
	        wr.flush();
	
	        wr.close();
	
	        int responseCode = con.getResponseCode();
	
	        //System.out.println("\nSending 'POST' request to URL : " + url);
	
	        //System.out.println("Post parameters : " + urlParameters);
	
	        //System.out.println("Response Code : " + responseCode);
	
	        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	
	        String inputLine;
	
	        StringBuffer response = new StringBuffer();

	        while ((inputLine = in.readLine()) != null) {
	
	                response.append(inputLine);
	
	        }
	
	        in.close();
	        
	    	Scanner sc = new Scanner(response.toString());
	    	
	    	String text = sc.next();
	    	while(text.indexOf("value|") == -1){
	    		text = sc.next();
	    	}
	    	
	    	int startIndex = text.indexOf("value|") + 6;
	    	
	    	return text.substring(startIndex);
	
		}catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}
	
	public boolean canMoveToSquare(Piece p, Pair square, Pair piecePos, Piece[][] ChessBoard, Pair WKingPos, Pair BKingPos, 
			boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle, 
			int numHalfMoves, int numFullMoves){
		MoveStates[][] possibleMoves = p.getPossibleMoves(piecePos, ChessBoard, WKingPos, BKingPos, numHalfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle, true);
	
		if(possibleMoves[square.y][square.x] != MoveStates.CLOSED){
			return true;
		}
		
		return false;
	}

	public String convertToFEDString(Piece[][] ChessBoard, Players p, boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle, 
			Pair enPassant, int numHalfMoves){
		//https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
		
		String FED = "";
		
		for(int a = 0; a < 8; a++){
			int counter = 0;
			for(int b = 0; b < 8; b++){
				if(ChessBoard[a][b].getColor() == Players.BLACK){
					
					if(counter != 0){
						FED += counter;
						counter = 0;
					}
					if(ChessBoard[a][b].getType() == PieceTypes.KNIGHT){
						FED += "n";
					}else{
						FED += ChessBoard[a][b].getType().toString().substring(0, 1).toLowerCase();
					}
				}else if(ChessBoard[a][b].getColor() == Players.WHITE){
					
					if(counter != 0){
						FED += counter;
						counter = 0;
					}
					
					if(ChessBoard[a][b].getType() == PieceTypes.KNIGHT){
						FED += "N";
					}else{
						FED += ChessBoard[a][b].getType().toString().substring(0, 1);
					}
				}else
					counter++;
			}
			
			if(counter != 0)
				FED += counter;
			
			if(a != 7)
				FED += "/";
		}
		
		FED += " " + p.toString().substring(0, 1).toLowerCase() + " ";
		
		boolean canCastle = false;
		if(whiteKingCastle){
			FED += "K";
			canCastle = true;
		}
		if(whiteQueenCastle){
			FED += "Q";
			canCastle = true;
		}
		if(blackKingCastle){
			FED += "k";
			canCastle = true;
		}
		if(blackQueenCastle){
			FED += "q";
			canCastle = true;
		}
		if(!canCastle)
			FED += "-";
		
		if(enPassant == null){
			FED += " -";
		}else{
			//do stuff
		}
		
		/*
		FED += numHalfMoves + " ";
		FED += numHalfMoves/2;
		*/
		return FED;
	}
}
