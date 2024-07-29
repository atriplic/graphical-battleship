package model;

import java.io.File;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TestExplosion extends Application{
	
	public BorderPane pane;
	Canvas canvas;
	public GraphicsContext gc;	
	Image explosionSheet;


	public static void main(String[] args) {
		launch(args);
	}
	
	public void start (Stage stage) {
		pane = new BorderPane();
		canvas = new Canvas(400, 400);
		pane.setCenter(canvas);
		gc = canvas.getGraphicsContext2D();
		
		explosionSheet = new Image("file:images/newExplosion.png", false);
//		Image explosionSheet = new Image("file:songfiles/StartPic.jpeg", false);
		
//		String imagePath = "images/sprite-sheet-bomb.png";
//		Image image = new Image(new File(imagePath).toURI().toString());
		

//		gc.fillRect(0, 0, 200, 200);
		gc.drawImage(explosionSheet, 10, 10);
		
		Scene scene = new Scene(pane, 500, 500);
		stage.setScene(scene);
		stage.show();
		
		
	}

}
