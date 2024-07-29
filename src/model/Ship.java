package model;

import java.io.Serializable;

/**
 * A ship object for battleship game
 * 
 * Contributors: Dawson Szarek
 */
public class Ship implements Serializable{

	private static final long serialVersionUID = 5764481549780959957L;
	private int length;
	private char direction; // Direction (down or right)
	private int frontRow; // head of ships row
	private int frontCol; // head of ships column
	public int health;
	public int id;

	Ship(int length, int frontRow, int frontCol, int id) {
		this.length = length;
		this.health = length;
		this.frontRow = frontRow;
		this.frontCol = frontCol;
		this.id = id;
		this.direction = 'V';
	}

	public void hit() {
		health--;
	}

	public boolean isSunk() {
		if (health == 0) {
			return true;
		} else {
			return false;
		}
	}

	public int getLength() {
		return this.length;
	}

	public char getDirection() {
		return this.direction;
	}

	public int getFrontRow() {
		return this.frontRow;
	}

	public int getFrontCol() {
		return this.frontCol;
	}

	public void updateDirection(char direction) {
		this.direction = direction;
	}

	public void updateFront(int row, int col) {
		this.frontRow = row;
		this.frontCol = col;
	}
	public int getHealth() {
		return this.health;
	}
	public int getID() {
		return id;
	}
}
