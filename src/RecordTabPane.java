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
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.sql.*;

class RecordTabPane extends BorderPane
{
	private final TableView<Record> recordTable = new TableView<>();
    private final ObservableList<Record> recordData = FXCollections.observableArrayList();
//    String DBUrl="jdbc:ucanaccess:///D:/eclipse-workspace/Library-Management-System3/src/management.accdb";
    String DBUrl="jdbc:ucanaccess:///D:/Codefield/CODE_Java/EclipseProjects/Library-Management-System/src/management.accdb";
    String DBUser="", DBPassword="";
    
    public RecordTabPane(int ulf, String uid)
    {	loadDataFromRecord(ulf, uid); //  从数据库提取 Record 数据更新 data 列表
    	if(ulf ==0)
        	recordTable.setEditable(false);
        else 
        	recordTable.setEditable(true);
        recordTable.setMaxWidth(1000);
        
        Callback<TableColumn<Record, String>, TableCell<Record, String>> cellStringFactory = 
        	new Callback<TableColumn<Record, String>, TableCell<Record, String>>() 
            {   public TableCell<Record, String> call(TableColumn p) 
                {   return new EditingStringCell<Record>();  
                }  
            };
        Callback<TableColumn<Record, Integer>, TableCell<Record, Integer>> cellIntegerFactory = 
            new Callback<TableColumn<Record, Integer>, TableCell<Record, Integer>>() 
            {   public TableCell<Record, Integer> call(TableColumn p) 
                {   return new EditingIntegerCell<Record>();  
                }  
            };

        TableColumn<Record, Integer> recordIdCol = new TableColumn<>("借阅编号");
        recordIdCol.setMinWidth(100);
        recordIdCol.setCellValueFactory(new PropertyValueFactory<Record, Integer>("recordId"));
        recordIdCol.setCellFactory(cellIntegerFactory);
        recordIdCol.setOnEditCommit(new EventHandler<CellEditEvent<Record, Integer>>() {
            @Override
            public void handle(CellEditEvent<Record, Integer> t) {
            	if(!t.getNewValue().equals(t.getOldValue()) && ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() != 2)
            	    ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDTag(1);
                ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).setRecordId(t.getNewValue());
            }
        });
        
        TableColumn<Record, Integer> bookIdCol = new TableColumn<>("书本编号");
        bookIdCol.setMinWidth(100);
        bookIdCol.setCellValueFactory(new PropertyValueFactory<Record, Integer>("bookId"));
        bookIdCol.setCellFactory(cellIntegerFactory);
        bookIdCol.setOnEditCommit(new EventHandler<CellEditEvent<Record, Integer>>() {
            @Override
            public void handle(CellEditEvent<Record, Integer> t) {
            	if(!t.getNewValue().equals(t.getOldValue()) && ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() != 2)
            	    ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDTag(1);
                ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).setBookId(t.getNewValue());
            }
        });
        
        TableColumn<Record, String> bookNameCol = new TableColumn<>("书籍名称");
        bookNameCol.setMinWidth(200);
        bookNameCol.setCellValueFactory(new PropertyValueFactory<Record, String>("bookName"));
        bookNameCol.setCellFactory(cellStringFactory);
        bookNameCol.setOnEditCommit(new EventHandler<CellEditEvent<Record, String>>() {
            @Override
            public void handle(CellEditEvent<Record, String> t) {
            	if(!t.getNewValue().equals(t.getOldValue()) && ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() != 2)
            	    ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDTag(1);
                ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).setBookName(t.getNewValue());
            }
        });
                
        TableColumn<Record, String> uidCol = new TableColumn<>("读者ID");
        uidCol.setMinWidth(150);
        uidCol.setCellValueFactory(new PropertyValueFactory<Record, String>("uid"));
        uidCol.setCellFactory(cellStringFactory);
        uidCol.setOnEditCommit(new EventHandler<CellEditEvent<Record, String>>() {
            @Override
            public void handle(CellEditEvent<Record, String> t) {
            	if(!t.getNewValue().equals(t.getOldValue()) && ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() != 2)
            	    ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDTag(1);
                ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).setUid(t.getNewValue());
            }
        });

        TableColumn<Record, String> unameCol = new TableColumn<>("读者名字");
        unameCol.setMinWidth(180);
        unameCol.setCellValueFactory(new PropertyValueFactory<Record, String>("uname"));
        unameCol.setCellFactory(cellStringFactory);
        unameCol.setOnEditCommit(new EventHandler<CellEditEvent<Record, String>>() {
            @Override
            public void handle(CellEditEvent<Record, String> t) {
            	if(!t.getNewValue().equals(t.getOldValue()) && ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() != 2)
            	    ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDTag(1);
                ((Record) t.getTableView().getItems().get(t.getTablePosition().getRow())).setUname(t.getNewValue());
            }
        });        
        
        recordTable.setItems(recordData);
        recordTable.getColumns().addAll(recordIdCol, bookIdCol, bookNameCol, uidCol, unameCol);


        final Button returnButton = new Button("还书");
        
        returnButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{	Record p = recordTable.getSelectionModel().getSelectedItem();
        		int pos = recordTable.getSelectionModel().getSelectedIndex();
        		if(p != null)
        		{	int delRecordId = p.getRecordId();
        			int returnBookId = p.getBookId();
    				if(CommonDialog.ConfirmDialog("温馨提示", "确认所归还的书的借阅记录的编号为 "+delRecordId+" 吗？"))
    				{    
    					if(p.getDTag() != 2) { // 不是新增加的记录，需要删除数据库中的记录
    						try
    				        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
    				            Statement stmt=conn.createStatement();
    				            String updateSql="UPDATE book SET bookQuantity=bookQuantity+1,availability='yes' WHERE bookId="+returnBookId;
    				            int count=stmt.executeUpdate(updateSql);
    				            System.out.println(count);
    				            updateRecordDataBase();
      
    				            stmt.close();
    				            conn.close();
    				        }
    				        catch(Exception e1) 
    				        {   e1.printStackTrace();
    				        }    						  
    					}
    					deleteDataFromRecord(p.getRecordId()); 
    					recordData.remove(pos);
    				} 
        		}
        		else
        			CommonDialog.WarningDialog("你没有选中要借阅的书籍");
    		}
        });
        
        final Button addButton = new Button("Add");
        final Button deleteButton = new Button("Delete");
                
        deleteButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{	Record p = recordTable.getSelectionModel().getSelectedItem();
        		int pos = recordTable.getSelectionModel().getSelectedIndex();
        		
        		if(p != null)
        	    {   int delRecordId = p.getRecordId();
        			if(CommonDialog.ConfirmDialog("温馨提示", "确认要删除借阅编号为 "+delRecordId+" 的借阅记录吗？"))
        			{    
        				if(p.getDTag() != 2)  // 不是新增加的记录，需要删除数据库中的记录     	            	
        					deleteDataFromRecord(p.getRecordId());      			
        		        recordData.remove(pos);
        		    }
        		}
        		else
        		    CommonDialog.WarningDialog("你没有选中需要删除的记录"); 
        	}
            
        });
        
        addButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{	recordData.add(new Record(0, 0, "", "", "", 2));
        	    recordTable.refresh();
        	    recordTable.getSelectionModel().selectLast();
        	    recordTable.scrollTo(recordData.size());
        	    recordTable.requestFocus();
        	}
            
        });
                
        final HBox hb = new HBox();
        if(ulf==0)
        	hb.getChildren().addAll(returnButton);
        else
        	hb.getChildren().addAll(addButton,deleteButton);
        hb.setSpacing(20);
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.setMargin(returnButton, new Insets(0, 0, 0, 10));
        hb.setMargin(addButton, new Insets(0, 0, 0, 10));
        
                                
        ScrollPane spp = new ScrollPane(recordTable);
        spp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        spp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        
        setCenter(spp);
        setBottom(hb);
        setMargin(hb, new Insets(5, 10, 5, 10));
    }
    
    private void deleteDataFromRecord(int rid)
    {   try
        {   Connection conn = DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt = conn.createStatement();
            String deleteSql = "DELETE FROM record WHERE recordId="+rid;
            int count = stmt.executeUpdate(deleteSql); 
            System.out.println("删除了 Record 表中编号为 "+rid+" 的借阅记录");
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    public void updateRecordDataBase()
    {   Record up;
        String bookName, uid, uname;
        int recordId, bookId, udtag;
        
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
                        
            for(int i=0;i<recordData.size();i++)
            {   up = (Record)recordData.get(i);
            	recordId = up.getRecordId();
            	bookId = up.getBookId();
            	bookName = up.getBookName();
                uid = up.getUid();
                uname = up.getUname();
                udtag = up.getDTag();
                if(udtag == 2)  // 新增加记录
                {   String insertSql="INSERT INTO record(recordId,bookId,bookName,uid,uname) "+
                                     "VALUES("+recordId+","+bookId+",'"+bookName+"','"+uid+"','"+uname+"')";
                    int count=stmt.executeUpdate(insertSql);
                    System.out.println(count);
                }
                else if(udtag == 1)  // 更改过的记录
                {  
                    String updateSql="UPDATE record SET bookId="+bookId+",bookName='"+bookName+"',uid='"+uid+"',uname='"+uname+"' WHERE recordId="+recordId;
                    int count=stmt.executeUpdate(updateSql);
                    System.out.println(count);
                }
            } 
                           
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    private void loadDataFromRecord(int ulf, String readerID)
    {   String sql;
    	if(ulf == 0) {
    		sql="SELECT recordId,bookId,bookName,uid,uname FROM record WHERE uid='"+readerID+"'";
    	}
    	else {
    		sql="SELECT recordId,bookId,bookName,uid,uname FROM record";
    	}
        
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            
            recordData.clear();
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {	int rid = rs.getInt("recordId");
             	int bid = rs.getInt("bookId");
                String bookName = rs.getString("bookName"); if(bookName==null) bookName="";
                String uid = rs.getString("uid"); if(uid==null) uid="";
                String uname = rs.getString("uname"); if(uname==null) uname="";
                recordData.add(new Record(new Integer(rid), new Integer(bid), bookName, uid, uname, 0));
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    public static class Record
    {	private final SimpleIntegerProperty recordId;
    	private final SimpleIntegerProperty bookId;
    	private final SimpleStringProperty bookName;
        private final SimpleStringProperty uid;
        private final SimpleStringProperty uname;
        private int dtag; // 0 无需更改      1 编辑更改过的数据   2  新增加的记录
        
        private Record(int recordId, int bookId, String bookName, String uid, String uname, int udtag)
        {   this.recordId = new SimpleIntegerProperty(recordId);
        	this.bookId = new SimpleIntegerProperty(bookId);
        	this.bookName = new SimpleStringProperty(bookName);
            this.uid = new SimpleStringProperty(uid);
            this.uname = new SimpleStringProperty(uname);
            dtag = udtag;
        }
        
        public int getRecordId() 
        {   return recordId.get();
        }
        
        public void setRecordId(int urecordId) 
        {   recordId.set(urecordId);
        }
        
        public int getBookId() 
        {   return bookId.get();
        }
        
        public void setBookId(int ubookId) 
        {   bookId.set(ubookId);
        }
        
        public String getBookName() 
        {   return bookName.get();
        }

        public void setBookName(String ubookName) 
        {   bookName.set(ubookName);
        }
        
        public String getUid() 
        {   return uid.get();
        }
        
        public void setUid(String uuid) 
        {   uid.set(uuid);
        }

        public String getUname() 
        {   return uname.get();
        }

        public void setUname(String uuname) 
        {   uname.set(uuname);
        }
        
        public void setDTag(int udtag) 
        {   dtag = udtag;
        }
        
        public int getDTag() 
        {   return dtag;
        }
    }
}