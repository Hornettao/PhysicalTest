package com.example.hornettao.physicaltest2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hornettao.http.HttpUtils;
import com.example.hornettao.json.JsonTools;

import java.util.List;
import java.util.Map;

/**
 * Created by hornettao on 14-9-4.
 */
public class ChangePasswordActivity extends Activity {
    private ImageButton backButton;
    private Button changePasswordButton;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        backButton = (ImageButton) this.findViewById(R.id.backButton);
        changePasswordButton = (Button) this.findViewById(R.id.changePasswordButton);
        newPasswordEditText = (EditText) this.findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = (EditText) this.findViewById(R.id.confirmPasswordEditText);

        confirmPasswordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    changePassword();
                }
                return false;
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void changePassword() {
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        int newPasswordLength = newPassword.length();
        int confirmPasswordLength = confirmPassword.length();
        if (newPassword.equals(confirmPassword)) {
            if (newPasswordLength >= 6 && newPasswordLength <=20 && confirmPasswordLength >= 6 && confirmPasswordLength <= 20) {
                url = "http://192.168.1.129:8080/AppTest/servlet/AppStudentChangePasswordServlet?method=changeMyPassword&newPassword=" + newPassword;
                new MyTask().execute(url);
            } else if (newPasswordLength > 0 && confirmPasswordLength > 0) {
                //新密码长度不符合要求时
                newPasswordEditText.setText("");
                confirmPasswordEditText.setText("");
                newPasswordEditText.setError("新密码长度不符合要求");
                confirmPasswordEditText.setError("新密码长度不符合要求");
            } else {
                //新密码为空时
                newPasswordEditText.setError("新密码不能为空");
                confirmPasswordEditText.setError("新密码不能为空");
            }
        } else {
            //两次密码输入不一致时
            Dialog dialog = new AlertDialog.Builder(ChangePasswordActivity.this)
                    .setTitle("提示").setMessage("两次密码输入不一致，请重新输入")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            newPasswordEditText.setText("");
                            confirmPasswordEditText.setText("");
                        }
                    }).create();
            dialog.show();
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
            if (m.get(0).get("result").equals("succ")){
                //首先弹出AlertDialog，提示用户修改密码成功
                Dialog dialog = new AlertDialog.Builder(ChangePasswordActivity.this)
                        .setTitle("提示").setMessage("修改密码成功,请牢记密码")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //点击确定，返回 我的账户 页面
                                finish();
                            }
                        }).create();
                dialog.show();
            } else {
                //密码修改失败时
                Dialog dialog = new AlertDialog.Builder(ChangePasswordActivity.this)
                        .setTitle("提示").setMessage("密码修改失败，请重新修改")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                newPasswordEditText.setText("");
                                confirmPasswordEditText.setText("");
                            }
                        }).create();
                dialog.show();
            }
        }
    }
}
