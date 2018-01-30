package Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.view.daohang.nfc.R;

import org.litepal.crud.DataSupport;

import java.util.List;

import Adapter.SubmitAdapter;
import Bean.SubmitScanResult;
import Utils.HttpUtil;
import Utils.LocalInfor;
import view.ChangeDatePopwindow;

/**
 * Created by Administrator on 2018/1/5 0005.
 */

public class SubmitFragment extends Fragment{

    private int resultCount;
    private View view,subview,subview2;
    private ImageView imageView,fail_image;
    private TextView select_data,fail_textView,xj_point,xj_remark,delete_result;
    private LinearLayout main;
    private String date;
    private ListView submit_listView;
    private LinearLayout xj_beiwang;
    private LinearLayout sublist;
    private List<SubmitScanResult> resultList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.submitfrag_layout, container, false);
        initView();
        setClick();
        Log.e("SubmitFragment","onCreateView方法被调用");
        showList();
        return view;
    }

    private void initView() {
        imageView = (ImageView) view.findViewById(R.id.back_image);
        fail_image = (ImageView) view.findViewById(R.id.fail_image);
        fail_textView = (TextView) view.findViewById(R.id.fail_textView);
        select_data = (TextView) view.findViewById(R.id.select_data);
        xj_remark = (TextView) view.findViewById(R.id.xj_remark);
        xj_point = (TextView) view.findViewById(R.id.xj_point);
        delete_result = (TextView) view.findViewById(R.id.delete_result);
        select_data.setText(LocalInfor.getCurrentTime("yyyy年-M月-d日"));
        main = (LinearLayout) view.findViewById(R.id.main);
        submit_listView = (ListView) view.findViewById(R.id.submit_listView);
        xj_beiwang = (LinearLayout) view.findViewById(R.id.xj_beiwang);
        sublist = (LinearLayout) view.findViewById(R.id.sublist);
        subview = view.findViewById(R.id.subview);
        subview2 = view.findViewById(R.id.subview2);
    }

    private void setClick() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.mFragment,new HomeFragment());
                fragmentTransaction.commit();
            }
        });
        select_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] str = new String[10];
                final ChangeDatePopwindow mChangeBirthDialog = new ChangeDatePopwindow(getActivity());
                //mChangeBirthDialog.setDate("2017", "6", "20");
                mChangeBirthDialog.showAtLocation(main, Gravity.BOTTOM, 0, 0);
                mChangeBirthDialog.setBirthdayListener(new ChangeDatePopwindow.OnBirthListener() {

                    @Override
                    public void onClick(String year, String month, String day) {
                        // TODO Auto-generated method stub
                        StringBuilder sb = new StringBuilder();
                        sb.append(year.substring(0, year.length() - 1)).append("-").append(month.substring(0, day.length() - 1)).append("-").append(day);
                        str[0] = year + "-" + month + "-" + day;
                        str[1] = sb.toString();
                        Log.e("year",year);
                        select_data.setText(str[0]);
                        showList();
                    }
                });
            }
        });
        delete_result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (resultCount != 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("警告");
                        builder.setMessage("确定要删除以下的扫描记录吗？删除后将无法恢复。");
                        builder.setNegativeButton("取消",null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataSupport.deleteAll(SubmitScanResult.class,"acount = ?",HttpUtil.getUserName());
                                fail_image.setVisibility(View.VISIBLE);
                                fail_textView.setVisibility(View.VISIBLE);
                                xj_beiwang.setVisibility(View.GONE);
                                sublist.setVisibility(View.GONE);
                                Toast.makeText(getContext(),"数据已删除",Toast.LENGTH_SHORT).show();
                                showList();
                            }
                        });
                        builder.show();
                    }else {
                        Toast.makeText(getContext(),"本地没有已提交记录",Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    public void showList(){

        resultList = DataSupport.where("date = ? and acount = ?",select_data.getText().toString(), HttpUtil.getUserName()).find(SubmitScanResult.class);
        resultCount = resultList.size();
        Log.e("SubmitFragment",resultList.size()+ "");
        if (resultList == null || resultList.size()==0){
            fail_image.setVisibility(View.VISIBLE);
            fail_textView.setVisibility(View.VISIBLE);
            xj_beiwang.setVisibility(View.GONE);
            sublist.setVisibility(View.GONE);
            subview.setVisibility(View.GONE);
            subview2.setVisibility(View.GONE);
        }else {
            Log.e("SubmitFragment",resultList.size()+ "");
            fail_image.setVisibility(View.GONE);
            fail_textView.setVisibility(View.GONE);
            xj_beiwang.setVisibility(View.VISIBLE);
            sublist.setVisibility(View.VISIBLE);
            subview.setVisibility(View.VISIBLE);
            subview2.setVisibility(View.VISIBLE);
            //submitAdapter = new SubmitAdapter(R.layout.item_submit,resultList,getContext());
            //submit_listView.setAdapter(submitAdapter);
            submit_listView.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return resultList.size();
                }

                @Override
                public Object getItem(int position) {
                    return resultList.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    SubmitScanResult submitScanResult = resultList.get(position);
                    for (int i=0;i<resultList.size();i++){
                        submitScanResult.setPictures(R.drawable.setting_icon_arrow_normal);
                    }
                    View view = View.inflate(getContext(),R.layout.item_submit,null);
                    TextView point_textView = (TextView) view.findViewById(R.id.point_textView);
                    point_textView.setText(submitScanResult.getScanResult());
                    TextView user_textView = (TextView) view.findViewById(R.id.user_textView);
                    user_textView.setText(submitScanResult.getUsername());
                    TextView time_textView = (TextView) view.findViewById(R.id.time_textView);
                    time_textView.setText(submitScanResult.getTime());
                    ImageView sub_image = (ImageView) view.findViewById(R.id.sub_image);
                    sub_image.setImageResource(submitScanResult.getPictures());
                    if ("正常".equals(submitScanResult.getRemark())){
                        point_textView.setTextColor(Color.BLACK);
                        user_textView.setTextColor(Color.BLACK);
                        time_textView.setTextColor(Color.BLACK);
                    }else if (!"正常".equals(submitScanResult.getRemark())){
                        Log.e("SubmitFragment",position+"不正常");
                        point_textView.setTextColor(Color.RED);
                        user_textView.setTextColor(Color.RED);
                        time_textView.setTextColor(Color.RED);
                    }
                    return view;
                }
            });
            submit_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SubmitScanResult submitScanResult = resultList.get(position);
                    xj_point.setText("位置:" + submitScanResult.getScanResult());
                    xj_remark.setText("备注:" + submitScanResult.getRemark());
                }
            });
        }
    }
}
