package com.view.daohang.nfc;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Bean.MyBean;

public class AboutActivity extends AppCompatActivity {

    private ImageView back;
    private ListView guanyu_listView;
    private List<MyBean> list;
    private MyBean myBean;
    private int[] pictures = new int[]{R.drawable.setting_icon_arrow_normal};
    private String[] titles = new String[]{"联系我们"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_about);

        initView();
        setClick();
    }

    private void setClick() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutActivity.this,MainActivity.class));
                AboutActivity.this.finish();
            }
        });

        guanyu_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
                        builder.setTitle("技术支持电话");
                        builder.setMessage("tel:025-83433087");
                        builder.setPositiveButton("确定",null);
                        builder.setNegativeButton("取消",null);
                        builder.show();
                        break;
                    default:
                        break;
                }

            }
        });
    }

    private void initView() {

        list = new ArrayList<>();
        for (int i = 0;i<titles.length;i++){
            myBean = new MyBean(pictures[i],titles[i]);
            list.add(myBean);
        }
        back = (ImageView) findViewById(R.id.back);
        guanyu_listView = (ListView) findViewById(R.id.guanyu_listView);
        guanyu_listView.setAdapter(new MyAdapter());
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyBean myBean = list.get(position);
            View view = View.inflate(AboutActivity.this,R.layout.item_my,null);
            TextView textView = (TextView) view.findViewById(R.id.My_textView);
            textView.setText(myBean.getTitles());
            ImageView imageView = (ImageView) view.findViewById(R.id.My_image);
            imageView.setImageResource(myBean.getPictures());
            return view;
        }
    }
}
