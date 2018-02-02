package javaDemo.view;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javaDemo.MainApp;
import javaDemo.model.Function;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import javax.swing.text.TableView;
import javax.swing.text.html.ListView;
import javax.swing.tree.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static javaDemo.util.processCollection.*;

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

    @FXML
    private javafx.scene.control.ListView<String> paraList;

    @FXML
    private TextField paraContent;


    int num = 1;


    public TreeItem<String> rootNode;

    public HashMap<TreeItem<String>,Integer> treeItemObjectHashMap = new HashMap<TreeItem<String>,Integer>();

    public HashMap<TreeItem<String>,Boolean> treeItemBooleanHashMap = new HashMap<TreeItem<String>,Boolean>();

    public HashMap<TreeItem<String>,Object> treeItemObjectHashMap1 = new HashMap<TreeItem<String>,Object>();

    public ArrayList<Object> funcResult = new ArrayList<Object>();

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
                new TreeItem<>(num +" "+ selectedFunction.classNameProperty().getValue()+"."+getMethodNameWithoutExtra(selectedFunction.methodNameProperty().getValue())+" 类型："+selectedFunction.enableNameProperty().getValue());
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
            if(treeItemObjectHashMap.containsKey(tmp))
                treeItemObjectHashMap.remove(tmp);
            if(treeItemObjectHashMap1.containsKey(tmp))
                treeItemObjectHashMap1.remove(tmp);
            if(treeItemBooleanHashMap.containsKey(tmp))
                treeItemBooleanHashMap.remove(tmp);
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

    @FXML
    private void infoFunction(){
        if(processResult.getSelectionModel().getSelectedItem().getParent().equals(rootNode)){
            TreeItem<String> indexNode =  processResult.getSelectionModel().getSelectedItem();
            int indexOfInfo = 0;
            for(indexOfInfo = 0 ; rootNode.getChildren().get(indexOfInfo) != indexNode ; indexOfInfo ++);
            ObservableList<String> items =FXCollections.observableArrayList ();
            for(int i = 0 ; i < indexOfInfo ; i ++){
                String[] typeStrings = rootNode.getChildren().get(i).getValue().split(" ");
                String typeName = typeStrings[2].substring(3);
                if(!typeName.equals("void")){
                    items.add(typeStrings[0]+" "+ typeStrings[1] + typeStrings[2]);
                }
            }
            paraList.setItems(items);
        }
    }

    @FXML
    private void addParaFunction(){
        TreeItem<String> tmp =  processResult.getSelectionModel().getSelectedItem();
        if(rootNode.getChildren().contains(tmp.getParent())){
            String[] nums = paraList.getSelectionModel().getSelectedItem().split(" ");
            int funNum = Integer.valueOf(nums[0]);
            if(funNum < Integer.valueOf(tmp.getParent().getValue().split(" ")[0])){
                treeItemObjectHashMap.put(tmp,funNum);
                tmp.setValue(tmp.getValue().split("->")[0]+"->"+nums[0]+nums[1]);
                treeItemBooleanHashMap.put(tmp,true);
            }
        }
    }

    @FXML
    private void addPara(){
        TreeItem<String> tmp =  processResult.getSelectionModel().getSelectedItem();
        if(rootNode.getChildren().contains(tmp.getParent())){
            String para = paraContent.getText();
            Class tmpClass = getClassFromName(para);
            Object tmpObj = getObjectFromStringAndClass(tmpClass,para);
            treeItemObjectHashMap1.put(tmp,tmpObj);
            tmp.setValue(tmp.getValue().split("->")[0]+"->"+para);
            treeItemBooleanHashMap.put(tmp,false);
        }
    }

    @FXML
    private void getLogicData(){
        funcResult.clear();
        for(int i = 0 ; i < rootNode.getChildren().size() ; i ++){
            String content = rootNode.getChildren().get(i).getValue();
            String[] strings = content.split(" ");
            String classAndMethod = strings[1];
            String[] splitClassAndMethods = classAndMethod.split("\\.");
            String className = "";
            String methodName = "";
            for(int j = 0 ; j < splitClassAndMethods.length - 1 ; j ++){
                className += (splitClassAndMethods[j]+".");
            }
            methodName = splitClassAndMethods[splitClassAndMethods.length-1];
            className = className.substring(0,className.length()-1);

            List paras =  rootNode.getChildren().get(i).getChildren();
            for(int k = 0 ; k < paras.size() ; k ++){
                String singlePara = (String) ((TreeItem)paras.get(k)).getValue();
                String[] paraInfo = singlePara.split(" ");
                Class tmpClass = getClassFromName(paraInfo[0]);
                Object tmpObj = getObjectFromStringAndClass(tmpClass,para);

            }



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
