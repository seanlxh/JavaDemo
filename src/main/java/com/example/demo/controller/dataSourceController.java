package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.Impl.*;
import com.sun.tools.corba.se.idl.StringGen;
import javafx.scene.control.TreeItem;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

@Controller
@RequestMapping("/execute")
public class dataSourceController {
    @Resource
    private dataTaskImpl dataTaskService;
    @Resource
    private dataSourceImpl dataSourceService;
    @Resource
    private functionLogicImpl functionLogicService;
    @Resource
    private inputParaImpl inputParaService;
    @Resource
    private paraInfoImpl paraInfoService;
    @Resource
    private resultColumnImpl resultColumnService;

    private static ArrayList<ArrayList<String>> listContent = new ArrayList<ArrayList<String>>();

    @RequestMapping("/execute")
    @ResponseBody
    public Object toIndex(HttpServletRequest request, Model model){
        HashMap<Integer,Object> integerObjectHashMap = new HashMap<Integer,Object>();
        Long typeId = Long.parseLong(request.getParameter("id"));
        dataSource ds = this.dataSourceService.findById(typeId);
        List<functionLogic> functionLogics = functionLogicService.selectByDSIDWithLogic(ds.getDsId());
        List<inputPara> inputParas = inputParaService.findBytypeId(ds.getType());
        resultColumn resultColumn =  resultColumnService.selectByPrimaryKey(ds.getDsId());
        if(ds.getType() == 1){
            for(int i = 0 ; i < functionLogics.size(); i ++){
                    String methodName = functionLogics.get(i).getMethodname();
                    String className = functionLogics.get(i).getClassname();
                    int num = functionLogics.get(i).getParanum();
                    List<paraInfo> paras = null;
                    if(num != 0){
                        paras = paraInfoService.getParaInfoByFuncId(functionLogics.get(i).getFunctionId());
                    }
                    ArrayList<Object> objectArray = new ArrayList<Object>();
                    ArrayList<Object> curInputClasses = new ArrayList<Object>();
                    Object[] objectFinalArray;
                    if(paras!=null) {
                        for (int k = 0; k < paras.size(); k++) {
                            paraInfo para = paras.get(k);
                            Class tmpClass;
                            if (para.getParaType().contains("[ ]")) {
                                tmpClass = getClassFromName(para.getParaType().split("\\[")[0] + "[ ]");
                            } else {
                                tmpClass = getClassFromName(para.getParaType());
                            }
                            curInputClasses.add(tmpClass);
                            Object tmpObj = null;
                            if (para.getOriType() == 2) {
                                Object tmptmpObj;
                                if(judgeBasicTypeByName(tmpClass)){
                                    tmpObj = getObjectFromStringAndClass(tmpClass, para.getInputContent().substring(1,para.getInputContent().length()-1));
                                }
//                                else{
//                                    JSONObject tmpStr = (JSONObject)jsonArray.get(0);
//                                    tmptmpObj = (Object)JSONObject.toBean(tmpStr, tmpClass);
//                                }
                            } else if(para.getOriType() == 1){
                                tmpObj = integerObjectHashMap.get(para.getFuncResult());
                            }
                            objectArray.add(tmpObj);
                        }
                    }

                    objectFinalArray = (Object[]) objectArray.toArray();
                    Class[] classArray = (Class[]) curInputClasses.toArray(new Class[curInputClasses.size()]);


                    Object resultText = null;
                    try {
                        resultText = execute(className,
                                methodName , classArray, objectFinalArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (resultText != null) {
                            JSONObject json = new JSONObject();
                            Collection collection = setItems(resultText);
                            if(collection!=null){
                                json.put("count", collection.size());
                            }
                            else{
                                json.put("count", 1);
                            }
                             JSONArray jsonArray = new JSONArray();
                             jsonArray.add(0, json);
                             JSONObject result = new JSONObject();
                             result.put("result",jsonArray);
                             result.put("code","0000");
                             result.put("msg","服务调用成功");
                            return result;
                    }
            }
        }
        else if(ds.getType() == 2){

            for(int i = 0 ; i < functionLogics.size(); i ++){
                String methodName = functionLogics.get(i).getMethodname();
                String className = functionLogics.get(i).getClassname();
                int num = functionLogics.get(i).getParanum();
                List<paraInfo> paras = null;
                if(num != 0){
                    paras = paraInfoService.getParaInfoByFuncId(functionLogics.get(i).getFunctionId());
                }
                ArrayList<Object> objectArray = new ArrayList<Object>();
                ArrayList<Object> curInputClasses = new ArrayList<Object>();
                Object[] objectFinalArray;
                if(paras!=null) {
                    for (int k = 0; k < paras.size(); k++) {
                        paraInfo para = paras.get(k);
                        Class tmpClass;
                        if (para.getParaType().contains("[ ]")) {
                            tmpClass = getClassFromName(para.getParaType().split("\\[")[0] + "[ ]");
                        } else {
                            tmpClass = getClassFromName(para.getParaType());
                        }
                        curInputClasses.add(tmpClass);
                        Object tmpObj = null;
                        if (para.getOriType() == 2) {
                            Object tmptmpObj;
                            if(judgeBasicTypeByName(tmpClass)){
                                tmpObj = getObjectFromStringAndClass(tmpClass, para.getInputContent().substring(1,para.getInputContent().length()-1));
                            }
//                                else{
//                                    JSONObject tmpStr = (JSONObject)jsonArray.get(0);
//                                    tmptmpObj = (Object)JSONObject.toBean(tmpStr, tmpClass);
//                                }
                        } else if(para.getOriType() == 1){
                            tmpObj = integerObjectHashMap.get(para.getFuncResult());
                        }
                        objectArray.add(tmpObj);
                    }
                }

                objectFinalArray = (Object[]) objectArray.toArray();
                Class[] classArray = (Class[]) curInputClasses.toArray(new Class[curInputClasses.size()]);


                Object resultText = null;
                try {
                    resultText = execute(className,
                            methodName , classArray, objectFinalArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (resultText != null) {
                    Collection collection = setItems(resultText);
                    JSONArray jsonArray = new JSONArray();
                    if (collection != null) {
                        Iterator it = collection.iterator();
                        int num1 = Integer.parseInt(request.getParameter("Num"));
                        int count = 0;
                        while (it.hasNext()) {
                            if(count >= num1)
                                break;
                            count++;
                            JSONObject jsonArray1 = new JSONObject();
                            Object obj = it.next();
                                Collection collection1 = setItems(obj);
                                if (collection1 != null) {
                                    ArrayList<Object> tmp = new ArrayList<Object>(collection1);
                                    String[] columnname = resultColumn.getColumnname().split("\\|");

                                    for (int m = 0; m < tmp.size(); m++) {
                                        Object value = tmp.get(m);
                                        if (judgeBasicType(value)) {
                                            //result.appendText(value.toString()+" ");
                                            jsonArray1.put(columnname[m],value.toString());
                                        } else {
                                            //result.appendText(ReflectionToStringBuilder.toString(value)+" ");
                                            jsonArray1.put(columnname[m],ReflectionToStringBuilder.toString(value));
                                        }
                                    }


                                }
                                else {
                                    if (judgeBasicType(obj)) {
                                        //result.appendText(obj.toString()+" ");
                                        jsonArray1.put("obj",obj.toString());
                                    } else {
                                        //result.appendText(ReflectionToStringBuilder.toString(obj)+" ");
                                        jsonArray1.put("obj",ReflectionToStringBuilder.toString(obj));
                                    }
                                    //result.appendText("\n");
                                }
                            jsonArray.add(jsonArray1);
                        }

                    }
                    else {
                            if (judgeBasicType(resultText)) {
                                //result.appendText(resultText.toString()+" ");
                                jsonArray.add(resultText.toString());
                            } else {
                                //result.appendText(ReflectionToStringBuilder.toString(resultText)+" ");
                                jsonArray.add(ReflectionToStringBuilder.toString(resultText));
                            }

                            //result.appendText("\n");
                        }


                    JSONObject result = new JSONObject();
                    result.put("result",jsonArray);
                    result.put("code","0000");
                    result.put("msg","服务调用成功");
                    return result;
                }
            }


        }
        else if(ds.getType() == 3){

            for(int i = 0 ; i < functionLogics.size(); i ++){
                String methodName = functionLogics.get(i).getMethodname();
                String className = functionLogics.get(i).getClassname();
                int num = functionLogics.get(i).getParanum();
                List<paraInfo> paras = null;
                if(num != 0){
                    paras = paraInfoService.getParaInfoByFuncId(functionLogics.get(i).getFunctionId());
                }
                ArrayList<Object> objectArray = new ArrayList<Object>();
                ArrayList<Object> curInputClasses = new ArrayList<Object>();
                Object[] objectFinalArray;
                if(paras!=null) {
                    for (int k = 0; k < paras.size(); k++) {
                        paraInfo para = paras.get(k);
                        Class tmpClass;
                        if (para.getParaType().contains("[ ]")) {
                            tmpClass = getClassFromName(para.getParaType().split("\\[")[0] + "[ ]");
                        } else {
                            tmpClass = getClassFromName(para.getParaType());
                        }
                        curInputClasses.add(tmpClass);
                        Object tmpObj = null;
                        if (para.getOriType() == 2) {
                            Object tmptmpObj;
                            if(judgeBasicTypeByName(tmpClass)){
                                tmpObj = getObjectFromStringAndClass(tmpClass, para.getInputContent().substring(1,para.getInputContent().length()-1));
                            }
//                                else{
//                                    JSONObject tmpStr = (JSONObject)jsonArray.get(0);
//                                    tmptmpObj = (Object)JSONObject.toBean(tmpStr, tmpClass);
//                                }
                        } else if(para.getOriType() == 1){
                            tmpObj = integerObjectHashMap.get(para.getFuncResult());
                        }
                        else if(para.getOriType() == 3){
                            tmpObj = Integer.parseInt(request.getParameter("PageNum"));
                        }
                        objectArray.add(tmpObj);
                    }
                }

                objectFinalArray = (Object[]) objectArray.toArray();
                Class[] classArray = (Class[]) curInputClasses.toArray(new Class[curInputClasses.size()]);


                Object resultText = null;
                try {
                    resultText = execute(className,
                            methodName , classArray, objectFinalArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (resultText != null) {
                    Collection collection = setItems(resultText);
                    JSONArray jsonArray = new JSONArray();
                    if (collection != null) {
                        Iterator it = collection.iterator();
                        int num1 = Integer.parseInt(request.getParameter("Num"));
                        int count = 0;
                        while (it.hasNext()) {
                            if(count >= num1)
                                break;
                            count++;
                            JSONObject jsonArray1 = new JSONObject();
                            Object obj = it.next();
                            Collection collection1 = setItems(obj);
                            if (collection1 != null) {
                                ArrayList<Object> tmp = new ArrayList<Object>(collection1);
                                String[] columnname = resultColumn.getColumnname().split("\\|");

                                for (int m = 0; m < tmp.size(); m++) {
                                    Object value = tmp.get(m);
                                    if (judgeBasicType(value)) {
                                        //result.appendText(value.toString()+" ");
                                        jsonArray1.put(columnname[m],value.toString());
                                    } else {
                                        //result.appendText(ReflectionToStringBuilder.toString(value)+" ");
                                        jsonArray1.put(columnname[m],ReflectionToStringBuilder.toString(value));
                                    }
                                }


                            }
                            else {
                                if (judgeBasicType(obj)) {
                                    //result.appendText(obj.toString()+" ");
                                    jsonArray1.put("obj",obj.toString());
                                } else {
                                    //result.appendText(ReflectionToStringBuilder.toString(obj)+" ");
                                    jsonArray1.put("obj",ReflectionToStringBuilder.toString(obj));
                                }
                                //result.appendText("\n");
                            }
                            jsonArray.add(jsonArray1);
                        }

                    }
                    else {
                        if (judgeBasicType(resultText)) {
                            //result.appendText(resultText.toString()+" ");
                            jsonArray.add(resultText.toString());
                        } else {
                            //result.appendText(ReflectionToStringBuilder.toString(resultText)+" ");
                            jsonArray.add(ReflectionToStringBuilder.toString(resultText));
                        }

                        //result.appendText("\n");
                    }


                    JSONObject result = new JSONObject();
                    result.put("result",jsonArray);
                    result.put("code","0000");
                    result.put("msg","服务调用成功");
                    return result;
                }
            }



        }

        return ds;
    }






    @RequestMapping("/store")
    @ResponseBody
    public JSONObject toStore(HttpServletRequest request, Model model){
        HashMap<Integer,Object> integerObjectHashMap = new HashMap<Integer,Object>();
        Long typeId = Long.parseLong(request.getParameter("id"));
        dataSource ds = this.dataSourceService.findById(typeId);
        List<functionLogic> functionLogics = functionLogicService.selectByDSIDWithLogic(ds.getDsId());
        List<inputPara> inputParas = inputParaService.findBytypeId(ds.getType());
        resultColumn resultColumn =  resultColumnService.selectByPrimaryKey(ds.getDsId());

        if(ds.getType() == 2){
            listContent.clear();
            for(int i = 0 ; i < functionLogics.size(); i ++){
                String methodName = functionLogics.get(i).getMethodname();
                String className = functionLogics.get(i).getClassname();
                int num = functionLogics.get(i).getParanum();
                List<paraInfo> paras = null;
                if(num != 0){
                    paras = paraInfoService.getParaInfoByFuncId(functionLogics.get(i).getFunctionId());
                }
                ArrayList<Object> objectArray = new ArrayList<Object>();
                ArrayList<Object> curInputClasses = new ArrayList<Object>();
                Object[] objectFinalArray;
                if(paras!=null) {
                    for (int k = 0; k < paras.size(); k++) {
                        paraInfo para = paras.get(k);
                        Class tmpClass;
                        if (para.getParaType().contains("[ ]")) {
                            tmpClass = getClassFromName(para.getParaType().split("\\[")[0] + "[ ]");
                        } else {
                            tmpClass = getClassFromName(para.getParaType());
                        }
                        curInputClasses.add(tmpClass);
                        Object tmpObj = null;
                        if (para.getOriType() == 2) {
                            Object tmptmpObj;
                            if(judgeBasicTypeByName(tmpClass)){
                                tmpObj = getObjectFromStringAndClass(tmpClass, para.getInputContent().substring(1,para.getInputContent().length()-1));
                            }
//                                else{
//                                    JSONObject tmpStr = (JSONObject)jsonArray.get(0);
//                                    tmptmpObj = (Object)JSONObject.toBean(tmpStr, tmpClass);
//                                }
                        } else if(para.getOriType() == 1){
                            tmpObj = integerObjectHashMap.get(para.getFuncResult());
                        }
                        objectArray.add(tmpObj);
                    }
                }

                objectFinalArray = (Object[]) objectArray.toArray();
                Class[] classArray = (Class[]) curInputClasses.toArray(new Class[curInputClasses.size()]);


                Object resultText = null;
                try {
                    resultText = execute(className,
                            methodName , classArray, objectFinalArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (resultText != null) {
                    Collection collection = setItems(resultText);
                    integerObjectHashMap.put(i+1,resultText);
                    listContent.add(new ArrayList<String>());
                    listContent.get(listContent.size() - 1).add("第" + String.valueOf(i + 1) + "个：");
                    if (collection != null) {
                        Iterator it = collection.iterator();
                        int num1 = Integer.parseInt(request.getParameter("Num"));
                        int count = 0;
                        while (it.hasNext()) {
                            if(count >= num1)
                                break;
                            count++;
                            listContent.add(new ArrayList<String>());

                            Object obj = it.next();
                            Collection collection1 = setItems(obj);
                            if (collection1 != null) {
                                ArrayList<Object> tmp = new ArrayList<Object>(collection1);
                                String[] columnname = resultColumn.getColumnname().split("\\|");

                                for (int m = 0; m < tmp.size(); m++) {
                                    Object value = tmp.get(m);
                                    if (judgeBasicType(value)) {
                                        //result.appendText(value.toString()+" ");

                                        listContent.get(listContent.size() - 1).add(value.toString());
                                    } else {
                                        //result.appendText(ReflectionToStringBuilder.toString(value)+" ");
                                        listContent.get(listContent.size() - 1).add(ReflectionToStringBuilder.toString(value));
                                    }
                                }


                            }
                            else {
                                listContent.add(new ArrayList<String>());
                                if (judgeBasicType(obj)) {
                                    //result.appendText(obj.toString()+" ");
                                    listContent.get(listContent.size() - 1).add(obj.toString());
                                } else {
                                    //result.appendText(ReflectionToStringBuilder.toString(obj)+" ");
                                    listContent.get(listContent.size() - 1).add(ReflectionToStringBuilder.toString(obj));
                                }
                                //result.appendText("\n");
                            }

                        }

                    }
                    else {
                        listContent.add(new ArrayList<String>());
                        if (judgeBasicType(resultText)) {
                            //result.appendText(resultText.toString()+" ");
                            listContent.get(listContent.size() - 1).add(resultText.toString());
                        } else {
                            //result.appendText(ReflectionToStringBuilder.toString(resultText)+" ");
                            listContent.get(listContent.size() - 1).add(ReflectionToStringBuilder.toString(resultText));
                        }

                        //result.appendText("\n");
                    }

                    boolean isSucess=false;
                    java.io.FileOutputStream out=null;
                    java.io.OutputStreamWriter osw=null;
                    java.io.BufferedWriter bw=null;
                    try {
                        out = new java.io.FileOutputStream(ClassUtils.getDefaultClassLoader().getResource("").getPath()+System.currentTimeMillis()+".csv",true);
                        System.out.println(ClassUtils.getDefaultClassLoader().getResource("").getPath());
                        osw = new java.io.OutputStreamWriter(out,"utf-8");
                        bw =new java.io.BufferedWriter(osw);

                        for(int m = 0 ; m < listContent.size() ; m ++){
                            String a =  "";
                            for(int n = 0 ; n < listContent.get(m).size() ; n ++){
                                a += listContent.get(m).get(n)+"',";

                            }
                            bw.append(a).append("\n");
                        }
                        System.out.println("123");
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
                    JSONObject result = new JSONObject();
                    result.put("code","0000");
                    result.put("msg","服务调用成功");
                    dataTask tmp = new dataTask(System.currentTimeMillis(),ds.getDsId(),1,"userName",System.currentTimeMillis());
                    dataTaskService.save(tmp);
                    return result;
                }
            }


        }
        else if(ds.getType() == 3){
            listContent.clear();
            for(int i = 0 ; i < functionLogics.size(); i ++){
                String methodName = functionLogics.get(i).getMethodname();
                String className = functionLogics.get(i).getClassname();
                int num = functionLogics.get(i).getParanum();
                List<paraInfo> paras = null;
                if(num != 0){
                    paras = paraInfoService.getParaInfoByFuncId(functionLogics.get(i).getFunctionId());
                }
                ArrayList<Object> objectArray = new ArrayList<Object>();
                ArrayList<Object> curInputClasses = new ArrayList<Object>();
                Object[] objectFinalArray;
                if(paras!=null) {
                    for (int k = 0; k < paras.size(); k++) {
                        paraInfo para = paras.get(k);
                        Class tmpClass;
                        if (para.getParaType().contains("[ ]")) {
                            tmpClass = getClassFromName(para.getParaType().split("\\[")[0] + "[ ]");
                        } else {
                            tmpClass = getClassFromName(para.getParaType());
                        }
                        curInputClasses.add(tmpClass);
                        Object tmpObj = null;
                        if (para.getOriType() == 2) {
                            Object tmptmpObj;
                            if(judgeBasicTypeByName(tmpClass)){
                                tmpObj = getObjectFromStringAndClass(tmpClass, para.getInputContent().substring(1,para.getInputContent().length()-1));
                            }
//                                else{
//                                    JSONObject tmpStr = (JSONObject)jsonArray.get(0);
//                                    tmptmpObj = (Object)JSONObject.toBean(tmpStr, tmpClass);
//                                }
                        } else if(para.getOriType() == 1){
                            tmpObj = integerObjectHashMap.get(para.getFuncResult());
                        }
                        else if(para.getOriType() == 3){
                            tmpObj = Integer.parseInt(request.getParameter("PageNum"));
                        }
                        objectArray.add(tmpObj);
                    }
                }

                objectFinalArray = (Object[]) objectArray.toArray();
                Class[] classArray = (Class[]) curInputClasses.toArray(new Class[curInputClasses.size()]);


                Object resultText = null;
                try {
                    resultText = execute(className,
                            methodName , classArray, objectFinalArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (resultText != null) {
                    Collection collection = setItems(resultText);
                    integerObjectHashMap.put(i+1,resultText);
                    listContent.add(new ArrayList<String>());
                    listContent.get(listContent.size() - 1).add("第" + String.valueOf(i + 1) + "个：");
                    if (collection != null) {
                        Iterator it = collection.iterator();
                        int num1 = Integer.parseInt(request.getParameter("Num"));
                        int count = 0;
                        while (it.hasNext()) {
                            if(count >= num1)
                                break;
                            count++;
                            listContent.add(new ArrayList<String>());
                            Object obj = it.next();
                            Collection collection1 = setItems(obj);
                            if (collection1 != null) {
                                ArrayList<Object> tmp = new ArrayList<Object>(collection1);
                                String[] columnname = resultColumn.getColumnname().split("\\|");

                                for (int m = 0; m < tmp.size(); m++) {
                                    Object value = tmp.get(m);
                                    if (judgeBasicType(value)) {
                                        //result.appendText(value.toString()+" ");
                                        listContent.get(listContent.size() - 1).add(value.toString());
                                    } else {
                                        //result.appendText(ReflectionToStringBuilder.toString(value)+" ");
                                        listContent.get(listContent.size() - 1).add(ReflectionToStringBuilder.toString(value));
                                    }
                                }


                            }
                            else {
                                listContent.add(new ArrayList<String>());
                                if (judgeBasicType(obj)) {
                                    //result.appendText(obj.toString()+" ");
                                    listContent.get(listContent.size() - 1).add(obj.toString());
                                } else {
                                    //result.appendText(ReflectionToStringBuilder.toString(obj)+" ");
                                    listContent.get(listContent.size() - 1).add(ReflectionToStringBuilder.toString(obj));
                                }
                                //result.appendText("\n");
                            }

                        }

                    }
                    else {
                        listContent.add(new ArrayList<String>());
                        if (judgeBasicType(resultText)) {
                            //result.appendText(resultText.toString()+" ");
                            listContent.get(listContent.size() - 1).add(resultText.toString());
                        } else {
                            //result.appendText(ReflectionToStringBuilder.toString(resultText)+" ");
                            listContent.get(listContent.size() - 1).add(ReflectionToStringBuilder.toString(resultText));
                        }

                        //result.appendText("\n");
                    }


                    boolean isSucess=false;
                    java.io.FileOutputStream out=null;
                    java.io.OutputStreamWriter osw=null;
                    java.io.BufferedWriter bw=null;
                    try {
                        out = new java.io.FileOutputStream(ClassUtils.getDefaultClassLoader().getResource("").getPath()+System.currentTimeMillis()+".csv",true);
                        System.out.println(ClassUtils.getDefaultClassLoader().getResource("").getPath());
                        osw = new java.io.OutputStreamWriter(out,"utf-8");
                        bw =new java.io.BufferedWriter(osw);

                        for(int m = 0 ; m < listContent.size() ; m ++){
                            String a =  "";
                            for(int n = 0 ; n < listContent.get(m).size() ; n ++){
                                a += listContent.get(m).get(n)+"',";

                            }
                            bw.append(a).append("\n");
                        }
                        System.out.println("123");
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
                    JSONObject result = new JSONObject();
                    result.put("code","0000");
                    result.put("msg","服务调用成功");
                    dataTask tmp = new dataTask(System.currentTimeMillis(),ds.getDsId(),1,"userName",System.currentTimeMillis());
                    dataTaskService.save(tmp);
                    return result;
                }
            }



        }

        JSONObject result = new JSONObject();
        result.put("code","0001");
        result.put("msg","服务调用失败");
        return result;
    }








    public static Boolean judgeBasicTypeByName(Class obj){
        Class cls = obj;
        if(cls == (int.class))
            return true;
        else if(cls == (boolean.class))
            return true;
        else if(cls == char.class)
            return true;
        else if(cls == byte.class)
            return true;
        else if(cls == short.class)
            return true;
        else if(cls == long.class)
            return true;
        else if(cls == double.class)
            return true;
        else if(cls == float.class)
            return true;
        else if(cls == String.class)
            return true;
        else if(cls == java.math.BigDecimal.class)
            return true;
        else if(cls == java.math.BigInteger.class)
            return true;
        else if(cls == Boolean.class)
            return true;
        else if(cls == Byte.class)
            return true;
        else if(cls == Character.class)
            return true;
        else if(cls == CharSequence.class)
            return true;
        else if(cls == Double.class)
            return true;
        else if(cls == Float.class)
            return true;
        else if(cls == Integer.class)
            return true;
        else if(cls == Long.class)
            return true;
        else if(cls == Number.class)
            return true;
        else if(cls == Short.class)
            return true;
        else if(cls.isArray())
            return true;
        else
            return false;
    }

    public static Class getClassFromName(String className){
        if(className.equals("int"))
            return int.class;
        else if(className.equals("boolean"))
            return boolean.class;
        else if(className.equals("char"))
            return char.class;
        else if(className.equals("byte"))
            return byte.class;
        else if(className.equals("short"))
            return short.class;
        else if(className.equals("long"))
            return long.class;
        else if(className.equals("double"))
            return double.class;
        else if(className.equals("float"))
            return float.class;
        else if(className.contains("[ ]")){
            Class temp = getClassFromName(className.substring(0,className.length()-3));
            Class result = Array.newInstance(temp, 1).getClass();
            return result;
        }
        else if(className.contains("[]")){
            Class temp = getClassFromName(className.substring(0,className.length()-2));
            Class result = Array.newInstance(temp, 1).getClass();
            return result;
        }
        else{
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Object getObjectFromStringAndClass(Class className , String value){
        Object object = null;
        if(className.getTypeName() == "int"){
            object = Integer.parseInt(value);
        }
        else if(className.getTypeName() == "boolean"){
            object = Boolean.parseBoolean(value);
        }
        else if(className.getTypeName() == "char"){
            object = value.charAt(0);
        }
        else if(className.getTypeName() == "short"){
            object = Short.parseShort(value);
        }
        else if(className.getTypeName() == "long"){
            object = Long.parseLong(value);
        }
        else if(className.getTypeName() == "double"){
            object = Double.parseDouble(value);
        }

        else if(className.getTypeName() == "float"){
            object = Float.parseFloat(value);
        }
        //
        else if(className.getTypeName().contains("[")||className.getTypeName().contains("]")){

            int count = 1;
            int last = 1;
            ArrayList<String> strs = new ArrayList<String>();
            String tmp = "";
            for(int i = 0 ; i < value.length() ; i ++){
                if(value.charAt(i) != '"')
                    tmp += value.charAt(i);
            }
            value = tmp;

            for(int i = 1 ; i < value.length()-1 ; i ++){
                if(value.charAt(i) == ','){
                    count ++;
                    strs.add(value.substring(last,i));
                    last = i + 1;
                }

            }
            strs.add(value.substring(last,value.length()-1));


            Class temp = getClassFromName(className.getTypeName().substring(0,className.getTypeName().length()-2));

            Object[] result = new Object[count];

            for(int i = 0 ; i < count ; i ++){
                result[i] = getObjectFromStringAndClass(temp,strs.get(i));
            }

            object = result;



        }

        else if(className.getTypeName() == "java.lang.String"){
            object = value;
        }
        else{
            try {
                object = Class.forName(className.getTypeName()).newInstance();
                object = value;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }


        }
        return object;


    }
    public static Object execute(String className , String functionName,Class[] classes,Object[] objects) throws  Exception{
        String className1 = className;
        String functionName1 = functionName;
        Class claz = Class.forName(className1);

        Object obj = claz.newInstance();
        Method mth = obj.getClass().getDeclaredMethod(functionName1,classes);


        Object tmp =  mth.invoke(obj,objects);

        return tmp;

    }
    public static Collection setItems(Object items) {

        if(items instanceof List) {
            return (List) items;
        }

        if(items instanceof Collection) {
            return (Collection) items;
        }

        if(items instanceof Map) {
            Map map = (Map) items;
            return map.entrySet();  //Set
        }

        if(items.getClass().isArray()) {
            Collection coll = new ArrayList();
            int length = Array.getLength(items);
            for(int i=0;i<length;i++) {
                Object value = Array.get(items, i);
                coll.add(value);
            }
            return coll;
        }

        return null;
    }

    public static Boolean judgeBasicType(Object obj){
        Class cls = obj.getClass();
        if(cls == (int.class))
            return true;
        else if(cls == (boolean.class))
            return true;
        else if(cls == char.class)
            return true;
        else if(cls == byte.class)
            return true;
        else if(cls == short.class)
            return true;
        else if(cls == long.class)
            return true;
        else if(cls == double.class)
            return true;
        else if(cls == float.class)
            return true;
        else if(cls == String.class)
            return true;
        else if(cls == java.math.BigDecimal.class)
            return true;
        else if(cls == java.math.BigInteger.class)
            return true;
        else if(cls == Boolean.class)
            return true;
        else if(cls == Byte.class)
            return true;
        else if(cls == Character.class)
            return true;
        else if(cls == CharSequence.class)
            return true;
        else if(cls == Double.class)
            return true;
        else if(cls == Float.class)
            return true;
        else if(cls == Integer.class)
            return true;
        else if(cls == Long.class)
            return true;
        else if(cls == Number.class)
            return true;
        else if(cls == Short.class)
            return true;
        else if(cls.isArray())
            return true;
        else
            return false;
    }

}