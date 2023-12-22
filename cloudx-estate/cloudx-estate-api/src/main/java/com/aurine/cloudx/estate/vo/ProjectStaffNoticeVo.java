package com.aurine.cloudx.estate.vo;

import java.util.List;

import com.aurine.cloudx.estate.entity.ProjectStaffNotice;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
