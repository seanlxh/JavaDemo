package com.example.demo.util;


    public class jsonUtil {

        public static net.sf.json.JSONArray getJsonArrayFromString(String json){
            net.sf.json.JSONArray jsonarray;
            //读取JSONArray，用下标索引获取
            String array = json;
            jsonarray = net.sf.json.JSONArray.fromObject(array);
            return jsonarray;
        }

    }

