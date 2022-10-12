import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Modality;

class InfoInputDialog extends Stage {   
	BookTabPane btp;
	RecordTabPane rtp;
	ReaderTabPane itp;
    OperatorTabPane otp;

    public InfoInputDialog()
    {   
    	BorderPane infoBP = new BorderPane();   
    
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-tab-min-width:90px; -fx-tab-max-width:9px; -fx-tab-min-height:30px; -fx-tab-max-height:30px;");
        tabPane.setStyle("-fx-background-color:lightgray;");
        
        Tab bookTab = new Tab(" ��Ŀ ");
        bookTab.setClosable(false);
        bookTab.setStyle("-fx-font-family:kaiti; -fx-font-size: 14;-fx-background-radius: 20");
        btp = new BookTabPane(1,"","");
        bookTab.setContent(btp);
        
        Tab recordTab = new Tab(" ���ļ�¼ ");
        recordTab.setClosable(false);
        recordTab.setStyle("-fx-font-family:kaiti; -fx-font-size: 14;-fx-background-radius: 20;");
        rtp = new RecordTabPane(1, "");
        recordTab.setContent(rtp);
        
        
        Tab readerTab = new Tab(" ���� ");
        readerTab.setClosable(false);
        readerTab.setStyle("-fx-font-family:kaiti; -fx-font-size: 14;-fx-background-radius: 20;");
        itp = new ReaderTabPane();
        readerTab.setContent(itp);
        
        Tab managTab = new Tab(" ����Ա ");
        managTab.setClosable(false);
        managTab.setStyle("-fx-font-family:kaiti; -fx-font-size: 14;-fx-background-radius: 20;");
        otp = new OperatorTabPane();
        managTab.setContent(otp);
        
        tabPane.getTabs().addAll(bookTab, recordTab, readerTab, managTab);
        tabPane.setPadding(new Insets(4, 0, 0, 0));
        
        infoBP.setCenter(tabPane);
        Button exitButton = new Button("�˳�", new ImageView(new Image("image/quit.png")));
        
        exitButton.setOnAction(e->{
            btp.updateBookDataBase();	
            rtp.updateRecordDataBase();
            itp.updateReaderDataBase();
            otp.updateOperatorDataBase();
            close();  // �ر��������봰��
        });
               
        infoBP.setBottom(exitButton);
        infoBP.setAlignment(exitButton, Pos.CENTER_RIGHT);
        infoBP.setMargin(exitButton, new Insets(8, 20, 8, 10));
        Scene scene=new Scene(infoBP, 730, 510);
        setTitle("����¼��");
        setScene(scene);
        setX(430);
        setY(250);
        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }
}