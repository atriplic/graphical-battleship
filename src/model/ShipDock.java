package model;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class ShipDock extends GridPane {
	public String frontV = "src/resources/shipImages/front.png";
	public String bodyV = "src/resources/shipImages/body.png";
	public String backV = "src/resources/shipImages/back.png";
	public String frontH = "src/resources/shipImages/frontHorizontal.png";
	public String bodyH = "src/resources/shipImages/bodyHorizontal.png";
	public String backH = "src/resources/shipImages/backHorizontal.png";

	private Board board;
	private GridPoint[][] shipDock;
	private int shipCount;
	
	public ShipDock(Board board) {
		this.board = board;
		this.shipDock = createShipDoc();
		this.shipCount = 5;
		placeShips();
	}

	
	public GridPoint[][] createShipDoc(){
		// initializing the length and width of the board
		GridPoint[][] shipDock = new GridPoint[6][6];
		// multiple for loops to create the 2d board
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				GridPoint gridPoint = new GridPoint(40, 40, i, j); // creates new GridPoint object
				gridPoint.setStroke(Color.WHITE);
				gridPoint.setFill(Color.WHITE);
				shipDock[i][j] = gridPoint; // sets point on board to new GridPoint
			}
		}
		return shipDock;
	}
	
	public void placeShips() {
		for (Ship ship : this.board.getShips()) {
			addUserShip(ship);
		}
	}
	
	/**
	 * Updates GridPoint objects' ID to 'C' to indicate ship occupies that point on
	 * the board. Also updates the GridPoint objects' ship variable.
	 * 
	 * @param ship - Ship class object
	 */
	public void addUserShip(Ship ship) {
		// placing the ship as 'C' for cruiser
		char token = 'C';
		int row = ship.getFrontRow();
		int col = ship.getFrontCol();
		char direction = ship.getDirection();
		// for loop to place the ship on the specific spot in the board
		if (Character.toLowerCase(direction) == 'v') {
			for (int i = 0; i < ship.getLength(); i++) {
				if (i == 0) {
					shipDock[row + i][col].setURL(frontV);
				} else if (i+1 == ship.getLength()) {
					shipDock[row + i][col].setURL(backV);
				} else {
					shipDock[row + i][col].setURL(bodyV);
				}
				if (shipDock[row + i][col].getID() != ' ') {
					shipDock[row + i][col].setID(token);
					shipDock[row + i][col].addShip(ship);
				}
			}
		} else if (Character.toLowerCase(direction) == 'h') {
			for (int i = 0; i < ship.getLength(); i++) {
				if (i == 0) {
					shipDock[row][col+ i].setURL(frontH);
				} else if (i+1 == ship.getLength()) {
					shipDock[row][col + i].setURL(backH);
				} else {
					shipDock[row][col + i].setURL(bodyH);
				}
				if (shipDock[row][col + i].getID() != ' ') {
					shipDock[row][col + i].setID(token);
					shipDock[row][col + i].addShip(ship);
				}
			}
		}
	}
	
	/**
	 * Clears the given Ship object from the board
	 * 
	 * @param ship - Ship class object
	 */
	public void removeUserShip(Ship ship) {
		// placing the ship as 'C' for cruiser
		if (ship != null) {
			char token = '█';
			int row = ship.getFrontRow();
			int col = ship.getFrontCol();
			char direction = ship.getDirection();
			// for loop to place the ship on the specific spot in the board
			if (Character.toLowerCase(direction) == 'v') {
				for (int i = 0; i < ship.getLength(); i++) {
					shipDock[row + i][col].setURL(null);
					if (shipDock[row + i][col].getID() != ' ') {
						shipDock[row + i][col].setID(token);
					}
				}
			} else if (Character.toLowerCase(direction) == 'h') {
				for (int i = 0; i < ship.getLength(); i++) {
					shipDock[row + i][col].setURL(null);
					if (shipDock[row][col + i].getID() != ' ') {
						shipDock[row][col + i].setID(token);
					}
				}
			}
			shipCount--; // Decrement ship count when a ship is removed
		}

	}
	
	public GridPoint getGridPoint(int row, int col) {
		return shipDock[row][col];
	}
	
    public boolean isEmpty() {
        return shipCount == 0; // Return true if no ships are left in the dock
    }
}
