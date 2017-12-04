package javaDemo.view;

import javaDemo.MainApp;
import javaDemo.model.Function;
import javaDemo.service.Service;
import javaDemo.util.jsonUtil;
import javaDemo.util.processCollection;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static javaDemo.util.processCollection.setItems;
import static javaDemo.util.processCollection.stringPropertyToString;


public class FunctionOverviewController{

    @FXML
    private TableView<Function> functionTabel;

    @FXML
    private TableColumn<Function,String> classNameColumn;

    @FXML
    private TableColumn<Function,String> methodNameColumn;

    @FXML
    private TableColumn<Function,String> levelColumn;

    @FXML
    private TextField fileAddress;

    @FXML
    private TextField num;

    @FXML
    private TextField inputParam;

    @FXML
    private TextField tableNameField;

    @FXML
    private Label inputType;

    @FXML
    private Label resultType;

    @FXML
    private TextArea result;

    private Function curFunction;

    private ArrayList<Class> curInputClasses = null;

    private Type Resulttype = null;

    private static ArrayList<ArrayList<String>> listContent;


    private MainApp mainApp;


    public FunctionOverviewController(){

    }

    @FXML
    private void initialize(){
        showClassAndMethodInTable();
       functionTabel.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showType(newValue));
    }

    private void showClassAndMethodInTable(){

        classNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().classNameProperty());

        methodNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().methodNameProperty());

        levelColumn.setCellValueFactory(
                cellData -> cellData.getValue().levelNameProperty());
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        functionTabel.setItems(mainApp.getFunctionData());
    }

    @FXML
    private void handleAnalysis() {
        mainApp.getFunctionData().clear();
        if (isAnalysisInputValid()) {
            Service service = new Service();
            ArrayList<String> tmpClassAndFunction = service.
                    getFunctionList(Integer.parseInt(num.getText()),fileAddress.getText());
            ArrayList<String> classAndFunction = new  ArrayList<String>();
            for (String cd:tmpClassAndFunction) {
                if(!classAndFunction.contains(cd)){
                    classAndFunction.add(cd);
                }
            }
            int listNum = (classAndFunction.size());//%Integer.parseInt(num.getText()) == 0)?
                    //classAndFunction.size()/Integer.parseInt(num.getText()):classAndFunction.size();

            for(int i = 0 ; i < listNum; i ++){
                String className ;
                String methodName ;
                String levelName ;
                ArrayList<String> splitResult = processCollection.splitClassAndMethodAndLevel(classAndFunction.get(i));
                className = splitResult.get(0);
                methodName = splitResult.get(1);
                levelName = splitResult.get(2);

                Function tempfunction = new Function(className,methodName,levelName);
                mainApp.getFunctionData().add(tempfunction);
            }

        }
    }


    @FXML
    private void handleExecute() {
        listContent.clear();
        result.clear();
        ArrayList<Object> objectArray = new ArrayList<Object>();
        Object[] objectFinalArray;
        if (inputParam.getText() != null && inputParam.getText().length() != 0) {
            String inputParams = inputParam.getText();
            JSONArray jsonArray = jsonUtil.getJsonArrayFromString(inputParams);
            for (int i = 0; i < curInputClasses.size(); i++) {
                //TODO:先完成假设都不是数组
                Object object = processCollection.getObjectFromStringAndClass(curInputClasses.get(i), jsonArray.getString(i));
                objectArray.add(object);
            }
        }
            objectFinalArray = (Object[]) objectArray.toArray();

            Class[] classArray = (Class[])curInputClasses.toArray(new Class[curInputClasses.size()]);
            Object resultText = null;
            try {
                resultText  = processCollection.execute(curFunction.classNameProperty().getValue(),
                        curFunction.methodNameProperty().getValue(),classArray,objectFinalArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Collection collection = setItems(resultText);
            if(collection != null){
                Iterator it = collection.iterator();
                while(it.hasNext()){
                    Object obj = it.next();
                    Collection collection1 = setItems(obj);
                    if(collection1 != null){
                        ArrayList<Object> tmp = new ArrayList<Object>(collection1);
                                for(int i=0;i<tmp.size();i++) {
                                    Object value = tmp.get(i);
                                    if(processCollection.judgeBasicType(value))
                                        result.appendText(value.toString()+" ");

                                    else
                                        result.appendText(ReflectionToStringBuilder.toString(value)+" ");

                                }
                        result.appendText("\n");
                    }
                    else{
                        if(processCollection.judgeBasicType(obj))
                            result.appendText(obj.toString()+" ");
                        else
                            result.appendText(ReflectionToStringBuilder.toString(obj)+" ");

                        result.appendText("\n");
                    }
//                    String[] tmp1 = (String[])it.next();
                }
            }
            else{
                if(processCollection.judgeBasicType(resultText))
                    result.appendText(resultText.toString()+" ");
                else
                    result.appendText(ReflectionToStringBuilder.toString(resultText)+" ");
                result.appendText("\n");
            }
    }

    @FXML
    private void storeInDatabase() {
        String tableName = tableNameField.getText();







    }


    private void showType(Function function){
        curFunction = function;
        StringProperty className = function.classNameProperty();
        StringProperty methodName = function.methodNameProperty();
        System.out.println(String.valueOf(methodName));
        ArrayList<String> paras =  processCollection.getParaTypeFromMethod(String.valueOf(methodName));

        try{
            String newTypeLabel = "";
            for(String para : paras){
                newTypeLabel += para;
                newTypeLabel += " ";
            }
            inputType.setText(newTypeLabel);
        }
        catch (Exception e){
            System.out.println(e);
        }
        ArrayList<Class> tempClasses = new ArrayList<Class>();
        for(int i = 0 ; i < paras.size() ; i ++){
            tempClasses.add(processCollection.getClassFromName(paras.get(i)));
        }
        curInputClasses = tempClasses;
        try {
            Class claz = Class.forName(stringPropertyToString(className));

            boolean isAbs = Modifier.isAbstract(claz.getModifiers()) ;
            if(isAbs)
                resultType.setText("abstract class");
            else{
                Type type = processCollection.judgeClass(stringPropertyToString(className),
                        stringPropertyToString(methodName),tempClasses);
                resultType.setText(type.getTypeName());
                Resulttype = type;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultType.setText("can't execute");
        }


    }


    private boolean isAnalysisInputValid() {
        String errorMessage = "";

        if (fileAddress.getText() == null || fileAddress.getText().length() == 0) {
            errorMessage += "No valid fileAddress!\n";
        }
        if (num.getText() == null || num.getText().length() <= 0||num.getText().length() >=5
                || !processCollection.isNumeric(num.getText())) {
            errorMessage += "No valid num!\n";
        }


        if (errorMessage.length() == 0) {
            return true;
        } else {
           System.out.println(errorMessage);
            }
            return false;
        }

    }










