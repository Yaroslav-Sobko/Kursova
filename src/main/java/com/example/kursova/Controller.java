package com.example.kursova;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.text.ParseException;
import java.util.*;

public class Controller implements Initializable {

	public TextField fieldX;
	public TextField fieldY;
	public TextField containerX;
	public TextField containerY;
	public Rectangle mainContainer;
	@FXML
	private VBox rectangleContainer;

	private ObservableList<Rectangle> rectangleList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		Rectangle rectangle1 = new Rectangle(100, 100);
		Rectangle rectangle2 = new Rectangle(150, 150);
		Rectangle rectangle3 = new Rectangle(200, 200);

		rectangleList.addAll(rectangle1, rectangle2, rectangle3);

		for (Rectangle rectangle : rectangleList) {
			makeDraggable(rectangle);
		}

		rectangleContainer.getChildren().addAll(rectangleList);

		for (Rectangle rectangle : rectangleList) {
			makeDraggable(rectangle);

			rectangle.setOnMouseClicked(mouseEvent -> {
				resetSelection();
				rectangle.setEffect(new DropShadow());
				mouseEvent.consume();
			});
		}

	}

	private void resetSelection() {
		for (Rectangle rectangle : rectangleList) {
			rectangle.setEffect(null);
		}
	}


	private void makeDraggable(Rectangle rectangle) {
		final Delta dragDelta = new Delta();

		rectangle.setOnMousePressed(mouseEvent -> {
			dragDelta.x = rectangle.getTranslateX() - mouseEvent.getSceneX();
			dragDelta.y = rectangle.getTranslateY() - mouseEvent.getSceneY();
		});

		rectangle.setOnMouseDragged(mouseEvent -> {
			rectangle.setTranslateX(mouseEvent.getSceneX() + dragDelta.x);
			rectangle.setTranslateY(mouseEvent.getSceneY() + dragDelta.y);
		});
	}

	public void buttonAdd(ActionEvent actionEvent) throws ParseException {
		double newRecWidth = Double.parseDouble(fieldX.getText());
		double newRecHeight = Double.parseDouble(fieldY.getText());
		if ( newRecWidth >= 10 && newRecWidth <= 350 && newRecHeight >= 10 && newRecHeight <= 350) {
			Rectangle rectangle = new Rectangle(newRecWidth, newRecHeight);
			rectangle.setFill(Color.color(Math.random(), Math.random(), Math.random()));
			makeDraggable(rectangle);
			rectangleList.add(rectangle);
			for (Rectangle rectangle1 : rectangleList) {
				makeDraggable(rectangle1);

				rectangle1.setOnMouseClicked(mouseEvent -> {
					resetSelection();
					rectangle1.setEffect(new DropShadow());
					mouseEvent.consume();
				});
			}
			rectangleContainer.getChildren().add(rectangle);
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Непривильно введений розмір!");
			alert.showAndWait();
		}
	}

	public void buttonRemove(ActionEvent actionEvent) {
		Rectangle selectedRectangle = (Rectangle) rectangleContainer.getChildren().stream()
			.filter(node -> node.getEffect() != null)
			.findFirst()
			.orElse(null);

		if (selectedRectangle != null) {
			rectangleContainer.getChildren().remove(selectedRectangle);
		}

	}

	public void changeContainer(ActionEvent actionEvent) {
		double newWidth = Double.parseDouble(containerX.getText());
		double newHeight = Double.parseDouble(containerY.getText());
		if ( newWidth >= 350 && newWidth <= 1000 && newHeight >= 350 && newHeight <= 3000) {
			mainContainer.setWidth(newWidth);
			mainContainer.setHeight(newHeight);
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Непривильно введений розмір!");
			alert.showAndWait();
		}

	}

	public void rotateRectangle(ActionEvent actionEvent) {
		Rectangle selectedRectangle = (Rectangle) rectangleContainer.getChildren().stream()
			.filter(node -> node.getEffect() != null)
			.findFirst()
			.orElse(null);

		if (selectedRectangle != null) {
			double prevWidth = selectedRectangle.getWidth();
			double prevHeight = selectedRectangle.getHeight();
			selectedRectangle.setWidth(prevHeight);
			selectedRectangle.setHeight(prevWidth);
		}


	}


	public void findSolution(ActionEvent actionEvent) {
		int count = rectangleContainer.getChildren().size();
		double maxY = 0;
		double maxX = 0;
		double maxXCommon = 0;
		double maxYCommon = 0;
		double yLast = 0;
		double yForCheck = 0;
		double startX = 841 - (14 + mainContainer.getWidth()) + 701;
		for (int i = 0; i < count; i++) {
			Rectangle currentRec = (Rectangle) rectangleContainer.getChildren().get(i);
			if (yForCheck + maxY + currentRec.getHeight() < mainContainer.getHeight()) {
				rectangleContainer.getChildren().get(i).setTranslateX(-(startX + maxXCommon));
				rectangleContainer.getChildren().get(i).setTranslateY(-maxYCommon + yLast);
				maxY += currentRec.getHeight();
				if (maxX < currentRec.getWidth()) {
					maxX = currentRec.getWidth();
				}

			} else {
				maxXCommon += maxX;
				maxYCommon += maxY;
				rectangleContainer.getChildren().get(i).setTranslateX(-(startX + maxXCommon));
				rectangleContainer.getChildren().get(i).setTranslateY(-maxYCommon);
				maxYCommon += currentRec.getHeight();
				yLast = currentRec.getHeight();
				yForCheck = currentRec.getHeight();
				maxY = 0;
				maxX = 0;
			}
		}
		if (maxXCommon > mainContainer.getWidth()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Ширина об'єктів завелика!");
			alert.showAndWait();
		}
	}


	private class Delta {
		double x;
		double y;
	}
}

