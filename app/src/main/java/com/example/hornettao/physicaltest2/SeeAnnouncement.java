package com.example.hornettao.physicaltest2;

import android.app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hornettao.http.HttpUtils;
import com.example.hornettao.json.JsonTools;

import java.util.List;
import java.util.Map;


public class SeeAnnouncement extends Activity {

    private TextView announcementTextView;
    private String url;
    private Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_annoucement);
        announcementTextView = (TextView) this.findViewById(R.id.announcementTextView);
        //加载网络数据进度条
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("加载网络信息中，请稍等");
        url = "http://192.168.1.129:8080/AppTest/servlet/AppStudentLookTestAnnouncementServlet?method=lookLatestTestAnnouncement";
        new MyTask().execute(url);


        System.out.println("-->announcement on create");
    }





    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("--->announcement on start");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("--->announcement on start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("--->announcement on resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("--->announcement on pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("--->announcement on stop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("--->announcement on destroy");
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //异步类，与网络连接并进行UI控件的更新
    class MyTask extends AsyncTask<String, Void, List<Map<String, String>>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected List<Map<String, String>> doInBackground(String... params) {
            String result = "";
            String jsonString = HttpUtils.sendGetMethod(params[0], "utf-8");
            List<Map<String, String>> mapList = JsonTools.parseList(jsonString);
            return mapList;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> m) {
            super.onPostExecute(m);
            System.out.println("--->没跳");
            if (m.get(0).get("result").equals("succ")) {
                announcementTextView.setText(m.get(0).get("content"));
            } else {
                announcementTextView.setText("没有最新的体测通知");
            }
            progressDialog.dismiss();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            System.out.println("--->reserve back");
            Dialog dialog2 = new AlertDialog.Builder(SeeAnnouncement.this).
                    setMessage("点击确定将退回到登录界面")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            dialog2.show();
        }
        return true;
    }
}
