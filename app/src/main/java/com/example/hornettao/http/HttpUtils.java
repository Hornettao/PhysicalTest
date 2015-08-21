package com.example.hornettao.http;

import android.content.Entity;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by hornettao on 14-8-31.
 */
public class HttpUtils {

    static HttpClient httpClient;

    public static void httpOpen() {
        httpClient = new DefaultHttpClient();
    }


    //HttpGet方法，传入一个url和一个编码方式，返回json数据
    public static String sendGetMethod(String url, String code) {

        String result = "";

        HttpGet httpGet = new HttpGet(url);
        System.out.println("--->进来了");
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                System.out.println("--->有网");
                System.out.println("--->response:" + httpResponse.getEntity());
                result = EntityUtils.toString(httpResponse.getEntity(), code);
                System.out.println("--->httputils  " + result);
            } else {
                Log.v("tyc", "没有网络");
                System.out.println("--->没网");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    public static void httpShutDown() {
        httpClient.getConnectionManager().shutdown();
    }
}
