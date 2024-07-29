package model;

import java.util.Random;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

/**
 * EasyAI that randomly selects target
 * 
 * Contributors: Dawson Szarek
 */
public class EasyAI implements AI{
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

	EasyAI() {
	}
	
    public void setUserBoard(Board board) {
        this.userBoard = board;
    }

	public boolean attemptStrike(GridPoint[][] board) {
		total += 1;
		// creating a random variable and the bound
		Random rand = new Random();
		// the row and column values will be random
		int row = rand.nextInt(6);
		int col = rand.nextInt(10);
		// if statement to make sure the coordinate hasn't already been selected
		if (board[row][col].getID() == 'H' || board[row][col].getID() == 'M') {
			// while loop that repeats until a new coordinate is selected
			while (board[row][col].getID() == 'H' || board[row][col].getID() == 'M') {
				// the new row and column values will also be random
				row = rand.nextInt(6);
				col = rand.nextInt(10);
			}
		}
		// if there is a ship, 'H' for hit will be placed to replace the 'C'
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
						board[ship.getFrontRow()][ship.getFrontCol() + i].setFill(transparentBlack);; // sets ship to black
																								// when sunk
					}
				}
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
	
	public float getShotPercentage() {
		return (hits/total) * 100;
	}

	public static float getFinalShotPercentage() {
		return shotPercentage;
	}
	

	public static int getAIMaxHitsInARow() {
		return maxHitsInARow;
	}
	
}
