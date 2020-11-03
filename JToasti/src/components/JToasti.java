package components;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * @author GingerBread
 * @version 2.0
 */
public class JToasti extends VBox
{
	private OnUndoClick undoClickEvent;
	private Image informationImage;
	private Image successImage;
	private Image warningImage;
	private Image undoWarningImage;
	private double lifeTime;
	private double toastPrefHeight;
	
	/**
	 * 	Constructs a simple toast container with a life time of five seconds
	 */
	public JToasti()
	{
		this(5d);
	}
	
	/**
	 * @param lifeTime
	 * @description
	 * Constructs a toast container with the specified life time
	 */
	public JToasti(double lifeTime)
	{
		this.setSpacing(10d);
		this.lifeTime = lifeTime;
		toastPrefHeight = 40d;
		informationImage = new Image("/style/InformationIcon.png");
		successImage = new Image("/style/SuccessIcon.png");
		warningImage = new Image("/style/WarningIcon.png");
		undoWarningImage = new Image("/style/UndoWarningIcon.png");
	}
	
	/**
	 *	Provides four different toast types 
	 */
	public enum ToastType
	{
		INFORMATION,
		WARNING,
		SUCCESS,
		UNDO
	}
	
	/**
	 * 
	 * @param toastType
	 * @param titleText
	 * @param descriptionText
	 * 
	 * @description
	 * Adds a simple toast with a given title and description to the toast container
	 */
	public void spawnToast(ToastType toastType, String titleText, String descriptionText)
	{
		// Creates the BorderPane with the preferred height
		BorderPane toast = new BorderPane();
		toast.setPrefHeight(toastPrefHeight);
		
		// Adds the respective stylesheet and icon to the toast
		toast.getStylesheets().add("/style/ToastStyle.css");
		ImageView iconView = null;
		if (toastType == ToastType.INFORMATION)
		{
			toast.pseudoClassStateChanged(PseudoClass.getPseudoClass("information"), true);
			iconView = new ImageView(informationImage);
		}
		else if (toastType == ToastType.SUCCESS)
		{
			toast.pseudoClassStateChanged(PseudoClass.getPseudoClass("success"), true);
			iconView = new ImageView(successImage);
		}
		else if (toastType == ToastType.WARNING)
		{
			toast.pseudoClassStateChanged(PseudoClass.getPseudoClass("warning"), true);
			iconView = new ImageView(warningImage);
		}
		else if (toastType == ToastType.UNDO) 
		{
			if (undoClickEvent == null)
			{
				throw new IllegalStateException("Trying to spawn an undo toast without having added an undo event");
			}
			
			toast.pseudoClassStateChanged(PseudoClass.getPseudoClass("undo"), true);
			iconView = new ImageView(undoWarningImage);
			
			Button undoButton = new Button("UNDO");
			undoButton.setOnAction(e -> undoClickEvent.onUndoClick());
			undoButton.getStyleClass().add("undo-button");
			BorderPane.setAlignment(undoButton, Pos.BOTTOM_CENTER);
			toast.setBottom(undoButton);
		}
		toast.setLeft(iconView);
		BorderPane.setAlignment(iconView, Pos.CENTER_LEFT);
		
		// Sets the specified title of the toast 
		Label title = new Label(titleText);
		title.getStyleClass().add("title");
		toast.setTop(title);
		BorderPane.setAlignment(title, Pos.TOP_CENTER);
		
		// Sets the specified description of the toast
		Label description = new Label(descriptionText);
		description.getStyleClass().add("description");
		toast.setCenter(description);
		BorderPane.setAlignment(description, Pos.CENTER);
		
		// Adds a close button to the toast that allows to remove it before the lifetime is over
		Button closeButton = new Button();
		closeButton.setStyle("-fx-background-color: TRANSPARENT;");
		closeButton.setGraphic(new ImageView(new Image("/style/CloseIcon.png")));
		BorderPane.setAlignment(closeButton, Pos.CENTER_RIGHT);
		closeButton.setOnAction(e ->
		{
			this.getChildren().remove(toast);
		});
		toast.setRight(closeButton);
		
		// Sets up the toast's lifetime 
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(lifeTime), ev -> 
		{
			this.getChildren().remove(toast);
	    }));
	    timeline.setCycleCount(Animation.INDEFINITE);
	    timeline.play();
		
	    // Adds the toast to the toast container
		this.getChildren().add(toast);
	}
	
	/**
	 * @description
	 * Removes all toasts from the container
	 */
	public void clear()
	{
		this.getChildren().clear();
	}
	
	// EVENTS
	
	/**
	 * @param undoClickEvent
	 * 
	 * @description
	 * Adds an event that will be consumed when the user has pressed the undo button on an undo toast
	 */
	public void setOnUndo(OnUndoClick undoClickEvent)
	{
		this.undoClickEvent = undoClickEvent;
	}
	
	// GETTERS & SETTERS

	public double getLifeTime()
	{
		return lifeTime;
	}

	public void setLifeTime(double lifeTime)
	{
		this.lifeTime = lifeTime;
	}

	public double getToastPrefHeight()
	{
		return toastPrefHeight;
	}

	public void setToastPrefHeight(double toastPrefHeight)
	{
		this.toastPrefHeight = toastPrefHeight;
	}

	public Image getInformationImage()
	{
		return informationImage;
	}

	public void setInformationImage(Image informationImage)
	{
		this.informationImage = informationImage;
	}

	public Image getSuccessImage()
	{
		return successImage;
	}

	public void setSuccessImage(Image successImage)
	{
		this.successImage = successImage;
	}

	public Image getWarningImage()
	{
		return warningImage;
	}

	public void setWarningImage(Image warningImage)
	{
		this.warningImage = warningImage;
	}

	public Image getUndoWarningImage()
	{
		return undoWarningImage;
	}

	public void setUndoWarningImage(Image undoWarningImage)
	{
		this.undoWarningImage = undoWarningImage;
	}
}