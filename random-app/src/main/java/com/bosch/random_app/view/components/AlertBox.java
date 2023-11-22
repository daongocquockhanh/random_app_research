package com.bosch.random_app.view.components;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	
	private static String RESULT_STRING = "";

	public static void display(String title, String message) {
		Stage window = new Stage();
		Button closeButton = new Button("Close");
		Label label = new Label();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(300);
		if (title.equals("Current Data Random")) {
			if (message.equals(RESULT_STRING)) {
				label.setText("Data is being duplicated");
				closeButton.setOnAction(e -> window.close());
			} else {
				RESULT_STRING = message;
				label.setText("Wheel stopped at: " + message);
				closeButton.setOnAction(e -> window.close());
			}
		} else {
			label.setText("Data is being duplicated");
			closeButton.setOnAction(e -> window.close());
		}
		
		VBox layout = new VBox();
		layout.getChildren().addAll(label, closeButton);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
	
}
