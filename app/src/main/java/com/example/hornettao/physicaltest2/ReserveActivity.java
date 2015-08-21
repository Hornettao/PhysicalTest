package com.example.hornettao.physicaltest2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ReserveActivity extends Activity {

    private ListView listView;
    private Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        System.out.println("--->reserve on create");

        listView = (ListView) this.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getData());
        listView.setAdapter((ListAdapter)adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(ReserveActivity.this, ReserveDetailActivity.class);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(ReserveActivity.this, SeeReserveActivity.class);
                    startActivity(intent);

                }
            }
        });
    }


    public List<String> getData() {
        List<String> list = new ArrayList<String>();
        list.add("预约");
        list.add("查看预约");
        return list;
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


    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("--->reserve on start");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("--->reserve on start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("--->reserve on resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("--->reserve on pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("--->reserve on stop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("--->reserve on destroy");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            System.out.println("--->reserve back");
            Dialog dialog2 = new AlertDialog.Builder(ReserveActivity.this).
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
