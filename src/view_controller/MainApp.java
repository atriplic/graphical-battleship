package view_controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Extends BattleShip game to a JavaFX application
 * 
 * Contributors: Sohan Bhakta
 */

public class MainApp extends Application {

	@Override
	public void start(Stage primaryStage) {
		// "Not playing means there is not background music currently playing
		// Variable needed for restarting game
		GameStartGUI gameStartGUI = new GameStartGUI(primaryStage, "Play music");
		primaryStage.setScene(new Scene(gameStartGUI.getLayout(), 450, 450));
		primaryStage.setTitle("Battle Ship");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}