package com.view.daohang.nfc;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;

import Utils.HttpUtil;
import Utils.LocalInfor;
import Utils.SHAPassword;

public class LoginActivity extends AppCompatActivity {

    private EditText et_user_name,et_password;
    private ProgressBar progressBar;
    private TextView text_view;
    private ImageView iv_logo;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(LoginActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    LoginActivity.this.finish();
                    progressBar.setVisibility(View.GONE);
                    text_view.setText("");
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    text_view.setText("");
                    break;
                case 3:
                    Toast.makeText(LoginActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    text_view.setText("");
                    break;
                default:
                    break;
            }
        }
    };
    private HttpUtil httpUtil = new HttpUtil(handler);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        initView();
        isNetWork();
        setOnclick();
    }

    private void initView() {
        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_password = (EditText) findViewById(R.id.et_password);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        text_view = (TextView) findViewById(R.id.text_view);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
    }

    public void Login(View view){

        String username = et_user_name.getText().toString();
        String password = et_password.getText().toString();
        //安卓设备的唯一Id
        final String str_mac = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final String SHAassword = SHAPassword.getSHA256StrJava(password);
        if (TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
                Toast.makeText(LoginActivity.this,"账号或密码不能为空",Toast.LENGTH_SHORT).show();
            }else {
                if (!LocalInfor.isNetworkAvailable(LoginActivity.this)){
                    Toast.makeText(LoginActivity.this,"网络未连接",Toast.LENGTH_SHORT).show();
                }else {
                    String json = httpUtil.blowJson(username, SHAassword);
                    httpUtil.login(json,str_mac);
                    progressBar.setVisibility(View.VISIBLE);
                    text_view.setText("登录中...");
                }
            }
    }

    public void isNetWork(){
        if (!LocalInfor.isNetworkAvailable(LoginActivity.this)) {
            //弹窗提示 网络不可用
            String showMsg = "当前网络不可用，请检查网络连接";
            DialogUIUtils.showAlert(LoginActivity.this, "提示", showMsg, "", "", "确定", "", true, true, true, new DialogUIListener() {
                @Override
                public void onPositive() {
                }
                @Override
                public void onNegative() {
                }
            }).show();
        }
    }

    public void setOnclick(){

        iv_logo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(LoginActivity.this,"长按登录按钮",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}
