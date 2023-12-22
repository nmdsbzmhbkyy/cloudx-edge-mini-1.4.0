package com.aurine.cloudx.estate.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.ParCarRegisterExcelEnum;
import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.excel.parking.ParCarRegisterExcel;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ExcelPlaceNumResultVo;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: ProjectParCarRegisterCheckNumListener
 * @author: 王良俊 <>
 * @date: 2020年09月04日 上午09:27:32
 * @Copyright:
 */
public class ProjectParCarRegisterCheckNumListener<T> extends AnalysisEventListener<T> {

    ProjectParkingInfoService projectParkingInfoService;

    ProjectParkingPlaceService projectParkingPlaceService;

    /**
     * 导入错误信息列表
     */
    HashMap<String, String> errorMessageMap = new HashMap<>();


    /**
     * 导入错误信息列表
     */
    HashMap<String, Integer> freePublicPlaceNumMap = new HashMap<>();

    /**
     * 导入数据列表
     */
    HashMap<Integer, Object> errorMap = new HashMap<>();


    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
    String parkingRedisKey = "parking" + uuid;

    /**
     * Excel处理结果
     */
    private final ExcelPlaceNumResultVo excelResultVo;

    public ProjectParCarRegisterCheckNumListener(ProjectParkingInfoService projectParkingInfoService,
                                                 ProjectParkingPlaceService projectParkingPlaceService,
                                                 ExcelPlaceNumResultVo excelResultVo) {
        this.projectParkingInfoService = projectParkingInfoService;
        this.projectParkingPlaceService = projectParkingPlaceService;
        this.excelResultVo = excelResultVo;
    }

    /**
     * <p>
     * 每一行excel都会执行这个方法
     * </p>
     *
     * @param context 上下文 获取行数信息
     * @param data    每一行的数据
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        ParCarRegisterExcel parCarRegisterExcel = new ParCarRegisterExcel();
        BeanUtil.copyProperties(data, parCarRegisterExcel);
        Integer currentRow = context.readRowHolder().getRowIndex();
        String parkNameCh = parCarRegisterExcel.getParkNameCh();
        if (StrUtil.isNotBlank(parkNameCh)) {
            String parkIdByParkName = projectParkingInfoService.getParkIdByParkName(this.parkingRedisKey, parkNameCh, ProjectContextHolder.getProjectId());
            if (StrUtil.isNotEmpty(parkIdByParkName)) {
                // 对车场进行计数
                freePublicPlaceNumMap.merge(parkIdByParkName, 1, Integer::sum);
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        setErrorException();
        projectParkingInfoService.deleteParkTmpCache(this.parkingRedisKey);
    }

    /**
     * <p>
     * 处理异常
     * </p>
     *
     * @param context   上下文
     * @param exception 异常对象
     * @author: 许亮亮
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        exception.printStackTrace();
    }

    /**
     * <p>
     * 进行数据保存操作
     * </p>
     */
    protected void saveData() {
        if (MapUtil.isNotEmpty(freePublicPlaceNumMap)) {
            Set<String> parkIdSet = freePublicPlaceNumMap.keySet();
            List<ProjectParkingInfo> parkingInfoList = projectParkingInfoService.list(new QueryWrapper<ProjectParkingInfo>()
                    .lambda().in(ProjectParkingInfo::getParkId, parkIdSet));
            Map<String, ProjectParkingInfo> parkingInfoMap = parkingInfoList.stream().collect(Collectors.toMap(ProjectParkingInfo::getParkId, projectParkingInfo -> projectParkingInfo, (t, t2) -> t2));
            parkIdSet.forEach(parkId -> {
                int freePublicParkingSpaceNum = projectParkingPlaceService.getFreePublicParkingSpaceNum(parkId);
                if (CollUtil.isNotEmpty(parkingInfoMap)) {
                    ProjectParkingInfo parkingInfo = parkingInfoMap.get(parkId);
                    if (parkingInfo != null) {
                        // 如果剩余车位数少于excel所需要的车位数则写入错误数据
                        if (freePublicParkingSpaceNum < freePublicPlaceNumMap.get(parkId)) {
                            saveErrorMessageMap(parkingInfo.getParkName(),
                                    "<span style=\"color:red\"' class=\"result\">车位不足</span>-剩余公共车位" +
                                            "<span style=\"color:green\"' class=\"num1\"> " + freePublicParkingSpaceNum + "个</span>" +
                                            "，当前所需要的车位<span style=\"color:red\"' class=\"num2\"> " +
                                            freePublicPlaceNumMap.get(parkId) +
                                            "个</span>");
                            excelResultVo.setContinueAble(false);
                        } else if (freePublicParkingSpaceNum == freePublicPlaceNumMap.get(parkId)) {
                            saveErrorMessageMap(parkingInfo.getParkName(),
                                    "<span style=\"color:green\"' class=\"result\">车位足够</span>-本次导入后无剩余车位");
                        } else {
                            saveErrorMessageMap(parkingInfo.getParkName(), "<span style=\"color:green\"' class=\"result\">车位足够" +
                                    "</span>(剩余" + freePublicParkingSpaceNum + ")-导入完成后可剩余<span style=\"color:green\"' class=\"num3\"> "
                                    + (freePublicParkingSpaceNum - freePublicPlaceNumMap.get(parkId)) + "</span>个空闲公共车位");
                        }
                    }
                }
            });
        }
    }

    /**
     * @author: 许亮亮
     */
    private void saveErrorMessageMap(String parkName, String message) {
        errorMessageMap.put(parkName, message);
    }

    private void saveErrorMap(Integer row, Object object) {
        if (errorMap.get(row) == null) {
            errorMap.put(row, object);
        } else {
            errorMap.replace(row, object);
        }
    }

    /**
     * 保存错误文件信息
     *
     * @author: 许亮亮
     */
    private void setErrorException() {
        if (errorMessageMap.size() > 0) {
            excelResultVo.setDescribe(errorMessageMap);
        }
    }

}
