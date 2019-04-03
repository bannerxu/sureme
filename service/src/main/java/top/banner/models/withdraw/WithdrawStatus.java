package top.banner.models.withdraw;

/**
 * @author XGL
 * 提现状态
 */
public enum WithdrawStatus {
    /**
     * 待审核
     */
    WAIT
    ,
    /**
     * 审核通过
     */
    SUCCESS,
    /**
     * 失败（拒绝）
     */
    FAILURE,
}
