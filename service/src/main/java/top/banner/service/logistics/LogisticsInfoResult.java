package top.banner.service.logistics;

import java.util.List;

/**
 * @author jinguoguo
 */
public class LogisticsInfoResult {
    /**
     * resultcode : 200
     * reason : 查询成功
     * result : {"company":"圆通","com":"yt","no":"800880930574569338","status":"1","list":[{"datetime":"2018-08-01 18:01:28","remark":"安徽省马鞍山市公司取件人: 阳师（18205557655）已收件","zone":""},{"datetime":"2018-08-01 19:23:44","remark":"快件已发往 芜湖转运中心","zone":""},{"datetime":"2018-08-01 20:49:52","remark":"快件已到达 芜湖转运中心","zone":""},{"datetime":"2018-08-01 21:35:38","remark":"快件已发往 佛山转运中心","zone":""},{"datetime":"2018-08-02 15:47:25","remark":"快件已到达 广州转运中心","zone":""},{"datetime":"2018-08-02 18:07:58","remark":"快件已发往 广东省广州市天河区车陂公司","zone":""},{"datetime":"2018-08-03 02:59:48","remark":"快件已到达 佛山转运中心","zone":""},{"datetime":"2018-08-03 08:55:28","remark":"广东省广州市天河区车陂公司许**（18520275297） 正在派件 ","zone":""},{"datetime":"2018-08-03 11:20:55","remark":"快件已签收 签收人: 美宜家 感谢使用圆通速递，期待再次为您服务","zone":""}]}
     * error_code : 0
     */

    private String resultcode;
    private String reason;
    private LogisticsInfo result;
    private int error_code;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LogisticsInfo getResult() {
        return result;
    }

    public void setResult(LogisticsInfo result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class LogisticsInfo {
        /**
         * company : 圆通
         * com : yt
         * no : 800880930574569338
         * status : 1
         * list : [{"datetime":"2018-08-01 18:01:28","remark":"安徽省马鞍山市公司取件人: 阳师（18205557655）已收件","zone":""},{"datetime":"2018-08-01 19:23:44","remark":"快件已发往 芜湖转运中心","zone":""},{"datetime":"2018-08-01 20:49:52","remark":"快件已到达 芜湖转运中心","zone":""},{"datetime":"2018-08-01 21:35:38","remark":"快件已发往 佛山转运中心","zone":""},{"datetime":"2018-08-02 15:47:25","remark":"快件已到达 广州转运中心","zone":""},{"datetime":"2018-08-02 18:07:58","remark":"快件已发往 广东省广州市天河区车陂公司","zone":""},{"datetime":"2018-08-03 02:59:48","remark":"快件已到达 佛山转运中心","zone":""},{"datetime":"2018-08-03 08:55:28","remark":"广东省广州市天河区车陂公司许**（18520275297） 正在派件 ","zone":""},{"datetime":"2018-08-03 11:20:55","remark":"快件已签收 签收人: 美宜家 感谢使用圆通速递，期待再次为您服务","zone":""}]
         */

        private String company;
        private String com;
        private String no;
        private String status;
        private List<ListBean> list;

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getCom() {
            return com;
        }

        public void setCom(String com) {
            this.com = com;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * datetime : 2018-08-01 18:01:28
             * remark : 安徽省马鞍山市公司取件人: 阳师（18205557655）已收件
             * zone :
             */

            private String datetime;
            private String remark;
            private String zone;

            public String getDatetime() {
                return datetime;
            }

            public void setDatetime(String datetime) {
                this.datetime = datetime;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getZone() {
                return zone;
            }

            public void setZone(String zone) {
                this.zone = zone;
            }
        }
    }
}
