package chess_game;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

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
		new SOAPClient().test();
	}
	*/
	
	public PieceTypes getPieceTypeFromString(String move){
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
	
	public Pair getPieceEndingLocationFromString(String move){
		String strLocation;
		
		if(move.toLowerCase().equals(move)){
			strLocation = move.substring(0, 2);
		}else{
			if(move.charAt(1) == 'x')
				strLocation = move.substring(2, 4);
			else
				strLocation = move.substring(1, 3);
		}
		
		Pair endLoc = new Pair(strLocation.charAt(0) - 'a', 7 - (Integer.parseInt(strLocation.substring(1)) - 1));
	
		return endLoc;
	}
	
	public Pair getPieceStartingLocationFromString(String move, Players p, Piece[][] ChessBoard, Pair WKingPos, Pair BKingPos, 
			boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle, 
			int numHalfMoves){
		
		Pair endLoc = getPieceEndingLocationFromString(move);
		
		PieceTypes type = getPieceTypeFromString(move);
		
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
	
	public String getMove(Piece[][] ChessBoard, Players p, boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle, 
			Pair enPassant, int numHalfMoves){
		
		try {
			final String USER_AGENT = "Mozilla/5.0";
	
	        String url = "http://tb7.chessok.com/probe/branch";
	
	        URL obj = new URL(url);
	
	        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	
	        //add reuqest header
	
	        con.setRequestMethod("POST");
	
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
	
	        BufferedReader in = new BufferedReader(
	
	                new InputStreamReader(con.getInputStream()));
	
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

	public void test(){
		try {
			/*
			String urlParameters  = "fen=8%2F3QK3%2F6q1%2F8%2F2R5%2F6k1%2F8%2F8%20w%20-%20-%200%201";
			byte[] postData       = urlParameters.getBytes(StandardCharsets.UTF_8);
			int    postDataLength = postData.length;
			String request        = "http://chessok.com/onlineserv/endbase/connection.php?timestamp=1495379048835";
			URL    url            = new URL(request);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();           
			conn.setDoOutput( true );
			conn.setInstanceFollowRedirects( false );
			conn.setRequestMethod( "POST" );
			conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
			conn.setRequestProperty( "charset", "utf-8");
			conn.setRequestProperty( "Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches( false );
			try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
			   wr.write(postData);
			}
			System.out.println(postData);
			*/
			
			final String USER_AGENT = "Mozilla/5.0";

	           String url = "http://chessok.com/onlineserv/endbase/connection.php";

               URL obj = new URL(url);

               HttpURLConnection con = (HttpURLConnection) obj.openConnection();

               //add reuqest header

               con.setRequestMethod("POST");

               con.setRequestProperty("User-Agent", USER_AGENT);

               con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");



               String urlParameters = "fen=8%2F3QK3%2F6q1%2F8%2F2R5%2F6k1%2F8%2F8%20w%20-%20-%200%201";



               // Send post request

               con.setDoOutput(true);

               DataOutputStream wr = new DataOutputStream(con.getOutputStream());

               wr.writeBytes(urlParameters);

               wr.flush();

               wr.close();



               int responseCode = con.getResponseCode();

               System.out.println("\nSending 'POST' request to URL : " + url);

               System.out.println("Post parameters : " + urlParameters);

               System.out.println("Response Code : " + responseCode);



               BufferedReader in = new BufferedReader(

                       new InputStreamReader(con.getInputStream()));

               String inputLine;

               StringBuffer response = new StringBuffer();



               while ((inputLine = in.readLine()) != null) {

                       response.append(inputLine);

               }

               in.close();



               //print result

               System.out.println(response.toString());




            
	} catch (Exception e1) {
		e1.printStackTrace();
	}
		
		/*
		try {
			//creating the message
			message = messageFactory.createMessage();
			SOAPBody body = message.getSOAPBody();
			QName bodyName = new QName("http://www.lokasoft.nl/tbweb/tbapi", "TB2ComObj.ProbePositions", "wsdlns");
			SOAPBodyElement test = body.addBodyElement(bodyName);
			
			QName movesName = new QName("fen");
			SOAPElement moves = test.addChildElement(movesName);
			moves.addTextNode(convertToFEDString(ChessBoard, Players.WHITE, true, true, true, true, null, 0, 1));
			message.saveChanges();
			
			//sending/receiving the message
			connection = connectionFactory.createConnection();
			SOAPMessage response = connection.call(message, new URL("http://www.lokasoft.nl/tbweb/tbapi.wsdl"));
			connection.close();
			
			//decoding the content of the message
			SOAPBody retBody = response.getSOAPBody();
			Iterator iter = retBody.getChildElements(bodyName);
			SOAPBodyElement retElement = (SOAPBodyElement) iter.next();
			System.out.println(retElement.getValue());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
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
			FED += " - ";
		}else{
			//do stuff
		}
		
		FED += numHalfMoves + " ";
		FED += numHalfMoves/2;
		
		return FED;
	}
}
