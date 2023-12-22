package com.aurine.cloudx.estate.thirdparty.module.wr20.entity.tree;

import com.aurine.cloudx.common.core.entity.TreeNode;
import lombok.Data;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-21
 * @Copyright:
 */
@Data
public class FrameObjNode extends TreeNode {
    private String createTime;
    private String frameDesc;
    private String frameNo;
    /**
     * 框架类型
     * 0 为社区框架类型
     * 1 为住户框架类型
     * 99 为区口顶级框架类型
     */
    private Integer frameType;
    private String updateTime;
    /**
     * 框架层级 1-N
     * 1：
     */
    private Integer levelNo;

    /**
     * 云平台框架层级 7-1
     */
    private Integer frameLevel;
}
