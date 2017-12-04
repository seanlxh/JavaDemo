package javaDemo.util;

import javafx.beans.property.StringProperty;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class processCollection {


    public static ArrayList<String> splitClassAndMethodAndLevel(String classFunctionAsString){

        String levelName , methodName , className;
        int i = classFunctionAsString.length() - 1;
        while(Character.isDigit(classFunctionAsString.charAt(i))){
            i --;
        }
        levelName = classFunctionAsString.substring(i + 1);

        int j = i;
        boolean point = false;
        while(classFunctionAsString.charAt(j) != '.' || !point){
            j --;
            if(classFunctionAsString.charAt(j) == '(')
                point = true;
        }
        methodName = classFunctionAsString.substring(j + 1,i + 1);

        int k = j;
        className = classFunctionAsString.substring(0 , k);

        ArrayList<String> result = new ArrayList<String>();
        result.add(className);
        result.add(methodName);
        result.add(levelName);

        return result;
    }

    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
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

    public static Type judgeClass(String className , String functionName,ArrayList<Class> classes) throws  Exception{
        String className1 = className;

        String functionName1 = functionName.substring(0,functionName.indexOf('('));


        System.out.println(className1.getClass());

        Class claz = Class.forName(className1);


        Object obj = claz.newInstance();

        Class[] classArray = (Class[])classes.toArray(new Class[classes.size()]);

        Method mth = obj.getClass().getDeclaredMethod(functionName1,classArray);

        Type t = mth.getAnnotatedReturnType().getType();

        return t;

    }

    public static String stringPropertyToString(StringProperty strpro){
        String temp = strpro.getValue();
        String result = "";
        for(int i = 0 ; i < temp.length(); i ++){
            result += temp.charAt(i);
        }
        return result;
    }

    public static String getMethodNameWithoutExtra(String functionName){
        String result = "";
        result = functionName.substring(0,functionName.indexOf('('));
        return result;
    }

    public static Object execute(String className , String functionName,Class[] classes,Object[] objects) throws  Exception{
        String className1 = className;
        String functionName1 = getMethodNameWithoutExtra(functionName);
        Class claz = Class.forName(className1);

        Object obj = claz.newInstance();
        Method mth = obj.getClass().getDeclaredMethod(functionName1,classes);


        Object tmp =  mth.invoke(obj,objects);

        return tmp;

    }

    public static ArrayList<String> getParaTypeFromMethod(String methodName){
        ArrayList<String> paras = new ArrayList<String>();
        if(methodName.indexOf('(')+1 == methodName.indexOf(')'))
            return paras;
        String allPara = methodName.substring(methodName.indexOf('(')+1,methodName.indexOf(')'));
        String[] temp = allPara.split(",");
        for(String onePara : temp){
            paras.add(onePara.trim());
        }
        return paras;

    }

    public static Boolean judgeBasicType(Object obj){
        if(obj.getClass() == (int.class))
            return true;
        else if(obj.getClass() == (boolean.class))
            return true;
        else if(obj.getClass() == char.class)
            return true;
        else if(obj.getClass() == byte.class)
            return true;
        else if(obj.getClass() == short.class)
            return true;
        else if(obj.getClass() == long.class)
            return true;
        else if(obj.getClass() == double.class)
            return true;
        else if(obj.getClass() == float.class)
            return true;
        else if(obj.getClass() == java.lang.String.class)
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
}
