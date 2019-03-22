package top.banner.common;


public class Result {
    public static final Result SUCCESS = new Result("success");
    private String code;
    private String desc;

    private Result(String code) {
        this.code = code;
    }

    public Result(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
