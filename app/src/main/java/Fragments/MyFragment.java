package Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.view.daohang.nfc.AboutActivity;
import com.view.daohang.nfc.LoginActivity;
import com.view.daohang.nfc.R;

import java.util.ArrayList;
import java.util.List;

import Bean.MyBean;
import Utils.HttpUtil;

/**
 * Created by Administrator on 2018/1/5 0005.
 */

public class MyFragment extends Fragment{

    private View view;
    private ImageView imageView;
    private ListView My_listView;
    private List<MyBean> list;
    private MyBean myBean;
    private TextView myfrag_user_name;
    private int[] pictures = new int[]{R.drawable.setting_icon_arrow_normal,R.drawable.setting_icon_arrow_normal,R.drawable.setting_icon_arrow_normal,
            R.drawable.setting_icon_arrow_normal};
    private String[] titles = new String[]{"使用说明","关于我们","切换账号","退出应用"};
    private String realName = HttpUtil.getRealName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.myfrag_layout, container, false);
        initView();
        setClick();
        return view;
    }

    private void initView() {
        list = new ArrayList<>();
        for (int i = 0;i<titles.length;i++){
            myBean = new MyBean(pictures[i],titles[i]);
            list.add(myBean);
        }
        imageView = (ImageView) view.findViewById(R.id.back_image);
        My_listView = (ListView) view.findViewById(R.id.My_listView);
        My_listView.setAdapter(new MyAdapter());
        myfrag_user_name = (TextView) view.findViewById(R.id.myfrag_user_name);

        if (realName!=null){
            myfrag_user_name.setText(realName);
        }else {
            myfrag_user_name.setText("巡检人员");
        }
    }

    private void setClick() {
        My_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("使用说明");
                        builder.setMessage("终端背部靠近巡更点开始提交。如有卡顿现象,请及时删除历史数据");
                        builder.setPositiveButton("确定",null);
                        builder.setNegativeButton("取消",null);
                        builder.show();
                        break;
                    case 1:
                        Intent intent = new Intent(getActivity(), AboutActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                        break;
                    case 3:
                        final View v = View.inflate(getContext(),R.layout.password_layout,null);
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        builder1.setTitle("退出");
                        builder1.setMessage("你想退出此程序吗？");
                        builder1.setView(v);
                        builder1.setNegativeButton("取消", null);
                        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText text = (EditText) v.findViewById(R.id.et_password);
                                String password = text.getText().toString();
                                if (password.equals("uanfun888")){
                                    Process.killProcess(Process.myPid());
                                    System.exit(0);
                                }else {
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                                    builder2.setTitle("提示");
                                    builder2.setMessage("密码不正确,请重新输入");
                                    builder2.setPositiveButton("确定",null);
                                    builder2.show();
                                }
                            }
                        });
                        builder1.show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter{

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
            View view = View.inflate(getContext(),R.layout.item_my,null);
            TextView textView = (TextView) view.findViewById(R.id.My_textView);
            textView.setText(myBean.getTitles());
            ImageView imageView = (ImageView) view.findViewById(R.id.My_image);
            imageView.setImageResource(myBean.getPictures());
            return view;
        }
    }
}
