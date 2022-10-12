import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.sql.*;

public class BookDialog extends Stage{
	BookTabPane btp;

    public BookDialog(String uid,String uname)
    {   
    	BorderPane infoBP = new BorderPane();   
    
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-tab-min-width:90px; -fx-tab-max-width:9px; -fx-tab-min-height:30px; -fx-tab-max-height:30px;");
        tabPane.setStyle("-fx-background-color:lightgray;");
        
        Tab bookTab = new Tab(" 书目 ");
        bookTab.setClosable(false);
        bookTab.setStyle("-fx-font-family:kaiti; -fx-font-size: 14;-fx-background-radius: 20");
        btp = new BookTabPane(0,uid,uname);
        bookTab.setContent(btp);
     
        tabPane.getTabs().addAll(bookTab);
        tabPane.setPadding(new Insets(4, 0, 0, 0));
        
        infoBP.setCenter(tabPane);
        Button exitButton = new Button("退出", new ImageView(new Image("image/quit.png")));
        
        exitButton.setOnAction(e->{
            btp.updateBookDataBase();	
            close();  // 关闭资料输入窗口
        });
               
        infoBP.setBottom(exitButton);
        infoBP.setAlignment(exitButton, Pos.CENTER_RIGHT);
        infoBP.setMargin(exitButton, new Insets(8, 20, 8, 10));
        Scene scene=new Scene(infoBP, 730, 510);
        setTitle("书目");
        setScene(scene);
        setX(430);
        setY(250);
        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }
}