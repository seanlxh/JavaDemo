package javaDemo.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class jsonUtil {

    public static JSONArray getJsonArrayFromString(String json){
        JSONArray jsonarray;
        //读取JSONArray，用下标索引获取
        String array = json;
        jsonarray = JSONArray.fromObject(array);
        return jsonarray;
    }

}
