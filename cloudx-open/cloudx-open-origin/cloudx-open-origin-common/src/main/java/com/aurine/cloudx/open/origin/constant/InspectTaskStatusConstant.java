package com.aurine.cloudx.open.origin.constant;


/**
 * @ClassName: InspectTaskStatusConstant
 * @author: 王良俊 <>
 * @date: 2020年07月30日 下午07:15:21
 * @Copyright:
 */
public interface InspectTaskStatusConstant {

    /**
     * 待处理
     */
    String PENDING = "0";

    /**
     * 处理中
     */
    String PROCESSING = "1";

    /**
     * 已完成
     */
    String COMPLETED = "2";

    /**
     * 已取消
     */
    String CANCEL = "3";

    /**
     * 已过期
     */
    String EXPIRED = "4";

}
