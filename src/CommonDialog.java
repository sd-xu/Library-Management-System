import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

public class CommonDialog 
{	public static void WarningDialog(String text) 
    {  	Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("æØ∏Ê");                  
        alert.setContentText(text);
        alert.initStyle(StageStyle.UTILITY);
        alert.show();
	}
	
	public static void InformationDialog(String text) 
    {  	Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Ã· æ");                  
        alert.setContentText(text);
        alert.initStyle(StageStyle.UTILITY);
        alert.show();
	}

    public static boolean ConfirmDialog(String title, String text)
    {   Alert alert = new Alert(Alert.AlertType.CONFIRMATION); 
        alert.setHeaderText(title);
        alert.setContentText(text);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get().equals(ButtonType.OK))
        	return true;
        else
        	return false;
    }
}
