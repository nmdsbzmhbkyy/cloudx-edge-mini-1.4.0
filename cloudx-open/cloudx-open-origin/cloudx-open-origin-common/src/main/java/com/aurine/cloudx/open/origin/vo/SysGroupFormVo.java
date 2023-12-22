package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.SysGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Title: SysGroupFormVo
 * Description: 项目组视图 用于新增删除操作
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/7 14:23
 */
@Data
@ApiModel("项目组Vo(用于新增查询操作)")
public class SysGroupFormVo extends SysGroup {
    @ApiModelProperty("人员（用于新增）")
    private SysUserVo user;
    @ApiModelProperty("人员列表（用于查询）")
    private List<SysUserVo> userList;


}
