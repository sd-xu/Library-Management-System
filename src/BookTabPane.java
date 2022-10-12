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

public class BookTabPane extends BorderPane {
	private final TableView<Book> bookTable = new TableView<>();
	private final ObservableList<Book> bookData = FXCollections.observableArrayList();
	
//	String DBUrl="jdbc:ucanaccess:///D:/eclipse-workspace/Library-Management-System3/src/management.accdb";
	String DBUrl="jdbc:ucanaccess:///D:/Codefield/CODE_Java/EclipseProjects/Library-Management-System/src/management.accdb";

    String DBUser="", DBPassword="";
    
    public BookTabPane(int ulf,String uid,String uname) 
    {   loadDataFromBook(); 
        if(ulf ==0)
        	bookTable.setEditable(false);
        else 
        	bookTable.setEditable(true);
        bookTable.setMaxWidth(730);
        
        Callback<TableColumn<Book, String>, TableCell<Book, String>> cellStringFactory = 
        		new Callback<TableColumn<Book, String>, TableCell<Book, String>>() 
                {   public TableCell<Book, String> call(TableColumn p) 
                    {   return new EditingStringCell<Book>();  
                    }  
                };  
        Callback<TableColumn<Book, Integer>, TableCell<Book, Integer>> cellIntegerFactory = 
                new Callback<TableColumn<Book, Integer>, TableCell<Book, Integer>>() 
                {   public TableCell<Book, Integer> call(TableColumn p) 
                    {   return new EditingIntegerCell<Book>();  
                    }  
                };  
                       
        TableColumn<Book, Integer> numCol = new TableColumn<>("编号");
        numCol.setMinWidth(50);
        numCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("bookId"));
        numCol.setCellFactory(cellIntegerFactory);
        numCol.setOnEditCommit(new EventHandler<CellEditEvent<Book, Integer>>() {
            @Override
            public void handle(CellEditEvent<Book, Integer> t) {
            	if(!t.getNewValue().equals(t.getOldValue()) && ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() != 2)
            	    ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDTag(1);
                ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setBookId(t.getNewValue());
            }
        });
        
        TableColumn<Book, String> nameCol = new TableColumn<>("书籍名称");
        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(new PropertyValueFactory<Book, String>("bookName"));
        nameCol.setCellFactory(cellStringFactory);
        nameCol.setOnEditCommit(new EventHandler<CellEditEvent<Book, String>>() {
            @Override
            public void handle(CellEditEvent<Book, String> t) {
            	if(!t.getNewValue().equals(t.getOldValue()) && ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() != 2)
            	    ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDTag(1);
                ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setBookName(t.getNewValue());
        
            }
        });
                    
        TableColumn<Book, Integer> quantityCol = new TableColumn<>("数量");
        quantityCol.setMinWidth(40);
        quantityCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("bookQuantity"));
        quantityCol.setCellFactory(cellIntegerFactory);
        quantityCol.setOnEditCommit(new EventHandler<CellEditEvent<Book, Integer>>() {
            @Override
            public void handle(CellEditEvent<Book, Integer> t) {
            	if(!t.getNewValue().equals(t.getOldValue()) && ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() != 2)
            	    ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDTag(1);
                ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setBookQuantity(t.getNewValue());
            }
        });
               
        TableColumn<Book, String> authorCol = new TableColumn<>("作者");
        authorCol.setMinWidth(130);
        authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("bookAuthor"));
        authorCol.setCellFactory(cellStringFactory);
        authorCol.setOnEditCommit(new EventHandler<CellEditEvent<Book, String>>() {
            @Override
            public void handle(CellEditEvent<Book, String> t) {
            	if(!t.getNewValue().equals(t.getOldValue()) && ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() != 2)
            	    ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDTag(1);
                ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setBookAuthor(t.getNewValue());
            }
        });
        
        TableColumn<Book, String> publisherCol = new TableColumn<>("出版社");
        publisherCol.setMinWidth(200);
        publisherCol.setCellValueFactory(new PropertyValueFactory<Book, String>("bookPublisher"));
        publisherCol.setCellFactory(cellStringFactory);
        publisherCol.setOnEditCommit(new EventHandler<CellEditEvent<Book, String>>() {
            @Override
            public void handle(CellEditEvent<Book, String> t) {
            	if(!t.getNewValue().equals(t.getOldValue()) && ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() != 2)
            	    ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDTag(1);
                ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setBookPublisher(t.getNewValue());
            }
        });
        
        TableColumn<Book, String> availabilityCol = new TableColumn<>("借阅");
        availabilityCol.setMinWidth(110);
        availabilityCol.setCellValueFactory(new PropertyValueFactory<Book, String>("availability"));
        availabilityCol.setCellFactory(cellStringFactory);
        availabilityCol.setOnEditCommit(new EventHandler<CellEditEvent<Book, String>>() {
            @Override
            public void handle(CellEditEvent<Book, String> t) {
            	if(!t.getNewValue().equals(t.getOldValue()) && ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() != 2)
            	    ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDTag(1);
                ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAvailability(t.getNewValue());
            }
        });
        
        bookTable.setItems(bookData);
        bookTable.getColumns().addAll(numCol, nameCol, quantityCol, authorCol, publisherCol, availabilityCol);    
      
        final Button borrowButton = new Button("借书");
        
        borrowButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{	Book p = bookTable.getSelectionModel().getSelectedItem();
        			if(p != null && p.getAvailability().equals("yes"))
        				{	BorrowDialog addeditD = new BorrowDialog(p,uid,uname);  
        				}
        			else if(p.getAvailability().equals("no"))
        				CommonDialog.WarningDialog("当前书籍不可借阅"); 
        			else
        				CommonDialog.WarningDialog("你没有选中要借阅的书籍");
    					}
        });
        
        
        final Button addButton = new Button("Add");
        final Button deleteButton = new Button("Delete");
                
        deleteButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{	Book p = bookTable.getSelectionModel().getSelectedItem();
        		int pos = bookTable.getSelectionModel().getSelectedIndex();
        		
        		if(p != null)
        	    {   String delName = p.getBookName();
        			if(CommonDialog.ConfirmDialog("温馨提示", "确认要删除名字为 《"+delName+"》 的书籍吗？"))
        			{   if(p.getDTag() != 2)  // 不是新增加的记录，需要删除数据库中的记录
        			        deleteDataFromBook(p.getBookId());      			
        		        bookData.remove(pos);
        		    }
        		}
        		else
        		    CommonDialog.WarningDialog("你没有选中需要删除的记录"); 
        	}
            
        });
        

        addButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{	bookData.add(new Book(0,"",0, "", "","",2));
    	    	bookTable.refresh();
    	    	bookTable.getSelectionModel().selectLast();
    	    	bookTable.scrollTo(bookData.size());
    	    	bookTable.requestFocus();
    	    	updateBookDataBase();
        	}
            
        });
               
        final HBox hb = new HBox();
        if(ulf==0)
        	hb.getChildren().addAll(borrowButton);
        else
        	hb.getChildren().addAll(addButton,deleteButton);
        hb.setSpacing(20);
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.setMargin(borrowButton, new Insets(0, 0, 0, 10));
        hb.setMargin(addButton, new Insets(0, 0, 0, 10));
                                
        ScrollPane spp = new ScrollPane(bookTable);
        spp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        spp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        
        setCenter(spp);
        setBottom(hb);
        setMargin(hb, new Insets(5, 10, 5, 10));
        
    }
    
        
    private class BorrowDialog
    {   Stage borrowStage;
    
        Label numLB = new Label("编号：");
        TextField numTF = new TextField();
    
    	Label nameLB = new Label("书名：");
	    TextField nameTF = new TextField();
        
        Label quantityLB = new Label("数量：");
	    TextField quantityTF = new TextField();
        	    
	    Label authorLB = new Label("作者：");
	    TextField authorTF = new TextField();
	    		
	    Label publisherLB = new Label("出版社：");
	    TextField publisherTF = new TextField();
	    
	    Label availabilityLB = new Label("是否可借阅：");
	    TextField availabilityTF = new TextField();
	    
		    	
	    Button EditButton;
	    Book opp;

	    public BorrowDialog(Book p,String uid,String uname)
	    {   opp = p;
	        numTF.setPrefWidth(80);
	        nameTF.setPrefWidth(100);
	        quantityTF.setPrefWidth(40);
	        authorTF.setPrefWidth(120);
	        publisherTF.setPrefWidth(240);
	        availabilityTF.setPrefWidth(120);
    	   	
    	    EditButton = new Button("借书");
    	    EditButton.setStyle("-fx-text-fill:blue");
    	    numTF.setText(""+p.getBookId());
    	    numTF.setDisable(true);
    	    nameTF.setText(p.getBookName());
    	    nameTF.setDisable(true);
    	    quantityTF.setText(""+p.getBookQuantity());
    	    quantityTF.setDisable(true);
    	    authorTF.setText(""+p.getBookAuthor());
    	    authorTF.setDisable(true);
    	    publisherTF.setText(p.getBookPublisher());
    	    publisherTF.setDisable(true);
    	    availabilityTF.setText(p.getAvailability());
    	    availabilityTF.setDisable(true);
    	    EditButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e)
                {	opp.setBookId(Integer.parseInt(numTF.getText()));
                	opp.setBookName(nameTF.getText());
        	   	    opp.setBookAuthor((String)authorTF.getText());
        	   	    opp.setBookPublisher((String)publisherTF.getText());
        	   	    opp.setAvailability((String)availabilityTF.getText());
        	   	    opp.setDTag(1);
        	   	    String recordID = Integer.parseInt(numTF.getText())-1000+"";
        	   	    if(Integer.parseInt(quantityTF.getText())-1 != 0)
        	   	    	opp.setBookQuantity(Integer.parseInt(quantityTF.getText())-1);
        	   	    else {
        	   	    	opp.setBookQuantity(Integer.parseInt(quantityTF.getText())-1);
        	   	    	opp.setAvailability("no");
        	   	    }
        	   	    try
        	   	    	{   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
        	   	    		Statement stmt=conn.createStatement();
        	   	    		String insertSql="INSERT INTO record(recordId,bookId,bookName,uid,uname) "+
                                "VALUES("+recordID+","+Integer.parseInt(numTF.getText())+",'"+nameTF.getText()+"','"+uid+"','"+uname+"')";
        	   	    		int count=stmt.executeUpdate(insertSql);
        	   	    		updateBookDataBase();
        	   	    		System.out.println(count);

			            stmt.close();
			            conn.close();
			        }
			        catch(Exception e1) 
			        {   e1.printStackTrace();
			        }    	
                    
        	   	    bookTable.refresh();
        	   	    borrowStage.close();    	    
                }
            });
    	    
    	    EditButton.setPrefWidth(60);
    	    Button cancelButton = new Button("取消");
    	    cancelButton.setOnAction(new EventHandler<ActionEvent>(){
        	    public void handle(ActionEvent e)
        	    {	borrowStage.close();    	    
        	    }
            });
    	
    	    HBox hb1 = new HBox();
            hb1.getChildren().addAll(numLB, numTF, nameLB, nameTF,quantityLB, quantityTF);
            hb1.setMargin(numLB, new Insets(3,0,0,0));
            hb1.setMargin(nameLB, new Insets(3,0,0,10));
            hb1.setMargin(quantityLB, new Insets(3,0,0,15));
                    
            HBox hb2 = new HBox();
            hb2.getChildren().addAll(authorLB, authorTF,publisherLB,publisherTF);
            hb2.setMargin(authorLB, new Insets(3,0,0,0));
            hb2.setMargin(publisherLB, new Insets(3,0,0,15));
            
            HBox hb3 = new HBox();
            hb3.getChildren().addAll(availabilityLB,availabilityTF);
            hb3.setMargin(availabilityLB, new Insets(3,0,0,0));
            
                                
            HBox hb4 = new HBox();
            hb4.setSpacing(20);
            hb4.setAlignment(Pos.CENTER);
            hb4.getChildren().addAll(EditButton, cancelButton);
        
            VBox vbox = new VBox();
            vbox.setSpacing(15);
            vbox.setPadding(new Insets(30, 10, 10, 30));
            vbox.getChildren().addAll(hb1,hb2,hb3);
            vbox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                    + "-fx-border-width: 1;" + "-fx-border-insets: 10;"
                    + "-fx-border-radius: 10;" + "-fx-border-color: gray lightgray lightgray gray;");
            
            borrowStage = new Stage();
            borrowStage.setX(750);
            borrowStage.setY(500);
            borrowStage.initModality(Modality.APPLICATION_MODAL);

            BorderPane bPane = new BorderPane();
            bPane.setCenter(vbox);
            bPane.setBottom(hb4);
            bPane.setPadding(new Insets(0,0,15,0));
            borrowStage.setScene(new Scene(bPane, 540, 200));
            borrowStage.setTitle("借书界面");
            borrowStage.showAndWait();
	    }
    }
        
    //通过书名直接删除某本书
    private void deleteDataFromBook(int num)
    {   try
        {   Connection conn = DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt = conn.createStatement();
            String deleteSql = "DELETE FROM book WHERE bookId='"+num+"'";
            int count = stmt.executeUpdate(deleteSql); 
            //System.out.println("删除了 book 表的《"+num+"》"+count+"条记录");
            updateBookDataBase();
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    public void updateBookDataBase()
    {   Book up;
        String uname,ua,upb,ava;
        int uid,uq,udtag;
                
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
                        
            for(int i=0;i<bookData.size();i++)
            {   up = (Book)bookData.get(i);
                uid = up.getBookId();
                uname = up.getBookName();
                uq = up.getBookQuantity();
                ua = up.getBookAuthor();
                upb = up.getBookPublisher();
                ava = up.getAvailability();
                udtag = up.getDTag();
                if(udtag == 2)  // 新增加记录
                {   String insertSql="INSERT INTO book(bookId,bookName,bookQuantity,bookAuthor,bookPublisher,availability) "+
                                     "VALUES("+uid+",'"+uname+"',"+uq+",'"+ua+"','"+upb+"','"+ava+"')";
                    int count=stmt.executeUpdate(insertSql);
                    System.out.println(count);
                }
                
                if(udtag == 1)  // 更改过的记录
                {  
                    String updateSql="UPDATE book SET bookName='"+uname+"',bookQuantity="+uq+",bookAuthor='"+ua+"',bookPublisher='"+upb+"',availability='"+ava+"' WHERE bookId="+uid;
                    System.out.println(upb);
	                System.out.println(updateSql);
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
       
    private void loadDataFromBook()
    {   String sql="SELECT bookId,bookName,bookQuantity,bookAuthor,bookPublisher,availability FROM book";
               
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            
            bookData.clear();
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {   int no=rs.getInt("bookId");
                String name=rs.getString("bookName"); if(name==null) name="";
                int quantity=rs.getInt("bookQuantity"); 
                String aut=rs.getString("bookAuthor"); if(aut==null) aut="";
                String pub=rs.getString("bookPublisher"); if(pub==null) pub="";
                String ava=rs.getString("availability"); if(ava==null) ava="";
                bookData.add(new Book(new Integer(no), name,new Integer(quantity), aut, pub, ava,0));
//                bookData.add(new Book(no, name, quantity, aut, pub, ava, 0));
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    public static class Book
    {   private final SimpleIntegerProperty bookId;
    	private final SimpleStringProperty bookName;
        private final SimpleIntegerProperty bookQuantity;
        private final SimpleStringProperty bookAuthor;
        private final SimpleStringProperty bookPublisher;
        private final SimpleStringProperty availability;
        private int dtag; // 0 无需更改      1 编辑更改过的数据   2  新增加的记录
        
        private Book(int ubookid, String ubookname, int ubookq, String ubooka, String ubookp,String uava,int udtag) 
        {   this.bookId = new SimpleIntegerProperty(ubookid);
            this.bookName = new SimpleStringProperty(ubookname);
            this.bookQuantity = new SimpleIntegerProperty(ubookq);
            this.bookAuthor = new SimpleStringProperty(ubooka);
            this.bookPublisher = new SimpleStringProperty(ubookp);
            this.availability = new SimpleStringProperty(uava);  
            dtag = udtag;
        }
        
        public int getBookId() 
        {   return bookId.get();
        }
        
        public void setBookId(int ubookid) 
        {   bookId.set(ubookid);
        }
        
        public String getBookName() 
        {   return bookName.get();
        }

        public void setBookName(String uName) 
        {   bookName.set(uName);
        }

        public int getBookQuantity() 
        {   return bookQuantity.get();
        }
        
        public void setBookQuantity(int uq) 
        {   bookQuantity.set(uq);
        }

        public String getBookAuthor() 
        {   return bookAuthor.get();
        }

        public void setBookAuthor(String ua) 
        {   bookAuthor.set(ua);
        }
        
        public String getBookPublisher() 
        {   return bookPublisher.get();
        }

        public void setBookPublisher(String up) 
        {   bookPublisher.set(up);
        }
        
        public void setAvailability(String ava) 
        {   availability.set(ava);
        }
        
        public String getAvailability() 
        {   return availability.get();
        }
        
        public void setDTag(int udtag) 
        {   dtag = udtag;
        }
        
        public int getDTag() 
        {   return dtag;
        }
    }
}