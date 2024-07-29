package view_controller;

import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Extends BattleShip game to a JavaFX application
 * 
 * Contributors: Sohan Bhakta, Atul Triplicane
 */
public class GameOverGUI {
	private VBox endPage;
	private Button restart;
	private Button exit;
	private Label label;
	private Label userFinalShotPercentageLabel;
	private Label AIFinalShotPercentageLabel;
	private Label userMaxHitsInARowLabel;
	private Label AIMaxHitsInARowLabel;
	private Label credits;
	private Stage primaryStage; // Reference to the primary stage

	public GameOverGUI(String message, float userFinalShotPercentage, float AIFinalShotPercentage,
			int userMaxHitsInARow, int AIMaxHitsInARow, Stage primaryStage) {
		this.primaryStage = primaryStage; // Store the primary stage reference
		label = new Label(message);
		label.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
		String strUserFinalShotPercentage = String.valueOf(userFinalShotPercentage);
		String strAIFinalShotPercentage = String.valueOf(AIFinalShotPercentage);
		String strUserMaxHitsInARow = String.valueOf(userMaxHitsInARow);
		String strAIMaxHitsInARow = String.valueOf(AIMaxHitsInARow);
		userFinalShotPercentageLabel = new Label("User Shot Percentage: " + strUserFinalShotPercentage + "%");
		AIFinalShotPercentageLabel = new Label("Opponent Shot Percentage: " + strAIFinalShotPercentage + "%");
		userMaxHitsInARowLabel = new Label("User Max Hits In A Row: " + strUserMaxHitsInARow);
		AIMaxHitsInARowLabel = new Label("Opponent Max Hits In A Row: " + strAIMaxHitsInARow);
		credits = new Label("                                           Game Developers:" + "\n" + "Dawson Szarek, Angela Mankin, Atul Triplicane, and Sohan Bhakta");
		endPage = new VBox();
		endPage.setAlignment(Pos.CENTER);
		endPage.setSpacing(10);
		restart = new Button("Restart");
		restart.setStyle("-fx-background-image: url('resources/ButtonTile.jpeg'); "
		        + "-fx-background-size: cover; "
		        + "-fx-border-color: white; "
		        + "-fx-border-width: 2; "
		        + "-fx-text-fill: #ffffff; "
		        + "-fx-font-weight: bold; "
		        + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 5);");
		exit = new Button("Exit");
		exit.setStyle("-fx-background-image: url('resources/ButtonTile.jpeg'); "
		        + "-fx-background-size: cover; "
		        + "-fx-border-color: white; "
		        + "-fx-border-width: 2; "
		        + "-fx-text-fill: #ffffff; "
		        + "-fx-font-weight: bold; "
		        + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 5);");
		label.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-background-color: blue; -fx-text-fill: white;");
	    userFinalShotPercentageLabel.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
	    AIFinalShotPercentageLabel.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
	    userMaxHitsInARowLabel.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
	    AIMaxHitsInARowLabel.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
	    credits.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
		endPage.getChildren().addAll(label, userFinalShotPercentageLabel, AIFinalShotPercentageLabel,
				userMaxHitsInARowLabel, AIMaxHitsInARowLabel, restart, exit, credits);
		String imagePath = "src/resources/resizedOceanTile.png";
		Image image = new Image(new File(imagePath).toURI().toString());
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		endPage.setBackground(new Background(backgroundImage));
		registerHandlers();
	}

	private void registerHandlers() {
		restart.setOnAction((event) -> {
			startBoardGUI();
		});
		exit.setOnAction((event) -> {
			System.exit(0);
		});
	}

	public VBox getLayout() {
		return endPage;
	}

	private void startBoardGUI() {
		// Start the BoardGUI
		GameStartGUI gameStartGUI = new GameStartGUI(primaryStage, "Don't restart music");
		try {
			primaryStage.setScene(new Scene(gameStartGUI.getLayout(), 450, 450));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
