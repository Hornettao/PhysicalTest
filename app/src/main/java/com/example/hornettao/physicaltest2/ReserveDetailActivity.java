package com.example.hornettao.physicaltest2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hornettao.http.HttpUtils;
import com.example.hornettao.json.JsonTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hornettao on 14-9-2.
 */
public class ReserveDetailActivity extends Activity {
    private String urlReserveDetail;
    private ListView listView;
    private Adapter adapter;
    private ImageButton backButton;
    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        urlReserveDetail = "http://192.168.1.129:8080/AppTest/servlet/AppStudentReservationServlet?method=makeReservation_getAllowReservedDateAndHaveReservedNumAndRemainedNum";
        setContentView(R.layout.activity_reserve_detail);
        listView = (ListView) this.findViewById(R.id.listView);
        backButton = (ImageButton) this.findViewById(R.id.backButton);
        new MyTaskReserveDetail().execute(urlReserveDetail);

        adapter = new MyAdapter();
        listView.setAdapter((ListAdapter)adapter);

        //list = getData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Dialog dialog = new AlertDialog.Builder(ReserveDetailActivity.this)
                        .setTitle("确认预约").setMessage("您选择的预约时间为"
                        + seperateTime(list.get(position).get("startTime"), list.get(position).get("endTime")))
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("--->id   " + list.get(position).get("allowReserveDateId"));
                                String url =  "http://192.168.1.129:8080/AppTest/servlet/AppStudentReservationServlet?method=makeReservation_makeMyReservation&allowReserveDateId=" + list.get(position).get("allowReserveDateId");
                                new MyTask().execute(url);
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(ReserveDetailActivity.this).inflate(R.layout.list_item, null);
            TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
            TextView peopleTextView = (TextView) view.findViewById(R.id.peopleTextView);
            String newTime = seperateTime(list.get(position).get("startTime"), list.get(position).get("endTime"));
            timeTextView.setText(newTime);
            peopleTextView.setText(list.get(position).get("haveReservedNum") + "/" + list.get(position).get("remainedNum"));
            return view;
        }
    }



    //异步类，与网络连接并进行UI控件的更新
    class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String jsonString = HttpUtils.sendGetMethod(params[0], "utf-8");

            List<Map<String, String>> mapList = JsonTools.parseList(jsonString);
            result = mapList.get(0).get("result");
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("reserveSucc")) {
                Dialog dialog2 = new AlertDialog.Builder(ReserveDetailActivity.this).
                        setTitle("预约成功").setMessage("点击确定查看预约")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ReserveDetailActivity.this, SeeReserveActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("返回主界面", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Intent intent = new Intent(ReserveDetailActivity.this, ReserveActivity.class);
                                //startActivity(intent);
                                finish();
                            }
                        }).create();
                dialog2.show();
            } else {
                Dialog dialog2 = new AlertDialog.Builder(ReserveDetailActivity.this).
                        setTitle("提示").setMessage("您已经预约过了，请不要重复预约")
                        .setPositiveButton("查看预约", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ReserveDetailActivity.this, SeeReserveActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("返回主界面", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Intent intent = new Intent(ReserveDetailActivity.this, ReserveActivity.class);
                                //startActivity(intent);
                                finish();
                            }
                        }).create();
                dialog2.show();
            }

        }
    }

    //异步类，与网络连接并进行UI控件的更新
    class MyTaskReserveDetail extends AsyncTask<String, Void, List<Map<String, String>>> {

        @Override
        protected List<Map<String, String>> doInBackground(String... params) {
            String result = "";
            String jsonString = HttpUtils.sendGetMethod(params[0], "utf-8");
            List<Map<String, String>> mapList = JsonTools.parseList(jsonString);
            list = mapList;
            return list;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> m) {
            super.onPostExecute(m);
            adapter = new MyAdapter();
            listView.setAdapter((ListAdapter)adapter);
        }
    }
}

