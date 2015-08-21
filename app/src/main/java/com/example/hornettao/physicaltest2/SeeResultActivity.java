package com.example.hornettao.physicaltest2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hornettao.http.HttpUtils;
import com.example.hornettao.json.JsonTools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hornettao on 14-9-2.
 */
public class SeeResultActivity extends Activity {
    private String url;
    private Adapter adapter;
    private ListView listView;
    //private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private List<String> itemList = new ArrayList<String>();
    private List<String> resultList = new ArrayList<String>();
    private List<String> itemListReal = new ArrayList<String>();
    private List<String> resultListReal = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_result);
        listView = (ListView) this.findViewById(R.id.listView);
        url = "http://192.168.1.129:8080/AppTest/servlet/AppStudentLookTestResultServlet?method=lookMyTestResult";
        new MyTask().execute(url);
    }



    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return itemListReal.size();
        }

        @Override
        public Object getItem(int position) {
            return itemListReal.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(SeeResultActivity.this).inflate(R.layout.result_list_item, null);
            TextView itemTextView = (TextView) view.findViewById(R.id.itemTextView);
            TextView resultTextView = (TextView) view.findViewById(R.id.resultTextView);
            itemTextView.setText(itemListReal.get(position));
            resultTextView.setText(resultListReal.get(position));
            return view;
        }
    }


    //异步类，与网络连接并进行UI控件的更新
    class MyTask extends AsyncTask<String, Void, List<Map<String, String>>> {

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
                Set<String> s = m.get(0).keySet();
                Iterator itemIterator = s.iterator();
                while (itemIterator.hasNext()) {
                    String item = (String) itemIterator.next();

                    itemList.add(item);
                }

                Collection<String> c = m.get(0).values();
                Iterator resultIterator = c.iterator();
                while (resultIterator.hasNext()) {
                    String result = (String) resultIterator.next();


                    resultList.add(result);
                }

                //真正的顺序
                itemListReal.add("身     高 :");
                itemListReal.add("体     重 :");
                itemListReal.add("肺 活 量 :");
                itemListReal.add("耐     力 :");
                itemListReal.add("柔 韧 度 :");
                itemListReal.add("速度灵活性:");


                resultListReal.add(resultList.get(3) + "cm");
                resultListReal.add(resultList.get(2) + "kg");
                resultListReal.add(resultList.get(4) + "ml");
                resultListReal.add(resultList.get(0) + "xx");
                resultListReal.add(resultList.get(6) + "xx");
                resultListReal.add(resultList.get(5) + "xx");


                adapter = new MyAdapter();

                listView.setAdapter((ListAdapter) adapter);
            } else {
                Dialog dialog2 = new AlertDialog.Builder(SeeResultActivity.this).
                        setTitle("提示").setMessage("还没有您的体测结果")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                dialog2.show();
            }
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            System.out.println("--->reserve back");
            Dialog dialog2 = new AlertDialog.Builder(SeeResultActivity.this).
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
