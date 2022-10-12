import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.sql.*;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Modality;

class LoginDialog extends Stage{
	Label userLabel = new Label("用户名: ");
	TextField userName = new TextField();
	Label passLabel = new Label("密   码: ");
	PasswordField userPassword = new PasswordField();
	
	Label sfLabel = new Label("身份：");
	RadioButton readerRB = new RadioButton("读者");
	RadioButton adminRB = new RadioButton("管理员");
	
	Button loginButton = new Button("登录");
	Button quitButton = new Button("退出");
	
	String userId;
	
	MainInterface appUser;

//	String DBUrl="jdbc:ucanaccess:///D:/eclipse-workspace/Library-Management-System3/src/management.accdb";
	String DBUrl="jdbc:ucanaccess:///D:/Codefield/CODE_Java/EclipseProjects/Library-Management-System/src/management.accdb";
	
	String DBUser="", DBPassword="";
	
	public LoginDialog(MainInterface app, int width, int height)
	{
		appUser = app;
		userName.setPrefWidth(150);
        userPassword.setPrefWidth(150);
        
        loginButton.setStyle("-fx-text-fill:red");
    	loginButton.setOnAction(new EventHandler<ActionEvent>()
    	{   public void handle(ActionEvent e)
            {   loginAction();   	    
            }
        });
    	
    	quitButton.setOnAction(new EventHandler<ActionEvent>()
    	{   public void handle(ActionEvent e)
        	{	close();   
        	    Platform.exit(); 	    
        	}
        });
    	
    	VBox vb = new VBox();
        vb.setSpacing(10);
        
        HBox hb1 = new HBox();
        hb1.getChildren().addAll(userLabel, userName);
        hb1.setMargin(userLabel, new Insets(3,0,0,0));
        hb1.setMargin(userName, new Insets(0,0,0,3));
        
        HBox hb2 = new HBox();
        hb2.getChildren().addAll(passLabel, userPassword);
        hb2.setMargin(passLabel, new Insets(3,0,0,0));
        hb2.setMargin(userPassword, new Insets(0,0,0,6));
        
        HBox hb3 = new HBox();
        hb3.getChildren().addAll(sfLabel, readerRB, adminRB);
        hb3.setSpacing(5);
        hb3.setMargin(sfLabel, new Insets(2,0,0,0));
        hb3.setMargin(readerRB, new Insets(0,0,0,5));
        
        ToggleGroup tg = new ToggleGroup();
        readerRB.setToggleGroup(tg);
        adminRB.setToggleGroup(tg);
        
        HBox hb4 = new HBox();
        hb4.setSpacing(20);
        hb4.setAlignment(Pos.CENTER);
        hb4.getChildren().addAll(loginButton, quitButton);
        
        vb.getChildren().addAll(hb1, hb2, hb3);
        
        BorderPane bPane = new BorderPane();
        bPane.setCenter(vb);
        bPane.setBottom(hb4);
        bPane.setPadding(new Insets(15,40,20,40));
        
        setX(820);
        setY(500);
        initModality(Modality.APPLICATION_MODAL);
        setScene(new Scene(bPane));
        
        setWidth(325);
        setHeight(200);
        setX((width - getWidth()) / 2);
	    setY((height - getHeight()) / 2);
	    setTitle("登录系统");
        showAndWait();
	}
	
	public void loginAction() 
	{
		int sflb = 0;
	    String sfStr = "", tbName = "";
	    userId = (userName.getText()).trim();
		String pw = (userPassword.getText()).trim();
	    if(userId.length() == 0)
	    {   CommonDialog.WarningDialog("请输入用户名！");
	        return;
	    }
	    if(readerRB.isSelected())
	    {   sflb = 0; sfStr = "读者号：";
	        tbName = "reader";
	    }
	    if(adminRB.isSelected())
	    {   sflb = 1; sfStr = "管理员号：";
	        tbName = "operator";
	    }
	    if(sfStr.length() == 0)
	    {   CommonDialog.WarningDialog("请选择登录的身份类别！");
	        return;
	    }
	    try
	    {   Connection con = DriverManager.getConnection(DBUrl, DBUser, DBPassword);
	        Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tbName + " where uid = '" + userId + "'");
	        if(rs.next()) 
	        {   String pwStr = rs.getString("password");
	            String nameStr = rs.getString("uname").trim();
	            if(pwStr==null || pwStr.equals(pw))
	            {   appUser.sfLB = sflb;
	                appUser.sfID = userId;
	                appUser.sfName = nameStr;
	                rs.close();
	                stmt.close();
	                con.close();
	                close();
	            }
	            else
	                CommonDialog.WarningDialog("密码不正确，请重新输入！");   
	        }
	        else
	            CommonDialog.WarningDialog(sfStr+"《"+userId+"》不存在，请重新输入！");
	        rs.close();
	        stmt.close();
	        con.close();
		} 
	    catch(SQLException s)  
	    {     System.out.println("数据库操作失败！");			
		}
	}
	public String getDBUser()
	{
		return DBUser;
	}
	
	public String getDBPassword()
	{
		return DBPassword;
	}
}