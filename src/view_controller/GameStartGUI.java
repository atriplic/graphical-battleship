package view_controller;


import java.io.File;
import java.net.URI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Extends BattleShip game to a JavaFX application
 * 
 * Contributors: Sohan Bhakta
 */
public class GameStartGUI {
	String imagePath = "src/resources/gameBackground.jpeg";
	String imagePath2 = "src/resources/battleship_banner.png";
	
	private Button easy;
	private Button hard;
	private Button multiplayer;
	private BorderPane layout;
	private Stage primaryStage; // Reference to the primary stage
	public static MediaPlayer mediaPlayer;
	private Media media;

	public GameStartGUI(Stage primaryStage, String isMediaPlaying) {
		this.primaryStage = primaryStage; // Store the primary stage reference
		
		Image titleImage = new Image(new File(imagePath2).toURI().toString());
		ImageView titleImageView = new ImageView(titleImage);
		
		titleImageView.setFitWidth(300); // You can adjust this width as needed
        titleImageView.setFitHeight(100); // You can adjust this height as needed
        titleImageView.setPreserveRatio(true);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.BLACK); // You can adjust the color as needed

        // Apply the shadow effect to the ImageView
        titleImageView.setEffect(dropShadow);


		easy = new Button("Easy");
		hard = new Button("Hard");
		multiplayer = new Button("MultiPlayer");
        ////////////////////////////////////
        // Set a retro-style font
		easy.setStyle("-fx-background-image: url('resources/ButtonTile.jpeg'); "
		        + "-fx-background-size: cover; "
		        + "-fx-border-color: white; "
		        + "-fx-border-width: 2; "
		        + "-fx-text-fill: #ffffff; "
		        + "-fx-font-weight: bold; "
		        + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 5);");

		hard.setStyle("-fx-background-image: url('resources/ButtonTile.jpeg'); "
		        + "-fx-background-size: cover; "
		        + "-fx-border-color: white; "
		        + "-fx-border-width: 2; "
		        + "-fx-text-fill: #ffffff; "
		        + "-fx-font-weight: bold; "
		        + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 5);");

		multiplayer.setStyle("-fx-background-image: url('resources/ButtonTile.jpeg'); "
		        + "-fx-background-size: cover; "
		        + "-fx-border-color: white; "
		        + "-fx-border-width: 2; "
		        + "-fx-text-fill: #ffffff; "
		        + "-fx-font-weight: bold; "
		        + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 5);");


        ////////////////////////////////////
		
		HBox topWrapper = new HBox();
        topWrapper.setAlignment(Pos.CENTER); // Center alignment
        topWrapper.getChildren().add(titleImageView);
		
		
        easy.setFont(new Font("Helvetica", 18));
        easy.setMinSize(150, 50);
        easy.setMaxSize(150, 50);
        hard.setFont(new Font("Helvetica", 18));
        hard.setMinSize(150, 50);
        hard.setMaxSize(150, 50);
        multiplayer.setFont(new Font("Helvetica", 18));
        multiplayer.setMinSize(150, 50);
        multiplayer.setMaxSize(150, 50);


        easy.setPrefSize(150, 20);
        hard.setPrefSize(150, 20);
        multiplayer.setPrefSize(150, 20);

        easy.setAlignment(Pos.CENTER);
        hard.setAlignment(Pos.CENTER);
        multiplayer.setAlignment(Pos.CENTER);


		HBox buttonLayout = new HBox(40);
		buttonLayout.setAlignment(Pos.CENTER);
		buttonLayout.getChildren().addAll(easy, hard);
		
		VBox allButtons = new VBox(20);
		allButtons.setAlignment(Pos.CENTER);
		allButtons.getChildren().addAll(buttonLayout, multiplayer);

		layout = new BorderPane();
		layout.setTop(topWrapper);
		layout.setCenter(allButtons);

		layout.setPadding(new Insets(80, 0, 0, 0));

		// Background image
		
		Image image = new Image(new File(imagePath).toURI().toString());
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

		layout.setBackground(new Background(backgroundImage));

		registerHandlers();
		
//		// Background music
//        if (isMediaPlaying == "Playing") {	// For when game is restarted, stop and restart background music
//            mediaPlayer.stop();
//        }

		if (isMediaPlaying != "Don't restart music") {
			String backgroundPath = "src/songfiles/BackgroundMusic.mp3";
			File backgroundFile = new File(backgroundPath);
			URI backgroundUri = backgroundFile.toURI();
			media = new Media(backgroundUri.toString());
			mediaPlayer = new MediaPlayer(media);

			// Play background music repeatedly
			mediaPlayer.setOnEndOfMedia(new Runnable() {
				@Override
				public void run() {
					mediaPlayer.seek(Duration.ZERO);
				}
			});
			mediaPlayer.play();
		}

	}
	
	public void toggleMute() {
        if (mediaPlayer.isMute()) {
            mediaPlayer.setMute(false); // Unmute
        } else {
            mediaPlayer.setMute(true); // Mute
        }
	}

	public BorderPane getLayout() {
		return layout;
	}

	private void registerHandlers() {
		easy.setOnAction((event) -> {
			// Start a new game with easy AI
			startBoardGUI("easy");
		});
		hard.setOnAction((event) -> {
			// Start a new game with hard AI
			startBoardGUI("hard");
		});
		multiplayer.setOnAction((event) -> {
			MultiPlayerOptionGUI MPOG = new MultiPlayerOptionGUI(primaryStage, this);
			primaryStage.setScene(new Scene(MPOG.getLayout(), 450, 450));
			primaryStage.setTitle("Battle Ship");
			primaryStage.show();
		});
	}

	private void startBoardGUI(String difficulty) {
		// Start the BoardGUI
		BoardGUI boardGUI = new BoardGUI(difficulty, this);
		try {
			boardGUI.start(primaryStage); // Start the BoardGUI using the primary stage
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
