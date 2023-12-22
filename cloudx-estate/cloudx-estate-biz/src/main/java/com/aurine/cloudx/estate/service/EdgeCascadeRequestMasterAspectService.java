package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.CascadeRequestInfoDTO;
import com.aurine.cloudx.estate.entity.EdgeCascadeRequestMaster;
import com.aurine.cloudx.estate.entity.EdgeCascadeResponse;
import com.aurine.cloudx.estate.vo.CascadeManageQuery;
import com.aurine.cloudx.estate.vo.CascadeProjectInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * <p>级联申请管理（主）服务</p>
 * @author : 王良俊
 * @date : 2021-12-17 09:37:40
 */
public interface EdgeCascadeRequestMasterAspectService extends IService<EdgeCascadeRequestMaster> {

    R<EdgeCascadeRequestMaster> requestCascadeSaveOrUpdate(EdgeCascadeRequestMaster requestMaster);

    R<EdgeCascadeRequestMaster> passRequestUpdate(EdgeCascadeRequestMaster requestMaster);

    R<EdgeCascadeRequestMaster> rejectRequestUpdate(EdgeCascadeRequestMaster requestMaster);

    R<EdgeCascadeRequestMaster> removeEdgeUpdate(EdgeCascadeRequestMaster requestMaster);

    R<EdgeCascadeRequestMaster> cancelRequestUpdate(EdgeCascadeRequestMaster requestMaster);

}
