package Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/12 0012.
 */

public class StringValue {

    //可以处理的码值列表
    public final static String[] ScanResultHeaders = {
            "js",
            "mhq",
            "gj",
            "dq"
    };
    //提交到后台的编号前缀xj010001 xj020001
    public final static String[] SubmitServer = {
            "js",
            "mhq",
            "gj",
            "dq"
    };
    //码值检查  如果是待处理的码值，则拦截
    public static boolean scanResultCheck(String scanResult) {
        for (String scanHeader : ScanResultHeaders) {
            int flag = scanResult.indexOf(scanHeader);
            if (flag == 0) {
                //合法的格式
                return true;
            }
        }
        return false;
    }
    public static String getSubmitServer(String scanResult) {
        int size = ScanResultHeaders.length;
        for (int i = 0; i < size; ++i) {
            String scanHeader = ScanResultHeaders[i];
            int flag = scanResult.indexOf(scanHeader);
            if (flag == 0) {
                //合法的格式
                return SubmitServer[i];
            }
        }
        return null;
    }
}
