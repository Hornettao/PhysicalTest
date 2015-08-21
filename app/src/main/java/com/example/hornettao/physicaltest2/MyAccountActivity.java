package com.example.hornettao.physicaltest2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.hornettao.http.HttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hornettao on 14-9-2.
 */
public class MyAccountActivity extends Activity {
    private ListView listView;
    private Adapter adapter;
    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        listView = (ListView) this.findViewById(R.id.listView);
        backButton = (ImageButton) this.findViewById(R.id.backButton);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getData());
        listView.setAdapter((ListAdapter)adapter);



        System.out.println("--->My account on create");


        //ListView上选项点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(MyAccountActivity.this, ChangePasswordActivity.class);
                    startActivity(intent);
                } else {

                    //首先弹出AlertDialog，提示用户是否确定注销
                    Dialog dialog = new AlertDialog.Builder(MyAccountActivity.this)
                            .setTitle("提示").setMessage("是否确定注销?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击确定，关闭网络连接
                                    HttpUtils.httpShutDown();
//                                    Intent intent = new Intent(MyAccountActivity.this, LoginActivity.class);
//                                    startActivity(intent);
                                    finish();

                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .create();
                    dialog.show();
                }
            }
        });
    }

    public List<String> getData() {
        List<String> list = new ArrayList<String>();
        list.add("修改密码");
        list.add("注销");
        return list;
    }




    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("--->My account on start");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("--->My account on start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("--->My account on resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("--->My account on pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("--->My account on stop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("--->My account on destroy");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            System.out.println("--->reserve back");
            Dialog dialog2 = new AlertDialog.Builder(MyAccountActivity.this).
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
