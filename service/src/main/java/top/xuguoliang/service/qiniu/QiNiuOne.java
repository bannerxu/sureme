package top.xuguoliang.service.qiniu;

import io.swagger.annotations.ApiModelProperty;

public class QiNiuOne {

    @ApiModelProperty("单个文件地址")
    private String fileURL;

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

}
