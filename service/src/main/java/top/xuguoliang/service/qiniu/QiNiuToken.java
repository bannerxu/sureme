package top.xuguoliang.service.qiniu;

import io.swagger.annotations.ApiModelProperty;

public class QiNiuToken {

    @ApiModelProperty("七牛token")
    private String token;

    public QiNiuToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
