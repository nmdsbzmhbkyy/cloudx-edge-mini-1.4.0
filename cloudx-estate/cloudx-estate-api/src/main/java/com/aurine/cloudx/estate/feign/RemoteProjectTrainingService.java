package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.entity.ProjectTrainingFileDb;
import com.aurine.cloudx.estate.entity.ProjectTrainingStaffDetail;
import com.aurine.cloudx.estate.vo.ProjectTrainingFormVo;
import com.aurine.cloudx.estate.vo.ProjectTrainingPageVo;
import com.aurine.cloudx.estate.vo.TrainingStaffDetailVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *
 * </p>
 * @ClassName: RemoteProjectTrainingService
 * @author: guhl <>
 * @date:  2021年01月19日 上午11:02:41
 * @Copyright:
 */
@FeignClient(contextId = "remoteProjectTrainingService", value = "cloudx-estate-biz")
public interface RemoteProjectTrainingService {
    /**
     * <p>
     *  查询员工培训信息
     * </p>
     *
     * @param trainingStaffDetailVo
     */
    @GetMapping({"/projectTraining/listTrainByStaff"})
    R<TrainingStaffDetailVo> listTrainByStaff(@RequestParam("trainingStaffDetailVo") TrainingStaffDetailVo trainingStaffDetailVo);

    /**
     * <p>
     *  根据培训ID获取培训资料
     * </p>
     *
     * @param trainingId 培训ID
     */
    @GetMapping({"/projectTraining/{trainingId}"})
    R<ProjectTrainingFormVo> getTraining(@PathVariable("trainingId") String trainingId);


    /**
     * 根据文件id批量查询
     * @param projectTrainingFormVo
     * @return 所有数据
     */
    @GetMapping({"/projectTrainingFileDb/selectFileByIds"})
    R<ProjectTrainingFileDb> selectFileByIds(@RequestBody ProjectTrainingFormVo projectTrainingFormVo);

    /**
     * <p>
     *  已读资料
     * </p>
     *
     * @param projectTrainingStaffDetail
     */
    @PutMapping({"/projectTraining/setProgress"})
    R<Boolean> setProgress(@RequestBody ProjectTrainingStaffDetail projectTrainingStaffDetail);

    /**
     * 获取员工已读资料数
     * @param staffId
     * @return
     */
    @GetMapping({"/projectTraining/countReadByStaffId/{staffId}/{trainingId}"})
    R<Integer> countReadByStaffId(@PathVariable("staffId") String staffId, @PathVariable("trainingId") String trainingId);
}
