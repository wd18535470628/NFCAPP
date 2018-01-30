package com.view.daohang.nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import Fragments.HomeFragment;
import Fragments.SubmitFragment;
import Fragments.NoSubmitFragment;
import Fragments.MyFragment;
import Utils.HttpUtil;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup mRadioGroup;
    private List<Fragment> fragments = new ArrayList<>();
    private Fragment fragment;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private RadioButton rb_Home,rb_Message,rb_Find,rb_My;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    private NetworkConnectChangeReceiver netWorkChangeReceiver;
    private Intent nfcintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //用以实时监控网络
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkChangeReceiver = new NetworkConnectChangeReceiver();
        registerReceiver(netWorkChangeReceiver,intentFilter);
        initView(); //初始化组件
        mRadioGroup.setOnCheckedChangeListener(this); //点击事件
        fragments = getFragments(); //添加布局
        //添加默认布局
        normalFragment();
        Connector.getDatabase();
    }

    //默认布局
    private void normalFragment() {
        fm=getSupportFragmentManager();
        transaction=fm.beginTransaction();
        fragment=fragments.get(0);
        transaction.replace(R.id.mFragment,fragment);
        transaction.commit();
    }

    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        rb_Home= (RadioButton) findViewById(R.id.mRb_home);
        rb_Message= (RadioButton) findViewById(R.id.mRb_message);
        rb_Find= (RadioButton) findViewById(R.id.mRb_find);
        rb_My= (RadioButton) findViewById(R.id.mRb_my);

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter = NfcAdapter.getDefaultAdapter(MainActivity.this);
        if (nfcAdapter == null){
            Toast.makeText(MainActivity.this,"该设备不支持NFC功能！",Toast.LENGTH_SHORT).show();
        }else {
            if (!nfcAdapter.isEnabled()){
                Toast.makeText(MainActivity.this,"请在系统设置中先启用NFC功能！",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group,int checkedId) {
        fm=getSupportFragmentManager();
        transaction=fm.beginTransaction();
        switch (checkedId){
            case R.id.mRb_home:
                fragment=fragments.get(0);
                transaction.replace(R.id.mFragment,fragment);
                if (nfcAdapter!=null){
                    if (null == HttpUtil.getRealName()){
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        MainActivity.this.finish();
                    }else {
                        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
                    }
                }
                break;
            case R.id.mRb_message:
                fragment=fragments.get(1);
                transaction.replace(R.id.mFragment,fragment);
                break;
            case R.id.mRb_find:
                fragment=fragments.get(2);
                transaction.replace(R.id.mFragment,fragment);
                break;
            case R.id.mRb_my:
                fragment=fragments.get(3);
                transaction.replace(R.id.mFragment,fragment);
                break;
        }
        setTabState();
        transaction.commit();
    }

    //设置选中和未选择的状态
    private void setTabState() {
        setHomeState();
        setMessageState();
        setFindState();
        setMyState();
    }

    private void setMyState() {
        if (rb_My.isChecked()){
            rb_My.setTextColor(ContextCompat.getColor(this,R.color.colorRadioButtonP));
        }else{
            rb_My.setTextColor(ContextCompat.getColor(this,R.color.colorRadioButtonN));
        }
    }

    private void setFindState() {
        if (rb_Find.isChecked()){
            rb_Find.setTextColor(ContextCompat.getColor(this,R.color.colorRadioButtonP));
        }else{
            rb_Find.setTextColor(ContextCompat.getColor(this,R.color.colorRadioButtonN));
        }
    }

    private void setMessageState() {
        if (rb_Message.isChecked()){
            rb_Message.setTextColor(ContextCompat.getColor(this,R.color.colorRadioButtonP));
        }else{
            rb_Message.setTextColor(ContextCompat.getColor(this,R.color.colorRadioButtonN));
        }
    }

    private void setHomeState() {
        if (rb_Home.isChecked()){
            rb_Home.setTextColor(ContextCompat.getColor(this,R.color.colorRadioButtonP));
            //Log.e("HomeColor",rb_Home.getCurrentTextColor()+ "");
        }else{
            rb_Home.setTextColor(ContextCompat.getColor(this,R.color.colorRadioButtonN));
        }
    }

    public List<Fragment> getFragments() {
        fragments.add(new HomeFragment());
        fragments.add(new SubmitFragment());
        fragments.add(new NoSubmitFragment());
        fragments.add(new MyFragment());
        return fragments;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MainActivity","onResume被调用");
        /*if (nfcAdapter!=null){
            if (null == HttpUtil.getRealName()){
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                MainActivity.this.finish();
            }else {
                nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
            }
        }*/
        ((HomeFragment) this.fragments.get(0)).NetWork(netWorkChangeReceiver);
        if (rb_Home.isChecked() && null != nfcintent){
            ((HomeFragment) this.fragments.get(0)).onNewInent(nfcintent);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nfcintent = intent;

        /*ColorStateList textColors = rb_Home.getTextColors()
        if (rb_Home.getCurrentTextColor()== Color.RED){

        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netWorkChangeReceiver);
    }
}
