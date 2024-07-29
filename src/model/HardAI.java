package model;

import java.util.Random;
import java.util.Iterator;
import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

/**
 * EasyAI that randomly selects target
 * 
 * Contributors: Dawson Szarek
 */
public class HardAI implements AI {
	public double opacity = 0.10;
	public double strongerOpacity = 0.5;
	public Color transparentGray = new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), opacity);
	public Color transparentRed = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), strongerOpacity);
	public Color transparentBlack = Color.BLACK;
	float hits = 0;
	float total = 0;
	static float shotPercentage = 0;
	int hitsInARow = 0;
	static int maxHitsInARow = 0;
	private Board userBoard;

	// these will keep track of potential moves
	ArrayList<Tuple<Integer, Integer>> givenMoves = new ArrayList<>();
    ArrayList<Tuple<Integer, Integer>> potentialMoves = new ArrayList<>();
    ArrayList<Tuple<Integer, Integer>> prevHits = new ArrayList<>();
    //Set<Tuple<Integer, Integer>> pastMoves = new HashSet<>();
    Tuple<Integer, Integer> lastMove;
    Tuple<Integer, Integer> curMove;

	HardAI() {

	}
	
    public void setUserBoard(Board board) {
        this.userBoard = board;
    }

	public void printMovesAndHits() {
		System.out.println("------------------------");
	    System.out.println("Given Moves:");
	    for (Tuple<Integer, Integer> move : givenMoves) {
	        System.out.println("(" + move.getX() + ", " + move.getY() + ")");
	    }

	    System.out.println("\nPotential Moves:");
	    for (Tuple<Integer, Integer> move : potentialMoves) {
	        System.out.println("(" + move.getX() + ", " + move.getY() + ")");
	    }

	    System.out.println("\nPrevious Hits:");
	    for (Tuple<Integer, Integer> hit : prevHits) {
	        System.out.println("(" + hit.getX() + ", " + hit.getY() + ")");
	    }

	    if (lastMove != null) {
	        System.out.println("\nLast Move: (" + lastMove.getX() + ", " + lastMove.getY() + ")");
	    } else {
	        System.out.println("\nLast Move: None");
	    }

	    if (curMove != null) {
	        System.out.println("Current Move: (" + curMove.getX() + ", " + curMove.getY() + ")");
	    } else {
	        System.out.println("Current Move: None");
	    }
	    System.out.println("------------------------");
	}

    

	public void findAdjacent(Tuple<Integer, Integer> move, GridPoint[][] board) {
	    int col = move.getX();
	    int row = move.getY();
	    
	    // Add a move only if the adjacent tile is not a hit, a miss, and the ship at that tile (if any) is not sunk
	    if (row > 0 && isViableMove(board, col, row - 1)) {
	    	GridPoint checkAbove = board[row-1][col];
	
	        if (checkAbove.getID() == 'C') {
	        	if ((board[row][col].getShip().getID() == board[row-1][col].getShip().getID())) {
		        	potentialMoves.add(new Tuple<Integer, Integer>(col, row - 1));
	        	}
	        } else {
	        	potentialMoves.add(new Tuple<Integer, Integer>(col, row - 1));
	        }
	    }
	    if (row < board.length - 1 && isViableMove(board, col, row + 1)) {
	    	GridPoint checkbelow = board[row+1][col];
	    	
	    	if (checkbelow.getID() == 'C') {
	        	if ((board[row][col].getShip().getID() == board[row+1][col].getShip().getID())) {
		        	potentialMoves.add(new Tuple<Integer, Integer>(col, row + 1));
	        	}
	        } else {
	        	potentialMoves.add(new Tuple<Integer, Integer>(col, row + 1));
	        }
	    }
	    if (col > 0 && isViableMove(board, col - 1, row)) {
	    	GridPoint checkLeft = board[row][col-1];

	    	if (checkLeft.getID() == 'C') {
	    		if ((board[row][col].getShip().getID() == board[row][col-1].getShip().getID())) {
	    	        potentialMoves.add(new Tuple<Integer, Integer>(col - 1, row));
	    		}
	    	} else {
    	        potentialMoves.add(new Tuple<Integer, Integer>(col - 1, row));
	    	}
	    }
	    if (col < board[0].length - 1 && isViableMove(board, col + 1, row)) {
	    	GridPoint checkRight = board[row][col+1];
	    	
	    	if (checkRight.getID() == 'C') {
	    		if ((board[row][col].getShip().getID() == board[row][col+1].getShip().getID())) {
	    	        potentialMoves.add(new Tuple<Integer, Integer>(col + 1, row));
	    		}
	    	} else {
		        potentialMoves.add(new Tuple<Integer, Integer>(col + 1, row));
	    	}

	    }
	}

	private boolean isViableMove(GridPoint[][] board, int col, int row) {
	    GridPoint point = board[row][col];
	    if (point.getID() == 'H' || point.getID() == 'M') {
	        return false;
	    }
	    if (point.getShip() != null && point.getShip().isSunk()) {
	        return false;
	    }
	    return true;
	}


	public Tuple<Integer, Integer> findMove() {
	    Tuple<Integer, Integer> retval = null;
	    Iterator<Tuple<Integer, Integer>> iterator = givenMoves.iterator();

	    while (iterator.hasNext()) {
	        Tuple<Integer, Integer> move = iterator.next();

	        int row = move.getX();
	        int col = move.getY();

	        int oldRow = lastMove.getX();
	        int oldCol = lastMove.getY();

	        if ((row == oldRow + 1 && col == oldCol) || (row == oldRow - 1 && col == oldCol)
	                || (row == oldRow && col == oldCol + 1) || (row == oldRow && col == oldCol - 1)) {
	            retval = move;
	            iterator.remove(); // Safe removal while iterating
	            break;
	        }
	    }
	    return retval;
	}

	
	public void loadingQueue(Ship ship, GridPoint[][] board, int curCol, int curRow) {
	    int lastCol = lastMove.getX();
	    int lastRow = lastMove.getY();
	    if (Character.toLowerCase(ship.getDirection()) == 'v') {
	        for (int i = 0; i < ship.getLength(); i++) {
	            int col = ship.getFrontCol(); 
	            int row = ship.getFrontRow() + i;
	            if (!(board[row][col].getID() == 'H' || board[row][col].getID() == 'M') && !(curCol == col && curRow == row)) {
	                givenMoves.add(0, new Tuple<Integer, Integer>(col, row));
	            }
	        }
	    } else { // Assuming horizontal direction
	        for (int i = 0; i < ship.getLength(); i++) {
	            int col = ship.getFrontCol() + i; 
	            int row = ship.getFrontRow();
	            if (!(board[row][col].getID() == 'H' || board[row][col].getID() == 'M') && !(curCol == col && curRow == row)) {
	                givenMoves.add(0, new Tuple<Integer, Integer>(col, row));
	            }
	        }
	    }
	}

	public void nextMove(Tuple<Integer, Integer> move, GridPoint[][] board) {
	    String direction;
	    int col = move.getX();
	    int row = move.getY();

	    int oldCol = lastMove.getX();
	    int oldRow = lastMove.getY();

	    // Determine the direction of the last move
	    if (row == oldRow + 1 && col == oldCol) {
	        direction = "D"; // Down
	    } else if (row == oldRow - 1 && col == oldCol) {
	        direction = "U"; // Up
	    } else if (row == oldRow && col == oldCol + 1) {
	        direction = "R"; // Right
	    } else if (row == oldRow && col == oldCol - 1) {
	        direction = "L"; // Left
	    } else {
	        direction = "None"; // No clear direction or first move
	    }

	    // Add next move in the same direction to potentialMoves if it's valid
	    if (direction.equals("U") && row > 0 && !(board[row-1][col].getID() == 'H' || board[row-1][col].getID() == 'M')) {
	        potentialMoves.add(new Tuple<Integer, Integer>(col, row - 1));
	    } else if (direction.equals("D") && row < board.length - 1 && !(board[row+1][col].getID() == 'H' || board[row+1][col].getID() == 'M')) {
	        potentialMoves.add(new Tuple<Integer, Integer>(col, row + 1));
	    } else if (direction.equals("R") && col < board[0].length - 1 && !(board[row][col+1].getID() == 'H' || board[row][col+1].getID() == 'M')) {
	        potentialMoves.add(new Tuple<Integer, Integer>(col + 1, row));
	    } else if (direction.equals("L") && col > 0 && !(board[row][col-1].getID() == 'H' || board[row][col-1].getID() == 'M')) {
	        potentialMoves.add(new Tuple<Integer, Integer>(col - 1, row));
	    }
	}
	public void specialStrike(GridPoint[][] board, int col, int row) {
		
	}

	public boolean attemptStrike(GridPoint[][] board) {
		//System.out.println("Before");
		//printPastMoves();
	    total += 1;
	    Random rand = new Random();
	    int col;
	    int row;
	    if (!givenMoves.isEmpty()) {
	        curMove = givenMoves.remove(0);
	        
	            col = curMove.getX();
	            row = curMove.getY();
	       
	     } else if (!potentialMoves.isEmpty()) {
	        curMove = potentialMoves.remove(0); // Dequeues the first element
	        col = curMove.getX();
	        row = curMove.getY();
	        if (board[row][col].getID() == 'C') {
	            potentialMoves.clear();
	            if (board[row][col].getShip().getHealth() < board[row][col].getShip().getLength()) {
	                Ship ship = board[row][col].getShip();
	                loadingQueue(ship, board, col, row);
	                lastMove = new Tuple<Integer, Integer>(col, row);
	                prevHits.add(new Tuple<Integer, Integer>(col, row));
	            } else {
	                nextMove(curMove, board);
	            }
	        } else {
	            if (prevHits.size() > 0) {
	                Tuple<Integer, Integer> lastHit = prevHits.get(prevHits.size() - 1);
	                int lastHitCol = lastHit.getX();
	                int lastHitRow = lastHit.getY();
	                findAdjacent(lastHit, board);  // Adjust this to add adjacent moves of the last hit
	                prevHits.clear();
	            }
	        }
	    }else { // random move
	        row = rand.nextInt(board.length);
	        col = rand.nextInt(board[0].length);
	        curMove = new Tuple<Integer, Integer>(col, row);
	        while (!isViableMove(board, col, row)) {
	            row = rand.nextInt(board.length);
	            col = rand.nextInt(board[0].length);
	            curMove = new Tuple<Integer, Integer>(col, row);
	        }
	        if (board[row][col].getID() == 'C') {
	        	if (board[row][col].getShip().getHealth() < board[row][col].getShip().getLength()) {
	        		potentialMoves.clear();
	                Ship ship = board[row][col].getShip();
	                loadingQueue(ship, board, col, row);
	                lastMove = new Tuple<Integer, Integer>(col, row);
	                prevHits.add(new Tuple<Integer, Integer>(col, row));
	        	}
	            findAdjacent(curMove, board);
	            prevHits.add(curMove);
	        }
	    }
    
	    //pastMoves.add(new Tuple<Integer, Integer>(col, row));
	   
	    //printMovesAndHits();
	    lastMove = new Tuple<Integer, Integer>(col, row);
	    return updateBoard(row, col, board);
	}


	public boolean updateBoard(int row, int col, GridPoint[][] board) {
		
		if (board[row][col].getID() == 'C' || board[row][col].getID() == 'H') {
			Ship ship = board[row][col].getShip();
			ship.hit();
			board[row][col].setID('H');
			board[row][col].setFill(transparentRed); // adjusts Color of GridPoint object
			
			Canvas currentAnimatingCanvas = userBoard.getAnimationCanvases()[row][col];
			ExplosionAnimation explosion = new ExplosionAnimation(currentAnimatingCanvas, "Explosion");
			explosion.startExplosionAnimation();
			
			if (ship.isSunk()) {
				if (Character.toLowerCase(ship.getDirection()) == 'v') {
					for (int i = 0; i < ship.getLength(); i++) {
						board[ship.getFrontRow() + i][ship.getFrontCol()].setFill(transparentBlack); // sets ship to black
																								// when sunk
					}
				} else {
					for (int i = 0; i < ship.getLength(); i++) {
						board[ship.getFrontRow()][ship.getFrontCol() + i].setFill(transparentBlack); // sets ship to black
																								// when sunk
					}
				}
				removeSunkShipMoves(ship, board);
				
			}
			hits += 1;
			shotPercentage = getShotPercentage();
			hitsInARow += 1;
			return true;
			// if there is no ship, 'M' for miss will be placed to replace the '█'
		} else {
			board[row][col].setID('M');
			board[row][col].setFill(transparentGray); // adjusts Color of GridPoint object
			
			Canvas currentAnimatingCanvas = userBoard.getAnimationCanvases()[row][col];
			ExplosionAnimation splash = new ExplosionAnimation(currentAnimatingCanvas, "Splash");
			splash.startExplosionAnimation();
			
			shotPercentage = getShotPercentage();
			if (hitsInARow > maxHitsInARow) {
				maxHitsInARow = hitsInARow;
			}
			hitsInARow = 0;
			return false;
		}
	}
	private void removeSunkShipMoves(Ship ship, GridPoint[][] board) {
	    int frontRow = ship.getFrontRow();
	    int frontCol = ship.getFrontCol();
	    int length = ship.getLength();
	    char direction = Character.toLowerCase(ship.getDirection());

	    for (int i = 0; i < length; i++) {
	        int row = direction == 'v' ? frontRow + i : frontRow;
	        int col = direction == 'v' ? frontCol : frontCol + i;

	        Tuple<Integer, Integer> shipCoordinate = new Tuple<>(col, row);
	        givenMoves.remove(shipCoordinate);
	        potentialMoves.remove(shipCoordinate);
	    }
	}

	public float getShotPercentage() {
		return (hits / total) * 100;
	}

	public static float getFinalShotPercentage() {
		return shotPercentage;
	}

	public static int getAIMaxHitsInARow() {
		return maxHitsInARow;
	}

}
