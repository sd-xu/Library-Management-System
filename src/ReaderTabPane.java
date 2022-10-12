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

public class ReaderTabPane extends BorderPane{
	 private final TableView<Reader> readerTable = new TableView<>();
	    private final ObservableList<Reader> readerData = FXCollections.observableArrayList();
//	    String DBUrl="jdbc:ucanaccess:///D:/eclipse-workspace/Library-Management-System3/src/management.accdb";
	    String DBUrl="jdbc:ucanaccess:///D:/Codefield/CODE_Java/EclipseProjects/Library-Management-System/src/management.accdb";
	    String DBUser="", DBPassword="";
	    
	    public ReaderTabPane() 
	    {   loadDataFromReader(); //  从数据库提取 Reader 数据更新 data 列表
	        readerTable.setEditable(true);
	        readerTable.setMaxWidth(730);
	        
	        Callback<TableColumn<Reader, String>, TableCell<Reader, String>> cellStringFactory = 
	        		new Callback<TableColumn<Reader, String>, TableCell<Reader, String>>() 
	                {   public TableCell<Reader, String> call(TableColumn p) 
	                    {   return new EditingStringCell<Reader>();  
	                    }  
	                };  
	                
	        TableColumn<Reader, String> numCol = new TableColumn<>("ID");
	        numCol.setMinWidth(200);
	        numCol.setCellValueFactory(new PropertyValueFactory<Reader, String>("uid"));
	        numCol.setCellFactory(cellStringFactory);
	        numCol.setOnEditCommit(new EventHandler<CellEditEvent<Reader, String>>() {
	            @Override
	            public void handle(CellEditEvent<Reader, String> t) {
	            	if(((Reader) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() == 2)
	                    ((Reader) t.getTableView().getItems().get(t.getTablePosition().getRow())).setUid(t.getNewValue());
	            }
	        });
	        
	        TableColumn<Reader, String> nameCol = new TableColumn<>("名字");
	        nameCol.setMinWidth(230);
	        nameCol.setCellValueFactory(new PropertyValueFactory<Reader, String>("name"));
	        nameCol.setCellFactory(cellStringFactory);
	        nameCol.setOnEditCommit(new EventHandler<CellEditEvent<Reader, String>>() {
	            @Override
	            public void handle(CellEditEvent<Reader, String> t) {
	            	if(((Reader) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() == 2)
	                    ((Reader) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
	            }
	        });
	        	                                              
	        TableColumn<Reader, String> passwordCol = new TableColumn<>("密码");
	        passwordCol.setMinWidth(300);
	        passwordCol.setCellValueFactory(new PropertyValueFactory<Reader, String>("password"));
	        passwordCol.setCellFactory(cellStringFactory);
	        passwordCol.setOnEditCommit(new EventHandler<CellEditEvent<Reader, String>>() {
	            @Override
	            public void handle(CellEditEvent<Reader, String> t) {
	            	if(!t.getNewValue().equals(t.getOldValue()) && ((Reader) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() != 2)
	            	    ((Reader) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDTag(1);
	                ((Reader) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPassword(t.getNewValue());
	            }
	        });
	        
	        readerTable.setItems(readerData);
	        readerTable.getColumns().addAll(numCol, nameCol, passwordCol);
	      
	        final Button addButton = new Button("Add");
	        final Button deleteButton = new Button("Delete");
	                
	        deleteButton.setOnAction(new EventHandler<ActionEvent>(){
	        	public void handle(ActionEvent e)
	        	{	Reader p = readerTable.getSelectionModel().getSelectedItem();
	        		int pos = readerTable.getSelectionModel().getSelectedIndex();
	        		
	        		if(p != null)
	        	    {   String delName = p.getName();
	        			if(CommonDialog.ConfirmDialog("温馨提示", "确认要删除名字为 《"+delName+"》 的学生吗？"))
	        			{   if(p.getDTag() != 2)  // 不是新增加的记录，需要删除数据库中的记录
	        			        deleteDataFromReader(p.getUid());      			
	        		        readerData.remove(pos);
	        		    }
	        		}
	        		else
	        		    CommonDialog.WarningDialog("你没有选中需要删除的记录"); 
	        	}
	            
	        });
	        
	        addButton.setOnAction(new EventHandler<ActionEvent>(){
	        	public void handle(ActionEvent e)
	        	{	readerData.add(new Reader("", "", "", 2));
	        	    readerTable.refresh();
	   	            readerTable.getSelectionModel().selectLast();
	   	            readerTable.scrollTo(readerData.size());
	   	            readerTable.requestFocus();
	        	}
	            
	        });
	                
	        final HBox hb = new HBox();
	        hb.getChildren().addAll(addButton,deleteButton);
	        hb.setSpacing(20);
	        hb.setAlignment(Pos.CENTER_LEFT);
	        hb.setMargin(addButton, new Insets(0, 0, 0, 10));
	                                
	        ScrollPane spp = new ScrollPane(readerTable);
	        spp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
	        spp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
	        
	        setCenter(spp);
	        setBottom(hb);
	        setMargin(hb, new Insets(5, 10, 5, 10));
	        
	    }
	    
	    private void deleteDataFromReader(String numstr)
	    {   try
	        {   Connection conn = DriverManager.getConnection(DBUrl, DBUser, DBPassword);
	            Statement stmt = conn.createStatement();
	            String deleteSql = "DELETE FROM Reader WHERE uid='"+numstr+"'";
	            int count = stmt.executeUpdate(deleteSql); 
	            System.out.println("删除了 Reader 表的《"+numstr+"》"+count+"条记录");
	            updateReaderDataBase();
	            stmt.close();
	            conn.close();
	        }
	        catch(Exception e) 
	        {   e.printStackTrace();
	        }
	    }
	    
	    public void updateReaderDataBase()
	    {   Reader up;
	        String unum, uname, upassword;
	        int udtag;
	                
	        try
	        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
	            Statement stmt=conn.createStatement();
	                        
	            for(int i=0;i<readerData.size();i++)
	            {   up = (Reader)readerData.get(i);
	                udtag = up.getDTag();
	                unum = up.getUid();
	                uname = up.getName();
	                upassword = up.getPassword();
	                if(udtag == 2)  // 新增加记录
	                {   String insertSql="INSERT INTO reader(uid,uname,password) "+
	                                     "VALUES('"+unum+"','"+uname+"','"+upassword+"')";
	                    int count=stmt.executeUpdate(insertSql);
	                    System.out.println("添加 "+ count+" 条记录到reader表中");
	                }
	                
	                if(udtag == 1)  // 更改过的记录
	                {   String updateSql="UPDATE reader SET uname='"+uname+"',password='"+upassword+"' WHERE uid='"+unum+"'";
	                    System.out.println(updateSql);
	                    int count=stmt.executeUpdate(updateSql);
	                    System.out.println(updateSql);
	                    System.out.println("修改 "+ count+" 条记录到Reader表中");
	                }
	            } 
	                           
	            stmt.close();
	            conn.close();
	        }
	        catch(Exception e) 
	        {   e.printStackTrace();
	        }
	    }
	       
	    private void loadDataFromReader()
	    {   String sql="SELECT uid,uname,password FROM reader";
	               
	        try
	        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
	            Statement stmt=conn.createStatement();
	            
	            readerData.clear();
	            ResultSet rs=stmt.executeQuery(sql);
	            while(rs.next())
	            {   String no=rs.getString("uid"); if(no==null) no="";
	                String name=rs.getString("uname"); if(name==null) name="";
	                String password=rs.getString("password"); if(password==null) password="";
	                readerData.add(new Reader(no,name, password,0));
	            }
	            rs.close();
	            stmt.close();
	            conn.close();
	        }
	        catch(Exception e) 
	        {   e.printStackTrace();
	        }
	    }
	    
	    public static class Reader
	    {   private final SimpleStringProperty uid;
	    	private final SimpleStringProperty name;
	        private final SimpleStringProperty password;
	        private int dtag; // 0 无需更改      1 编辑更改过的数据   2  新增加的记录

	        private Reader(String uUid, String uName, String uPassword,int udtag) 
	        {   this.uid = new SimpleStringProperty(uUid);
	            this.name = new SimpleStringProperty(uName);
	            this.password = new SimpleStringProperty(uPassword);
	            dtag = udtag;
	        }
	        
//	        private Reader(String uUid, String uName, String uPassword)
//	        {   this(uUid, uName, uPassword,0);
//	        }
	        
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

