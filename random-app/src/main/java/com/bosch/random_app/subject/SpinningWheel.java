package com.bosch.random_app.subject;

import java.util.ArrayList;
import java.util.List;

import com.bosch.random_app.observer.WheelObserver;
import com.bosch.random_app.view.components.AlertBox;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class SpinningWheel {
	private List<WheelObserver> observers = new ArrayList<>();
	private static List<String> LIST_ELEMENTS = new ArrayList<>();
	private int currentSegmentIndex = -1;
	private static Canvas canvas;
	private static int centerX = 0;
	private static int centerY = 0;
	private static int radius = 0;
	
	public SpinningWheel(Canvas canvas, int centerX, int centerY, int radius) {
		LIST_ELEMENTS.add("Input Data");
		SpinningWheel.canvas = canvas;
		SpinningWheel.centerX = centerX;
		SpinningWheel.centerY = centerY;
		SpinningWheel.radius = radius;
		drawWheel(canvas.getGraphicsContext2D(), centerX, centerY, radius, LIST_ELEMENTS);
	}
	
	public void addObserver(WheelObserver observer) {
		this.observers.add(observer);
	}
	
	public void removeObserver(WheelObserver observer) {
		this.observers.remove(observer);
	}
	
	public List<String> getListElements() {
		return LIST_ELEMENTS;
	}
	
	private void notifyObservers() {
        if (currentSegmentIndex != -1) {
            String segmentText = LIST_ELEMENTS.get(currentSegmentIndex - 1);
            for (WheelObserver observer : observers) {
                observer.onWheelStopped(segmentText);
            }
        }
    }
	
	public static void addElement(String data) {
		if (LIST_ELEMENTS.get(0).equals("Input Data")) {
			LIST_ELEMENTS.remove(0);
		}
		if (LIST_ELEMENTS.contains(data)) {
			AlertBox.display("Data is duplicated", data);
			return;
		}
		LIST_ELEMENTS.add(data);
		refreshWheel();
	}
	
	private static void refreshWheel() {
        drawWheel(canvas.getGraphicsContext2D(), centerX, centerY, radius, LIST_ELEMENTS);
	}
	
	public static void removeElement(String data) {
		LIST_ELEMENTS.remove(data);
		if (LIST_ELEMENTS.size() == 0) {
			LIST_ELEMENTS.add("Input Data");
		}
		refreshWheel();
	}

    public void stopWheelAtSegment(int segmentIndex) {
        this.currentSegmentIndex = segmentIndex;
        notifyObservers();
        refreshWheel();
    }
    
    private static void drawWheel(GraphicsContext gc, int centerX, int centerY, int radius, List<String> listElements) {
    	int numSegments = listElements.size();
    	double arcExtent = 360.0 / numSegments;
    	double fontSize = 14;
    	Font font = new Font(fontSize);
    	Text textNode = new Text();
        for (int i = 0; i < numSegments; i++) {
            // Set color for each segment
            gc.setFill(Color.BLACK);
            // Calculate segment angles for clockwise rotation
            // Since JavaFX's 0 degrees is at the 3 o'clock position, we subtract from 360 to rotate clockwise
            double startAngle = 360 - (arcExtent * i);
            // Draw segment
            gc.fillArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, arcExtent, javafx.scene.shape.ArcType.ROUND);
            gc.setStroke(Color.WHITE);
            gc.strokeArc(centerX - radius, centerY - radius, radius * 2, radius * 2, startAngle, arcExtent, javafx.scene.shape.ArcType.ROUND);
            
            // Add text labels in a clockwise direction
            String text = listElements.get(i);
            textNode.setText(text);
            
            double textWidth = textNode.getLayoutBounds().getWidth();

            // Decrease font size until the text fits within the segment
            while (textWidth > radius && fontSize > 0) {
                fontSize--;
                font = new Font(font.getName(), fontSize);
                textNode.setFont(font);
                textWidth = textNode.getLayoutBounds().getWidth();
            }
            
            gc.setFill(Color.WHITE);
            gc.setFont(font);
            // Calculate the angle for the text, adjusting for the clockwise direction
            double textAngle = 360 - (startAngle + arcExtent / 2);
            double textRadius = radius - 50; // Adjust as needed
            // Calculate the text's position
            double textX = centerX + textRadius * Math.cos(Math.toRadians(textAngle));
            double textY = centerY + textRadius * Math.sin(Math.toRadians(textAngle));
            
            // Save the current state of the graphics context
            gc.save();
            // Translate to the text's position
            gc.translate(textX, textY);
            // Rotate to make the text upright
            gc.rotate(textAngle); // Adjust the rotation to make text upright
            // Draw the text at (0, 0) since we've translated to the correct position
            gc.fillText(text, -textWidth / 2, font.getSize() / 4);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            // Restore the graphics context to its original state
            gc.restore();
        }
    }
}
