package Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.view.daohang.nfc.R;

import java.util.List;

import Bean.SubmitScanResult;

/**
 * Created by Administrator on 2018/1/16 0016.
 */

public class SubmitAdapter extends BaseAdapter{

    private int resourceId;
    private List<SubmitScanResult> listResult;
    private Context context;

    public SubmitAdapter(int resourceId, List<SubmitScanResult> listResult, Context context) {
        this.resourceId = resourceId;
        this.listResult = listResult;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listResult.size();
    }

    @Override
    public Object getItem(int position) {
        return listResult.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (listResult!= null||listResult.size()!=0){
            SubmitScanResult submitScanResult = listResult.get(position);
            View view = View.inflate(context,resourceId,null);
            TextView point_textView = (TextView) view.findViewById(R.id.point_textView);
            point_textView.setText(submitScanResult.getScanResult());
            TextView user_textView = (TextView) view.findViewById(R.id.user_textView);
            user_textView.setText(submitScanResult.getUsername());
            TextView time_textView = (TextView) view.findViewById(R.id.time_textView);
            time_textView.setText(submitScanResult.getTime());
            return view;
        }
        return null;
    }
}
