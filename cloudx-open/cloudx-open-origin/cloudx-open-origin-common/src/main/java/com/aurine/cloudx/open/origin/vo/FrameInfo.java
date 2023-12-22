package com.aurine.cloudx.open.origin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>组团信息类</p>
 * @author : 王良俊
 * @date : 2021-09-14 15:42:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrameInfo {

    /**
     * 带父组团信息的组团名使用'/'分隔
     */
    private String frameName;

    /**
     * 组团ID
     */
    private String entityId;

}
