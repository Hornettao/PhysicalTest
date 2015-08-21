package com.example.hornettao.physicaltest2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hornettao.http.HttpUtils;
import com.example.hornettao.json.JsonTools;

import java.security.PrivilegedAction;
import java.util.List;
import java.util.Map;

/**
 * Created by hornettao on 14-9-2.
 */
public class SeeReserveActivity extends Activity {
    private Dialog progressDialog;
    private ImageButton backButton;
    private Button goToReserveButton;
    private Button cancelReserveButton;
    private Button changeReserveButton;
    private TextView reserveInfoTextView;
    private String urlReserveInfo = "http://192.168.1.129:8080/AppTest/servlet/AppStudentReservationServlet?method=lookMyReservation";
    private String urlCancelReserve = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_reserve);
        backButton = (ImageButton) this.findViewById(R.id.backButton);
        goToReserveButton = (Button) this.findViewById(R.id.goToReserveButton);
        cancelReserveButton = (Button) this.findViewById(R.id.cancelReserveButton);
        changeReserveButton = (Button) this.findViewById(R.id.changeReserveButton);
        reserveInfoTextView = (TextView) this.findViewById(R.id.reserveInfoTextView);
        cancelReserveButton.setVisibility(View.INVISIBLE);
        changeReserveButton.setVisibility(View.INVISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //加载网络数据进度条
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("加载网络信息中，请稍等");

        //progressDialog.setCancelable(false);

        new MyTaskReserveInfo().execute(urlReserveInfo);

        //点击取消预约按钮，与服务器连接取消预约
        cancelReserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //首先弹出AlertDialog，提示用户是否确定取消预约
                Dialog dialog = new AlertDialog.Builder(SeeReserveActivity.this)
                        .setTitle("取消预约").setMessage("是否确定取消预约?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new MyTaskCancelReserve().execute(urlCancelReserve);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                dialog.show();

            }
        });

        //点击修改预约按钮，首先与服务器连接，取消预约；然后，跳转到预约界面
        changeReserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先弹出AlertDialog，提示用户是否确定取消预约
                Dialog dialog = new AlertDialog.Builder(SeeReserveActivity.this)
                        .setTitle("取消预约").setMessage("是否确定取消预约?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new MyTaskChangeReserve().execute(urlCancelReserve);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                dialog.show();
            }
        });


        //点击去预约按钮，跳转到预约界面
        goToReserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeReserveActivity.this, ReserveDetailActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    //异步类，与网络连接 取消预约 并进行UI控件的更新
    class MyTaskChangeReserve extends AsyncTask<String, Void, List<Map<String, String>>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Map<String, String>> doInBackground(String... params) {

            String jsonString = HttpUtils.sendGetMethod(params[0], "utf-8");
            List<Map<String, String>> mapList = JsonTools.parseList(jsonString);
            return mapList;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> m) {
            super.onPostExecute(m);
            if (m.get(0).get("result").equals("succ")) {

                goToReserveButton.setVisibility(View.VISIBLE);
                cancelReserveButton.setVisibility(View.INVISIBLE);
                changeReserveButton.setVisibility(View.INVISIBLE);
                reserveInfoTextView.setText("您还没有预约");

                Dialog dialog = new AlertDialog.Builder(SeeReserveActivity.this)
                        .setTitle("取消预约成功").setMessage("点击确定去预约")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SeeReserveActivity.this, ReserveDetailActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                dialog.show();
            } else {
                Dialog dialog = new AlertDialog.Builder(SeeReserveActivity.this)
                        .setMessage("取消预约失败，请重新取消")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                dialog.show();
            }
        }
    }


    //异步类，与网络连接 取消预约 并进行UI控件的更新
    class MyTaskCancelReserve extends AsyncTask<String, Void, List<Map<String, String>>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            if (m.get(0).get("result").equals("succ")) {

                goToReserveButton.setVisibility(View.VISIBLE);
                cancelReserveButton.setVisibility(View.INVISIBLE);
                changeReserveButton.setVisibility(View.INVISIBLE);
                reserveInfoTextView.setText("您还没有预约");

                Dialog dialog = new AlertDialog.Builder(SeeReserveActivity.this)
                        .setMessage("取消预约成功")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                dialog.show();
            } else {
                Dialog dialog = new AlertDialog.Builder(SeeReserveActivity.this)
                        .setMessage("取消预约失败，请重新取消")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                dialog.show();
            }
        }
    }


    //异步类，与网络连接 返回已预约信息 并进行UI控件的更新
    class MyTaskReserveInfo extends AsyncTask<String, Void, List<Map<String, String>>> {


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
            //list = mapList;
            return mapList;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> m) {
            super.onPostExecute(m);
            if (m.get(0).get("result").equals("succ")) {
                String startTime = m.get(0).get("startTime");
                String endTime = m.get(0).get("endTime");
                String newTime = seperateTime(startTime, endTime);
                reserveInfoTextView.setText(newTime);

                cancelReserveButton.setVisibility(View.VISIBLE);
                changeReserveButton.setVisibility(View.VISIBLE);
                goToReserveButton.setVisibility(View.INVISIBLE);
                urlCancelReserve ="http://192.168.1.129:8080/AppTest/servlet/AppStudentReservationServlet?method=deleteMyReservation&allowReserveDateId=" + m.get(0).get("allowReserveDateId");
            }
            progressDialog.dismiss();

        }
    }

    public String seperateTime(String startTime, String endTime) {
        String newTime = "";
        String[] startTimeArray = startTime.split(":");
        String[] endTimeArray = endTime.split(":");
        newTime = startTimeArray[0] + "年" + startTimeArray[1] + "月"
                + startTimeArray[2] + "日" + startTimeArray[3] + ":" + startTimeArray[4]
                + "~" + endTimeArray[3] + ":" + endTimeArray[4];
        return newTime;
    }

}
