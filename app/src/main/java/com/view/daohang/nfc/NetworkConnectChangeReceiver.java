package com.view.daohang.nfc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

import Bean.NoSubmitScanResult;
import Bean.Status;
import Bean.SubmitScanResult;
import Utils.HttpUtil;
import Utils.LocalInfor;

/**
 * Created by Administrator on 2018/1/16 0016.
 */

public class NetworkConnectChangeReceiver extends BroadcastReceiver{

    private HttpUtil httpUtil = new HttpUtil();
    private MediaPlayer mediaPlayer;
    private Status status = new Status();

    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "3G网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }
        return connType;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        /*if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听wifi的打开与关闭，与wifi的连接无关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            Log.e("TAG", "wifiState:" + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
            }
        }
        // 监听wifi的连接状态即是否连上了一个有效无线路由
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                // 获取联网状态的NetWorkInfo对象
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                //获取的State对象则代表着连接成功与否等状态
                NetworkInfo.State state = networkInfo.getState();
                //判断网络是否已经连接
                boolean isConnected = state == NetworkInfo.State.CONNECTED;
                Log.e("TAG", "isConnected:" + isConnected);
                if (isConnected) {
                } else {

                }
            }
        }*/
        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent
                    .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI
                            || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        status.setStatus("");
                        Log.e("TAG", getConnectionType(info.getType()) + "连上");
                        if (null!=HttpUtil.getUserName()){
                            List<NoSubmitScanResult> noSubmitScanResults = DataSupport.where("acunt = ?", HttpUtil.getUserName()).find(NoSubmitScanResult.class);
                            Log.e("TAG", noSubmitScanResults.size() + "条数据");
                            if (noSubmitScanResults!=null && !noSubmitScanResults.isEmpty()){
                                for (NoSubmitScanResult nosubmit : noSubmitScanResults){
                                    //Log.e("TAG", nosubmit.getDate());
                                    String date = nosubmit.getDate();
                                    String[] split = date.split("-");
                                    if (LocalInfor.getCurrentTime("d日").equals(split[2])){
                                        // Log.e("TAG", nosubmit.getDate());
                                        Log.e("TAG", nosubmit.getScanResult());
                                        Log.e("TAG", nosubmit.getAcunt());
                                        Log.e("TAG", nosubmit.getRemark());
                                        Log.e("TAG", nosubmit.getFinaldate());
                                        //提交当天的数据
                                        if (null != nosubmit.getScanResult() && null != nosubmit.getAcunt() && null != nosubmit.getRemark() && null != nosubmit.getFinaldate()){
                                            String scanJson = httpUtil.SubmitScan(nosubmit.getScanResult(), nosubmit.getAcunt(), nosubmit.getRemark(),nosubmit.getFinaldate());
                                            httpUtil.SubmitServer(scanJson);
                                            SubmitScanResult submitResult = new SubmitScanResult();
                                            submitResult.setTime(nosubmit.getTime());
                                            submitResult.setRemark(nosubmit.getRemark());
                                            submitResult.setScanResult(nosubmit.getScanResult());
                                            submitResult.setDate(nosubmit.getDate());
                                            submitResult.setUsername(nosubmit.getUsername());
                                            submitResult.setAcount(nosubmit.getAcunt());
                                            submitResult.save();
                                            DataSupport.deleteAll(NoSubmitScanResult.class,"date = ?",LocalInfor.getCurrentTime("yyyy年-M月-d日"));
                                        }else {
                                            Toast.makeText(context,"数据有异常",Toast.LENGTH_SHORT).show();
                                        }
                                        Log.e("TAG", "添加成功");
                                        Log.e("TAG", "删除成功");
                                    }else {
                                        //删除不是当天的数据
                                        DataSupport.deleteAll(NoSubmitScanResult.class);
                                    }
                                }
                                //MediaPlayer.create(context,R.raw.scan).start();
                                Toast.makeText(context,"遗留数据已提交",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    Log.e("TAG", getConnectionType(info.getType()) + "断开");
                    status.setStatus("当前网络不可用");
                }
            }
        }
    }

    public Status getSta(){
        return status;
    }
}
