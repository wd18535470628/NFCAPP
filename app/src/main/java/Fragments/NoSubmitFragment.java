package Fragments;

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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.view.daohang.nfc.R;

import org.litepal.crud.DataSupport;

import java.util.List;

import Bean.NoSubmitScanResult;
import Bean.SubmitScanResult;
import Utils.HttpUtil;
import Utils.LocalInfor;
import view.ChangeDatePopwindow;

/**
 * Created by Administrator on 2018/1/5 0005.
 */

public class NoSubmitFragment extends Fragment{

    private View view,subview,subview2;
    private ImageView imageView,fail_image;
    private TextView nosubmit_data,no_submit_count,fail_textView;
    private LinearLayout nosubmit_main,no_sublist;
    private ListView nosubmit_listView;
    private int nosubmit_count;

    private List<NoSubmitScanResult> resultList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.nosubmitfrag_layout, container, false);
        initView();
        setClick();
        showList();
        if (nosubmit_count!=0){
            no_submit_count.setText(String.valueOf(nosubmit_count) + " 条");
        }else {
            no_submit_count.setText("0 条");
        }
        return view;
    }

    private void initView() {
        imageView = (ImageView) view.findViewById(R.id.back_image);
        fail_image = (ImageView) view.findViewById(R.id.fail_image);
        nosubmit_data = (TextView) view.findViewById(R.id.nosubmit_data);
        nosubmit_data.setText(LocalInfor.getCurrentTime("yyyy年-M月-d日"));
        nosubmit_main = (LinearLayout) view.findViewById(R.id.nosubmit_main);
        nosubmit_listView = (ListView) view.findViewById(R.id.nosubmit_listView);
        no_submit_count = (TextView) view.findViewById(R.id.no_submit_count);
        fail_textView = (TextView) view.findViewById(R.id.fail_textView);
        no_sublist = (LinearLayout) view.findViewById(R.id.no_sublist);
        subview = view.findViewById(R.id.subview);
        subview2 = view.findViewById(R.id.subview2);
    }

    private void setClick() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.mFragment,new SubmitFragment());
                fragmentTransaction.commit();
            }
        });

        nosubmit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] str = new String[10];
                final ChangeDatePopwindow mChangeBirthDialog = new ChangeDatePopwindow(getActivity());
                //mChangeBirthDialog.setDate("2017", "6", "20");
                mChangeBirthDialog.showAtLocation(nosubmit_main, Gravity.BOTTOM, 0, 0);
                mChangeBirthDialog.setBirthdayListener(new ChangeDatePopwindow.OnBirthListener() {

                    @Override
                    public void onClick(String year, String month, String day) {
                        // TODO Auto-generated method stub
                        StringBuilder sb = new StringBuilder();
                        sb.append(year.substring(0, year.length() - 1)).append("-").append(month.substring(0, day.length() - 1)).append("-").append(day);
                        str[0] = year + "-" + month + "-" + day;
                        str[1] = sb.toString();
                        Log.e("year",year);
                        nosubmit_data.setText(str[0]);
                        showList();
                        if (nosubmit_count!=0){
                            no_submit_count.setText(String.valueOf(nosubmit_count) + " 条");
                        }else {
                            no_submit_count.setText("0 条");
                        }
                    }
                });
            }
        });
    }

    private void showList() {

        resultList = DataSupport.where("date = ? and acunt = ?",nosubmit_data.getText().toString(), HttpUtil.getUserName()).find(NoSubmitScanResult.class);
        nosubmit_count = resultList.size();
        String date = nosubmit_data.getText().toString();
        String[] split = date.split("-");
        Log.e("NoSubmitFragment",split[2]+" 1");
        Log.e("NoSubmitFragment",LocalInfor.getCurrentTime("d日")+" 2");
        Log.e("NoSubmitFragment",resultList.size()+"");
        Log.e("NoSubmitFragment",resultList+"");
        if (resultList!=null && !resultList.isEmpty()){
            fail_image.setVisibility(View.GONE);
            fail_textView.setVisibility(View.GONE);
            no_sublist.setVisibility(View.VISIBLE);
            subview.setVisibility(View.VISIBLE);
            subview2.setVisibility(View.VISIBLE);
            nosubmit_listView.setAdapter(new BaseAdapter() {
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
                    NoSubmitScanResult noSubmit = resultList.get(position);
                    View view = View.inflate(getContext(), R.layout.item_submit, null);
                    TextView point_textView = (TextView) view.findViewById(R.id.point_textView);
                    point_textView.setText(noSubmit.getScanResult());
                    TextView user_textView = (TextView) view.findViewById(R.id.user_textView);
                    user_textView.setText(noSubmit.getUsername());
                    TextView time_textView = (TextView) view.findViewById(R.id.time_textView);
                    time_textView.setText(noSubmit.getTime());
                    return view;
                }
            });
        }else {
            fail_image.setVisibility(View.VISIBLE);
            fail_textView.setVisibility(View.VISIBLE);
            no_sublist.setVisibility(View.GONE);
            subview.setVisibility(View.GONE);
            subview2.setVisibility(View.GONE);
        }
    }
}
