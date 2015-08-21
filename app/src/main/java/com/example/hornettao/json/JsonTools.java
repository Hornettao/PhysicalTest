package com.example.hornettao.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hornettao on 14-8-31.
 */
public class JsonTools {

    //解析得到的Json数据,返回List<Map<String, String>>
    public static List<Map<String, String>> parseList(String jsonString) {
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        try {

            System.out.println("--->0");
            System.out.println("--->string: " + jsonString);
            JSONObject returnJsonObject = new JSONObject(jsonString);
            System.out.println("--->1");
            JSONArray jsonArray = returnJsonObject.getJSONArray("return");
            System.out.println("--->2");
            //JSONArray jsonArray = new JSONArray(jsonString);
            for(int i = 0;i < jsonArray.length(); i++) {
                System.out.println("--->3");
                Map<String, String> map = new HashMap<String, String>();
                JSONObject infoJsonObject = jsonArray.getJSONObject(i);
                System.out.println("--->4");
                Iterator<String> stringIterator = infoJsonObject.keys();
                while(stringIterator.hasNext()) {
                    System.out.println("--->5");
                    String key = stringIterator.next();
                    String info = infoJsonObject.getString(key);
                    Log.v("tyc",key + "--->" + info);
                    System.out.println("--->");
                    map.put(key, info);
                }
                mapList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mapList;
    }
}
