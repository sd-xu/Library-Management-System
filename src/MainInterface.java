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
    
    int sfLB = -1; // 读者:0 管理员:1
    String sfID = "", sfName = ""; // id 名字
    
    public static void main(String[] args) 
    {   launch(args);
    }

    public void start(Stage stage) 
    {   
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        int screenWid = (int)primaryScreenBounds.getWidth(), screenHei = (int)primaryScreenBounds.getHeight(); 
        
        try 
	    {     Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); // 数据库装载
		}
		catch(Exception e)  
		{     System.out.println("数据库装载失败: " + e.toString());			
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
        stage.setTitle("图书馆信息管理系统");
        stage.setScene(scene);
        stage.show();
        
    }
    
    private MenuBar CreateMenu()  // 建立菜单栏
    {   MenuBar menuBar = new MenuBar();
    
        Menu operatorMenu = new Menu("功能操作");		 
        MenuItem book = new MenuItem(" 书目/借书 ");
		book.setOnAction(new MenuHandler());
		MenuItem retn = new MenuItem(" 借阅记录/还书 ");
		retn.setOnAction(new MenuHandler());
		MenuItem info = new MenuItem(" 资料查看与修改 ");
		info.setOnAction(new MenuHandler());
		operatorMenu.getItems().addAll(book, retn, info);
		menuBar.getMenus().add(operatorMenu);
		
		Menu userMenu = new Menu("用户管理");	 
		MenuItem menuPasswd = new MenuItem(" 密码修改 ");
		menuPasswd.setOnAction(new MenuHandler());
		MenuItem menuVersion = new MenuItem(" 版本 ");
		menuVersion.setOnAction(new MenuHandler());
		userMenu.getItems().addAll(menuPasswd, menuVersion);
		menuBar.getMenus().add(userMenu);
		
		return menuBar;
    }
    
    private class MenuHandler implements EventHandler<ActionEvent>
    {   public void handle(ActionEvent ae)
        {   
    		String itemName = ((MenuItem)ae.getTarget()).getText();
            if(itemName.equals(" 书目/借书 ")) 
            {   if(sfLB == 0)
                    new BookDialog(sfID,sfName);  
                else
                    CommonDialog.WarningDialog("只有《读者》才能借书 !"); 	
            }
            if(itemName.equals(" 借阅记录/还书 "))
            {   if(sfLB == 0)
                    new RecordDialog(sfLB, sfID);
                else
                	CommonDialog.WarningDialog("只有《读者》才能还书 !"); 
            }
            if(itemName.equals(" 资料查看与修改 "))
            {   if(sfLB == 1)
                    new InfoInputDialog();
                else
                    CommonDialog.WarningDialog("只有《管理员》才能查看与修改资料 !");
            }
            if(itemName.equals(" 密码修改 "))
            {   if(sfLB >= 0)
            		new PasswordDialog(sfLB, sfID);
            }
            if(itemName.equals(" 版本 "))
            {   new ShowVersionDialog();
            }
        }
    }
      
    private void CreateToolBar()  // 建立导航条
	{   
    	Button bookButton = new Button("",new ImageView(new Image("image/pic_bt_1.jpg", 100, 100, true, true)));
        Button recordButton = new Button("",new ImageView(new Image("image/pic_bt_2.jpg", 100, 100, true, true)));
        Button infoInputButton = new Button("",new ImageView(new Image("image/pic_bt_3.jpg", 100, 100, true, true)));
        Button passwordButton = new Button("",new ImageView(new Image("image/pic_bt_4.jpg", 100, 100, true, true)));
        
        // 书目/借书: 读者
        bookButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        bookButton.setStyle("-fx-padding: 0");
        bookButton.setStyle("-fx-background-insets: 0"); // 去除边框样式
        bookButton.setOnAction(e->{
        	if(sfLB == 0)
        		new BookDialog(sfID,sfName);
        	else 
        		CommonDialog.WarningDialog("只有《读者》才能借书 !");
        });
        
        // 借阅记录/还书: 读者
        recordButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        recordButton.setStyle("-fx-padding: 0");
        recordButton.setStyle("-fx-background-insets: 0");
        recordButton.setOnAction(e->{
            if(sfLB == 0)
            	new RecordDialog(sfLB, sfID);
            else
            	CommonDialog.WarningDialog("只有《读者》才能还书 !");
        });
        
        // 资料查看与修改: 管理员
        infoInputButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        infoInputButton.setStyle("-fx-padding: 0");
        infoInputButton.setStyle("-fx-background-insets: 0");
        infoInputButton.setOnAction(e->{
            if(sfLB == 1)
            	new InfoInputDialog();
            else
                CommonDialog.WarningDialog("只有《管理员》才能录入资料 !"); 	
        });
        
        // 修改密码: 自己的
        passwordButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        passwordButton.setStyle("-fx-padding: 0");
        passwordButton.setStyle("-fx-background-insets: 0");
        passwordButton.setOnAction(e->{
        	if(sfLB >= 0)
        		new PasswordDialog(sfLB, sfID);
        });
                       
        bookButton.setTooltip(new Tooltip(" 书目 "));
        recordButton.setTooltip(new Tooltip(" 借阅记录 "));
        infoInputButton.setTooltip(new Tooltip(" 资料录入 "));
        passwordButton.setTooltip(new Tooltip(" 修改密码 "));
        
        toolBar = new VBox(bookButton, recordButton, infoInputButton, passwordButton);
        toolBar.setSpacing(5);
    }
}