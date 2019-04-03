package top.banner.service.logistics;

import java.util.List;

/**
 * @author jinguoguo
 */
public class LogisticsCompanyResult {
    /**
     * resultcode : 200
     * reason : 查询支持的快递公司成功
     * result : [{"com":"顺丰","no":"sf"},{"com":"申通","no":"sto"},{"com":"圆通","no":"yt"},{"com":"韵达","no":"yd"},{"com":"天天","no":"tt"},{"com":"EMS","no":"ems"},{"com":"中通","no":"zto"},{"com":"汇通","no":"ht"},{"com":"全峰","no":"qf"},{"com":"德邦","no":"db"},{"com":"国通","no":"gt"},{"com":"如风达","no":"rfd"},{"com":"京东快递","no":"jd"},{"com":"宅急送","no":"zjs"},{"com":"EMS国际","no":"emsg"},{"com":"Fedex国际","no":"fedex"},{"com":"邮政国内（挂号信）","no":"yzgn"},{"com":"UPS国际快递","no":"ups"},{"com":"中铁快运","no":"ztky"},{"com":"佳吉快运","no":"jiaji"},{"com":"速尔快递","no":"suer"},{"com":"信丰物流","no":"xfwl"},{"com":"优速快递","no":"yousu"},{"com":"中邮物流","no":"zhongyou"},{"com":"天地华宇","no":"tdhy"},{"com":"安信达快递","no":"axd"},{"com":"快捷速递","no":"kuaijie"},{"com":"AAE全球专递","no":"aae"},{"com":"DHL","no":"dhl"},{"com":"DPEX国际快递","no":"dpex"},{"com":"D速物流","no":"ds"},{"com":"FEDEX国内快递","no":"fedexcn"},{"com":"OCS","no":"ocs"},{"com":"TNT","no":"tnt"},{"com":"东方快递","no":"coe"},{"com":"传喜物流","no":"cxwl"},{"com":"城市100","no":"cs"},{"com":"城市之星物流","no":"cszx"},{"com":"安捷快递","no":"aj"},{"com":"百福东方","no":"bfdf"},{"com":"程光快递","no":"chengguang"},{"com":"递四方快递","no":"dsf"},{"com":"长通物流","no":"ctwl"},{"com":"飞豹快递","no":"feibao"},{"com":"马来西亚（大包EMS）","no":"malaysiaems"},{"com":"安能快递","no":"ane66"}]
     * error_code : 0
     */

    private String resultcode;
    private String reason;
    private int error_code;
    private List<LogisticsCompany> result;

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

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<LogisticsCompany> getResult() {
        return result;
    }

    public void setResult(List<LogisticsCompany> result) {
        this.result = result;
    }

    public static class LogisticsCompany {
        /**
         * com : 顺丰
         * no : sf
         */

        private String com;
        private String no;

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
    }
}
