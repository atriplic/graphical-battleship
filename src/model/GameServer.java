package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import view_controller.GameStartGUI;
import view_controller.MultiPlayerOptionGUI;

public class GameServer implements Runnable {
	private ServerSocket ss;
	private int numPlayers;
	private ServerSideConnection player1;
	private ServerSideConnection player2;
	private Boolean player1Ready = false;
	private Boolean player2Ready = false;
	private int P1inputsReceived = 0;
	private int P2inputsReceived = 0;
	private Alert alert;
	private Stage stage;
	public boolean serverStartedSuccessfully = false;

	public GameServer(InetAddress ipAddress, int port, Stage stage) {
		System.out.println("---Game Server---");
		this.stage = stage;
		try {
			ss = new ServerSocket(port, 50, ipAddress);
			serverStartedSuccessfully = true;
			System.out.println(ss.getLocalSocketAddress());
			System.out.println(ss.getLocalPort());
		} catch (IOException e) {
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Could not start a server");
			alert.setHeaderText("Click cancel to exit application");
			alert.setContentText("To go back to Main Menu, click OK");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				GameStartGUI mainMenu = new GameStartGUI(stage, "Don't restart music");
				stage.setScene(new Scene(mainMenu.getLayout(), 450, 450));
				stage.setTitle("Battle Ship");
				stage.show();
			} else {
				Platform.exit();
				System.exit(0);
			}
			System.out.println("Could not connect");
		}
	}

	public void close() {
		try {
			if (ss != null) {
				ss.close();
				ss = null;
			}
			// System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			System.out.println("Waiting for connections...");
			while (numPlayers < 2) {
				Socket s = ss.accept();
				numPlayers++;
				System.out.println("Player #" + numPlayers + " has connected.");
				ServerSideConnection ssc = new ServerSideConnection(s, numPlayers);
				if (numPlayers == 1) {
					player1 = ssc;
				} else {
					player2 = ssc;
				}

				Thread thread = new Thread(ssc);
				thread.start();
			}
		} catch (IOException e) {
			System.out.println("Could not receive connections");
		}
	}
	/*
	 * public static void main(String[] args) { GameServer server = new
	 * GameServer(); server.acceptConnections(); }
	 */

	private class ServerSideConnection implements Runnable {

		private Socket socket;
		private ObjectInputStream dataIn;
		private ObjectOutputStream dataOut;
		private int playerID;
		boolean serverRunning = true;

		public ServerSideConnection(Socket socket, int id) {
			this.socket = socket;
			this.playerID = id;

			try {
				this.dataIn = new ObjectInputStream(this.socket.getInputStream());
				this.dataOut = new ObjectOutputStream(this.socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				this.dataOut.writeInt(playerID);
				this.dataOut.flush();

				while (serverRunning) {
					if (playerID == 1) {
						if (player1 != null) {
							if (P1inputsReceived == 0) {
								player1Ready = (boolean) dataIn.readObject();
								P1inputsReceived++;
							} else if (P1inputsReceived == 1 && player1Ready && player2 != null) {
								Ship[] ships = (Ship[]) dataIn.readObject();
								player2.sendShips(ships);
								System.out.println("Player 1's ships were read");
								P1inputsReceived++;
							} else if (player1Ready && player2 != null) {
								int[] shot = (int[]) dataIn.readObject();
								player2.sendShot(shot);
								P1inputsReceived++;
								System.out.println("Player 1's shot were read");
							}
						}

					} else {
						if (player2 != null) {
							if (P2inputsReceived == 0) {
								player1.sendOPJoined();
								P2inputsReceived++;
							} else if (P2inputsReceived == 1) {
								player2Ready = (boolean) dataIn.readObject();
								P2inputsReceived++;
							} else if (P2inputsReceived == 2 && player2Ready && player1 != null) {
								Ship[] ships = (Ship[]) dataIn.readObject();
								player1.sendShips(ships);
								P2inputsReceived++;
								System.out.println("Player 2's ships were read");
							} else if (player2Ready && player1 != null) {
								int[] shot = (int[]) dataIn.readObject();
								player1.sendShot(shot);
								P2inputsReceived++;
								System.out.println("Player 1's ships were read");
							}
						}
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				if (ss != null) {
					if (playerID == 1) {
						serverRunning = false;
					}
					Platform.runLater(() -> {
						alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Opponent Lost Connection to the Server");
						alert.setHeaderText("Click cancel to exit application");
						alert.setContentText("To go back to Main Menu, click OK");
						Optional<ButtonType> result = alert.showAndWait();
						try {
							ss.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (result.get() == ButtonType.OK) {
							GameStartGUI mainMenu = new GameStartGUI(stage, "Don't restart music");
							stage.setScene(new Scene(mainMenu.getLayout(), 450, 450));
							stage.setTitle("Battle Ship");
							stage.show();
						} else {
							Platform.exit();
							System.exit(0);
						}
					});
				}
			}
		}

		public void sendShot(int[] lastStrike) {
			try {
				this.dataOut.writeObject(lastStrike);
				this.dataOut.flush();
			} catch (IOException e) {
				if (ss != null) {
					if (playerID == 1) {
						serverRunning = false;
					}
					Platform.runLater(() -> {
						alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Opponent Lost Connection to Server");
						alert.setHeaderText("Click cancel to exit application");
						alert.setContentText("To go back to Main Menu, click OK");
						Optional<ButtonType> result = alert.showAndWait();
						try {
							ss.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (result.get() == ButtonType.OK) {
							GameStartGUI mainMenu = new GameStartGUI(stage, "Don't restart music");
							stage.setScene(new Scene(mainMenu.getLayout(), 450, 450));
							stage.setTitle("Battle Ship");
							stage.show();
						} else {
							Platform.exit();
							System.exit(0);
						}
					});
				}
			}
		}

		public void sendShips(Ship[] ships) {
			try {
				System.out.println("Sending Ships SSC");
				this.dataOut.writeObject(ships);
				this.dataOut.flush();
			} catch (IOException e) {
				if (ss != null) {
					if (playerID == 1) {
						serverRunning = false;
					}
					Platform.runLater(() -> {
						alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Opponent Lost Connection to Server");
						alert.setHeaderText("Click cancel to exit application");
						alert.setContentText("To go back to Main Menu, click OK");
						Optional<ButtonType> result = alert.showAndWait();
						try {
							ss.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (result.get() == ButtonType.OK) {
							GameStartGUI mainMenu = new GameStartGUI(stage, "Don't restart music");
							stage.setScene(new Scene(mainMenu.getLayout(), 450, 450));
							stage.setTitle("Battle Ship");
							stage.show();
						} else {
							Platform.exit();
							System.exit(0);
						}
					});
				}
			}
		}

		public void sendOPJoined() {
			try {
				System.out.println("Opponent Joined //SSC" + playerID);
				this.dataOut.writeObject(true);
				this.dataOut.flush();
			} catch (IOException e) {
				if (ss != null) {
					if (playerID == 1) {
						serverRunning = false;
					}
					Platform.runLater(() -> {
						alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Opponent Lost Connection to Server");
						alert.setHeaderText("Click cancel to exit application");
						alert.setContentText("To go back to Main Menu, click OK");
						Optional<ButtonType> result = alert.showAndWait();
						try {
							ss.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (result.get() == ButtonType.OK) {
							GameStartGUI mainMenu = new GameStartGUI(stage, "Don't restart music");
							stage.setScene(new Scene(mainMenu.getLayout(), 450, 450));
							stage.setTitle("Battle Ship");
							stage.show();
						} else {
							Platform.exit();
							System.exit(0);
						}
					});
				}
			}
		}

	}
}
