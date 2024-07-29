package model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class ExplosionAnimation {

	private Pane pane;
	private GraphicsContext gc;
	private Image explosionSheet;
	private Timeline timeline;
	private static final int SPRITES_COUNT = 6;
	private String animationEvent;

	public ExplosionAnimation(Canvas canvas, String animationEvent) {
		
		this.animationEvent = animationEvent;
		
		this.gc = canvas.getGraphicsContext2D();
		
		if ("Splash".equals(animationEvent)) {
			this.explosionSheet = new Image("file:images/waterPlop.png", false);
		}
		
		if ("Explosion".equals(animationEvent)) {
			this.explosionSheet = new Image("file:images/newExplosion.png", false);
		}
		
		setUpTimeline();
	}

	private void setUpTimeline() {
		// How long each frame should be displayed
		double frameDuration = 200;

		KeyFrame keyFrame = new KeyFrame(Duration.millis(frameDuration), new FrameUpdater());
		timeline = new Timeline(keyFrame);
		timeline.setCycleCount(SPRITES_COUNT); // # of frames in sprite sheet

	}

	public void startExplosionAnimation() {
		timeline.playFromStart();
	}

	private class FrameUpdater implements EventHandler<ActionEvent> {
		private int currentFrame = 0;
//		double sx = 0, sy = 0, sw = (int) explosionSheet.getWidth() / 6, sh = explosionSheet.getHeight();
//		double dx = 0, dy = 0, dw = 0, dh = 0;

		@Override
		public void handle(ActionEvent event) {
			
			int totalFrames = SPRITES_COUNT; // # of frames in sprite sheet
			int spriteWidth = (int) explosionSheet.getWidth() / totalFrames;
			int sx = currentFrame * spriteWidth; // source x based on the current frame

			// Clear the canvas and draw the new frame
			gc.clearRect(0, 0, 40, 40);
			gc.drawImage(explosionSheet, sx, 0, spriteWidth, explosionSheet.getHeight(), 0, 0, 40, 40);

			if (currentFrame >= totalFrames - 1) {
				timeline.stop();
				if (animationEvent.equals("Splash")) {
					gc.clearRect(0, 0, 40, 40);
				}
			} else {
				currentFrame++;
			}
		}
	}
	

}
