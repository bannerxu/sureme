package top.banner.service.qiniu;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class QiNiuMany {

    @ApiModelProperty("多个文件地址")
    private List<String> fileURLList;

    public void setFileURLList(List<String> fileURLList) {
        this.fileURLList = fileURLList;
    }

    public List<String> getFileURLList() {
        return fileURLList;
    }

}
