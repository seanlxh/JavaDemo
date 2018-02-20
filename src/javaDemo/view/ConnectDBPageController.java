/**
 * 直接操作数据库功能
 */


package javaDemo.view;

import javaDemo.service.Csv;
import javaDemo.util.JDBCUtil;
import javaDemo.util.jsonUtil;
import javaDemo.util.processCollection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectDBPageController {

    @FXML
    private TreeView<String> processResult;

    @FXML
    private TreeItem<String> rootNode;

    @FXML
    private TextField logAddr;

    @FXML
    private TextField userName;

    @FXML
    private TextField password;

    @FXML
    private TextField paras;

    @FXML
    private TextField FileAddr;

    @FXML
    private TextArea resultContent;

    private ArrayList<String> result;



    public ConnectDBPageController() {
        this.rootNode = new TreeItem<>("数据库事件");
    }

    @FXML
    private void initialize(){
        rootNode.getChildren().clear();
//        functionTabel.getSelectionModel().selectedItemProperty().addListener(
//                (observable, oldValue, newValue) > showType(newValue));
        rootNode.setExpanded(true);
        processResult.setRoot(rootNode);
        processResult.setEditable(true);
        processResult.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSql(newValue));
    }

    @FXML
    public void initTable(){
        ArrayList<String> arrays = Csv.readLog(logAddr.getText(),true);

        int max = 0;
        for(int i = 0 ; i < arrays.size() ; i ++){
            String[] contents = arrays.get(i).split(",");
            int num = Integer.valueOf(contents[4]);
            if(num > max)
            {
                max = num;
            }
        }

        for(int i = 0 ; i < max ; i ++){
            rootNode.getChildren().add(new TreeItem<String>());
        }

        for(int i = 0 ; i < arrays.size() ; i ++){
            String[] contents = arrays.get(i).split(",");
            int num = Integer.valueOf(contents[4])-1;
            String type = contents[2];
            if(type.equals("Connection opened")){
                rootNode.getChildren().get(num).setValue(contents[5]);
            }
            else if(type.equals("Prepared statement execution")){
                rootNode.getChildren().get(num).getChildren().add(new TreeItem<String>(contents[5]));
            }

        }

    }

    @FXML
    public void getData(){
        resultContent.clear();
        String user = userName.getText();
        String pwd = password.getText();
        String url;
        if(rootNode.getChildren().contains(processResult.getSelectionModel().getSelectedItem().getParent()))
            url = processResult.getSelectionModel().getSelectedItem().getParent().getValue();
        else
            url = processResult.getSelectionModel().getSelectedItem().getValue();
        try {
            JDBCUtil.init(url,"",user,pwd);
            String sql = paras.getText();
            System.out.println(sql);
            result = JDBCUtil.getResult(sql);
            for(int i = 0 ; i < result.size() ; i ++){
                resultContent.appendText(result.get(i)+"\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void storeInDatabase(){
        boolean isSucess=false;
        java.io.FileOutputStream out=null;
        java.io.OutputStreamWriter osw=null;
        java.io.BufferedWriter bw=null;
        try {
            out = new java.io.FileOutputStream(FileAddr.getText(),true);
            osw = new java.io.OutputStreamWriter(out,"utf-8");
            bw =new java.io.BufferedWriter(osw);

            for(int i = 0 ; i < result.size() ; i ++){
                bw.append(result.get(i)).append("\n");
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
            processCollection.showInfo("success","success");
        }
    }

    public void showSql(TreeItem<String> item){
        if(rootNode.getChildren().contains(item.getParent()))
        paras.setText(item.getValue());
    }
}
