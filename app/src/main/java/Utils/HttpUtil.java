package Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import Bean.DescPoint;
import Bean.LoginUser;
import Bean.SubmitInfo;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/11 0011.
 */

public class HttpUtil {

    private Handler handler;
    static private String token;
    private static String realName;
    private static String userName;
    private String code;
    private static String ip;
    private static String descpoints;
    private String numbers;
    private Gson gson = new Gson();
    public HttpUtil(){}
    public HttpUtil(Handler handler) {
        this.handler = handler;
    }
    OkHttpClient okHttpClient = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    //请求服务器的uri地址 http://192.168.2.20:8888/ScanServer/ScanServlet  http://39.106.7.51:8888/app/user/login  http://192.168.2.31:8888/app/user/login
    //http://192.168.2.20:8888/ScanServer/ScanResultServer  http://39.106.7.51:8888/app/patrol/add   http://192.168.2.31:8888/app/patrol/add
    private static String LoginUrl = "http://39.106.7.51:8888/app/user/login";
    private static String AddUrl = "http://39.106.7.51:8888/app/patrol/add";

    public void login(String json,String str_mac){
        new LoginThread(json,str_mac).start();
    }
    class LoginThread extends Thread{

        private String json;
        private String str_mac;

        public LoginThread(String json,String str_mac){
            this.json = json;
            this.str_mac = str_mac;
        }

        @Override
        public void run() {
            super.run();
            RequestBody body = RequestBody.create(JSON,json);
            Request request = new Request.Builder()
                    .url(LoginUrl)
                    .addHeader("token", str_mac)
                    .post(body)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                String result = response.body().string();
                JSONObject oj = new JSONObject(result);
                String statusCode = oj.getString("statusCode");
                Log.e("HttpUtil",oj.toString()+ ",oj");
                if ("200".equals(statusCode)){
                    SendMessage(1, 0, 0, "登录成功");
                    JSONObject obj = oj.getJSONObject("data");
                    Log.e("HttpUtil",obj.toString());
                    token = obj.getString("token");
                    realName =  obj.getString("realName");
                    userName = obj.getString("userName");
                    Log.e("HttpUtil",realName+ " ," + userName);
                    return;
                }else if ("201".equals(statusCode)){
                    SendMessage(2, 0, 0, "账号或密码错误");
                    return;
                }
            }  catch (JSONException e) {
                e.printStackTrace();
            }catch (SocketTimeoutException e){
                //请求连接超时异常捕获
                SendMessage(3, 0, 0, "网络不稳定,请稍后重试");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void SubmitServer(String json){
        new SubThread(json).start();
    }

    class SubThread extends Thread{

        private String json;

        public SubThread(String json){
            this.json = json;
        }

        @Override
        public void run() {
            super.run();
            RequestBody body = RequestBody.create(JSON,json);
            Log.e("HttpUtil",json);
            Request request = new Request.Builder()
                    .url(AddUrl)
                    .addHeader("token",token)
                    .post(body)
                    .build();
            Log.e("HttpUtil",token);
            try {
                Response response = okHttpClient.newCall(request).execute();
                String result = response.body().string();
                JSONObject object = new JSONObject(result);
                Log.e("HttpUtil",result);
                String statusCode = object.getString("statusCode");
                if ("200".equals(statusCode)){
                    SendMessage(200, 0, 0, "提交成功");
                    String descpoint = object.getString("data");
                    descpoints = descpoint;
                    return;
                }else if ("202".equals(statusCode)){
                    SendMessage(202, 0, 0, "请在规定的时间段内巡检");
                    return;
                }else if ("203".equals(statusCode)){
                    SendMessage(203, 0, 0, "不是有效的巡更卡");
                    return;
                }else {
                    SendMessage(2, 0, 0, "提交的数据有异常");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
            SendMessage(400, 0, 0, "数据已保存");
        }
    }

    public String blowJson(String name,String password){
        LoginUser loginUser = new LoginUser();
        loginUser.setReqNo("101");
        loginUser.setVersionNo("1.0.0");
        LoginUser.DataBean dataBean = loginUser.new DataBean(name,password);
        loginUser.setData(dataBean);
        String toJson = gson.toJson(loginUser, LoginUser.class);
        return toJson;
    }

    public String SubmitScan(String number,String username,String remark,String date){
        numbers = number;
        SubmitInfo submitInfo = new SubmitInfo();
        submitInfo.setReqNo("101");
        submitInfo.setVersionNo("1.0.0");
        SubmitInfo.DataBean dataBean = submitInfo.new DataBean(number,username,remark,date);
        submitInfo.setData(dataBean);
        String toJson = gson.toJson(submitInfo, SubmitInfo.class);
        return toJson;
    }

    void SendMessage(int what, int arg1, int arg2, Object obj) {
        if (handler != null) {
            Message.obtain(handler, what, arg1, arg2, obj).sendToTarget();
        }
    }

    public static String getRealName(){
        return realName;
    }
    public static String getUserName(){
        return userName;
    }
    public String getpoint(){
        return descpoints;
    }
}
