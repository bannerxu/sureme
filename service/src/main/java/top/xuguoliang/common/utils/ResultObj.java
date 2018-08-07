package top.xuguoliang.common.utils;

/**
 * 现金红包发送结果
 */
public class ResultObj {
    /*微信返回的发送结果
       {total_amount=100, 
    result_code=SUCCESS, 
    mch_id=1388107602, 
    mch_billno=138810760220160908152254, 
    err_code=SUCCESS, 
    send_listid=10000417012016090830053806097, 
    wxappid=wx45310e1220339304, 
    err_code_des=发放成功, 
    return_msg=发放成功, 
    re_openid=oiCHPv2Et1ujnas2UgknXeK5Bxhc, 
    return_code=SUCCESS}*/

    public int total_amount;
    public String result_code;
    public String mch_id;
    public String mch_billno;
    public String err_code;
    public String send_listid;
    public String wxappid;
    public String err_code_des;
    public String return_msg;
    public String re_openid;
    public String return_code;

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getMch_billno() {
        return mch_billno;
    }

    public void setMch_billno(String mch_billno) {
        this.mch_billno = mch_billno;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getSend_listid() {
        return send_listid;
    }

    public void setSend_listid(String send_listid) {
        this.send_listid = send_listid;
    }

    public String getWxappid() {
        return wxappid;
    }

    public void setWxappid(String wxappid) {
        this.wxappid = wxappid;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getRe_openid() {
        return re_openid;
    }

    public void setRe_openid(String re_openid) {
        this.re_openid = re_openid;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }
}