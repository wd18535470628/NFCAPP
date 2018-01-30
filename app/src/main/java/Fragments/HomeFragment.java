package Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.view.daohang.nfc.NetworkConnectChangeReceiver;
import com.view.daohang.nfc.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import Bean.DescPoint;
import Bean.NoSubmitScanResult;
import Bean.SubmitScanResult;
import Utils.ACache;
import Utils.HttpUtil;
import Utils.LocalInfor;
import Utils.StringValue;

/**
 * Created by Administrator on 2018/1/5 0005.
 */

public class HomeFragment extends Fragment{

    private View view;
    private TextView user_name;
    private TextView net_status;
    private EditText et_remark;
    private String realName = HttpUtil.getRealName();
    private ACache aCache;
    private TextView scan_info;//显示扫描信息
    private TextView scan_user_name;//扫描到的用户名
    private TextView scan_point;//扫描到的巡检位置
    private TextView scan_remak;//备注情况(异常或正常)
    private TextView scan_dates;//扫描日期
    private TextView scan_count;//记录扫描的次数
    private TextView no_submit;//记录未提交的次数
    private int count = 0;//显示总的扫描次数
    private int no_count = 0;//显示未提交次数
    private MediaPlayer mediaPlayer;
    private String disPlayresut;
    private String date;
    private String descPoint;
    private NetworkConnectChangeReceiver networkConnectChangeReceiver;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    if (null!=getContext()){
                        Toast.makeText(getContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                        //mediaPlayer.start();
                        ++count;
                        no_count = 0;
                        scan_info.setText("扫描信息:");
                        scan_info.setTextColor(getResources().getColor(R.color.colorAccent));
                        scan_user_name.setText(user_name.getText().toString().trim());
                        scan_count.setText(count + "次");
                        if (null != new HttpUtil().getpoint()){
                            scan_point.setText(new HttpUtil().getpoint());
                        }else {
                            scan_point.setText(descPoint);
                        }
                        scan_dates.setText(LocalInfor.getCurrentTime("HH:mm:ss"));
                        scan_remak.setText(et_remark.getText().toString().trim());
                        no_submit.setText(no_count + "次");
                        disPlayresut = new HttpUtil().getpoint();
                        if (null != disPlayresut && !disPlayresut.equals(aCache.getAsString(descPoint))){
                            aCache.put(descPoint,disPlayresut);
                        }
                        String date = LocalInfor.getCurrentTime("yyyy年-M月-d日");
                        SubmitScanResult submitResult = new SubmitScanResult();
                        submitResult.setTime(scan_dates.getText().toString());
                        submitResult.setRemark(scan_remak.getText().toString());
                        submitResult.setScanResult(scan_point.getText().toString());
                        submitResult.setDate(date);
                        submitResult.setUsername(user_name.getText().toString());
                        submitResult.setAcount(HttpUtil.getUserName());
                        submitResult.save();

                    }
                    /*List<SubmitScanResult> resultList = DataSupport.findAll(SubmitScanResult.class);
                    Log.e("SubmitFragment",resultList.size()+ "");*/
                    break;
                case 2:
                    if (null != getContext()){
                        Toast.makeText(getContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 202:
                    if (null != getContext()){
                        Toast.makeText(getContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 203:
                    if (null != getContext()){
                        Toast.makeText(getContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 400:
                    if (null!=getContext()){
                        Toast.makeText(getContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                        //mediaPlayer.start();
                        ++count;

                        scan_info.setText("扫描信息:");
                        scan_info.setTextColor(getResources().getColor(R.color.colorAccent));
                        scan_user_name.setText(user_name.getText().toString().trim());
                        scan_count.setText(count + "次");
                        if (null != aCache.getAsString(descPoint)){
                            scan_point.setText(aCache.getAsString(descPoint));
                        }else {
                            scan_point.setText(descPoint);
                        }
                        scan_dates.setText(LocalInfor.getCurrentTime("HH:mm:ss"));
                        scan_remak.setText(et_remark.getText().toString().trim());

                        String date2 = LocalInfor.getCurrentTime("yyyy年-M月-d日");
                        NoSubmitScanResult nosubmitResult = new NoSubmitScanResult();
                        nosubmitResult.setTime(scan_dates.getText().toString());
                        nosubmitResult.setRemark(scan_remak.getText().toString());
                        nosubmitResult.setScanResult(scan_point.getText().toString());
                        nosubmitResult.setDate(date2);
                        nosubmitResult.setUsername(user_name.getText().toString());
                        nosubmitResult.setAcunt(HttpUtil.getUserName());
                        nosubmitResult.setFinaldate(LocalInfor.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
                        nosubmitResult.save();

                        List<NoSubmitScanResult> noSubmitScanResults = DataSupport.where("date = ? and acunt = ?", LocalInfor.getCurrentTime("yyyy年-M月-d日"), HttpUtil.getUserName()).find(NoSubmitScanResult.class);
                        no_count = noSubmitScanResults.size();
                        no_submit.setText(no_count + "次");
                    }
                    break;
                case 10:
                    net_status.setText(networkConnectChangeReceiver.getSta().getStatus());
                    handler.sendEmptyMessageDelayed(10,1000);
                    break;
                default:
                    break;
            }
        }
    };

    private HttpUtil httpUtil = new HttpUtil(handler);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.homefrag_layout, container, false);
        initView();
        String submitTime = LocalInfor.getCurrentTime("yyyy-MM-dd HH:mm:ss");
        Log.e("HomeFragment",submitTime);
        new Thread(){
            @Override
            public void run() {
                super.run();
                handler.sendEmptyMessage(10);
            }
        }.start();
        return view;
    }

    private void initView() {
        user_name = (TextView) view.findViewById(R.id.user_name);
        if (realName!=null){
            user_name.setText(realName);
        }else {
            user_name.setText("巡检人员");
        }

        net_status = (TextView) view.findViewById(R.id.net_status);

        et_remark = (EditText) view.findViewById(R.id.et_remark);

        scan_info = (TextView)view.findViewById(R.id.scan_info);

        scan_count = (TextView)view.findViewById(R.id.scan_count);

        scan_user_name = (TextView)view.findViewById(R.id.scan_user_name);

        scan_point = (TextView)view.findViewById(R.id.scan_point);

        scan_remak = (TextView)view.findViewById(R.id.scan_remak);

        scan_dates = (TextView)view.findViewById(R.id.scan_dates);

        no_submit = (TextView)view.findViewById(R.id.no_submit);

        aCache = ACache.get(getContext());

        mediaPlayer = MediaPlayer.create(getContext(), R.raw.scan);
    }

    public void onNewInent(Intent intent){
        read(intent);
    }

    public void NetWork(NetworkConnectChangeReceiver networkConnectChangeReceiver){
        this.networkConnectChangeReceiver = networkConnectChangeReceiver;
    }


    private void read(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            return;
        }
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                ) {

            Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (data != null) {
                NdefMessage[] ndefMessages = new NdefMessage[data.length];
                for (int i = 0; i < ndefMessages.length; i++) {
                    ndefMessages[i] = (NdefMessage) data[i];
                    NdefRecord[] ndefRecords = ndefMessages[i].getRecords();
                    String scanResult = new String(ndefRecords[2].getPayload());
                    onSubmitResult(scanResult);
                }
            }else {
                scan_info.setText("此卡没有写入信息");
                scan_info.setTextColor(Color.RED);
                scan_count.setText("");
                scan_user_name.setText("");
                scan_point.setText("");
                scan_remak.setText("");
                scan_dates.setText("");
                no_submit.setText("");
            }
        }
    }

    public void onSubmitResult(String scanResult){
        //对scanResult进行格式检查,是否含有"_"
        int flag = scanResult.indexOf("_");
        if (flag == -1) {
            Toast.makeText(getContext(),"不可处理此卡信息",Toast.LENGTH_SHORT).show();
            return;
        }
        String strNo = scanResult.substring(flag + 1);
        String point = StringValue.getSubmitServer(scanResult)+strNo;
        descPoint = point;
        Log.e("HomeFragment",point);
        String username = user_name.getText().toString();
        String remark = et_remark.getText().toString();
        if (TextUtils.isEmpty(remark)) {
            Toast.makeText(getActivity(), "备注信息不能为空", Toast.LENGTH_SHORT).show();
        }else {
            date = LocalInfor.getCurrentTime("yyyy-MM-dd HH:mm:ss");
            String scanJson = httpUtil.SubmitScan(point, HttpUtil.getUserName(), remark,date);
            httpUtil.SubmitServer(scanJson);
            Log.e("HomeFragment",httpUtil.getpoint() + "-----");

        }
    }
}
