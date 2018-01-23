package javaDemo.view;

import javaDemo.MainApp;
import javaDemo.model.Function;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.ComboBoxListCell;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import javax.swing.text.TableView;
import javax.swing.text.html.ListView;

import java.util.ArrayList;

import static javaDemo.util.processCollection.getMethodNameWithoutExtra;
import static javaDemo.util.processCollection.getMethodParamNames;
import static javaDemo.util.processCollection.getParaTypeFromMethod;

public class LogicPage {
    private MainApp mainApp;
    @FXML
    private TableColumn<Function,String> classNameColumn;

    @FXML
    private TableColumn<Function,String> methodNameColumn;

    @FXML
    private TableColumn<Function,String> levelColumn;

    @FXML
    private TableColumn<Function,String> enableNameColumn;
    @FXML
    private javafx.scene.control.TableView<Function> functionTabel1;

    @FXML
    private TreeView<String> processResult;

    int num = 1;


    TreeItem<String> rootNode;

    public static final ObservableList names =
            FXCollections.observableArrayList();

    public LogicPage() {
        this.rootNode = new TreeItem<>("函数调用逻辑");
    }

    public void setMainApp(MainApp mainApp) {

        this.mainApp = mainApp;
        functionTabel1.setItems(mainApp.getFunctionData());
    }

    @FXML
    private void initialize(){
        showClassAndMethodInTable();
//        functionTabel.getSelectionModel().selectedItemProperty().addListener(
//                (observable, oldValue, newValue) > showType(newValue));
        rootNode.setExpanded(true);
        processResult.setRoot(rootNode);
    }

    @FXML
    private void addFunction(){
        Function selectedFunction = functionTabel1.getSelectionModel().getSelectedItem();
        TreeItem newFunction =
                new TreeItem<>(num +" "+ selectedFunction.classNameProperty().getValue()+"."+getMethodNameWithoutExtra(selectedFunction.methodNameProperty().getValue()));
        ClassPool pool = ClassPool.getDefault();
        CtClass pt = null;
        ArrayList<String> paraTypes = getParaTypeFromMethod(selectedFunction.methodNameProperty().getValue());
        try {
            pt = pool.get(selectedFunction.classNameProperty().getValue());
            CtMethod m1 = pt.getDeclaredMethod(getMethodNameWithoutExtra(selectedFunction.methodNameProperty().getValue()));
            String[] params = getMethodParamNames(m1);
            for(int i = 0 ; i < params.length ; i ++ ){
                TreeItem newPara =
                        new TreeItem<>(paraTypes.get(i)+" "+params[i]);
                newFunction.getChildren().add(newPara);
            }

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        rootNode.getChildren().add(newFunction);
        num ++ ;
    }

    @FXML
    private void removeFunction(){

        TreeItem<String> tmp =  processResult.getSelectionModel().getSelectedItem();
        int indexOfRemove =  processResult.getSelectionModel().getSelectedIndex();

        if(rootNode.getChildren().contains(tmp)) {
            for (int i = indexOfRemove; i < rootNode.getChildren().size(); i++) {
                String value = rootNode.getChildren().get(i).getValue();
                String[] tmpStrings = value.split(" ");
                int tmpInt = Integer.valueOf(tmpStrings[0]) - 1;
                rootNode.getChildren().get(i).setValue(String.valueOf(tmpInt) + " " + tmpStrings[1]);
            }


            rootNode.getChildren().remove(tmp);

            num--;
            processResult.getSelectionModel().select(indexOfRemove);
        }
    }

    @FXML
    private void upFunction(){
        for(int i = 0 ; i < rootNode.getChildren().size() ; i ++ ){
            rootNode.getChildren().get(i).setExpanded(false);
        }
        TreeItem<String> tmp =  processResult.getSelectionModel().getSelectedItem();
        int indexOfUp =  processResult.getSelectionModel().getSelectedIndex();
        if(indexOfUp != 1 && rootNode.getChildren().contains(tmp)){
            TreeItem<String> upItem = rootNode.getChildren().get(indexOfUp - 2);
            String[] curStrings = tmp.getValue().split(" ");
            String[] upStrings = upItem.getValue().split(" ");
            upItem.setValue(curStrings[0] + " " +upStrings[1]);
            tmp.setValue(upStrings[0] + " " +curStrings[1]);
            rootNode.getChildren().set(indexOfUp - 2,tmp);
            rootNode.getChildren().set(indexOfUp - 1,upItem);
            processResult.getSelectionModel().select(indexOfUp - 1);
        }
    }

    @FXML
    private void downFunction(){
        for(int i = 0 ; i < rootNode.getChildren().size() ; i ++ ){
            rootNode.getChildren().get(i).setExpanded(false);
        }
        TreeItem<String> tmp =  processResult.getSelectionModel().getSelectedItem();
        int indexOfDown =  processResult.getSelectionModel().getSelectedIndex();
        if(indexOfDown != rootNode.getChildren().size() && rootNode.getChildren().contains(tmp)){
            TreeItem<String> downItem = rootNode.getChildren().get(indexOfDown);
            String[] curStrings = tmp.getValue().split(" ");
            String[] downStrings = downItem.getValue().split(" ");
            downItem.setValue(curStrings[0] + " " +downStrings[1]);
            tmp.setValue(downStrings[0] + " " +curStrings[1]);
            rootNode.getChildren().set(indexOfDown,tmp);
            rootNode.getChildren().set(indexOfDown - 1,downItem);
            processResult.getSelectionModel().select(indexOfDown + 1);
        }
    }


    private void showClassAndMethodInTable(){

        classNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().classNameProperty());

        methodNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().methodNameProperty());

        levelColumn.setCellValueFactory(
                cellData -> cellData.getValue().levelNameProperty());

        enableNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().enableNameProperty());
    }
}
