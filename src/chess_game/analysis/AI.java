package chess_game.analysis;

import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import chess_game.ChessUtil;
import chess_game.MoveStates;
import chess_game.Pair;
import chess_game.Players;
import chess_game.chess_pieces.Empty;
import chess_game.chess_pieces.MoveObject;
import chess_game.chess_pieces.Piece;

public class AI {

	final int MAX_DEPTH;
	boolean inOpening;
	boolean inEndGame;
	Line currentLine;
	Players color;
	volatile Line bestLine;
	int counter = 0;
	Evaluator eval = new Evaluator();
	Hashtable<Long, HashTableEntry> whiteHashTable = new Hashtable<Long, HashTableEntry>();
	Hashtable<Long, HashTableEntry> blackHashTable = new Hashtable<Long, HashTableEntry>();
	Hashtable<Long, HashTableEntry> currentHashTable;
	int numHalfMoves;
	volatile boolean timesUp = false;
	Timer timer = new Timer();
	volatile MoveObject[][] killerMoves;
	
	public AI(int depth)
	{
		MAX_DEPTH = depth;
		Position.initZobristTable();
		killerMoves = new MoveObject[depth + 1][3];
	}
	
	public AIReturnObject makeMove(Pair WKingPos, Pair BKingPos) // have to updateLine() with opponent's move before running this method
	{
		long startTime = System.currentTimeMillis();
		
		analyzeUntilMaxDepth();
		currentLine = bestLine;
	
		System.out.println("Counter: " + counter);
		System.out.println("Move Time: " + (double)(System.currentTimeMillis() - startTime)/1000 + "seconds");
		System.out.println("Move Type: " + currentLine.getPosition().mov.getMoveType());
		System.out.println("eval: " + currentLine.evaluation);
		counter = 0;

		return makeAndysLifeEasy();
	}
	
	public void updateLine(Players color, Piece[][] b, int halfMoves, boolean whiteKingCastle, boolean whiteQueenCastle, boolean blackKingCastle, boolean blackQueenCastle) // currently calls analyzeUntilMaxDepth();
	// pre: recieves the current board after opponent made a move
	// post: changes currentLine to have the recieved position.
	    // Chooses a connecting line so priar analysis is preserved
	{
		this.color = color;
		currentLine = new Line(color);
		Position p = new Position (null, b, 1, halfMoves, whiteKingCastle, whiteQueenCastle, blackKingCastle, blackQueenCastle);	
		p.setHash(Position.hashPosition(p));
		currentLine = new Line(p, color);
		numHalfMoves = halfMoves;
		
		if(color == Players.WHITE){
			currentHashTable = whiteHashTable;
		}else{
			currentHashTable = blackHashTable;
		}
	}
	
	public void analyzeUntilMaxDepth()
	{
		evaluateLine(currentLine, Integer.MIN_VALUE, Integer.MAX_VALUE, MAX_DEPTH);
	}
	
	protected double evaluateLine(Line l, double alpha, double beta, int depth)
	// returns the evaluation of a line
	// sets bestLine and evaluation of the Line;
	{		
		Line[] possibleLines = l.expandAllLines(killerMoves[depth]);
		if (depth > 0) // uses later lines to evaluate
		{
			if (l.whosTurn == 1) // computer to move (opponent just moved). higher eval is better
			{
				
				long positionHash = l.getPosition().getHash();
				HashTableEntry entry = currentHashTable.get(positionHash);
				
				if(entry != null){
					if(entry.depth >= depth){
						if(entry.halfMoves + 5 >= numHalfMoves){
							
							//System.out.println("Transposition Table: found - " + positionHash);
							
							if(depth == 5){
								Position pos = l.getPosition();
								pos.mov = entry.bestMove;
								bestLine = l;
					            return entry.evaluation;
							}
							
							if(entry.Node_Type == HashTableEntry.EXACT){
					            return entry.evaluation;
					        }else if(entry.Node_Type == HashTableEntry.LOWER){
					        	 alpha = Math.max(alpha, entry.evaluation);
					        }else if(entry.Node_Type == HashTableEntry.UPPER){
					        	beta = Math.min(beta, entry.evaluation);
					        }
	
							if (alpha >= beta)
						        return alpha;
							
						}else{
							currentHashTable.remove(positionHash);
						}
					}
				}
				
				if(depth == MAX_DEPTH){
					iterativeDeepening();
				}
				
				double highest = Integer.MIN_VALUE;
				int highestIndex = 0;
				for(int i = 0; i < possibleLines.length; i++)
				{
					
					double temp;
					if(possibleLines[i].getPosition().kingsAlive == false){
						temp =  evaluateLine(possibleLines[i], alpha, beta, 0);
					}else{
						temp =  evaluateLine(possibleLines[i], alpha, beta, depth-1);
					}
	
					if (highest < temp)
					{
						if(i != 0)
							possibleLines[highestIndex] = null;
						highest = temp;
						highestIndex = i;
					}else{
						possibleLines[i] = null;
					}
					
					alpha = Math.max(alpha, highest);
					
					if (beta <= alpha) {
						
						if(possibleLines[highestIndex].getPosition().mov.getMoveType() != MoveStates.TAKE){
							for (int a = killerMoves[depth].length - 2; a >= 0; a--) 
							    killerMoves[depth][a + 1] = killerMoves[depth][a];
							killerMoves[depth][0] = possibleLines[highestIndex].getPosition().mov;
						}
						
						break;
						//System.out.println("Evaluated High Depth(pruned): " + depth);
		            }
				}
				
				l.evaluation = highest;
				if(possibleLines.length != 0){
					
					bestLine = possibleLines[highestIndex];
					
					int node_type = HashTableEntry.EXACT;
					if(highest <= alpha){
						node_type = HashTableEntry.LOWER;
					}else if(highest > beta){
						node_type = HashTableEntry.UPPER;
					}
			
					currentHashTable.put(positionHash, new HashTableEntry(highest, depth, numHalfMoves, node_type, possibleLines[highestIndex].getPosition().mov));
				
				}
				
				//System.out.println("Evaluated High Depth: " + depth);
				return highest;
			}
			else // opponent to counter move. Lower eval is better
			{
				
				long positionHash = l.getPosition().getHash();
				HashTableEntry entry = currentHashTable.get(positionHash);
				if(entry != null){
					if(entry.depth >= depth){
						if(entry.halfMoves + 5 >= numHalfMoves){
							
							//System.out.println("Transposition Table: found - " + positionHash);
							
							if(entry.Node_Type == HashTableEntry.EXACT){
					            return entry.evaluation;
					        }else if(entry.Node_Type == HashTableEntry.LOWER){
					        	 alpha = Math.max(alpha, entry.evaluation);
					        }else if(entry.Node_Type == HashTableEntry.UPPER){
					        	beta = Math.min(beta, entry.evaluation);
					        }
		
							if (alpha>=beta)
						        return alpha;
						        
						}else{
							currentHashTable.remove(positionHash);
						}
					}
				}
				
				double lowest = Integer.MAX_VALUE;
				int lowestIndex = 0;
				for(int i = 0; i < possibleLines.length; i++)
				{
					double temp;
					if(possibleLines[i].getPosition().kingsAlive == false){
						temp =  evaluateLine(possibleLines[i], alpha, beta, 0);
					}else{
						temp =  evaluateLine(possibleLines[i], alpha, beta, depth-1);
					}
					
					if (lowest > temp)
					{
						if(i != 0)
							possibleLines[lowestIndex] = null;
						lowest = temp;
						lowestIndex = i;
					}else{
						possibleLines[i] = null;
					}
					
					beta = Math.min(beta, lowest);
					
					if (beta <= alpha) {
						
						if(possibleLines[lowestIndex].getPosition().mov.getMoveType() != MoveStates.TAKE){
							for (int a = killerMoves[depth].length - 2; a >= 0; a--) 
							    killerMoves[depth][a + 1] = killerMoves[depth][a];
							killerMoves[depth][0] = possibleLines[lowestIndex].getPosition().mov;
						}
						
						break;
						//System.out.println("Evaluated Low Depth(pruned): " + depth);
		            }
				}
				
				l.evaluation = lowest;
				if(possibleLines.length != 0){	
					
					int node_type = HashTableEntry.EXACT;
					if(lowest <= alpha){
						node_type = HashTableEntry.LOWER;
					}else if(lowest > beta){
						node_type = HashTableEntry.UPPER;
					}
					System.out.println((possibleLines[lowestIndex]) + " size: " + possibleLines.length + " index: " + lowestIndex);
					currentHashTable.put(positionHash, new HashTableEntry(lowest, depth, numHalfMoves, node_type, possibleLines[lowestIndex].getPosition().mov));
				
				}
				
				//System.out.println("Transposition Table: added - " + positionHash);
				
				//System.out.println("Evaluated Low Depth: " + depth);
				return lowest;
			}
		}
		else // updates eval and uses it to evaluate the position
		{
		
			//System.out.println("Free Memory: " + (Runtime.getRuntime().freeMemory() * 100/Runtime.getRuntime().totalMemory()) + "%");
			//System.out.println("Total Memory: " + Runtime.getRuntime().totalMemory());
			counter++;
			
			if(counter % 10000 == 0)
				System.gc();
			
			if (l.whosTurn == 1) 
			{
				if (color == Players.WHITE)
					eval.setValues(l.getPosition(), Players.BLACK);
				else
					eval.setValues(l.getPosition(), Players.WHITE);
			}
			else
			{
				if (color == Players.WHITE)
					eval.setValues(l.getPosition(), Players.WHITE);
				else
					eval.setValues(l.getPosition(), Players.BLACK);
			}
			
			System.out.println("lines evaluated: " + counter);
			double value;
			if (color == Players.WHITE){
				value = eval.evalPosition();
			}else{
				value = -eval.evalPosition();
			}
			
			return value;
		}
	}
	
	public double iterativeDeepening(){
		timesUp = false;
		double ret = Integer.MIN_VALUE;
		for(int current_depth = 1; current_depth < MAX_DEPTH; current_depth++)
		{
			ret = evaluateLine(currentLine, Integer.MIN_VALUE, Integer.MAX_VALUE, current_depth);
			if(timesUp){ 
				break;
			}
		}
		
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				timesUp = true;
			}
			
		}, 5000);//5 seconds
		
		return ret;
	}

	public AIReturnObject makeAndysLifeEasy()
	{
		return new AIReturnObject(bestLine.getPosition(), bestLine.getPosition().mov, color);
	}
}

class HashTableEntry{
	
	double evaluation;
	int depth;
	int halfMoves;
	MoveObject bestMove;
	
	int Node_Type;
	final static int EXACT = 0;
	final static int LOWER = 1;
	final static int UPPER = 2;
	
	HashTableEntry(double evaluation, int depth, int halfMoves, int Node_Type, MoveObject move){
		this.evaluation = evaluation;
		this.depth = depth;
		this.halfMoves = halfMoves;
		this.Node_Type = Node_Type;
		this.bestMove = move;
	}
}
