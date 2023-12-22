package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectStaffNotice;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @ClassName:  ProjectStaffNoticeVo   
 * @author: 林港 <ling@aurine.cn>
 * @date:   2020年7月28日 下午3:19:52      
 * @Copyright:
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ProjectStaffNoticeVo extends ProjectStaffNotice {
    private static final long serialVersionUID = 1L;
    private String status;
    private String userId;
    private List<String> noticeIds;
}
