package javaDemo;

import javaDemo.model.Function;
import javaDemo.view.FunctionOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Function> functionData = FXCollections.observableArrayList();


    public MainApp() {
        // Add some sample data



    }

    public ObservableList<Function> getFunctionData() {
        return functionData;
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("JavaDemo");
        primaryStage.setMaximized(true);

        initRootLayout();
        showFunctionOverview();
    }

    public void initRootLayout(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void showFunctionOverview(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/FunctionOverview.fxml"));
            AnchorPane functionOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(functionOverview);

            FunctionOverviewController controller = loader.getController();
            controller.setMainApp(this);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }
}
