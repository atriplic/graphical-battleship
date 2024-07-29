package view_controller;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MultiPlayerOptionGUI {
	String imagePath = "src/resources/gameBackground.jpeg";
	String imagePath2 = "src/resources/battleship_banner.png";
	
	private Button host;
	private Button join;
	private Stage primaryStage;
	private BorderPane layout;
	private HBox ipBox;
	private HBox portBox;
	private VBox fields;
	private Label portLabel;
	private Label ipLabel;
	private TextField portField;
	private TextField ipField;
	private GameStartGUI GUI;
	
	public MultiPlayerOptionGUI(Stage primaryStage, GameStartGUI GUI){
		this.primaryStage = primaryStage; // Store the primary stage reference
		this.GUI = GUI;
		
		createBoxes();
		fields = new VBox();
		host = new Button("Host");
		join = new Button("Join");
		

		host.setStyle("-fx-background-image: url('resources/ButtonTile.jpeg'); "
		        + "-fx-background-size: cover; "
		        + "-fx-border-color: white; "
		        + "-fx-border-width: 2; "
		        + "-fx-text-fill: #ffffff; "
		        + "-fx-font-weight: bold; "
		        + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 5);");
		join.setStyle("-fx-background-image: url('resources/ButtonTile.jpeg'); "
		        + "-fx-background-size: cover; "
		        + "-fx-border-color: white; "
		        + "-fx-border-width: 2; "
		        + "-fx-text-fill: #ffffff; "
		        + "-fx-font-weight: bold; "
		        + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 5);");
		
        host.setFont(new Font("Helvetica", 18));
        host.setMinSize(150, 50);
        host.setMaxSize(150, 50);
        join.setFont(new Font("Helvetica", 18));
        join.setMinSize(150, 50);
        join.setMaxSize(150, 50);
		
		HBox buttonLayout = new HBox(40);
		buttonLayout.setAlignment(Pos.CENTER);
		buttonLayout.getChildren().addAll(host, join);
		
		fields.getChildren().addAll(ipBox, portBox, buttonLayout);
		fields.setAlignment(Pos.CENTER);
		fields.setSpacing(10);
		
		layout = new BorderPane();

		layout.setCenter(fields);

		layout.setPadding(new Insets(80, 0, 0, 0));

		Image image = new Image(new File(imagePath).toURI().toString());
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

		layout.setBackground(new Background(backgroundImage));

		registerHandlers();
	}
	
	public BorderPane getLayout() {
		return layout;
	}
	
	private void registerHandlers() {
		host.setOnAction((event) -> {
			try {
				InetAddress ipAddress = InetAddress.getByName(ipField.getText());
				int port = Integer.parseInt(portField.getText());
				startBoardGUI("PVP", port, ipAddress, true, primaryStage);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		join.setOnAction((event) -> {
			try {
				InetAddress ipAddress = InetAddress.getByName(ipField.getText());
				int port = Integer.parseInt(portField.getText());
				startBoardGUI("PVP", port, ipAddress, false, primaryStage);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	private void startBoardGUI(String difficulty, int port, InetAddress ipAddress, boolean host, Stage stage) {
		// Start the BoardGUI
		PVPBoardGUI boardGUI = new PVPBoardGUI(difficulty, port, ipAddress, host, GUI, stage);
		System.out.println("Board Made");
		try {
			boardGUI.start(primaryStage); // Start the BoardGUI using the primary stage
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createBoxes() {
		ipBox = new HBox();
		ipLabel = new Label("Enter an IP address: ");
		ipField = new TextField("localhost");
		portBox = new HBox();
		portLabel = new Label("Enter a Port #: ");
		portField = new TextField("22222");
		
		ipLabel.setFont(new Font("Helvetica", 18));
		portLabel.setFont(new Font("Helvetica", 18));
		ipLabel.setTextFill(Color.WHITE);
		portLabel.setTextFill(Color.WHITE);
		
		ipBox.getChildren().addAll(ipLabel, ipField);
		portBox.getChildren().addAll(portLabel, portField);
		
		ipBox.setSpacing(5);
		portBox.setSpacing(5);
		
		ipBox.setAlignment(Pos.CENTER);
		portBox.setAlignment(Pos.CENTER);
	}
}
