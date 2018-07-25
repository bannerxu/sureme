package top.xuguoliang.common.constants;

/**
 * Created by 袁雾头 on 2017/6/16.
 */
public class ModelContants {

    public interface Flag {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;
    }


    public interface WithdrawContant {
        //1-待审核 2-通过审核 3-拒绝审核 4-提现成功
        Integer WITHDRAW_STATUS_WAIT = 1;  //待审核
        Integer WITHDRAW_STATUS_PASSING = 2; //审核通过
        Integer WITHDRAW_STATUS_REFUSE = 3; //拒接审核
        Integer WITHDRAW_STATUS_SUCCESS = 4; //提现成功

    }

    public interface ReportContant{
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;
    }

    public interface TaskRecordContant {
        Integer FLAG_IS_UNDER_REVIEW = 1;  //审核中
        Integer FLAG_IS_REVIEW_PASSED = 2; //审核通过
        Integer FLAG_IS_REVIEW_FAILURE = 3; //失败

    }


    public interface PaymentContant {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;
    }

    public interface MyCarCodeContant {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;
    }

    public interface ParkingSpacesContant {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;


        Integer PARKING_IS_USING_LOCKING = 2;
        Integer PARKING_IS_USING_TRUE = 1;
        Integer PARKING_IS_USING_FALSE = 0;

        Integer DAY_IN_WEEK_TYPE_1_7 = 1;
        Integer DAY_IN_WEEK_TYPE_1_5 = 2;
        Integer DAY_IN_WEEK_TYPE_6_7 = 3;

        Integer PARKING_STATUS_LOCKING = 1; //锁定中
        Integer PARKING_STATUS_NO_LOCKING = 0; //锁定中

        Integer MASTER_USE_TRUE = 1;    //车位主使用
        Integer MASTER_USE_FALSE = 0;    //车位主使用

    }


    public interface ParkingMasterContant {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;
    }

    public interface CarParkContant {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;
    }

    public interface ManagerContant {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;
        String SUPER_MANAGER = "0";
        String SECONDARY_MANAGER = "1";
    }


    public interface StrategyContant {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;

        Integer STRATEGY_TYPE_FOOD = 1;
        Integer STRATEGY_TYPE_RIM = 2;
        Integer STRATEGY_TYPE_PLAY = 3;


    }

    public interface DiscussContant {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;

        Integer DISCUSS_TYPE_CIRCLEINFO = 1;
        Integer DISCUSS_TYPE_STRATEGY = 2;

    }

    public interface LiveTaskContant {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;

        Integer LINK_IS_TRUE = 1;
        Integer LINK_IS_FALSE = 0;
    }

    public interface LikeContant {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;

        Integer LIKE_TYPE_CIRCLEINFO = 1; //点赞类型，朋友圈消息
        Integer LIKE_TYPE_STRATEGY = 2;  //攻略
        Integer LIKE_TYPE_DISCUSS = 3;  //评论或回复

        Integer LIKE_IS_FALSE = 0;
        Integer LIKE_IS_TRUE = 1;
    }

    public interface MyCouponContant {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;
    }

    public interface FollowContant {
        Integer FOLLOW_TYPE_USER = 1;//主播
        Integer FOLLOW_TYPE_MERCHANT = 2;//商家

        Integer FOLLOW_IS_TRUE = 1;
        Integer FOLLOW_IS_FALSE = 0;

        Integer FOLLOW_IS_ALREADY = 1;
        Integer FOLLOW_IS_NOT_ALREADY = 0;

    }

    public interface CircleInfoContant {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;
    }

    public interface InfoContant {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;

        Integer INFO_ISREAD_TRUE = 1;
        Integer INFO_ISREAD_FALSE = 0;

        Integer INFO_TYPE_SYSTEM = 1; //系统消息
    }

    public interface AdminContant {
        Integer ADMIN_IS_HOT_TRUE = 1; //热门商家
        Integer ADMIN_IS_HOT_FLASE = 0;

        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;

        Integer ADMIN_TYPE_IS_SCENICAREA = 1; //是否是景区
        Integer ADMIN_TYPE_IS_FOOD = 2;  //是否是美食
        Integer ADMIN_TYPE_IS_PLAY = 3;  //是否是玩乐

        Integer TRUE = 1;
    }

    public interface TradeRecordContant {
        Integer REVENUE_IS_TRUE = 1; //收入
        Integer REVENUE_IS_FALSE = 0; //支出

        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;

    }

    public interface PackageContant {
        Integer PACKAGE_TYPE_IS_GIFT = 1; //礼物套餐
    }

    public interface UserContant {
        Integer FLAG_IS_TRUE = 1;  //有效
        Integer FLAG_IS_FALSE = 0;  //无效
        Integer FLAG_IS_DISABLED = 3; //禁用
        Integer VERIFIED_IS_TRUE = 1;
        Integer USER_IS_NEWPERSON_TRUE = 1;
        Integer USER_IS_NEWPERSON_FALSE = 0;
        Integer USER_IS_PAY_PARKING_DEPOSIT_TRUE = 1;
        Integer USER_IS_PAY_PARKING_DEPOSIT_FALSE = 0;
        Integer USER_IS_PARKINGMASTER_TRUE = 1;
        Integer USER_IS_PARKINGMASTER_FALSE = 0;
    }

    public interface GiftOrderContant {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;
    }

    public interface CouponContant {
        Integer DEFAULT_SHOW_IS_TRUE = 1; //默认显示
        Integer DEFAULT_SHOW_IS_FALSE = 0; //默认显示

        Integer ISSHOW_IS_TRUE = 1;
        Integer ISSHOW_IS_FALSE = 0;

        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;

        Integer COUPON_RECEIVE_IS_TRUE = 1;
        Integer COUPON_RECEIVE_IS_FALSE = 0;

    }

    public interface LiveBetweenContant {
        Integer FLAG_IS_TRUE = 1;  //直播中
        Integer FLAG_IS_FALSE = 0; //直播结束
        Integer FLAG_IS_OTHER = 2;   //回放

        Integer HOT_IS_TRUE = 1;
        Integer HOT_IS_FALSE = 0;
    }

    public interface BannerFlagContant {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;
    }

    public interface BannerTypeContant {
        Integer BANNER_IS_INDEX = 1;
        Integer BANNER_IS_STRATEGY = 2;
    }

    public interface MessageContant {
        Integer MESSAGE_TYPE_SYSTEM = 1;                    // 系统消息
        Integer MESSAGE_TYPE_ROOMINFO = 2;              //房间信息
        Integer MESSAGE_TYPE_ONLINEUSER_LIST = 3;   //在线人列表
        Integer MESSAGE_TYPE_GENERAL = 4;   //普通消息
        Integer MESSAGE_TYPE_GIFT = 5;   //礼物
        Integer MESSAGE_TYPE_ONCLOSE = 6;   //关闭直播
        Integer MESSAGE_TYPE_BALANCE = 7;   //余额
        Integer MESSAGE_TYPE_FREEGIFTNUMBER = 8;
    }


    public interface ParkingOrderContant {
        Integer FLAG_IS_TRUE = 1;
        Integer FLAG_IS_FALSE = 0;

        //
        Integer STATUS_LOCKED = 1;          //1-锁定中
        Integer STATUS_USING = 2;           //2-使用中
        Integer STATUS_CANCEL = 3;          //3-已取消
        Integer STATUS_FINISHED = 4;        //4-完成
        Integer STATUS_OVERTIME = 5;        //5-超时
        Integer STATUS_IS_PAY = 6;          //6-支付
    }

}
