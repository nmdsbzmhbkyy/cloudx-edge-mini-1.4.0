package com.aurine.cloudx.open.origin.constant;

/**
 * <p>边缘网关级联状态常量</p>
 *
 * @author : 王良俊
 * @date : 2021-12-20 09:26:09
 */
public interface CascadeStatusConstants {

    /**
     * 未级联
     */
    char NOT_CASCADE = '0';

    /**
     * 待审核
     */
    char PENDING_AUDIT = '1';

    /**
     * 已拒绝
     */
    char REJECT = '2';

    /**
     * 已级联
     */
    char CASCADE = '3';
}
