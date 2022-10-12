import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class OperatorTabPane extends BorderPane{
	 private final TableView<Operator> operatorTable = new TableView<>();
	    private final ObservableList<Operator> operatorData = FXCollections.observableArrayList();
//	    String DBUrl="jdbc:ucanaccess:///D:/eclipse-workspace/Library-Management-System3/src/management.accdb";
	    String DBUrl="jdbc:ucanaccess:///D:/Codefield/CODE_Java/EclipseProjects/Library-Management-System/src/management.accdb";
	    String DBUser="", DBPassword="";
	    
	    public OperatorTabPane() 
	    {   loadDataFromOperator(); //  从数据库提取 Operator 数据更新 data 列表
	        operatorTable.setEditable(true);
	        operatorTable.setMaxWidth(730);
	        
	        Callback<TableColumn<Operator, String>, TableCell<Operator, String>> cellStringFactory = 
	        		new Callback<TableColumn<Operator, String>, TableCell<Operator, String>>() 
	                {   public TableCell<Operator, String> call(TableColumn p) 
	                    {   return new EditingStringCell<Operator>();  
	                    }  
	                };  
	                
	        TableColumn<Operator, String> numCol = new TableColumn<>("ID");
	        numCol.setMinWidth(200);
	        numCol.setCellValueFactory(new PropertyValueFactory<Operator, String>("uid"));
	        numCol.setCellFactory(cellStringFactory);
	        numCol.setOnEditCommit(new EventHandler<CellEditEvent<Operator, String>>() {
	            @Override
	            public void handle(CellEditEvent<Operator, String> t) {
	            	if(((Operator) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() == 2)
	                    ((Operator) t.getTableView().getItems().get(t.getTablePosition().getRow())).setUid(t.getNewValue());
	            }
	        });
	        
	        TableColumn<Operator, String> nameCol = new TableColumn<>("名字");
	        nameCol.setMinWidth(230);
	        nameCol.setCellValueFactory(new PropertyValueFactory<Operator, String>("name"));
	        nameCol.setCellFactory(cellStringFactory);
	        nameCol.setOnEditCommit(new EventHandler<CellEditEvent<Operator, String>>() {
	            @Override
	            public void handle(CellEditEvent<Operator, String> t) {
	            	if(((Operator) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() == 2)
	                    ((Operator) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
	            }
	        });
	        	                                              
	        TableColumn<Operator, String> passwordCol = new TableColumn<>("密码");
	        passwordCol.setMinWidth(300);
	        passwordCol.setCellValueFactory(new PropertyValueFactory<Operator, String>("password"));
	        passwordCol.setCellFactory(cellStringFactory);
	        passwordCol.setOnEditCommit(new EventHandler<CellEditEvent<Operator, String>>() {
	            @Override
	            public void handle(CellEditEvent<Operator, String> t) {
	            	if(!t.getNewValue().equals(t.getOldValue()) && ((Operator) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() != 2)
	            	    ((Operator) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDTag(1);
	                ((Operator) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPassword(t.getNewValue());
	            }
	        });
	
	        operatorTable.setItems(operatorData);
	        operatorTable.getColumns().addAll(numCol, nameCol, passwordCol);    
	      
	        final Button addButton = new Button("Add");
	        final Button deleteButton = new Button("Delete");
	                
	        deleteButton.setOnAction(new EventHandler<ActionEvent>(){
	        	public void handle(ActionEvent e)
	        	{	Operator p = operatorTable.getSelectionModel().getSelectedItem();
	        		int pos = operatorTable.getSelectionModel().getSelectedIndex();
	        		
	        		if(p != null)
	        	    {   String delName = p.getName();
	        			if(CommonDialog.ConfirmDialog("温馨提示", "确认要删除名字为 《"+delName+"》 的学生吗？"))
	        			{   if(p.getDTag() != 2)  // 不是新增加的记录，需要删除数据库中的记录
	        			        deleteDataFromOperator(p.getUid());      			
	        		        operatorData.remove(pos);
	        		    }
	        		}
	        		else
	        		    CommonDialog.WarningDialog("你没有选中需要删除的记录"); 
	        	}
	            
	        });
	        
	        addButton.setOnAction(new EventHandler<ActionEvent>(){
	        	public void handle(ActionEvent e)
	        	{	operatorData.add(new Operator("", "", "",2));
	        	    operatorTable.refresh();
	   	            operatorTable.getSelectionModel().selectLast();
	   	            operatorTable.scrollTo(operatorData.size());
	   	            operatorTable.requestFocus();
	        	}
	            
	        });
	        
	                
	        final HBox hb = new HBox();
	        hb.getChildren().addAll(addButton,deleteButton);
	        hb.setSpacing(20);
	        hb.setAlignment(Pos.CENTER_LEFT);
	        hb.setMargin(addButton, new Insets(0, 0, 0, 10));
	                                
	        ScrollPane spp = new ScrollPane(operatorTable);
	        spp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
	        spp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
	        
	        setCenter(spp);
	        setBottom(hb);
	        setMargin(hb, new Insets(5, 10, 5, 10));
	        
	    }
	    
	    private void deleteDataFromOperator(String numstr)
	    {   try
	        {   Connection conn = DriverManager.getConnection(DBUrl, DBUser, DBPassword);
	            Statement stmt = conn.createStatement();
	            String deleteSql = "DELETE FROM Operator WHERE uid='"+numstr+"'";
	            int count = stmt.executeUpdate(deleteSql); 
	            System.out.println("删除了 operator 表的《"+numstr+"》"+count+"条记录");
	            updateOperatorDataBase();
	            stmt.close();
	            conn.close();
	        }
	        catch(Exception e) 
	        {   e.printStackTrace();
	        }
	    }
	    
	    public void updateOperatorDataBase()
	    {   Operator up;
	        String unum, uname, upassword;
	        int udtag;
	                
	        try
	        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
	            Statement stmt=conn.createStatement();
	                        
	            for(int i=0;i<operatorData.size();i++)
	            {   up = (Operator)operatorData.get(i);
	                udtag = up.getDTag();
	                unum = up.getUid();
	                uname = up.getName();
	                upassword = up.getPassword();
	                if(udtag == 2)  // 新增加记录
	                {   String insertSql="INSERT INTO operator(uid,uname,password) "+
	                                     "VALUES('"+unum+"','"+uname+"','"+upassword+"')";
	                    int count=stmt.executeUpdate(insertSql);
	                    System.out.println("添加 "+ count+" 条记录到Operator表中");
	                }
	                
	                if(udtag == 1)  // 更改过的记录
	                {   String updateSql="UPDATE operator SET uid='"+unum+"',uname='"+uname+"',password='"+upassword+"' WHERE uid='"+unum+"'";
	                    System.out.println(updateSql);
	                    int count=stmt.executeUpdate(updateSql);
	                    System.out.println(updateSql);
	                    System.out.println("修改 "+ count+" 条记录到Operator表中");
	                }
	            } 
	                           
	            stmt.close();
	            conn.close();
	        }
	        catch(Exception e) 
	        {   e.printStackTrace();
	        }
	    }
	       
	    private void loadDataFromOperator()
	    {   String sql="SELECT uid,uname,password FROM Operator";
	               
	        try
	        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
	            Statement stmt=conn.createStatement();
	            
	            operatorData.clear();
	            ResultSet rs=stmt.executeQuery(sql);
	            while(rs.next())
	            {   String no=rs.getString("uid"); if(no==null) no="";
	                String name=rs.getString("uname"); if(name==null) name="";
	                String password=rs.getString("password"); if(password==null) password="";
	                operatorData.add(new Operator(no,name, password,0));
	            }
	            rs.close();
	            stmt.close();
	            conn.close();
	        }
	        catch(Exception e) 
	        {   e.printStackTrace();
	        }
	    }
	    
	    public static class Operator
	    {   private final SimpleStringProperty uid;
	    	private final SimpleStringProperty name;
	        private final SimpleStringProperty password;
	        private int dtag; // 0 无需更改      1 编辑更改过的数据   2  新增加的记录

	        private Operator(String uUid, String uName, String uPassword,int udtag) 
	        {   this.uid = new SimpleStringProperty(uUid);
	            this.name = new SimpleStringProperty(uName);
	            this.password = new SimpleStringProperty(uPassword);
	            dtag = udtag;
	        }
	        

	        
	        public String getUid() 
	        {   return uid.get();
	        }
	        
	        public void setUid(String uUid) 
	        {   uid.set(uUid);
	        }
	        
	        public String getName() 
	        {   return name.get();
	        }

	        public void setName(String uName) 
	        {   name.set(uName);
	        }

	        public String getPassword() 
	        {   return password.get();
	        }

	        public void setPassword(String uPassword) 
	        {   password.set(uPassword);
	        }
	        
	        public void setDTag(int udtag) 
	        {   dtag = udtag;
	        }
	        
	        public int getDTag() 
	        {   return dtag;
	        }
	    }
	}
