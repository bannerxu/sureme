package top.xuguoliang.service.qiniu;

/**
 * Created by jdq on 2017/8/2.
 */
public class QiNiu {

    private String hash;

    private String key;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Qiniu{" +
                "hash='" + hash + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
