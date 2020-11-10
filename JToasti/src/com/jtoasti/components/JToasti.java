/**
 * THIS CODE IS LICENSED
 * FOR MORE INFORMATION, PLEASE VISIT THE GITHUB PAGE
 * https://github.com/GingaaBread/JToasti
 */
package com.jtoasti.components;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * <h1>JToasti</h1>
 * <p>
 * Used as a container that can spawn different types of toasts
 * </p>
 * <ul>
 * <li>Information: Used to inform the user about an event</li>
 * <li>Warning: Used to warn the user about an event</li>
 * <li>Success: Used to display a success message</li>
 * <li>Undo: Used to warn the user about an event that can be undone</li>
 * </ul>
 * 
 * @author GingerBread
 * @version 3.0
 * @since 1.0
 */
public class JToasti extends VBox
{
	private EventHandler<ActionEvent> toastCloseEvent;
	private EventHandler<ActionEvent> toastDeathEvent;
	private EventHandler<ActionEvent> toastRemovalEvent;
	private EventHandler<ActionEvent> undoClickEvent;
	private Image informationCloseButtonImage;
	private Image informationImage;
	private Image successCloseButtonImage;
	private Image successImage;
	private Image warningCloseButtonImage;
	private Image warningImage;
	private Image undoCloseButtonImage;
	private Image undoWarningImage;
	private double prefToastWidth;
	private double prefToastHeight;
	private double lifetime;
	private double fadeOutTime;
	private boolean useDefaultCloseButtonColor;

	/**
	 * Constructs a toast container with a life time of five seconds
	 * 
	 * @since 1.0
	 */
	public JToasti()
	{
		this(5d);
	}

	/**
	 * Constructs a toast container with the specified life time
	 * 
	 * @since 2.0
	 * @param lifeTime The lifetime of the toasts in seconds
	 */
	public JToasti(double lifeTime)
	{
		this.lifetime = lifeTime;
		fadeOutTime = 1d;
		prefToastWidth = 350d;
		prefToastHeight = 100d;
		informationCloseButtonImage = new Image("com/jtoasti/style/InformationCloseButtonIcon.png");
		informationImage = new Image("/com/jtoasti/style/InformationIcon.png");
		successCloseButtonImage = new Image("/com/jtoasti/style/SuccessCloseButtonIcon.png");
		successImage = new Image("/com/jtoasti/style/SuccessIcon.png");
		warningCloseButtonImage = new Image("/com/jtoasti/style/WarningCloseButtonIcon.png");
		warningImage = new Image("/com/jtoasti/style/WarningIcon.png");
		undoCloseButtonImage = new Image("/com/jtoasti/style/UndoCloseButtonIcon.png");
		undoWarningImage = new Image("/com/jtoasti/style/UndoWarningIcon.png");
		setSpacing(10d);
	}

	/**
	 * <p>
	 * Provides the four different toast types
	 * </p>
	 * <ul>
	 * <li>Information: Used to inform the user about an event</li>
	 * <li>Warning: Used to warn the user about an event</li>
	 * <li>Success: Used to display a success message</li>
	 * <li>Undo: Used to warn the user about an event that can be undone</li>
	 * </ul>
	 *
	 * @since 1.0
	 */
	public enum ToastType
	{
		INFORMATION, WARNING, SUCCESS, UNDO
	}

	/**
	 * Adds a toast with a given title and description to the toast container
	 * 
	 * @since 1.0
	 * @param toastType	The type of toast that is to be displayed
	 * @param titleText	The title of the toast
	 * @param descriptionText	The description of the toast
	 * @throws IllegalStateException	Thrown if the UNDO toast type is spawned without having set the OnUndo event
	 * @see ToastType
	 * @see setOnUndo
	 */
	public void spawnToast(ToastType toastType, String titleText, String descriptionText)
	{
		spawnToast(toastType, titleText, descriptionText, lifetime);
	}

	/**
	 * Adds a toast with a custom lifetime, a given title and description to the
	 * toast container
	 * 
	 * @since 3.0
	 * @param lifetime	The lifetime of the toast in seconds
	 * @param toastType	The type of toast that is to be displayed
	 * @param titleText	The title of the toast
	 * @param descriptionText	The description of the toast
	 * @throws IllegalStateException	Thrown if the UNDO toast type is spawned without having set the OnUndo event
	 * @see ToastType
	 * @see setOnUndo
	 */
	public void spawnToast(ToastType toastType, String titleText, String descriptionText, double lifetime)
	{
		if (toastType == ToastType.UNDO && undoClickEvent == null)
		{
			throw new IllegalStateException("Trying to spawn an undo toast without having added an undo event");
		}

		// Creates the BorderPane with the preferred height
		BorderPane toast = new BorderPane();
		toast.setPrefSize(prefToastWidth, prefToastHeight);

		// Adds the title to the toast
		Label title = new Label(titleText);
		title.getStyleClass().add("title");
		toast.setTop(title);
		BorderPane.setAlignment(title, Pos.TOP_CENTER);

		// Adds the description of the toast
		Label description = new Label(descriptionText);
		description.getStyleClass().add("description");
		toast.setCenter(description);
		BorderPane.setAlignment(description, Pos.CENTER);

		// Adds the close button to the toast
		Button closeButton = new Button();
		closeButton.getStyleClass().add("close-button");
		closeButton.setOnAction(e ->
		{
			// Removes the toast from the container
			this.getChildren().remove(toast);

			// Handles the events
			if (toastCloseEvent != null)
			{
				toastCloseEvent.handle(new ActionEvent());
			}
			if (toastRemovalEvent != null)
			{
				toastRemovalEvent.handle(new ActionEvent());
			}
		});
		toast.setPadding(new Insets(0, 0, 10, 0));
		toast.setRight(closeButton);
		BorderPane.setAlignment(closeButton, Pos.CENTER_RIGHT);

		// Adds the stylesheet, pseudo class and icon to the toast
		toast.getStylesheets().add("/com/jtoasti/style/ToastStyle.css");
		toast.pseudoClassStateChanged(PseudoClass.getPseudoClass(toastType.toString().toLowerCase()), true);
		ImageView iconView = null;
		switch (toastType)
		{
		case INFORMATION:
			iconView = new ImageView(informationImage);
			closeButton.setGraphic(new ImageView(informationCloseButtonImage));
			break;
		case SUCCESS:
			iconView = new ImageView(successImage);
			closeButton.setGraphic(new ImageView(successCloseButtonImage));
			break;
		case WARNING:
			iconView = new ImageView(warningImage);
			closeButton.setGraphic(new ImageView(warningCloseButtonImage));
			break;
		case UNDO:
			iconView = new ImageView(undoWarningImage);
			closeButton.setGraphic(new ImageView(undoCloseButtonImage));

			// Creates an undo button and adds it to the bottom of the BorderPane
			Button undoButton = new Button("UNDO");
			undoButton.getStyleClass().add("undo-button");
			undoButton.setOnAction(e -> undoClickEvent.handle(new ActionEvent()));
			toast.setBottom(undoButton);
			BorderPane.setAlignment(undoButton, Pos.BOTTOM_CENTER);
			break;
		}
		toast.setLeft(iconView);
		BorderPane.setAlignment(iconView, Pos.CENTER_LEFT);

		// Changes the close button colour if required
		if (useDefaultCloseButtonColor)
		{
			closeButton.setGraphic(new ImageView(new Image("/com/jtoasti/style/CloseIcon.png")));
		}

		// Adds the specified lifetime to the toast
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(lifetime), ev ->
		{
			// Fades the toast out after having reached its lifetime
			FadeTransition fateTransition = new FadeTransition(Duration.seconds(fadeOutTime), toast);
			fateTransition.setOnFinished(e ->
			{
				if (this.getChildren().contains(toast))
				{
					// Removes the toast from the container
					this.getChildren().remove(toast);

					// Handles the events
					if (toastDeathEvent != null)
					{
						toastDeathEvent.handle(new ActionEvent());
					}
					if (toastRemovalEvent != null)
					{
						toastRemovalEvent.handle(new ActionEvent());
					}
				}
			});
			fateTransition.setFromValue(1.0);
			fateTransition.setToValue(0);
			fateTransition.play();
		}));
		timeline.play();

		// Adds the toast to the toast container
		this.getChildren().add(toast);
	}

	/**
	 * Removes all toasts from the container
	 * 
	 * @since 2.0
	 */
	public void clear()
	{
		this.getChildren().clear();
	}

	// EVENTS

	/**
	 * Invoked whenever the user has pressed the undo button on an undo toast
	 * 
	 * @since 3.0
	 * @param undoClickEvent
	 */
	public void setOnUndo(EventHandler<ActionEvent> undoClickEvent)
	{
		this.undoClickEvent = undoClickEvent;
	}

	/**
	 * Invoked whenever the user has closed a toast using the close button
	 * 
	 * @since 3.0
	 * @param toastClosedEvent
	 */
	public void setOnToastClose(EventHandler<ActionEvent> toastCloseEvent)
	{
		this.toastCloseEvent = toastCloseEvent;
	}

	/**
	 * Invoked whenever the lifetime of a toast has been reached
	 * 
	 * @since 3.0
	 * @param toastDeathEvent
	 */
	public void setOnToastDeath(EventHandler<ActionEvent> toastDeathEvent)
	{
		this.toastDeathEvent = toastDeathEvent;
	}

	/**
	 * Invoked whenever a toast has been removed from the container either by having
	 * been removed or having reached its lifetime
	 * 
	 * @since 3.0
	 * @param toastRemovalEvent
	 */
	public void setOnToastRemoval(EventHandler<ActionEvent> toastRemovalEvent)
	{
		this.toastRemovalEvent = toastRemovalEvent;
	}

	// GETTERS & SETTERS

	/**
	 * The lifetime of the toast determines when it plays its fade out transition
	 * after which it will be removed. The standard value is 5.0 seconds and may not
	 * be or be smaller than 0.
	 * 
	 * @since 1.0
	 * @return The lifetime of the toast in seconds
	 */
	public double getLifeTime()
	{
		return lifetime;
	}

	/**
	 * The lifetime of the toast determines when it plays its fade out transition
	 * after which it will be removed. The standard value is 5.0 seconds and may not
	 * be or be smaller than 0.
	 * 
	 * @since 1.0
	 * @param lifeTime The lifetime of the toast in seconds
	 */
	public void setLifeTime(double lifeTime)
	{
		if (lifeTime <= 0)
		{
			throw new IllegalArgumentException("The lifetime of a toast may not be or be smaller than 0");
		}

		this.lifetime = lifeTime;
	}

	/**
	 * The fade out time determines the amount of seconds it takes the toast to
	 * reduce its opacity to zero. After that, the toast will be removed from the
	 * container. The standard value is 1.0 second and may not be or be smaller than
	 * 0.
	 * 
	 * @since 3.0
	 * @return the fade out time in seconds
	 */
	public double getFadeOutTime()
	{
		return fadeOutTime;
	}

	/**
	 * The fade out time determines the amount of seconds it takes the toast to
	 * reduce its opacity to zero. After that, the toast will be removed from the
	 * container. The standard value is 1.0 second and may not be or be smaller than
	 * 0.
	 * 
	 * @since 3.0
	 * @param fadeOutTime The fade out time in seconds
	 */
	public void setFadeOutTime(double fadeOutTime)
	{
		if (fadeOutTime <= 0)
		{
			throw new IllegalArgumentException("Fade out time may not be or be smaller than 0");
		}

		this.fadeOutTime = fadeOutTime;
	}

	/**
	 * @since 3.0
	 * @return The prefered width of the toast
	 * @see getPrefWidth
	 */
	public double getPrefToastWidth()
	{
		return prefToastWidth;
	}

	/**
	 * Sets the prefered width of the toast
	 * 
	 * @since 3.0
	 * @param prefToastWidth The width of the toast
	 * @see setPrefWidth
	 */
	public void setPrefToastWidth(double prefToastWidth)
	{
		this.prefToastWidth = prefToastWidth;
	}

	/**
	 * @since 1.0
	 * @return The prefered height of the toast
	 * @see getPrefHeight
	 */
	public double getPrefToastHeight()
	{
		return prefToastHeight;
	}

	/**
	 * Sets the prefered height of the toast
	 * 
	 * @since 1.0
	 * @param prefToastHeight The height of the toast
	 * @see setPrefHeight
	 */
	public void setPrefToastHeight(double prefToastHeight)
	{
		this.prefToastHeight = prefToastHeight;
	}

	/**
	 * This determines whether the toasts should display the default close button
	 * color or a unique one. The standard value is false
	 * 
	 * @since 3.0
	 * @return If the toasts will display the default close button color
	 */
	public boolean usesDefaultCloseButtonColor()
	{
		return useDefaultCloseButtonColor;
	}

	/**
	 * This determines whether the toasts should display the default close button
	 * color or a unique one. The standard value is false.
	 * 
	 * @since 3.0
	 * @param useDefaultCloseButtonColor
	 */
	public void setUseDefaultCloseButtonColor(boolean useDefaultCloseButtonColor)
	{
		this.useDefaultCloseButtonColor = useDefaultCloseButtonColor;
	}

	/**
	 * The information image is the icon that will be displayed on a toast with the
	 * INFORMATION toast type. The default image is taken from the style package,
	 * but can be changed.
	 * 
	 * @since 1.0
	 * @return The information image
	 */
	public Image getInformationImage()
	{
		return informationImage;
	}

	/**
	 * The information image is the icon that will be displayed on a toast with the
	 * INFORMATION toast type. The default image is taken from the style package,
	 * but can be changed.
	 * 
	 * @since 1.0
	 * @param informationImage The information image
	 */
	public void setInformationImage(Image informationImage)
	{
		this.informationImage = informationImage;
	}

	/**
	 * The success image is the icon that will be displayed on a toast with the
	 * SUCCESS toast type. The default image is taken from the style package, but
	 * can be changed.
	 * 
	 * @since 1.0
	 * @return The success image
	 */
	public Image getSuccessImage()
	{
		return successImage;
	}

	/**
	 * The success image is the icon that will be displayed on a toast with the
	 * SUCCESS toast type. The default image is taken from the style package, but
	 * can be changed.
	 * 
	 * @since 1.0
	 * @param successImage The success image
	 */
	public void setSuccessImage(Image successImage)
	{
		this.successImage = successImage;
	}

	/**
	 * The warning image is the icon that will be displayed on a toast with the
	 * WARNING toast type. The default image is taken from the style package, but
	 * can be changed.
	 * 
	 * @since 1.0
	 * @return The warning image
	 */
	public Image getWarningImage()
	{
		return warningImage;
	}

	/**
	 * The warning image is the icon that will be displayed on a toast with the
	 * WARNING toast type. The default image is taken from the style package, but
	 * can be changed.
	 * 
	 * @since 1.0
	 * @param warningImage The warning image
	 */
	public void setWarningImage(Image warningImage)
	{
		this.warningImage = warningImage;
	}

	/**
	 * The undo warning image is the icon that will be displayed on a toast with the
	 * UNDO toast type. The default image is identical to the default warning image,
	 * but can be changed.
	 * 
	 * @since 1.0
	 * @return The undo warning image
	 */
	public Image getUndoWarningImage()
	{
		return undoWarningImage;
	}

	/**
	 * The undo warning image is the icon that will be displayed on a toast with the
	 * UNDO toast type. The default image is identical to the default warning image,
	 * but can be changed.
	 * 
	 * @since 1.0
	 * @param undoWarningImage
	 */
	public void setUndoWarningImage(Image undoWarningImage)
	{
		this.undoWarningImage = undoWarningImage;
	}

	/**
	 * The information close button image is the icon that will be displayed on the
	 * close button of a toast with the INFORMATION toast type. In order to be
	 * applied, useDefaultCloseButtonColor must be false.
	 * 
	 * @since 3.0
	 * @return The information close button image
	 * @see setUseDefaultCloseButtonColor
	 */
	public Image getInformationCloseButtonImage()
	{
		return informationCloseButtonImage;
	}

	/**
	 * The information close button image is the icon that will be displayed on the
	 * close button of a toast with the INFORMATION toast type. In order to be
	 * applied, useDefaultCloseButtonColor must be false.
	 * 
	 * @since 3.0
	 * @param informationCloseButtonImage
	 * @see setUseDefaultCloseButtonColor
	 */
	public void setInformationCloseButtonImage(Image informationCloseButtonImage)
	{
		this.informationCloseButtonImage = informationCloseButtonImage;
	}

	/**
	 * The success close button image is the icon that will be displayed on the
	 * close button of a toast with the SUCCESS toast type. In order to be applied,
	 * useDefaultCloseButtonColor must be false.
	 * 
	 * @since 3.0
	 * @return The success close button image
	 * @see setUseDefaultCloseButtonColor
	 */
	public Image getSuccessCloseButtonImage()
	{
		return successCloseButtonImage;
	}

	/**
	 * The success close button image is the icon that will be displayed on the
	 * close button of a toast with the SUCCESS toast type. In order to be applied,
	 * useDefaultCloseButtonColor must be false.
	 * 
	 * @since 3.0
	 * @param successCloseButtonImage
	 * @see setUseDefaultCloseButtonColor
	 */
	public void setSuccessCloseButtonImage(Image successCloseButtonImage)
	{
		this.successCloseButtonImage = successCloseButtonImage;
	}

	/**
	 * The warning close button image is the icon that will be displayed on the
	 * close button of a toast with the WARNING toast type. In order to be applied,
	 * useDefaultCloseButtonColor must be false.
	 * 
	 * @since 3.0
	 * @return The warning close button image
	 * @see setUseDefaultCloseButtonColor
	 */
	public Image getWarningCloseButtonImage()
	{
		return warningCloseButtonImage;
	}

	/**
	 * The warning close button image is the icon that will be displayed on the
	 * close button of a toast with the WARNING toast type. In order to be applied,
	 * useDefaultCloseButtonColor must be false.
	 * 
	 * @since 3.0
	 * @param warningCloseButtonImage
	 * @see setUseDefaultCloseButtonColor
	 */
	public void setWarningCloseButtonImage(Image warningCloseButtonImage)
	{
		this.warningCloseButtonImage = warningCloseButtonImage;
	}

	/**
	 * The undo close button image is the icon that will be displayed on the close
	 * button of a toast with the UNDO toast type. In order to be applied,
	 * useDefaultCloseButtonColor must be false.
	 * 
	 * @since 3.0
	 * @return The information close button image
	 * @see setUseDefaultCloseButtonColor
	 */
	public Image getUndoCloseButtonImage()
	{
		return undoCloseButtonImage;
	}

	/**
	 * The undo close button image is the icon that will be displayed on the close
	 * button of a toast with the UNDO toast type. In order to be applied,
	 * useDefaultCloseButtonColor must be false.
	 * 
	 * @since 3.0
	 * @param undoCloseButtonImage
	 * @see setUseDefaultCloseButtonColor
	 */
	public void setUndoCloseButtonImage(Image undoCloseButtonImage)
	{
		this.undoCloseButtonImage = undoCloseButtonImage;
	}
	
}