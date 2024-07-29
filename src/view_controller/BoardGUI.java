package view_controller;

import java.io.File;
import java.net.URI;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Board;
import model.EasyAI;
import model.GridPoint;
import model.HardAI;
import model.ShipDock;

public class BoardGUI extends Application {
	public double opacity = 0.10;
	public double strongerOpacity = 0.5;
	public Color transparentLightBlue = new Color(Color.LIGHTBLUE.getRed(), Color.LIGHTBLUE.getGreen(),
			Color.LIGHTBLUE.getBlue(), opacity);
	public Color transparentRed = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), opacity);
	public Color transparentGray = new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), strongerOpacity);
	public Color transparentBlack = Color.BLACK;
	public Color transparentDarkBlue = new Color(Color.DARKBLUE.getRed(), Color.DARKBLUE.getGreen(),
			Color.DARKBLUE.getBlue(), opacity);

	private BorderPane pane;
	public static Board computerBoard;
	public static Board userBoard;
	private Button startGameButton = new Button("Start");
	private Button tutorialButton = new Button("Tutorial");
	private Button hintButton = new Button("Hint");
	private Button specialShotsButton = new Button("Special Shots");
	private Label gameMessage = new Label("Place Your Ships!");
	private Button optionsButton = new Button("♪");
	public static boolean specialShotsCalled = false;
	public static boolean verticalPressed = false;
	public static boolean horizontalPressed = false;
    public static boolean verticalPressedAI = false;
    public static boolean horizontalPressedAI = false;
	private boolean gameStarted = false;
	private GameOverGUI gameOverGUI;
	private MediaPlayer mediaPlayer;
	private ShipDock shipDock;
	private GridPane boardPane;
	private BorderPane buttonPane;
	private Media media;
	private GameStartGUI gameStartGUI;
	private String difficulty;
	private TextArea tutorial;
	private int tutorialCount = 0;
	private int optionsCount = 0;
	private Button verticalButton = new Button("V");
	private Button horizontalButton = new Button("H");
	Pane animationPane = new Pane();

	public BoardGUI(String difficulty, GameStartGUI gameStartGUI) {
		computerBoard = new Board(1, difficulty, animationPane, this);
		userBoard = new Board(0, difficulty, animationPane, this);
		this.difficulty = difficulty;
		this.gameStartGUI = gameStartGUI;
	}

	@Override
	public void start(Stage stage) throws Exception {
		startGameButton.setStyle("-fx-background-image: url('resources/ButtonTile.jpeg'); "
				+ "-fx-background-size: cover; " + "-fx-border-color: white; " + "-fx-border-width: 2; "
				+ "-fx-text-fill: #ffffff; " + "-fx-font-weight: bold; "
				+ "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 5);");

		tutorialButton.setStyle("-fx-background-image: url('resources/ButtonTile.jpeg'); "
				+ "-fx-background-size: cover; " + "-fx-border-color: white; " + "-fx-border-width: 2; "
				+ "-fx-text-fill: #ffffff; " + "-fx-font-weight: bold; "
				+ "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 5);");

		hintButton.setStyle("-fx-background-image: url('resources/ButtonTile.jpeg'); " + "-fx-background-size: cover; "
				+ "-fx-border-color: white; " + "-fx-border-width: 2; " + "-fx-text-fill: #ffffff; "
				+ "-fx-font-weight: bold; " + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 5);");

		specialShotsButton.setStyle("-fx-background-image: url('resources/ButtonTile.jpeg'); "
				+ "-fx-background-size: cover; " + "-fx-border-color: white; " + "-fx-border-width: 2; "
				+ "-fx-text-fill: #ffffff; " + "-fx-font-weight: bold; "
				+ "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 5);");

		optionsButton.setStyle("-fx-background-image: url('resources/ButtonTile.jpeg'); "
				+ "-fx-background-size: cover; " + "-fx-border-color: white; " + "-fx-border-width: 2; "
				+ "-fx-text-fill: #ffffff; " + "-fx-font-weight: bold; "
				+ "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 5);");

		verticalButton.setStyle("-fx-background-image: url('resources/ButtonTile.jpeg'); "
				+ "-fx-background-size: cover; " + "-fx-border-color: white; " + "-fx-border-width: 2; "
				+ "-fx-text-fill: #ffffff; " + "-fx-font-weight: bold; "
				+ "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 5);");

		horizontalButton.setStyle("-fx-background-image: url('resources/ButtonTile.jpeg'); "
				+ "-fx-background-size: cover; " + "-fx-border-color: white; " + "-fx-border-width: 2; "
				+ "-fx-text-fill: #ffffff; " + "-fx-font-weight: bold; "
				+ "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 5);");

		startGameButton.setFont(new Font("Helvetica", 14));
		startGameButton.setMinSize(60, 40);
		startGameButton.setMaxSize(60, 40);
		tutorialButton.setFont(new Font("Helvetica", 14));
		tutorialButton.setMinSize(80, 40);
		tutorialButton.setMaxSize(80, 40);
		hintButton.setFont(new Font("Helvetica", 14));
		hintButton.setMinSize(80, 40);
		hintButton.setMaxSize(80, 40);
		specialShotsButton.setFont(new Font("Helvetica", 12));
		specialShotsButton.setMinSize(100, 40);
		specialShotsButton.setMaxSize(100, 40);
		optionsButton.setFont(new Font("Helvetica", 14));
		optionsButton.setMinSize(50, 40);
		optionsButton.setMaxSize(50, 40);
		verticalButton.setFont(new Font("Helvetica", 14));
		verticalButton.setMinSize(50, 40);
		verticalButton.setMaxSize(50, 40);
		horizontalButton.setFont(new Font("Helvetica", 14));
		horizontalButton.setMinSize(50, 40);
		horizontalButton.setMaxSize(50, 40);

		pane = new BorderPane();
		pane.setPadding(new Insets(10, 10, 10, 10));

		// computerBoard = new Board(1);
		computerBoard.setAlignment(Pos.CENTER);
		// userBoard = new Board(0);
		userBoard.setAlignment(Pos.CENTER);

		createUserBoard();
		createCompBoard();
		createShipDoc();
		createButtonPane();

		tutorial = new TextArea();
		tutorial.setPrefSize(100, 100);
		tutorial.setEditable(false);
		tutorial.setWrapText(true);
		tutorial.setStyle("-fx-background-color: lightblue;");
		tutorial.setOpacity(0.8);
		getTutorialInfo();

		shipDock.setStyle("-fx-background-color: lightblue;");
		shipDock.setOpacity(0.8);

		verticalButton.setVisible(false);
		horizontalButton.setVisible(false);

		tutorial.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");
		computerBoard.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");
		userBoard.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");
		shipDock.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5;");

		boardPane = new GridPane();
		boardPane.add(computerBoard, 0, 0);
		boardPane.add(userBoard, 0, 1);
		boardPane.add(tutorial, 1, 0);
		boardPane.add(shipDock, 1, 1);
		boardPane.setHgap(10);
		boardPane.setVgap(10);

		updateBoard();

		computerBoard.setDisable(true);

		pane.setCenter(boardPane);
		pane.setBottom(buttonPane);

		// Add animation canvases to the pane that contains the boards
		Pane computerAnimationOverlay = createAnimationOverlayPane(computerBoard, boardPane, 0, 0);
		addCanvasesToBoardPane(computerBoard, computerAnimationOverlay);

		Pane userAnimationOverlay = createAnimationOverlayPane(userBoard, boardPane, 0, 250);
		addCanvasesToBoardPane(userBoard, userAnimationOverlay);

		Scene scene = new Scene(pane, 700, 600);
		stage.setTitle("BoardGUI");

		// Background image
		String imagePath = "src/resources/gameBackground.jpeg";
		Image image = new Image(new File(imagePath).toURI().toString());
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		pane.setBackground(new Background(backgroundImage));

		stage.setScene(scene);
		stage.show();

		pane.setOnMouseClicked(e -> {
			if (gameStarted) {
				if (userBoard.checkBoard()) { // if all ships on userBoard are sunk
					losingInfo(stage);
				} else if (computerBoard.checkBoard()) { // if all ships on computerBoard are sunk
					winningInfo(stage);
				}
				if (GridPoint.getUserMadeMove()) {
					userBoard.attemptStrikeAI();
					GridPoint.makeGetUserMadeMoveFalse();
					if (userBoard.checkBoard()) { // if all ships on userBoard are sunk
						losingInfo(stage);
					}
				}
			}
			updateBoard();
		});

		startGameButton.setOnAction(e -> {
			if (shipDock.isEmpty() && this.userBoard.getNumShipPoints() == 17) {
				updateBoard();
				for (int i = 0; i <= 5; i++) {
					for (int j = 0; j <= 9; j++) {
						computerBoard.getGridPoint(i, j).setFill(transparentLightBlue);
					}
				}
				this.gameStarted = true;
				computerBoard.setDisable(false);
				userBoard.stopSelectingShips();
				startGameButton.setDisable(true);
				specialShotsButton.setDisable(false);
				gameMessage.setText("Attack the Opponent's Ships!");
				shipDock.setVisible(false);
				hintButton.setDisable(false);
			} else {
				System.out.println("Either ship dock is not empty or board isn't full");
			}
		});
		tutorialButton.setOnAction(e -> {
			if (tutorialCount % 2 == 0) {
				tutorial.setVisible(false);
			} else {
				tutorial.setVisible(true);
			}
			tutorialCount += 1;
		});
		hintButton.setOnAction(e -> {
			this.computerBoard.giveUserHint();
			hintButton.setDisable(true);
		});
		optionsButton.setOnAction(e -> {
			gameStartGUI.toggleMute();
			
			if (optionsCount % 2 == 0) {
				optionsButton.setText("♫");
			} else {
				optionsButton.setText("♪");
			}
			optionsCount += 1;
		});
		specialShotsButton.setOnAction(e -> {
			verticalButton.setVisible(true);
			horizontalButton.setVisible(true);
			specialShotsButton.setDisable(true);
			specialShotsCalled = true;
		});
		verticalButton.setOnAction(e -> {
			verticalPressed = true;
			verticalPressedAI = true;
			verticalButton.setDisable(true);
			horizontalButton.setDisable(true);
		});
		horizontalButton.setOnAction(e -> {
			horizontalPressed = true;
			horizontalPressedAI = true;
			horizontalButton.setDisable(true);
			verticalButton.setDisable(true);
		});

	}

	private Pane createAnimationOverlayPane(Board board, Pane boardPane, int moveX, int moveY) {

		final int gridpointSize = 40; // Assuming each GridPoint is 40x40 pixels
		final int columns = 10;
		final int rows = 6;

		// Create the overlay Pane
		Pane animationOverlay = new Pane();
		animationOverlay.setPickOnBounds(false); // Allows click events to pass through

		// Calculate the size of the overlay Pane based on the GridPoint and grid size
		animationOverlay.setPrefWidth(gridpointSize * columns);
		animationOverlay.setPrefHeight(gridpointSize * rows);

		// Position the overlay Pane
		animationOverlay.setLayoutX(boardPane.getLayoutX() + moveX);
		animationOverlay.setLayoutY(boardPane.getLayoutY() + moveY);

		// Add the overlay Pane to the same parent as boardPane
		Pane parentContainer = (Pane) boardPane.getParent();
		parentContainer.getChildren().add(animationOverlay);

		return animationOverlay;
	}

	private void addCanvasesToBoardPane(Board board, Pane animationOverlay) {

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 10; j++) {
				String path = board.getGridPoint(i, j).getURL();
				
				Canvas[][] boardCanvases = board.getAnimationCanvases();
				Canvas canvas = boardCanvases[i][j]; // animationCanvases is static in Board

				// Position canvas over corresponding GridPoint
				// Offset by 15 to get animation in center
				canvas.setLayoutX(j * 40 + 17);
				canvas.setLayoutY(i * 40 + 15);
				canvas.setMouseTransparent(true);

				animationOverlay.getChildren().add(canvas);
			}
		}
	}

	private void createButtonPane() {
		this.buttonPane = new BorderPane();

		// this.buttonPane.setPadding(new Insets(0, 50, 20, 50));

		hintButton.setDisable(true);
		specialShotsButton.setDisable(true);

		HBox buttons = new HBox(10); // 10 is the spacing between buttons
		buttons.getChildren().addAll(startGameButton, tutorialButton, optionsButton, hintButton, specialShotsButton,
				verticalButton, horizontalButton);

		this.buttonPane.setCenter(buttons);
//		this.buttonPane.setRight(gameMessage);

		Font boldFont = Font.font("Arial", FontWeight.BOLD, 12);
		gameMessage.setFont(boldFont);
		gameMessage.setTextFill(Color.WHITE);

	}

	public void createUserBoard() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 10; j++) {
				userBoard.getGridPoint(i, j).updateBoard(userBoard);
				userBoard.add(userBoard.getGridPoint(i, j), j, i);
			}
		}
	}

	public void createCompBoard() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 10; j++) {
				computerBoard.getGridPoint(i, j).updateBoard(computerBoard);
				computerBoard.add(computerBoard.getGridPoint(i, j), j, i);
			}
		}
	}

	public void createShipDoc() {
		this.shipDock = new ShipDock(userBoard);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				shipDock.getGridPoint(i, j).updateBoard(userBoard);
				shipDock.getGridPoint(i, j).updateDock(shipDock);
				shipDock.add(shipDock.getGridPoint(i, j), j, i);
			}
		}
	}

	public void updateBoard() {
		for (int i = 0; i <= 5; i++) {
			for (int j = 0; j <= 9; j++) {
				if (!userBoard.isSettingDirection) {
					if (userBoard.getGridPoint(i, j).getID() == 'C') {
						userBoard.getGridPoint(i, j).showImage();
					} else if (userBoard.getGridPoint(i, j).getID() == '█') {
						userBoard.getGridPoint(i, j).setFill(transparentLightBlue);
					}
				}
			}
		}
		if (!gameStarted) {
			for (int i = 0; i <= 5; i++) {
				for (int j = 0; j <= 4; j++) {
					if (shipDock.getGridPoint(i, j).getID() == 'C') {
						shipDock.getGridPoint(i, j).showImage();
					} else if (shipDock.getGridPoint(i, j).getID() == '█') {
						shipDock.getGridPoint(i, j).setFill(Color.WHITE);

					}
				}
			}
		}
	}

	public boolean getSpecialShotsCalled() {
		return specialShotsCalled;
	}

	public void losingInfo(Stage stage) {
		userBoard.setDisable(true);
		computerBoard.setDisable(true);
		float AIFinalShotPercentage = 0;
		int AIMaxHitsInARow = 0;
		float userFinalShotPercentage = GridPoint.getFinalShotPercentage();
		System.out.println("User Final Percentage: " + userFinalShotPercentage + "%");
		int userMaxHitsInARow = GridPoint.getMaxHitsInARow();
		System.out.println("User Max Hits In A Row: " + userMaxHitsInARow);
		if (difficulty == "easy") {
			AIFinalShotPercentage = EasyAI.getFinalShotPercentage();
			System.out.println("AI Final Percentage: " + AIFinalShotPercentage + "%");
			AIMaxHitsInARow = EasyAI.getAIMaxHitsInARow();
			System.out.println("AI Max Hits In A Row: " + AIMaxHitsInARow);
		} else {
			AIFinalShotPercentage = HardAI.getFinalShotPercentage();
			System.out.println("AI Final Percentage: " + AIFinalShotPercentage + "%");
			AIMaxHitsInARow = HardAI.getAIMaxHitsInARow();
			System.out.println("AI Max Hits In A Row: " + AIMaxHitsInARow);
		}
		gameMessage.setText("You Lost!");
		gameOverGUI = new GameOverGUI("You Lost!", userFinalShotPercentage, AIFinalShotPercentage, userMaxHitsInARow,
				AIMaxHitsInARow, stage);

		String loserPath = "src/songfiles/Loser.mp3";
		File loserFile = new File(loserPath);
		URI loserUri = loserFile.toURI();
		media = new Media(loserUri.toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
		stage.setScene(new Scene(gameOverGUI.getLayout(), 400, 400));
	}

	public void winningInfo(Stage stage) {
		userBoard.setDisable(true);
		computerBoard.setDisable(true);
		float AIFinalShotPercentage = 0;
		int AIMaxHitsInARow = 0;
		float userFinalShotPercentage = GridPoint.getFinalShotPercentage();
		System.out.println("User Final Percentage: " + userFinalShotPercentage + "%");
		int userMaxHitsInARow = GridPoint.getMaxHitsInARow();
		System.out.println("User Max Hits In A Row: " + userMaxHitsInARow);
		if (difficulty == "easy") {
			AIFinalShotPercentage = EasyAI.getFinalShotPercentage();
			System.out.println("AI Final Percentage: " + AIFinalShotPercentage + "%");
			AIMaxHitsInARow = EasyAI.getAIMaxHitsInARow();
			System.out.println("AI Max Hits In A Row: " + AIMaxHitsInARow);
		} else {
			AIFinalShotPercentage = HardAI.getFinalShotPercentage();
			System.out.println("AI Final Percentage: " + AIFinalShotPercentage + "%");
			AIMaxHitsInARow = HardAI.getAIMaxHitsInARow();
			System.out.println("AI Max Hits In A Row: " + AIMaxHitsInARow);
		}
		gameMessage.setText("You Won!");
		gameOverGUI = new GameOverGUI("You Won!", userFinalShotPercentage, AIFinalShotPercentage, userMaxHitsInARow,
				AIMaxHitsInARow, stage);
		String winnerPath = "src/songfiles/Winner.mp3";
		File winnerFile = new File(winnerPath);
		URI winnerUri = winnerFile.toURI();
		media = new Media(winnerUri.toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
		stage.setScene(new Scene(gameOverGUI.getLayout(), 400, 400));
	}

	public void getTutorialInfo() {
		tutorial.setText("Battleship is a type of strategy game that is played on a grid.\n"
				+ "Here are the steps to play the game:\n" + "\n" + "Setup:\n" + "\n"
				+ "Your grid is on the bottom of the screen and the opponent's grid is on the top. In this version, the grids are 10x6.\n"
				+ "\n"
				+ "You will see 5 ships on the ship dock, which you can see on the bottom right. The type of ships are:\n"
				+ "\n" + "- Aircraft Carrier (5 spaces)\n" + "- Battleship (4 spaces)\n" + "- Cruiser (3 spaces)\n"
				+ "- Submarine (3 spaces)\n" + "- Destroyer (2 spaces)\n" + "\n"
				+ "To place the ships onto the grid, you will need to click on one of the ships and then click on one of the grid points.\n"
				+ "\n"
				+ "You will see 1 or 2 yellow colored grid points, which determines if you want the ship to go vertical or horizontal.\n"
				+ "\n"
				+ "Click on one of the yellow grid points to decide vertical or horizontal, then do the same for the other ships.\n"
				+ "\n" + "Once you have placed all the ships, then you will need to click on the start button.\n" + "\n"
				+ "While playing the game, you will notice that you are given additional features that will help you play the game better. "
				+ "The extra features are listed below:\n" + "\nTutorial, ♫, Hint, and Special Shots\n"
				+ "\nThe tutorial button adds a pop-up window that explains the basic rules of the game. This is helpful for people who have "
				+ "never played Battleship and need a better understanding of what needs to be done.\n"
				+ "\nThe ♫ button mutes the background music. This is useful for people who want to play the game in a quiet environment, as "
				+ "the sound is not a necessity to play the game properly.\n"
				+ "\nThe hint button basically gives the player a hint on one of the tiles that has a ship on it. When the player clicks on this "
				+ "button, you will see a green tile on the opponent's board, which represents one of the ship tiles. This will help the player "
				+ "be able to figure out where the rest of the ship tiles are. This can only be done once\n"
				+ "\nThe special shots button allows the player to hit three tiles instead of one. When the player clicks on this button, two "
				+ "new buttons will pop up. These buttons are \"V\" and \"H\". These buttons determine if the shots are going to be vertical or "
				+ "horizontal. After this, select a tile as you normally do, and you will see multiple tiles being attacked. This can only be done "
				+ "once by the players and AI.\n\n"
				+ "Playing the Game:\n" + "\n"
				+ "Once you start the game, you can click on any of the grid points and decide which one to attack.\n"
				+ "\n"
				+ "Once you click on a grid point, your opponent (AI or Human) will be able to select one the grid points in your grid.\n"
				+ "\n" + "Hits and Misses:\n" + "\n" + "When you click on a grid point, 1 of 3 colors will show:\n"
				+ "\n" + "- Gray: This means that you hit the ocean and not a ship (miss)\n"
				+ "- Red: This means that you have hit a part of the ship (hit)\n"
				+ "- Visible Ship(Red squares gone): This means that you hit all parts of that ship, and it has been sunk (sink)\n" + "\n"
				+ "Goal:\n" + "\n" + "The goal is to sink all of the opponent's ships.\n" + "\n" + "Winning:\n" + "\n"
				+ "The game will continue until you or your opponent has sunk all the ships.\n" + "\n"
				+ "The player who sinks all their opponent's ships first wins the game!\n");
	}
}
