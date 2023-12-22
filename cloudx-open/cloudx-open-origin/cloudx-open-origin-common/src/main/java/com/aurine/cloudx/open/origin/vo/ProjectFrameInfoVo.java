
package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectFrameInfo;
import lombok.Data;

/**
 * 框架信息vo
 *
 * @ClassName: ProjectFrameInfoVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-21 11:23
 * @Copyright:
 */
@Data
public class ProjectFrameInfoVo extends ProjectFrameInfo {

    /**
     * 项目编号
     */
    private Integer projectId;

    /**
     * 租户号
     */
    private Integer tenantId;
}
