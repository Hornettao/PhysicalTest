package com.example.hornettao.physicaltest2;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hornettao.http.HttpUtils;
import com.example.hornettao.json.JsonTools;

import java.util.List;
import java.util.Map;

public class LoginActivity extends Activity {

    private String url;
    private EditText userNameEditText;
    private EditText passWordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameEditText = (EditText) this.findViewById(R.id.userNameEditText);
        passWordEditText = (EditText) this.findViewById(R.id.passWordEditText);
        loginButton = (Button) this.findViewById(R.id.loginButton);
        //登录按钮点击
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEditText.getText().toString().trim();
                String passWord = passWordEditText.getText().toString().trim();
                if (userName == null || userName.equals("")) {
                    userNameEditText.setError("用户名不能为空");
                } else if (passWord == null || passWord.equals("")) {
                    passWordEditText.setError("密码不能为空");
                } else {
                    url = "http://192.168.1.129:8080/AppTest/servlet/AppStudentLoginServlet?method=login&username=" + userName + "&password=" + passWord;

                    new MyTask().execute(url);
//                  Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                  startActivity(intent);

                }
            }
        });

//        passWordEditText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                    String userName = userNameEditText.getText().toString().trim();
//                    String passWord = passWordEditText.getText().toString().trim();
//                    if (userName == null || userName.equals("")) {
//                        userNameEditText.setError("用户名不能为空");
//                    } else if (passWord == null || passWord.equals("")) {
//                        passWordEditText.setError("密码不能为空");
//                    } else {
//                        url = "http://192.168.1.132:8080/AppTest/servlet/AppStudentLoginServlet?method=login&username=" + userName + "&password=" + passWord;
//                        //url = "https://gitcafe.com/wcrane/XXXYYY/raw/master/baidu.json";
//                        new MyTask().execute(url);
////                  Intent intent = new Intent(LoginActivity.this, MainActivity.class);
////                  startActivity(intent);
//
//                    }
//
//                }
//                return true;
//            }
//
//
//        });

    }


    //异步类，与网络连接并进行UI控件的更新
    class MyTask extends AsyncTask<String, Void, List<Map<String, String>>> {

        @Override
        protected List<Map<String, String>> doInBackground(String... params) {
            String result = "";
            HttpUtils.httpOpen();
            String jsonString = HttpUtils.sendGetMethod(params[0], "utf-8");
            List<Map<String, String>> mapList = JsonTools.parseList(jsonString);
            return mapList;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> m) {
            super.onPostExecute(m);
            System.out.println("--->没跳");
            if (m.get(0).get("result").equals("loginSucc")) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                //finish();
                System.out.println("--->跳转了");
            } else if (m.get(0).get("reason").equals("wrongPassword")) {
                Toast.makeText(LoginActivity.this, "用户名或密码错误，今天剩余次数" + m.get(0).get("remainTimes"), Toast.LENGTH_SHORT).show();
                userNameEditText.setText("");
                passWordEditText.setText("");
            } else {
                Toast.makeText(LoginActivity.this, "已登录失败三次，请明天再尝试", Toast.LENGTH_SHORT).show();
                userNameEditText.setText("");
                passWordEditText.setText("");
            }
        }
    }


    @Override
    protected void onPause() {
        super.onStart();
        userNameEditText.setText("");
        passWordEditText.setText("");
    }
}
