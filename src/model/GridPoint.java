package model;

import java.io.File;
import java.net.URI;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import view_controller.BoardGUI;
import view_controller.PVPBoardGUI;

/**
 * GridPoint class to represent a point on the board for BattleShip game.
 * Extends Rectangle for JavaFX features.
 * 
 * Contributors: Dawson Szarek
 */
public class GridPoint extends Rectangle {
	private String url = null;
	private char id = '█';
	private int row;
	private int column;
	private Ship ship;
	private Board board;
	private ShipDock shipDock;
	static float shotPercentage = 0;
	static int maxHitsInARow = 0;
	private MediaPlayer mediaPlayer;
	private Media media;
	public static boolean userMadeMove = false;

	public GridPoint(double width, double height, int row, int col) {
		// sets box width and height for JavaFX game
		this.setWidth(width);
		this.setHeight(height);

		// sets box outline and color for JavaFX game
		this.setStroke(Color.WHITE);
		this.setFill(Color.GRAY);

		this.row = row;
		this.column = col;

		// Handles actions when a GridPoint is pressed during JavaFX game.
		this.setOnMouseClicked(e -> {

			if (this.shipDock != null
					&& Color.WHITE.equals(this.shipDock.getGridPoint(this.row, this.column).getFill())) {
				System.out.println("Clicked on white part of dock");
				return;
			}

			if (this.board.SelectingShips()) {
				if (this.board.isSelectingShip) {
					// Logic for picking up ship from dock
					System.out.println("selecting ship");
					if (this.shipDock != null) {
						this.board.updateLastShip(this.ship, this.row, this.column);
						this.shipDock.removeUserShip(this.ship);
						playClickSound();
						this.board.startSettingHead();
					}

					// If a GridPoint on the user board is selected that already has a ship, return
					// ship to dock
					else if (this.getShip() != null) {
						System.out.println("removing ship");
						// A ship is already placed here, pick it up for replacing
						Ship pickedUpShip = this.getShip();
						this.board.removeUserShip(pickedUpShip); // remove the ship from the board
						this.board.updateLastShip(pickedUpShip, this.row, this.column); // set it as the current ship to
																						// be placed playClickSound();
						playClickSound();
						// Reset the state to start selecting another ship
						this.board.startSettingHead();
					}

				} else if (this.board.isSettingHead) {
					// Logic for setting the head of the ship
					if (this.getShip() == null) { // Ensures there is not already a ship there
						System.out.println("placing ship");
						this.ship = this.board.getLastShip();
						this.ship.updateFront(this.row, this.column);
						playClickSound();

						if (this.board.check_vert_clear(this.row, this.column, this.ship)) {
							System.out.println("Vertical is clear");
							this.board.startSettingDirection();
							this.board.getGridPoint(this.row + 1, this.column).setFill(Color.YELLOW); // NOT WORKING
						}
						if (this.board.check_horz_clear(this.row, this.column, this.ship)) {
							System.out.println("Horizontal is clear");
							this.board.startSettingDirection();
							this.board.getGridPoint(this.row, this.column + 1).setFill(Color.YELLOW); // NOT WORKING
						}
						// Choose new head if current head has no clear paths
						if (!this.board.check_vert_clear(this.row, this.column, this.ship)
								&& !this.board.check_horz_clear(this.row, this.column, this.ship)) {
							System.out.println("Choose new head if current head has no clear paths");
							this.board.startSettingHead();
						}
					} else {
						System.out.println("this space is not null");
					}

				} else if (this.board.isSettingDirection) {
					// Logic for setting the direction of the ship
					if (this.board.getLastShip() != null) {
						// Confirms only a highlight spot can be clicked
						if (Color.YELLOW.equals(this.board.getGridPoint(this.row, this.column).getFill())) {

							// if this GridPoint is below head of Ship(last click), place down.
							if (this.board.getLastShip().getFrontRow() < this.row
									&& this.board.getLastShip().getFrontCol() == this.column) {
								this.board.getLastShip().updateDirection('V');
								this.board.addUserShip(this.board.getLastShip()); // Place ship on board
								playClickSound();
								this.board.startSelectingShip(); // Ready to select another ship

								// if this GridPoint is to right of head of Ship (last click)
							} else if (this.board.getLastShip().getFrontCol() < this.column
									&& this.board.getLastShip().getFrontRow() == this.row) {

								this.board.getLastShip().updateDirection('H');
								this.board.addUserShip(this.board.getLastShip()); // Place ship on board
								playClickSound();
								this.board.startSelectingShip(); // Ready to select another ship
							}
						} else {
							this.board.startSettingDirection();
						}
					}
				}
			} else {
				// Handle other phases of the game or ignore the click
				System.out.println("Tapping on opponent grid");
				if ((this.board.PVPGUI != null && PVPBoardGUI.specialShotsCalled == false)
						|| (this.board.GUI != null && BoardGUI.specialShotsCalled == false)) {
					if (this.board.getID() != 0) {
						this.board.attemptStrikeUser(this.row, this.column);
						shotPercentage = this.board.getShotPercentage();
						maxHitsInARow = this.board.getHitsInARow();
						if (board.wasUserMoveValid) {
							userMadeMove = true;
						} else {
							userMadeMove = false;
						}

					} else {
						userMadeMove = false;
					}
				} else {
					// Handle other phases of the game or ignore the click
					System.out.println("Tapping on opponent grid");
					if ((this.board.PVPGUI != null && PVPBoardGUI.specialShotsCalled == false)
							|| (this.board.GUI != null && BoardGUI.specialShotsCalled == false)) {
						if (this.board.getID() != 0) {
							this.board.attemptStrikeUser(this.row, this.column);
							shotPercentage = this.board.getShotPercentage();
							maxHitsInARow = this.board.getHitsInARow();
							if (board.wasUserMoveValid) {
								userMadeMove = true;
							} else {
								userMadeMove = false;
							}

						} else {
							userMadeMove = false;
						}
					} else {
						System.out.println("Special Shots has been pressed and something needs to be done");
						if ((this.board.PVPGUI != null && PVPBoardGUI.verticalPressed == true)
								|| (this.board.GUI != null && BoardGUI.verticalPressed == true)) {
							System.out.println("Vertical was pressed");
							if (this.board.getID() != 0) {
								if (this.row + 1 > 5) {
									this.board.attemptStrikeUser(this.row, this.column);
									this.board.attemptStrikeUser(this.row - 1, this.column);
									this.board.attemptStrikeUser(this.row - 2, this.column);
									this.board.specialShotPoints[0] = this.row;
									this.board.specialShotPoints[1] = this.column;
									this.board.specialShotPoints[2] = this.row - 1;
									this.board.specialShotPoints[3] = this.column;
									this.board.specialShotPoints[4] = this.row - 2;
									this.board.specialShotPoints[5] = this.column;
								} else if (this.row - 1 < 0) {
									this.board.attemptStrikeUser(this.row, this.column);
									this.board.attemptStrikeUser(this.row + 1, this.column);
									this.board.attemptStrikeUser(this.row + 2, this.column);
									this.board.specialShotPoints[0] = this.row;
									this.board.specialShotPoints[1] = this.column;
									this.board.specialShotPoints[2] = this.row + 1;
									this.board.specialShotPoints[3] = this.column;
									this.board.specialShotPoints[4] = this.row + 2;
									this.board.specialShotPoints[5] = this.column;
								} else {
									this.board.attemptStrikeUser(this.row, this.column);
									this.board.attemptStrikeUser(this.row + 1, this.column);
									this.board.attemptStrikeUser(this.row - 1, this.column);
									this.board.specialShotPoints[0] = this.row;
									this.board.specialShotPoints[1] = this.column;
									this.board.specialShotPoints[2] = this.row + 1;
									this.board.specialShotPoints[3] = this.column;
									this.board.specialShotPoints[4] = this.row - 1;
									this.board.specialShotPoints[5] = this.column;
								}
								shotPercentage = this.board.getShotPercentage();
								maxHitsInARow = this.board.getHitsInARow();
								if (board.wasUserMoveValid && this.board.PVPGUI == null) {
									Random rand = new Random();
									int rowAI = rand.nextInt(6); 
									int colAI = rand.nextInt(10);
									int VOrH = rand.nextInt(2);
									if (VOrH == 0) { //vertical
										 if (rowAI + 1 > 5) {
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI);
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI - 1, colAI);
											}
											else if (rowAI - 1 < 0) {
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI);
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI + 1, colAI);
											}
											else {
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI);
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI + 1, colAI);
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI - 1, colAI);
											}
									 }
									 else { //horizontal
										 if (colAI + 1 > 9) {
											 BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI);
											 BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI - 1);
											}
											else if (colAI - 1 < 0) {
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI);
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI + 1);
											}
											else {
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI);
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI + 1);
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI - 1);
											}
									 }
								} else if (board.wasUserMoveValid && this.board.PVPGUI != null) {
									userMadeMove = true;
								} else {
									userMadeMove = false;
								}

							} else {
								userMadeMove = false;
							}
						} else if ((this.board.PVPGUI != null && PVPBoardGUI.horizontalPressed == true)
								|| (this.board.GUI != null && BoardGUI.horizontalPressed == true)) {
							System.out.println("Horizontal was pressed");
							if (this.board.getID() != 0) {
								if (this.column + 1 > 9) {
									this.board.attemptStrikeUser(this.row, this.column);
									this.board.attemptStrikeUser(this.row, this.column - 1);
									this.board.attemptStrikeUser(this.row, this.column - 2);
									this.board.specialShotPoints[0] = this.row;
									this.board.specialShotPoints[1] = this.column;
									this.board.specialShotPoints[2] = this.row;
									this.board.specialShotPoints[3] = this.column - 1;
									this.board.specialShotPoints[4] = this.row;
									this.board.specialShotPoints[5] = this.column - 2;
								} else if (this.column - 1 < 0) {
									this.board.attemptStrikeUser(this.row, this.column);
									this.board.attemptStrikeUser(this.row, this.column + 1);
									this.board.attemptStrikeUser(this.row, this.column + 2);
									this.board.specialShotPoints[0] = this.row;
									this.board.specialShotPoints[1] = this.column;
									this.board.specialShotPoints[2] = this.row;
									this.board.specialShotPoints[3] = this.column + 1;
									this.board.specialShotPoints[4] = this.row;
									this.board.specialShotPoints[5] = this.column + 2;
								} else {
									this.board.attemptStrikeUser(this.row, this.column);
									this.board.attemptStrikeUser(this.row, this.column + 1);
									this.board.attemptStrikeUser(this.row, this.column - 1);
									this.board.specialShotPoints[0] = this.row;
									this.board.specialShotPoints[1] = this.column;
									this.board.specialShotPoints[2] = this.row;
									this.board.specialShotPoints[3] = this.column + 1;
									this.board.specialShotPoints[4] = this.row;
									this.board.specialShotPoints[5] = this.column - 1;
								}
								shotPercentage = this.board.getShotPercentage();
								maxHitsInARow = this.board.getHitsInARow();
								if (board.wasUserMoveValid && this.board.PVPGUI == null) {
									Random rand = new Random();
									int rowAI = rand.nextInt(6); 
									int colAI = rand.nextInt(10);
									int VOrH = rand.nextInt(2);
									if (VOrH == 0) { //vertical
										 if (rowAI + 1 > 5) {
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI);
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI - 1, colAI);
											}
											else if (rowAI - 1 < 0) {
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI);
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI + 1, colAI);
											}
											else {
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI);
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI + 1, colAI);
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI - 1, colAI);
											}
									 }
									 else { //horizontal
										 if (colAI + 1 > 9) {
											 BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI);
											 BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI - 1);
											}
											else if (colAI - 1 < 0) {
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI);
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI + 1);
											}
											else {
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI);
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI + 1);
												BoardGUI.userBoard.attemptStrikeUserSpecialShots(rowAI, colAI - 1);
											}
									 }
								} else if (board.wasUserMoveValid && this.board.PVPGUI != null) {
									userMadeMove = true;
								}else {
									userMadeMove = false;
								}

							} else {
								userMadeMove = false;
							}
						}
					}
						BoardGUI.specialShotsCalled = false;
				}
			}
		});
	}

	public void playClickSound() {
		String clickPath = "src/songfiles/ClickSound.mp3";
		File clickFile = new File(clickPath);
		URI clickUri = clickFile.toURI();
		media = new Media(clickUri.toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}

	public char getID() {
		return this.id;
	}

	public void setID(char id) {
		this.id = id;
	}

	public void addShip(Ship ship) {
		this.ship = ship;
	}

	public void removeShip() {
		this.ship = null;
	}

	public Ship getShip() {
		return ship;
	}

	public String toString() {
		return String.valueOf(id);
	}

	public void updateBoard(Board board) {
		this.board = board;
	}

	public void updateDock(ShipDock Dock) {
		this.shipDock = Dock;
	}

	public static float getFinalShotPercentage() {
		return shotPercentage;
	}

	public static int getMaxHitsInARow() {
		return maxHitsInARow;
	}

	public int getXPixel() {
		return column * 40;
	}

	public int getYPixel() {
		return row * 40;
	}

	public static boolean getUserMadeMove() {
		return userMadeMove;
	}

	public static void makeGetUserMadeMoveFalse() {
		userMadeMove = false;
	}

	public String getURL() {
		return this.url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public void showImage() {
		Image image = new Image(new File(url).toURI().toString());
		ImagePattern imagePattern = new ImagePattern(image);
		this.setFill(imagePattern);
	}

}