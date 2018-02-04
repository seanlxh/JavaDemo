package javaDemo.view;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import javaDemo.MainApp;
import javaDemo.model.Function;
import javaDemo.service.Csv;
import javaDemo.service.Service;
import javaDemo.util.JDBCUtil;
import javaDemo.util.jsonUtil;
import javaDemo.util.processCollection;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.*;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.*;

import static javaDemo.util.processCollection.*;


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
    private TextField leftCount;

    @FXML
    private TextField rightCount;

    @FXML
    private TextField inputParam;

    @FXML
    private TextField classAdrField;

    @FXML
    private TextField tableNameField;

    @FXML
    private TextField muiltInputParam;

    @FXML
    private Label inputType;

    @FXML
    private Label resultType;

    @FXML
    private TextField logAddress;

    @FXML
    private TextArea contentArea;

    @FXML
    private TextArea result;



    private Function curFunction;

    private ArrayList<Class> curInputClasses = null;

    private Type Resulttype = null;

    private static ArrayList<ArrayList<String>> listContent = new ArrayList<ArrayList<String>>();

    private static ArrayList<ArrayList<String>> muiltContent = new ArrayList<ArrayList<String>>();

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
        pool.importPackage("javax.swing");
        pool.importPackage("java.lang.reflect.Field");
        pool.importPackage("java.lang.reflect.Method");
            String className = curFunction.classNameProperty().getValue();
            String methodName = getMethodNameWithoutExtra(curFunction.methodNameProperty().getValue());

            CtClass pt = null;
            try {
                pt = pool.get(className);
                CtMethod m1 = pt.getDeclaredMethod(methodName);
                CtMethod m2 = CtNewMethod.make("public static boolean export(String methodName , String params, String returnType,String Addr){" +
                        "boolean isSucess=false;" +
                        " java.io.FileOutputStream out=null;" +
                        " java.io.OutputStreamWriter osw=null;" +
                        " java.io.BufferedWriter bw=null;" +
                        "        try {" +
                        "            out = new java.io.FileOutputStream(Addr,true);" +
                        "            osw = new java.io.OutputStreamWriter(out,\"UTF-8\");" +
                        "            bw =new java.io.BufferedWriter(osw);" +
                        "            if(methodName!=null && params != null  && returnType != null){" +
                        "            String a =  methodName +\" \"+ params +\" \"+ returnType ;  bw.append(a).append(\"\\n\");" +
                        "            }" +
                        "            isSucess=true;" +
                        "} catch (Exception e) {" +
                        "            isSucess=false;" +
                        "        }finally{" +
                        "            if(bw!=null){" +
                        "                try {" +
                        "                    bw.close();" +
                        "                    bw=null;" +
                        "                } catch (java.io.IOException e) {" +
                        "                    e.printStackTrace();" +
                        "                } " +
                        "            }" +
                        "            if(osw!=null){" +
                        "                try {" +
                        "                    osw.close();" +
                        "                    osw=null;" +
                        "                } catch (java.io.IOException e) {" +
                        "                    e.printStackTrace();" +
                        "                } " +
                        "            }" +
                        "            if(out!=null){" +
                        "                try {" +
                        "                    out.close();" +
                        "                    out=null;" +
                        "                } catch (java.io.IOException e) {" +
                        "                    e.printStackTrace();" +
                        "                } " +
                        "            }" +
                       "        }" +
                        "        return isSucess;" +
                        "    }", pt);



                pt.addMethod(m2);


                CtMethod m3 = CtNewMethod.make("public static boolean export1(String params, String returnType,String Addr){" +
                        "boolean isSucess=false;" +
                        " java.io.FileOutputStream out=null;" +
                        " java.io.OutputStreamWriter osw=null;" +
                        " java.io.BufferedWriter bw=null;" +
                        "        try {" +
                        "            out = new java.io.FileOutputStream(Addr,true);" +
                        "            osw = new java.io.OutputStreamWriter(out,\"UTF-8\");" +
                        "            bw =new java.io.BufferedWriter(osw);" +
                        "            if( params != null  && returnType != null){" +
                        "            String a = params +\",\"+ returnType ;  bw.append(a).append(\"\\n\");" +
                        "            }" +
                        "            isSucess=true;" +
                        "} catch (Exception e) {" +
                        "            isSucess=false;" +
                        "        }finally{" +
                        "            if(bw!=null){" +
                        "                try {" +
                        "                    bw.close();" +
                        "                    bw=null;" +
                        "                } catch (java.io.IOException e) {" +
                        "                    e.printStackTrace();" +
                        "                } " +
                        "            }" +
                        "            if(osw!=null){" +
                        "                try {" +
                        "                    osw.close();" +
                        "                    osw=null;" +
                        "                } catch (java.io.IOException e) {" +
                        "                    e.printStackTrace();" +
                        "                } " +
                        "            }" +
                        "            if(out!=null){" +
                        "                try {" +
                        "                    out.close();" +
                        "                    out=null;" +
                        "                } catch (java.io.IOException e) {" +
                        "                    e.printStackTrace();" +
                        "                } " +
                        "            }" +
                        "        }" +
                        "        return isSucess;" +
                        "    }", pt);
                pt.addMethod(m3);


                CtMethod m4 = CtNewMethod.make("public static int judgeBasicType(Object obj){" +
                        "        Class cls = obj.getClass();" +
                        "        if(cls == (int.class))" +
                        "            return 1;" +
                        "        else if(cls == (boolean.class))" +
                        "            return 1;" +
                        "        else if(cls == char.class)" +
                        "            return 1;" +
                        "        else if(cls == byte.class)" +
                        "            return 1;" +
                        "        else if(cls == short.class)" +
                        "            return 1;" +
                        "        else if(cls == long.class)" +
                        "            return 1;" +
                        "        else if(cls == double.class)" +
                        "            return 1;" +
                        "        else if(cls == float.class)" +
                        "            return 1;" +
                        "        else if(cls == java.lang.String.class)" +
                        "            return 1;" +
                        "        else if(cls == java.lang.Boolean.class)" +
                        "            return 1;" +
                        "        else if(cls == java.lang.Byte.class)" +
                        "            return 1;" +
                        "        else if(cls == java.lang.Character.class)" +
                        "            return 1;" +
                        "        else if(cls == java.lang.CharSequence.class)" +
                        "            return 1;" +
                        "        else if(cls == java.lang.Double.class)" +
                        "            return 1;" +
                        "        else if(cls == java.lang.Float.class)" +
                        "            return 1;" +
                        "        else if(cls == java.lang.Integer.class)" +
                        "            return 1;" +
                        "        else if(cls == java.lang.Long.class)" +
                        "            return 1;" +
                        "        else if(cls == java.lang.Number.class)" +
                        "            return 1;" +
                        "        else if(cls == java.lang.Short.class)" +
                        "            return 1;" +
                        "        else" +
                        "            return 0;" +
                        "    }", pt);
                pt.addMethod(m4);

                CtMethod m5 = CtNewMethod.make("private static String printFieldMessage(Object object) {" +
                        "        Class class1 = object.getClass();" +
                        "        Field[] fs = class1.getDeclaredFields();" +
                        "        String result = \"\";"+
                        "        for (int i = 0 ; i < fs.length ; i ++) {" +
                        "            fs[i].setAccessible(true);" +
                        "            Class filedType = fs[i].getType();" +
                        "            String typeName = filedType.getName();" +
                        "            String fieldName = fs[i].getName();" +
                        "            result += (typeName + \" \" + fieldName + \":\"+ fs[i].get(object)+ \",\");" +
                        "        }" +
                        "    return result;" +
                        "}", pt);
                pt.addMethod(m5);



                StringBuffer sb = new StringBuffer();
                String address = logAddress.getText();
                sb.append("for(int i = 0 ; i < "+curInputClasses.size()+" ; i++) {");
                   // sb.append("for(int j = 0 ; j < $args[i].length ; j++){");
                sb.append("if( $args[i].getClass().isArray() ) {");
                    sb.append("System.out.println();");
                sb.append("export(\""+"array:"+"\",java.util.Arrays.toString((Object[])$args[i]),\""+methodName+"\",\""+address+"\");");
                sb.append("}");
                sb.append("else{");
                sb.append("if(judgeBasicType($args[i]) == 1)");
                sb.append("export1($args[i].toString(),\""+methodName+"\",\""+address+"\");");
                sb.append("else {export1(printFieldMessage($args[i]),\""+methodName+"\",\""+address+"\");}");
                sb.append("}");

                   // sb.append("}");

                sb.append("}");
                String code = sb.toString();
                m1.insertBefore(code);

                pt.writeFile(classAdrField.getText());
                pt.defrost();
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
    private void handleShowInfo(){
        contentArea.clear();
        String path = logAddress.getText();
        Csv csv = new Csv();
        ArrayList<String> arrays = csv.readLog(path);

        for(int i = 0 ; i < arrays.size() ; i ++){
            contentArea.appendText(arrays.get(i)+"\n");
        }
    }




    @FXML
    private void handleNumberInput(){
        try{
        muiltContent.clear();
        int leftNum = Integer.valueOf(leftCount.getText());
        int rightNum;
        if(rightCount.getText() == null || rightCount.getText().length() == 0){
            rightNum = 2147483646;
        }
        else{
            rightNum = Integer.valueOf(rightCount.getText());
        }

        for(int i = leftNum ; i <= rightNum ; i ++){
            Object[] objectFinalArray;
            ArrayList<Object> objectArray = new ArrayList<Object>();
            Object object = processCollection.getObjectFromStringAndClass(curInputClasses.get(0), String.valueOf(i));
            objectArray.add(object);
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
                    muiltContent.add(new ArrayList<String>());
                    Object obj = it.next();
                    Collection collection1 = setItems(obj);
                    if(collection1 != null){
                        ArrayList<Object> tmp = new ArrayList<Object>(collection1);
                        for(int k=0;k<tmp.size();k++) {
                            Object value = tmp.get(k);
                            if(processCollection.judgeBasicType(value)){
                                muiltContent.get(muiltContent.size()-1).add(value.toString());
                            }
                            else{
                                muiltContent.get(muiltContent.size()-1).add(ReflectionToStringBuilder.toString(value));
                            }
                        }
                    }
                    else{
                        muiltContent.add(new ArrayList<String>());
                        if(processCollection.judgeBasicType(obj)){
                            muiltContent.get(muiltContent.size()-1).add(obj.toString());
                        }
                        else{
                            muiltContent.get(muiltContent.size()-1).add(ReflectionToStringBuilder.toString(obj));
                        }
                    }
                }
            }
            else {
                muiltContent.add(new ArrayList<String>());
                if(processCollection.judgeBasicType(resultText)){
                    muiltContent.get(muiltContent.size()-1).add(resultText.toString());
                }
                else{
                    muiltContent.get(muiltContent.size()-1).add(ReflectionToStringBuilder.toString(resultText));
                }
            }
        }

    } catch (Exception e) {
            e.printStackTrace();
        }
    finally {
            boolean isSucess=false;
            java.io.FileOutputStream out=null;
            java.io.OutputStreamWriter osw=null;
            java.io.BufferedWriter bw=null;
            try {
                out = new java.io.FileOutputStream(tableNameField.getText(),true);
                osw = new java.io.OutputStreamWriter(out,"utf-8");
                bw =new java.io.BufferedWriter(osw);

                for(int i = 0 ; i < muiltContent.size() ; i ++){
                    String a =  "";
                    for(int j = 0 ; j < muiltContent.get(i).size() ; j ++){
                        a += muiltContent.get(i).get(j)+"',";
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


        }

    }





    @FXML
    private void handleMuiltInput(){
        muiltContent.clear();
        String input = muiltInputParam.getText();

        JSONArray jsonArray = jsonUtil.getJsonArrayFromString(input);
        int count = jsonArray.size();
        for(int i = 0 ; i < count ; i ++){
            JSONArray childJsonArray = jsonUtil.getJsonArrayFromString(jsonArray.getString(i));
            Object[] objectFinalArray;
            ArrayList<Object> objectArray = new ArrayList<Object>();
            for(int j = 0 ; j < childJsonArray.size() ; j ++){
                Object object = processCollection.getObjectFromStringAndClass(curInputClasses.get(j), childJsonArray.getString(j));
                objectArray.add(object);
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
                    muiltContent.add(new ArrayList<String>());
                    Object obj = it.next();
                    Collection collection1 = setItems(obj);
                    if(collection1 != null){
                        ArrayList<Object> tmp = new ArrayList<Object>(collection1);
                        for(int k=0;k<tmp.size();k++) {
                            Object value = tmp.get(k);
                            if(processCollection.judgeBasicType(value)){
                                muiltContent.get(muiltContent.size()-1).add(value.toString());
                            }
                            else{
                                muiltContent.get(muiltContent.size()-1).add(ReflectionToStringBuilder.toString(value));
                            }
                        }
                    }
                    else{
                        muiltContent.add(new ArrayList<String>());
                        if(processCollection.judgeBasicType(obj)){
                            muiltContent.get(muiltContent.size()-1).add(obj.toString());
                        }
                        else{
                            muiltContent.get(muiltContent.size()-1).add(ReflectionToStringBuilder.toString(obj));
                        }
                    }
                }
            }
            else {
                muiltContent.add(new ArrayList<String>());
                if(processCollection.judgeBasicType(resultText)){
                    muiltContent.get(muiltContent.size()-1).add(resultText.toString());
                }
                else{
                    muiltContent.get(muiltContent.size()-1).add(ReflectionToStringBuilder.toString(resultText));
                }
            }
        }
        boolean isSucess=false;
        java.io.FileOutputStream out=null;
        java.io.OutputStreamWriter osw=null;
        java.io.BufferedWriter bw=null;
        try {
            out = new java.io.FileOutputStream(tableNameField.getText(),true);
            osw = new java.io.OutputStreamWriter(out,"utf-8");
            bw =new java.io.BufferedWriter(osw);

            for(int i = 0 ; i < muiltContent.size() ; i ++){
                String a =  "";
                for(int j = 0 ; j < muiltContent.get(i).size() ; j ++){
                    a += muiltContent.get(i).get(j)+"',";
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
    }





    @FXML
    private void handleExecute() {
        result.clear();
        ArrayList<Object> objectArray = new ArrayList<Object>();
        Object[] objectFinalArray;
        if (inputParam.getText() != null && inputParam.getText().length() != 0) {
            String inputParams = inputParam.getText();
            JSONArray jsonArray = jsonUtil.getJsonArrayFromString(inputParams);
            for (int i = 0; i < curInputClasses.size(); i++) {
                if(judgeBasicTypeByName(curInputClasses.get(i))){
                    Object object = processCollection.getObjectFromStringAndClass(curInputClasses.get(i), jsonArray.getString(i));
                    objectArray.add(object);
                }
                else{
                    JSONObject tmp = (JSONObject)jsonArray.get(i);
                    Class cur = curInputClasses.get(i);
                    Object obj=(Object)JSONObject.toBean(tmp, cur);
                    objectArray.add(obj);
//                    System.out.println(obj);
//                    Field[] fields = cur.getDeclaredFields();
//                    for(int j = 0 ; j < fields.length ; j ++){
//                        fields[j].setAccessible(true);
//                        String filedName = fields[j].getName();
//                        Class filedType = fields[j].getType();
//                        Object tmpValue = tmp.get(filedName);
//
//                    }
                }

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


        boolean isSucess=false;
        java.io.FileOutputStream out=null;
        java.io.OutputStreamWriter osw=null;
        java.io.BufferedWriter bw=null;
        try {
            out = new java.io.FileOutputStream(tableNameField.getText(),true);
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


//        String tableName = tableNameField.getText();
//        try {
//            JDBCUtil.init();
//            JDBCUtil.createTable(tableName,listContent.get(0).size());
//
//            for(int i = 0 ; i < listContent.size() ; i ++){
//                String tmp = "insert into "+tableName+" values(";
//                for(int j = 0 ; j < listContent.get(i).size() ; j ++){
//                    tmp += "'"+listContent.get(i).get(j)+"',";
//                }
//                tmp = tmp.substring(0,tmp.length()-1);
//                tmp += ")";
//                JDBCUtil.insert(tmp);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
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
                newTypeLabel += "/";
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
                //resultType.setText("abstract class");
                result = "abstract class";
            }
            else{
                Type type = processCollection.judgeClass(stringPropertyToString(className),
                        stringPropertyToString(methodName),tempClasses);
                //resultType.setText(type.getTypeName());
                Resulttype = type;
                result = type.getTypeName();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //resultType.setText("can't execute");
            result = "can't execute";
        }
        ClassPool pool  =  ClassPool.getDefault();
        CtClass cc  = null;
        try {
            resultType.setText("");
            cc = pool.get(stringPropertyToString(className));
            CtMethod cm  =  cc.getDeclaredMethod(getMethodNameWithoutExtra(stringPropertyToString(methodName)));
            MethodInfo methodInfo  =  cm.getMethodInfo();
            CodeAttribute codeAttribute  =  methodInfo.getCodeAttribute();
            LocalVariableAttribute attr  =  (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            String[] paramNames  =   new  String[cm.getParameterTypes().length];
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for  ( int  i  =   0 ; i  <  paramNames.length; i ++ )
                paramNames[i]  =  attr.variableName(i+pos);
            for  ( int  i  =   0 ; i  <  paramNames.length; i ++ ) {
                String content = resultType.getText();
                content += (paramNames[i]+"/");
                resultType.setText(content);
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
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