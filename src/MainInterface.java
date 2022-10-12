import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import java.sql.*;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.ToolBar;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.stage.Modality;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MainInterface extends Application 
{   LoginDialog loginDialog = null;
    VBox toolBar;
    
    int sfLB = -1; // ����:0 ����Ա:1
    String sfID = "", sfName = ""; // id ����
    
    public static void main(String[] args) 
    {   launch(args);
    }

    public void start(Stage stage) 
    {   
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        int screenWid = (int)primaryScreenBounds.getWidth(), screenHei = (int)primaryScreenBounds.getHeight(); 
        
        try 
	    {     Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); // ���ݿ�װ��
		}
		catch(Exception e)  
		{     System.out.println("���ݿ�װ��ʧ��: " + e.toString());			
		} 
	    
        loginDialog = new LoginDialog(this, screenWid, screenHei);
        
	    CreateToolBar();                
        BorderPane bordPane = new BorderPane();
        bordPane.setTop(CreateMenu());
        bordPane.setLeft(toolBar);
        
        ImageView backImage = new ImageView("image/pic_1.jpg");
        backImage.setFitWidth(screenWid);
        backImage.setPreserveRatio(true);
        bordPane.setCenter(backImage);
        
        Scene scene = new Scene(bordPane, screenWid, screenHei);
        stage.setTitle("ͼ�����Ϣ����ϵͳ");
        stage.setScene(scene);
        stage.show();
        
    }
    
    private MenuBar CreateMenu()  // �����˵���
    {   MenuBar menuBar = new MenuBar();
    
        Menu operatorMenu = new Menu("���ܲ���");		 
        MenuItem book = new MenuItem(" ��Ŀ/���� ");
		book.setOnAction(new MenuHandler());
		MenuItem retn = new MenuItem(" ���ļ�¼/���� ");
		retn.setOnAction(new MenuHandler());
		MenuItem info = new MenuItem(" ���ϲ鿴���޸� ");
		info.setOnAction(new MenuHandler());
		operatorMenu.getItems().addAll(book, retn, info);
		menuBar.getMenus().add(operatorMenu);
		
		Menu userMenu = new Menu("�û�����");	 
		MenuItem menuPasswd = new MenuItem(" �����޸� ");
		menuPasswd.setOnAction(new MenuHandler());
		MenuItem menuVersion = new MenuItem(" �汾 ");
		menuVersion.setOnAction(new MenuHandler());
		userMenu.getItems().addAll(menuPasswd, menuVersion);
		menuBar.getMenus().add(userMenu);
		
		return menuBar;
    }
    
    private class MenuHandler implements EventHandler<ActionEvent>
    {   public void handle(ActionEvent ae)
        {   
    		String itemName = ((MenuItem)ae.getTarget()).getText();
            if(itemName.equals(" ��Ŀ/���� ")) 
            {   if(sfLB == 0)
                    new BookDialog(sfID,sfName);  
                else
                    CommonDialog.WarningDialog("ֻ�С����ߡ����ܽ��� !"); 	
            }
            if(itemName.equals(" ���ļ�¼/���� "))
            {   if(sfLB == 0)
                    new RecordDialog(sfLB, sfID);
                else
                	CommonDialog.WarningDialog("ֻ�С����ߡ����ܻ��� !"); 
            }
            if(itemName.equals(" ���ϲ鿴���޸� "))
            {   if(sfLB == 1)
                    new InfoInputDialog();
                else
                    CommonDialog.WarningDialog("ֻ�С�����Ա�����ܲ鿴���޸����� !");
            }
            if(itemName.equals(" �����޸� "))
            {   if(sfLB >= 0)
            		new PasswordDialog(sfLB, sfID);
            }
            if(itemName.equals(" �汾 "))
            {   new ShowVersionDialog();
            }
        }
    }
      
    private void CreateToolBar()  // ����������
	{   
    	Button bookButton = new Button("",new ImageView(new Image("image/pic_bt_1.jpg", 100, 100, true, true)));
        Button recordButton = new Button("",new ImageView(new Image("image/pic_bt_2.jpg", 100, 100, true, true)));
        Button infoInputButton = new Button("",new ImageView(new Image("image/pic_bt_3.jpg", 100, 100, true, true)));
        Button passwordButton = new Button("",new ImageView(new Image("image/pic_bt_4.jpg", 100, 100, true, true)));
        
        // ��Ŀ/����: ����
        bookButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        bookButton.setStyle("-fx-padding: 0");
        bookButton.setStyle("-fx-background-insets: 0"); // ȥ���߿���ʽ
        bookButton.setOnAction(e->{
        	if(sfLB == 0)
        		new BookDialog(sfID,sfName);
        	else 
        		CommonDialog.WarningDialog("ֻ�С����ߡ����ܽ��� !");
        });
        
        // ���ļ�¼/����: ����
        recordButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        recordButton.setStyle("-fx-padding: 0");
        recordButton.setStyle("-fx-background-insets: 0");
        recordButton.setOnAction(e->{
            if(sfLB == 0)
            	new RecordDialog(sfLB, sfID);
            else
            	CommonDialog.WarningDialog("ֻ�С����ߡ����ܻ��� !");
        });
        
        // ���ϲ鿴���޸�: ����Ա
        infoInputButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        infoInputButton.setStyle("-fx-padding: 0");
        infoInputButton.setStyle("-fx-background-insets: 0");
        infoInputButton.setOnAction(e->{
            if(sfLB == 1)
            	new InfoInputDialog();
            else
                CommonDialog.WarningDialog("ֻ�С�����Ա������¼������ !"); 	
        });
        
        // �޸�����: �Լ���
        passwordButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        passwordButton.setStyle("-fx-padding: 0");
        passwordButton.setStyle("-fx-background-insets: 0");
        passwordButton.setOnAction(e->{
        	if(sfLB >= 0)
        		new PasswordDialog(sfLB, sfID);
        });
                       
        bookButton.setTooltip(new Tooltip(" ��Ŀ "));
        recordButton.setTooltip(new Tooltip(" ���ļ�¼ "));
        infoInputButton.setTooltip(new Tooltip(" ����¼�� "));
        passwordButton.setTooltip(new Tooltip(" �޸����� "));
        
        toolBar = new VBox(bookButton, recordButton, infoInputButton, passwordButton);
        toolBar.setSpacing(5);
    }
}