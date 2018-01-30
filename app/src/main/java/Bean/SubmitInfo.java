package Bean;

/**
 * Created by Administrator on 2018/1/12 0012.
 */

public class SubmitInfo {

    private String reqNo;
    private String versionNo;

    private DataBean data;

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean{
        private String qrNo;
        private String patrolName;
        private String remark;
        private String date;

        public DataBean(String qrNo, String patrolName, String remark, String date) {
            this.qrNo = qrNo;
            this.patrolName = patrolName;
            this.remark = remark;
            this.date = date;
        }

        public String getQrNo() {
            return qrNo;
        }

        public void setQrNo(String qrNo) {
            this.qrNo = qrNo;
        }

        public String getPatrolName() {
            return patrolName;
        }

        public void setPatrolName(String patrolName) {
            this.patrolName = patrolName;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

}
