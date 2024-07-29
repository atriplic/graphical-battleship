package model;

import java.io.File;
import java.net.URI;
import java.util.Random;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import view_controller.BoardGUI;
import view_controller.PVPBoardGUI;

/**
 * Board object for battleship game. Contains a 2d array of board points and 5
 * ships. Extends GridPane class.
 * 
 * Contributors: Dawson Szarek
 */
public class Board extends GridPane {
	public String frontV = "src/resources/shipImages/front.png";
	public String bodyV = "src/resources/shipImages/body.png";
	public String backV = "src/resources/shipImages/back.png";
	public String frontH = "src/resources/shipImages/frontHorizontal.png";
	public String bodyH = "src/resources/shipImages/bodyHorizontal.png";
	public String backH = "src/resources/shipImages/backHorizontal.png";

	public double opacity = 0.10;
	public double strongerOpacity = 0.5;
	public Color transparentLightBlue = new Color(Color.LIGHTBLUE.getRed(), Color.LIGHTBLUE.getGreen(),
			Color.LIGHTBLUE.getBlue(), opacity);
	public Color transparentRed = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(),
			strongerOpacity);
	public Color transparentGray = new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(),
			strongerOpacity);
	public Color transparentBlack = Color.BLACK;
	public Color transparentDarkBlue = new Color(Color.DARKBLUE.getRed(), Color.DARKBLUE.getGreen(),
			Color.DARKBLUE.getBlue(), opacity);

	private int id;
	private Ship[] ships = { new Ship(5, 0, 0, 5), new Ship(4, 0, 1, 4), new Ship(3, 0, 2, 3), new Ship(3, 0, 3, 2),
			new Ship(2, 0, 4, 1) };
	private GridPoint[][] board;
	private AI AIplayer;
	private HardAI hard;
	private EasyAI easy;
	// Variables below are for ship selection phase before game.
	private Ship lastShip;
	private int lastRow;
	private int lastCol;
	private int clickCounter = 1;
	private boolean selectingShips = true;
	float hits = 0;
	float total = 0;
	int hitsInARow = 0;
	int maxHitsInARow = 0;
	private MediaPlayer mediaPlayer;
	private Media media;
	public int[] lastStrike;
	public int[] specialShotPoints;
	public String difficulty;
	// State variables for ship placement
	public boolean isSelectingShip = true;
	public boolean isSettingHead = false;
	public boolean isSettingDirection = false;
	public boolean wasUserMoveValid = false;
	public Canvas[][] animationCanvases = new Canvas[6][10];
	public PVPBoardGUI PVPGUI;
	public BoardGUI GUI;

	public Board(int id, String difficulty, Pane animationPane, BoardGUI GUI) {
		this.hard = new HardAI();
		this.easy = new EasyAI();
		this.board = makeboard();
		this.difficulty = difficulty;
		this.id = id;
		this.GUI = GUI;
		
		lastStrike = new int[2];
		specialShotPoints = new int[6];
		
		if (difficulty.equals("easy")) {
			this.AIplayer = easy;
		} else if (difficulty.equals("hard")) {
			this.AIplayer = hard;
		}

		// Set the user's board in the AI instances
		// id 0 == user board
		if (id == 0) {
			this.easy.setUserBoard(this);
			this.hard.setUserBoard(this);
		}

		// id 1 == AI's board
		if (id == 1) {
			placeAIShips();
			this.selectingShips = false;
		}

	}
	
	public Board(int id, String difficulty, Pane animationPane, PVPBoardGUI GUI) {
		this.hard = new HardAI();
		this.easy = new EasyAI();
		this.board = makeboard();
		this.difficulty = difficulty;
		this.id = id;
		this.PVPGUI = GUI;
		
		lastStrike = new int[2];
		specialShotPoints = new int[6];
		
		if (difficulty.equals("easy")) {
			this.AIplayer = easy;
		} else if (difficulty.equals("hard")) {
			this.AIplayer = hard;
		}

		// Set the user's board in the AI instances
		// id 0 == user board
		if (id == 0) {
			this.easy.setUserBoard(this);
			this.hard.setUserBoard(this);
		}

		// id 1 == AI's board
		if (id == 1) {
			placeAIShips();
			this.selectingShips = false;
		}

	}

	private GridPoint[][] makeboard() {
		// initializing the length and width of the board
		GridPoint[][] board = new GridPoint[6][10];

		// multiple for loops to create the 2d board
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 10; j++) {
				GridPoint gridpoint = new GridPoint(40, 40, i, j); // creates new GridPoint object
				board[i][j] = gridpoint; // sets point on board to new GridPoint

				// creates a small canvas for each GridPoint
				// canvasses placed over top GridPoints in BoardGUI
				Canvas canvas = new Canvas(40, 40);
				animationCanvases[i][j] = canvas;
			}
		}
		return board;
	}

	public Canvas[][] getAnimationCanvases() {
		return animationCanvases;
	}

	public void placeShips() {
		for (Ship ship : ships) {
			addUserShip(ship);
		}
	}

	/**
	 * Checks for a cleared board to end game
	 * 
	 * @return true if board is cleared, else false
	 */
	public boolean checkBoard() {
		boolean cleared = true;

		for (int i = 0; i <= 5; i++) {
			for (int j = 0; j <= 9; j++) {
				if (board[i][j].getID() == 'C') {
					cleared = false;
				}
			}
		}

		return cleared;
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
					board[row + i][col].setURL(frontV);
				} else if (i + 1 == ship.getLength()) {
					board[row + i][col].setURL(backV);
				} else {
					board[row + i][col].setURL(bodyV);
				}
				if (board[row + i][col].getID() != ' ') {
					board[row + i][col].setID(token);
					board[row + i][col].addShip(ship);
				}
			}
		} else if (Character.toLowerCase(direction) == 'h') {
			for (int i = 0; i < ship.getLength(); i++) {
				if (i == 0) {
					board[row][col + i].setURL(frontH);
				} else if (i + 1 == ship.getLength()) {
					board[row][col + i].setURL(backH);
				} else {
					board[row][col + i].setURL(bodyH);
				}
				if (board[row][col + i].getID() != ' ') {
					board[row][col + i].setID(token);
					board[row][col + i].addShip(ship);
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

		if (ship == null) {
			return; // Exit the method if there is no ship to remove
		}

		// placing the ship as 'C' for cruiser
		char token = '█';
		int row = ship.getFrontRow();
		int col = ship.getFrontCol();
		char direction = ship.getDirection();
		// for loop to place the ship on the specific spot in the board
		if (Character.toLowerCase(direction) == 'v') {
			for (int i = 0; i < ship.getLength(); i++) {
				board[row + i][col].setURL(null);
				if (board[row + i][col].getID() != ' ') {
					board[row + i][col].setID(token);
					board[row + i][col].removeShip();
				}
			}
		} else if (Character.toLowerCase(direction) == 'h') {
			for (int i = 0; i < ship.getLength(); i++) {
				board[row][col + i].setURL(null);
				if (board[row][col + i].getID() != ' ') {
					board[row][col + i].setID(token);
					board[row][col + i].removeShip();
				}
			}
		}
	}

	public boolean attemptStrikeAI() {
		return AIplayer.attemptStrike(this.board);
	}

	/**
	 * Produces shot on inputed cords on player's target board and the computer's
	 * ocean board
	 * 
	 * @return returns true if shot was a hit else returns false
	 */
	public boolean attemptStrikeUser(int row, int column) {
		if (transparentLightBlue.equals(board[row][column].getFill())
				|| Color.GREEN.equals(board[row][column].getFill()) || difficulty == "PVP") { // moves can only be made
																								// on blue squares
			wasUserMoveValid = true;
			total += 1;
			lastStrike = new int[2];
			lastStrike[0] = row;
			lastStrike[1] = column;
			if (board[row][column].getID() == 'C') {
				Ship ship = board[row][column].getShip();
				ship.hit();
				board[row][column].setID('H');

				// Retrieve the GridPoint that was hit
				GridPoint hitPoint = board[row][column];

				hitPoint.setFill(transparentRed); // a hit

				Canvas currentAnimatingCanvas = animationCanvases[row][column];
				ExplosionAnimation explosion = new ExplosionAnimation(currentAnimatingCanvas, "Explosion");
				explosion.startExplosionAnimation();

				String hitPath = "src/songfiles/Hit.mp3";
				File hitFile = new File(hitPath);
				URI hitFileUri = hitFile.toURI();
				media = new Media(hitFileUri.toString());
				mediaPlayer = new MediaPlayer(media);
				mediaPlayer.play();

				if (ship.isSunk()) {
					if (Character.toLowerCase(ship.getDirection()) == 'v') {
						for (int i = 0; i < ship.getLength(); i++) {
							board[ship.getFrontRow() + i][ship.getFrontCol()].showImage();
						}
					} else {
						for (int i = 0; i < ship.getLength(); i++) {
							board[ship.getFrontRow()][ship.getFrontCol() + i].showImage();
						}
					}
					String shipDownPath = "src/songfiles/ShipDown.mp3";
					File shipDownFile = new File(shipDownPath);
					URI shipDownFileUri = shipDownFile.toURI();
					media = new Media(shipDownFileUri.toString());
					mediaPlayer = new MediaPlayer(media);
					mediaPlayer.play();
				}
				hits += 1;
				hitsInARow += 1;
				if (hitsInARow > maxHitsInARow) {
					maxHitsInARow = hitsInARow;
				}
				return true;
			} else if (board[row][column].getID() != 'H') {
				board[row][column].setID('M');

				// Retrieve the GridPoint that was hit
				GridPoint missPoint = board[row][column];

				missPoint.setFill(transparentGray); // a miss

				Canvas currentAnimatingCanvas = animationCanvases[row][column];
				ExplosionAnimation splash = new ExplosionAnimation(currentAnimatingCanvas, "Splash");
				splash.startExplosionAnimation();

				String missPath = "src/songfiles/SplashTrimmedMore.mp3";
				File missFile = new File(missPath);
				URI missUri = missFile.toURI();
				media = new Media(missUri.toString());
				mediaPlayer = new MediaPlayer(media);
				mediaPlayer.play();
				if (hitsInARow > maxHitsInARow) {
					maxHitsInARow = hitsInARow;
				}
				hitsInARow = 0;
				return false;
			}
		} else {
			wasUserMoveValid = false;
		}

		return false;
	}

	public boolean attemptStrikeUserSpecialShots(int row, int column) {
		wasUserMoveValid = true;
		total += 1;
		lastStrike = new int[2];
		lastStrike[0] = row;
		lastStrike[1] = column;
		if (board[row][column].getID() == 'C') {
			Ship ship = board[row][column].getShip();
			ship.hit();
			board[row][column].setID('H');
			// Retrieve the GridPoint that was hit
			GridPoint hitPoint = board[row][column];
			hitPoint.setFill(transparentRed); // a hit
			Canvas currentAnimatingCanvas = animationCanvases[row][column];
			ExplosionAnimation explosion = new ExplosionAnimation(currentAnimatingCanvas, "Explosion");
			explosion.startExplosionAnimation();
			String hitPath = "src/songfiles/Hit.mp3";
			File hitFile = new File(hitPath);
			URI hitFileUri = hitFile.toURI();
			media = new Media(hitFileUri.toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();
			if (ship.isSunk()) {
				if (Character.toLowerCase(ship.getDirection()) == 'v') {
					for (int i = 0; i < ship.getLength(); i++) {
						board[ship.getFrontRow() + i][ship.getFrontCol()].showImage();
					}
				} else {
					for (int i = 0; i < ship.getLength(); i++) {
						board[ship.getFrontRow()][ship.getFrontCol() + i].showImage();
					}
				}
				String shipDownPath = "src/songfiles/ShipDown.mp3";
				File shipDownFile = new File(shipDownPath);
				URI shipDownFileUri = shipDownFile.toURI();
				media = new Media(shipDownFileUri.toString());
				mediaPlayer = new MediaPlayer(media);
				mediaPlayer.play();
			}
			hits += 1;
			hitsInARow += 1;
			if (hitsInARow > maxHitsInARow) {
				maxHitsInARow = hitsInARow;
			}
			return true;
		} else if (board[row][column].getID() != 'H') {
			board[row][column].setID('M');
			// Retrieve the GridPoint that was hit
			GridPoint missPoint = board[row][column];
			missPoint.setFill(transparentGray); // a miss
			Canvas currentAnimatingCanvas = animationCanvases[row][column];
			ExplosionAnimation splash = new ExplosionAnimation(currentAnimatingCanvas, "Splash");
			splash.startExplosionAnimation();
			String missPath = "src/songfiles/SplashTrimmedMore.mp3";
			File missFile = new File(missPath);
			URI missUri = missFile.toURI();
			media = new Media(missUri.toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();
			if (hitsInARow > maxHitsInARow) {
				maxHitsInARow = hitsInARow;
			}
			hitsInARow = 0;
			return false;
		}
		return false;
	}

	/**
	 * Prints board in a 6x10 layout showing ships
	 * 
	 * @param board, a 2-d array
	 */
	public void printPlayerBoard() {
		for (int i = 0; i <= 5; i++) {
			for (int j = 0; j <= 9; j++) {
				System.out.print(board[i][j].toString() + " ");
			}

			System.out.print("\n");
		}

		System.out.print("\n");
	}

	/**
	 * Prints board in a 6x10 layout ignoring ships
	 * 
	 * @param board, a 2-d array
	 */
	public void printCompBoard() {
		for (int i = 0; i <= 5; i++) {
			for (int j = 0; j <= 9; j++) {
				if (board[i][j].getID() != 'C' && board[i][j].getID() != '█') {
					System.out.print(board[i][j].toString() + " ");
				} else {
					System.out.print("█ ");
				}
			}

			System.out.print("\n");
		}

		System.out.print("\n");
	}

	private void placeAIShips() {
		Random rand = new Random();
		// for loop to place the two ships
		for (int i = 0; i < 5; i++) {
			// the row and column value will be random
			int row = rand.nextInt(6);
			int col = rand.nextInt(10);
			// deciding if the ship is placed vertical or horizontal
			int direction = rand.nextInt(2);
			// ship will be vertical if 0, and horizontal if 1
			boolean clear = true; // flag to check if ship will hit another ship
			Ship ship = ships[i];

			if (direction == 0) {
				// deciding the placement of the ship based on the row number
				if (check_vert_clear(row, col, ship)) {
					ship.updateDirection('V');
					ship.updateFront(row, col);
					for (int j = 0; j < ship.getLength(); j++) {
						if (j == 0) {
							board[row + j][col].setURL(frontV);
						} else if (j + 1 == ship.getLength()) {
							board[row + j][col].setURL(backV);
						} else {
							board[row + j][col].setURL(bodyV);
						}
						board[row + j][col].setID('C');
						board[row + j][col].addShip(ship);
					}
				} else {
					i -= 1;
				}
			} else {
				if (check_horz_clear(row, col, ship)) {
					ship.updateDirection('H');
					ship.updateFront(row, col);
					for (int j = 0; j < ship.getLength(); j++) {
						if (j == 0) {
							board[row][col + j].setURL(frontH);
						} else if (j + 1 == ship.getLength()) {
							board[row][col + j].setURL(backH);
						} else {
							board[row][col + j].setURL(bodyH);
						}
						board[row][col + j].setID('C');
						board[row][col + j].addShip(ship);
					}
				} else {
					i -= 1;
				}
			}
		}
	}

	/**
	 * Checks for open board for a horizontal ship Keeps ships from occupying the
	 * same space on board
	 * 
	 * @return true if ship is clear to fit horizontally, else false
	 */
	public boolean check_horz_clear(int row, int col, Ship ship) {
		// Keeps ship in bounds of board
		if (col + (ship.getLength()) > 10) {
			return false;
		}
		for (int i = 0; i < ship.getLength(); i++) {
			if (board[row][col + i].getID() == 'C') {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks for open board for a vertical ship Keeps ships from occupying the same
	 * space on board
	 * 
	 * @return true if ship is clear to fit vertically, else false
	 */
	public boolean check_vert_clear(int row, int col, Ship ship) {
		if (row + ship.getLength() > 6) {
			return false;
		}
		for (int i = 0; i < ship.getLength(); i++) {
			if (board[row + i][col].getID() == 'C') {
				return false;
			}
		}

		return true;

	}

	public void updateShips(Ship[] ships) {
		for (int i = 0; i < 5; i++) {
			this.ships[i] = ships[i];
		}
	}

	public Ship[] getShips() {
		return ships;
	}

	public GridPoint getGridPoint(int row, int col) {
		return board[row][col];
	}

	public int getID() {
		return this.id;
	}

	public boolean SelectingShips() {
		return this.selectingShips;
	}

	public void stopSelectingShips() {
		this.selectingShips = false;
	}

	public void updateLastShip(Ship ship, int row, int col) {
		this.lastShip = ship;
		this.lastRow = row;
		this.lastCol = col;
	}

	/*
	 * public void updateLastStrike(int row, int col) { this.lastStrike[0] = row;
	 * this.lastStrike[1] = col; }
	 * 
	 * public int[] getLastStrike() { return this.lastStrike; }
	 */
	public int getLastRow() {
		return this.lastRow;
	}

	public int getLastCol() {
		return this.lastCol;
	}

	// used during ship adjustment phase before game starts.
	public int getClickCounter() {
		return this.clickCounter;
	}

	public Ship getLastShip() {
		return this.lastShip;
	}

	public void incrementClicker() {
		this.clickCounter++;
	}

	public void resetClicker() {
		this.clickCounter = 1;
	}

	public float getShotPercentage() {
		return (hits / total) * 100;
	}

	public int getHitsInARow() {
		return maxHitsInARow;
	}

	// Methods to update and check the state
	public void startSelectingShip() {
		isSelectingShip = true;
		isSettingHead = false;
		isSettingDirection = false;
	}

	public void startSettingHead() {
		isSelectingShip = false;
		isSettingHead = true;
		isSettingDirection = false;
	}

	public void startSettingDirection() {
		isSelectingShip = false;
		isSettingHead = false;
		isSettingDirection = true;
	}

	public void giveUserHint() {
		boolean loop = false;
		while (loop == false) {
			Random random = new Random();
			int row = random.nextInt(6);
			int col = random.nextInt(10);
			if (board[row][col].getID() == 'C') {
				GridPoint hitPoint = board[row][col];
				hitPoint.setFill(Color.GREEN);
				loop = true;
			}
		}
	}

	public int getNumShipPoints() {
		int count = 0;
		for (int i = 0; i <= 5; i++) {
			for (int j = 0; j <= 9; j++) {
				if (board[i][j].getURL() != null) {
					count += 1;
				}
			}
		}
		return count;
	}
}
