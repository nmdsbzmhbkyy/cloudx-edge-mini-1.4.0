package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.SysCompany;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Title: SysCompanyFormVo
 * Description: 集团管理 用于新增删除操作
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/7 14:21
 */
@Data
public class SysCompanyFormVo extends SysCompany {
    /**
     * 平台id
     */

    @ApiModelProperty(value = "平台id")
    private Integer parentId;
    /**
     * 新增用户
     */
    private SysUserVo user;
    /**
     * 更新用户
     */
    private List<SysUserVo> userList;
}
