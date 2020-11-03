package sample;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import components.JToasti;
import components.JToasti.ToastType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 * @author GingaaBread
 * 
 * @description
 * A sample document controller that you can use as a guide on how to use JToasti
 *
 */
public class DocumentController implements Initializable
{
	@FXML
    private AnchorPane container;
	
	@FXML
    private Button button;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		// Creates the toast container
		JToasti toastContainer = new JToasti();
		
		// [Sample Code] Adds an undo event (for undo toasts)
		toastContainer.setOnUndo( () -> System.out.println("Undid the last action"));
		
		// [Sample Code] Spawns a random toast type
		button.setOnAction(e ->
		{
			switch (new Random().nextInt(4))
			{
			case 0 :
				toastContainer.spawnToast(ToastType.INFORMATION, "Information", "You have received 1 new message");
				break;
			case 1 : 
				toastContainer.spawnToast(ToastType.SUCCESS, "Well done!", "Successfully added 50€ to your account");
				break;
			case 2 :
				toastContainer.spawnToast(ToastType.WARNING, "Attention", "Your password may not be secure!");
				break;
			case 3 :
				toastContainer.spawnToast(ToastType.UNDO, "Deletion", "Successfully deleted the file");
			}
		});
		
		// Adds the toast container to the anchor pane
		container.getChildren().add(toastContainer);
	}
}
