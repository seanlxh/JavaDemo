package javaDemo.view;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import javaDemo.MainApp;
import javaDemo.model.Function;
import javaDemo.service.Service;
import javaDemo.util.JDBCUtil;
import javaDemo.util.jsonUtil;
import javaDemo.util.processCollection;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javassist.*;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.IOException;
import java.lang.reflect.*;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static javaDemo.util.processCollection.getMethodNameWithoutExtra;
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
    private TableColumn<Function,String> enableNameColumn;

    @FXML
    private TextField fileAddress;

    @FXML
    private TextField numLeft;

    @FXML
    private TextField numRight;

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

    private static ArrayList<ArrayList<String>> listContent = new ArrayList<ArrayList<String>>();


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

        enableNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().enableNameProperty());
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
                    getFunctionList(Integer.parseInt(numLeft.getText()),Integer.parseInt(numRight.getText()),fileAddress.getText());
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
                String enable = showType(tempfunction);
                tempfunction.setEnableName(enable);
                mainApp.getFunctionData().add(tempfunction);

            }

        }
    }

    @FXML
    private void handleFilter() {
            ArrayList<Integer> arrays = new ArrayList<Integer>();
            for(int i = 0 ; i < mainApp.getFunctionData().size(); i ++){
                if(mainApp.getFunctionData().get(i).enableNameProperty().getValue().equals("can't execute")
                        ||mainApp.getFunctionData().get(i).enableNameProperty().getValue().equals("abstract class"))
                    mainApp.getFunctionData().remove(i);
            }


        }

    @FXML
    private void handleGetClassFile(){
        ClassPool pool = ClassPool.getDefault();

            String className = curFunction.classNameProperty().getValue();
            String methodName = getMethodNameWithoutExtra(curFunction.methodNameProperty().getValue());

            CtClass pt = null;
            try {
                pt = pool.get(className);
                CtMethod m1 = pt.getDeclaredMethod(methodName);
                CtMethod m2 = CtNewMethod.make("    public static Boolean judgeBasicType(Object obj){\n" +
                        "        if(obj.getClass() == (int.class))\n" +
                        "            return true;\n" +
                        "        else if(obj.getClass() == (boolean.class))\n" +
                        "            return true;\n" +
                        "        else if(obj.getClass() == char.class)\n" +
                        "            return true;\n" +
                        "        else if(obj.getClass() == byte.class)\n" +
                        "            return true;\n" +
                        "        else if(obj.getClass() == short.class)\n" +
                        "            return true;\n" +
                        "        else if(obj.getClass() == long.class)\n" +
                        "            return true;\n" +
                        "        else if(obj.getClass() == double.class)\n" +
                        "            return true;\n" +
                        "        else if(obj.getClass() == float.class)\n" +
                        "            return true;\n" +
                        "        else if(obj.getClass() == java.lang.String.class)\n" +
                        "            return true;\n" +
                        "        else\n" +
                        "            return false;\n" +
                        "    }", pt);
                pt.addMethod(m2);

                StringBuffer sb = new StringBuffer();
                sb.append("for(int i = 0 ; i < $1.length ; i ++){");
                sb.append("System.out.println($1[i]);");
                sb.append("System.out.println(\"\\n\");}");




                String code = sb.toString();
                m1.insertBefore(code);
                pt.writeFile("/Users/seanlxh/Downloads");
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // CtMethod m = CtNewMethod.make("public void xmove(int dx) {System.out.println(\"sdss\");}", pt);


            // pt.addMethod(m);
            //pt.writeFile();
//            Class c = pt.toClass();
//            TestBean bean = (TestBean) c.newInstance();
//            bean.aaa(1,2,3);


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
                    listContent.add(new ArrayList<String>());
                    Object obj = it.next();
                    Collection collection1 = setItems(obj);
                    if(collection1 != null){
                        ArrayList<Object> tmp = new ArrayList<Object>(collection1);
                                for(int i=0;i<tmp.size();i++) {
                                    Object value = tmp.get(i);
                                    if(processCollection.judgeBasicType(value)){
                                        result.appendText(value.toString()+" ");
                                        listContent.get(listContent.size()-1).add(value.toString());
                                    }
                                    else{
                                        result.appendText(ReflectionToStringBuilder.toString(value)+" ");
                                        listContent.get(listContent.size()-1).add(ReflectionToStringBuilder.toString(value));
                                    }
                                }
                        result.appendText("\n");
                    }
                    else{
                        listContent.add(new ArrayList<String>());
                        if(processCollection.judgeBasicType(obj)){
                            result.appendText(obj.toString()+" ");
                            listContent.get(listContent.size()-1).add(obj.toString());
                        }
                        else{
                            result.appendText(ReflectionToStringBuilder.toString(obj)+" ");
                            listContent.get(listContent.size()-1).add(ReflectionToStringBuilder.toString(obj));
                        }
                        result.appendText("\n");
                    }
//                    String[] tmp1 = (String[])it.next();
                }
            }
            else{
                listContent.add(new ArrayList<String>());
                if(processCollection.judgeBasicType(resultText)){
                    result.appendText(resultText.toString()+" ");
                    listContent.get(listContent.size()-1).add(resultText.toString());
                }
                else{
                    result.appendText(ReflectionToStringBuilder.toString(resultText)+" ");
                    listContent.get(listContent.size()-1).add(ReflectionToStringBuilder.toString(resultText));
                }

                result.appendText("\n");
            }
    }

    @FXML
    private void storeInDatabase() {
        String tableName = tableNameField.getText();
        try {
            JDBCUtil.init();
            JDBCUtil.createTable(tableName,listContent.get(0).size());

            for(int i = 0 ; i < listContent.size() ; i ++){
                String tmp = "insert into "+tableName+" values(";
                for(int j = 0 ; j < listContent.get(i).size() ; j ++){
                    tmp += "'"+listContent.get(i).get(j)+"',";
                }
                tmp = tmp.substring(0,tmp.length()-1);
                tmp += ")";
                JDBCUtil.insert(tmp);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    private String showType(Function function){
        String result = "";
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
            if(isAbs){
                resultType.setText("abstract class");
                result = "abstract class";
            }
            else{
                Type type = processCollection.judgeClass(stringPropertyToString(className),
                        stringPropertyToString(methodName),tempClasses);
                resultType.setText(type.getTypeName());
                Resulttype = type;
                result = type.getTypeName();
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultType.setText("can't execute");
            result = "can't execute";
        }
        return result;

    }


    private boolean isAnalysisInputValid() {
        String errorMessage = "";

        if (fileAddress.getText() == null || fileAddress.getText().length() == 0) {
            errorMessage += "No valid fileAddress!\n";
        }
        if (numLeft.getText() == null || numLeft.getText().length() <= 0||numLeft.getText().length() >=5
                || !processCollection.isNumeric(numLeft.getText())) {
            errorMessage += "No valid num!\n";
        }
        if (numRight.getText() == null || numRight.getText().length() <= 0||numRight.getText().length() >=5
                || !processCollection.isNumeric(numRight.getText())) {
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










