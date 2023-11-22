package com.bosch.random_app;

import java.util.List;

import com.bosch.random_app.observer.WheelObserverImpl;
import com.bosch.random_app.subject.SpinningWheel;

import javafx.animation.AnimationTimer;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainApp extends Application {
	private double cumulativeRotation = 0;
	private long lastUpdateTime = System.nanoTime();
	private AnimationTimer timer;
	
	@Override
    public void start(Stage primaryStage) {
        // Create a Canvas
        Canvas canvas = new Canvas(300, 300);
        SpinningWheel wheel = new SpinningWheel(canvas, 150, 150, 125);
        wheel.addObserver(new WheelObserverImpl());

        // Create a RotateTransition
        RotateTransition rotate = new RotateTransition(Duration.seconds(0.2), canvas);
        rotate.setByAngle(360);
        rotate.setCycleCount(RotateTransition.INDEFINITE);
        rotate.setAutoReverse(false);
        
        timer = new AnimationTimer() {
        @Override
	        public void handle(long now) {
	            long currentTime = System.nanoTime();
	            double elapsedSeconds = (currentTime - lastUpdateTime) / 1_000_000_000.0; // Convert nanoseconds to seconds
	            lastUpdateTime = currentTime;
	
	            // Assuming the wheel rotates at a constant speed of 360 degrees per second
	            double rotationSpeedDegreesPerSecond = 360;
	            double rotationThisFrame = rotationSpeedDegreesPerSecond * elapsedSeconds;
	            System.out.println("Rotation This Frame: " + rotationThisFrame);
	            cumulativeRotation += rotationThisFrame;
	            cumulativeRotation %= 360; // Keep the rotation in the range of [0, 360)
	
	            // Optional: Output the cumulative rotation for debugging
	            System.out.println("Cumulative Rotation: " + cumulativeRotation);
	        }
        };
        
        Button rotateButton = new Button("Rotate");
        rotateButton.setLayoutX(100);
        rotateButton.setLayoutY(300);

        rotateButton.setOnAction(e -> {
            // Start the rotation
            rotate.play();
            timer.start();
            
        });
        
        Button stopButton = new Button("Stop");
        stopButton.setLayoutX(150);
        stopButton.setLayoutY(300);

        stopButton.setOnAction(e -> {
            // Stop the rotation
            rotate.stop();
            timer.stop();

            // Calculate the current segment based on the cumulativeRotation  
            int currentSegment = getCurrentSegment(cumulativeRotation, wheel.getListElements());
            wheel.stopWheelAtSegment(currentSegment);
        });
        
//        Polygon pointer = new Polygon();
//        double pointerWidth = 20;
//        double pointerHeight = 30;
//        double pointerCenterX = canvas.getWidth() / 2;
//        double pointerCenterY = canvas.getHeight() / 2 - 100; // Adjust this to position the pointer above the wheel
//        pointer.getPoints().addAll(new Double[]{
//            pointerCenterX - pointerWidth / 2, pointerCenterY,
//            pointerCenterX + pointerWidth / 2, pointerCenterY,
//            pointerCenterX, pointerCenterY + pointerHeight
//        });
//        pointer.setFill(Color.WHITE);

        TextField field = new TextField();
        field.setText("");
        field.setMinWidth(100);
        field.setMinHeight(10);
        field.setLayoutX(350);
        field.setLayoutY(50);
        
        Button addElementButton = new Button("Add");
        addElementButton.setLayoutX(350);
        addElementButton.setLayoutY(80);

        addElementButton.setOnAction(e -> {
            SpinningWheel.addElement(field.getText());
        });
        
        // Add canvas to the scene and show the stage
        Group root = new Group(canvas, stopButton, rotateButton, field, addElementButton);
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setTitle("Spinning Wheel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private int getCurrentSegment(double rotation, List<String> listData) {
    	int numSegments = listData.size();
    	double normalizedRotation = (360 - rotation) % 360;
        if (normalizedRotation < 0) normalizedRotation += 360;

        // Determine the angle size of each segment
        double segmentAngleSize = 360.0 / numSegments;

        // Calculate the segment index
        int segmentIndex = (int)(normalizedRotation / segmentAngleSize);

        // Adjust for clockwise rotation and 1-based indexing
        int currentSegment = (numSegments - segmentIndex) % numSegments;
        return currentSegment == 0 ? numSegments : currentSegment;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
