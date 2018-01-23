package javaDemo.view;

import javaDemo.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RootLayout {

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    @FXML
    private void handleLogic() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LogicPage.fxml"));
        AnchorPane target = (AnchorPane)loader.load();
        Scene scene = new Scene(target); //创建场景；
        Stage stg=new Stage();//创建舞台；
        stg.setScene(scene); //将场景载入舞台；
        stg.show(); //显示窗口；
        LogicPage controller = loader.getController();
        controller.setMainApp(this.mainApp);
    }

}
