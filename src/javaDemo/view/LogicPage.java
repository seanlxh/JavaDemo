/***
 * 逻辑调用类
 */
package javaDemo.view;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javaDemo.MainApp;
import javaDemo.model.Function;
import javaDemo.util.jsonUtil;
import javaDemo.util.processCollection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import javax.swing.text.TableView;
import javax.swing.text.html.ListView;
import javax.swing.tree.TreeNode;

import java.util.*;

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

    @FXML
    private TextField storeAddr;


    int num = 1;

    private static ArrayList<ArrayList<String>> listContent = new ArrayList<ArrayList<String>>();

    public TreeItem<String> rootNode;

    public HashMap<TreeItem<String>,Integer> treeItemObjectHashMap = new HashMap<TreeItem<String>,Integer>();

    public HashMap<TreeItem<String>,Boolean> treeItemBooleanHashMap = new HashMap<TreeItem<String>,Boolean>();

    public HashMap<TreeItem<String>,Object> treeItemObjectHashMap1 = new HashMap<TreeItem<String>,Object>();

    public HashMap<Integer,Object> integerObjectHashMap = new HashMap<Integer,Object>();

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
        rootNode.getChildren().clear();
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
                new TreeItem<>(num +" "+ selectedFunction.classNameProperty().getValue()+"."+getMethodNameWithoutExtra(selectedFunction.methodNameProperty().getValue())+" 类型:"+selectedFunction.enableNameProperty().getValue());
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
        for(int i = 0 ; i < rootNode.getChildren().size() ; i ++ ){
            rootNode.getChildren().get(i).setExpanded(false);
        }

        TreeItem<String> tmp =  processResult.getSelectionModel().getSelectedItem();
        int indexOfRemove =  processResult.getSelectionModel().getSelectedIndex();

        if(rootNode.getChildren().contains(tmp)) {
            for (int i = indexOfRemove; i < rootNode.getChildren().size(); i++) {
                String value = rootNode.getChildren().get(i).getValue();
                String[] tmpStrings = value.split(" ");
                int tmpInt = Integer.valueOf(tmpStrings[0]) - 1;
                rootNode.getChildren().get(i).setValue(String.valueOf(tmpInt) + " " + tmpStrings[1]+ " " +tmpStrings[2]);
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
            upItem.setValue(curStrings[0] + " " +upStrings[1]+" "+upStrings[2]);
            tmp.setValue(upStrings[0] + " " +curStrings[1]+" "+curStrings[2]);
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
            downItem.setValue(curStrings[0] + " " +downStrings[1]+" "+downStrings[2]);
            tmp.setValue(downStrings[0] + " " +curStrings[1]+" "+curStrings[2]);
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
            JSONArray jsonArray = jsonUtil.getJsonArrayFromString(para);
            Class tmpClass;
            if(tmp.getValue().contains("[ ]")){
                tmpClass = getClassFromName(tmp.getValue().split("\\[")[0]+"[ ]");
            }
            else{
                tmpClass = getClassFromName(tmp.getValue().split(" ")[0]);
            }

            Object tmpObj;
            if(judgeBasicTypeByName(tmpClass)){
                tmpObj = processCollection.getObjectFromStringAndClass(tmpClass, jsonArray.getString(0));
            }
            else{
                JSONObject tmpStr = (JSONObject)jsonArray.get(0);
                tmpObj = (Object)JSONObject.toBean(tmpStr, tmpClass);
            }
            treeItemObjectHashMap1.put(tmp,tmpObj);
            tmp.setValue(tmp.getValue().split("->")[0]+"->"+para);
            treeItemBooleanHashMap.put(tmp,false);
        }
    }

    @FXML
    private void getLogicData(){
        funcResult.clear();
        listContent.clear();
        for(int i = 0 ; i < rootNode.getChildren().size() ; i ++) {
            String content = rootNode.getChildren().get(i).getValue();
            String[] strings = content.split(" ");
            String classAndMethod = strings[1];
            String[] splitClassAndMethods = classAndMethod.split("\\.");
            String className = "";
            String methodName = "";
            for (int j = 0; j < splitClassAndMethods.length - 1; j++) {
                className += (splitClassAndMethods[j] + ".");
            }
            methodName = splitClassAndMethods[splitClassAndMethods.length - 1];
            className = className.substring(0, className.length() - 1);

            List paras = rootNode.getChildren().get(i).getChildren();

            ArrayList<Object> objectArray = new ArrayList<Object>();
            ArrayList<Object> curInputClasses = new ArrayList<Object>();
            Object[] objectFinalArray;

            for (int k = 0; k < paras.size(); k++) {
                String singlePara = (String) ((TreeItem) paras.get(k)).getValue();
                Class tmpClass;
                if (singlePara.contains("[ ]")) {
                    tmpClass = getClassFromName(singlePara.split("\\[")[0] + "[ ]");
                } else {
                    tmpClass = getClassFromName(singlePara.split(" ")[0]);
                }
                curInputClasses.add(tmpClass);
                Object tmpObj;
                if (treeItemBooleanHashMap.get(paras.get(k)) == false) {
                    tmpObj = treeItemObjectHashMap1.get(paras.get(k));
                } else {
                    int tmpIndex = treeItemObjectHashMap.get(paras.get(k));
                    tmpObj = integerObjectHashMap.get(tmpIndex);
                }
                objectArray.add(tmpObj);
            }

            objectFinalArray = (Object[]) objectArray.toArray();
            Class[] classArray = (Class[]) curInputClasses.toArray(new Class[curInputClasses.size()]);


            Object resultText = null;
            try {
                resultText = processCollection.execute(className,
                        methodName + "()", classArray, objectFinalArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (resultText != null) {
                Collection collection = setItems(resultText);
                integerObjectHashMap.put(i + 1, resultText);
                listContent.add(new ArrayList<String>());
                listContent.get(listContent.size() - 1).add("第" + String.valueOf(i + 1) + "个：");
                if (collection != null) {
                    Iterator it = collection.iterator();
                    while (it.hasNext()) {
                        listContent.add(new ArrayList<String>());
                        Object obj = it.next();
                        Collection collection1 = setItems(obj);
                        if (collection1 != null) {
                            ArrayList<Object> tmp = new ArrayList<Object>(collection1);
                            for (int m = 0; m < tmp.size(); m++) {
                                Object value = tmp.get(m);
                                if (processCollection.judgeBasicType(value)) {
                                    //result.appendText(value.toString()+" ");
                                    listContent.get(listContent.size() - 1).add(value.toString());
                                } else {
                                    //result.appendText(ReflectionToStringBuilder.toString(value)+" ");
                                    listContent.get(listContent.size() - 1).add(ReflectionToStringBuilder.toString(value));
                                }
                            }
                            //result.appendText("\n");
                        } else {
                            listContent.add(new ArrayList<String>());
                            if (processCollection.judgeBasicType(obj)) {
                                //result.appendText(obj.toString()+" ");
                                listContent.get(listContent.size() - 1).add(obj.toString());
                            } else {
                                //result.appendText(ReflectionToStringBuilder.toString(obj)+" ");
                                listContent.get(listContent.size() - 1).add(ReflectionToStringBuilder.toString(obj));
                            }
                            //result.appendText("\n");
                        }
//                    String[] tmp1 = (String[])it.next();
                    }
                } else {
                    listContent.add(new ArrayList<String>());
                    if (processCollection.judgeBasicType(resultText)) {
                        //result.appendText(resultText.toString()+" ");
                        listContent.get(listContent.size() - 1).add(resultText.toString());
                    } else {
                        //result.appendText(ReflectionToStringBuilder.toString(resultText)+" ");
                        listContent.get(listContent.size() - 1).add(ReflectionToStringBuilder.toString(resultText));
                    }

                    //result.appendText("\n");
                }
                }
            }



        boolean isSucess=false;
        java.io.FileOutputStream out=null;
        java.io.OutputStreamWriter osw=null;
        java.io.BufferedWriter bw=null;
        try {
            out = new java.io.FileOutputStream(storeAddr.getText(),true);
            osw = new java.io.OutputStreamWriter(out,"utf-8");
            bw =new java.io.BufferedWriter(osw);

            for(int i = 0 ; i < listContent.size() ; i ++){
                String a =  "";
                for(int j = 0 ; j < listContent.get(i).size() ; j ++){
                    a += listContent.get(i).get(j)+"',";

                }
                bw.append(a).append("\n");
            }

            isSucess=true;

        } catch (Exception e) {
            isSucess=false;
        }finally{
            if(bw!=null){
                try {
                    bw.close();
                    bw=null;
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
            if(osw!=null){
                try {
                    osw.close();
                    osw=null;
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
            if(out!=null){
                try {
                    out.close();
                    out=null;
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setId("invalidCategoryAlert");
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("invalidCategoryAlertOkButton");
        alert.setTitle("Finish");
        alert.setHeaderText("Success");
        alert.setContentText("Success");
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        alert.showAndWait();
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
