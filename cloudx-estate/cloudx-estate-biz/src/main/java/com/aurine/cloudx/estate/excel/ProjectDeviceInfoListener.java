package com.aurine.cloudx.estate.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.estate.constant.DeviceInfoConstant;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.LoadStatusConstants;
import com.aurine.cloudx.estate.constant.enums.DeviceAccessMethodEnum;
import com.aurine.cloudx.estate.constant.enums.DeviceExcelEnum;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegParamEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceLoadLogDetail;
import com.aurine.cloudx.estate.excel.invoke.entity.RowInvokeResult;
import com.aurine.cloudx.estate.excel.invoke.factory.ExcelInvokeServiceFactory;
import com.aurine.cloudx.estate.excel.invoke.service.BaseExcelRowInvokeService;
import com.aurine.cloudx.estate.service.ProjectDeviceAbnormalService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectDeviceLoadLogDetailService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.entity.DeviceAbnormalHandleInfo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>设备导入AnalysisEventListener类</p>
 *
 * @author : 王良俊
 * @date : 2021-09-03 15:30:00
 */
@Slf4j
public class ProjectDeviceInfoListener extends AnalysisEventListener {

    /**
     * 导入失败数
     */
    private int errorNum = 0;

    /**
     * 导入数据列表
     */
    HashMap<Integer, Object> errorMap = new HashMap<>();

    /**
     * 设备信息操作实体
     */
    private final ProjectDeviceInfoService projectDeviceInfoService;

    /**
     * 设备信息操作实体
     */
    private final ProjectDeviceAbnormalService projectDeviceAbnormalService;

    /**
     * 楼栋单元房屋实体
     */
    private final ProjectDeviceLoadLogDetailService projectDeviceLoadLogDetailService;

    /**
     * 设备导入类枚举
     */
    private final DeviceExcelEnum deviceExcelEnum;

    /**
     * 设备导入日志ID
     */
    String batchId;

    RedisTemplate<String, String> redisTemplate;

    private final String REDIS_KEY_PRE = "device_name_";

    boolean isCover;

    public ProjectDeviceInfoListener(ProjectDeviceInfoService projectDeviceInfoService,
                                     ProjectDeviceLoadLogDetailService projectDeviceLoadLogDetailService,
                                     ProjectDeviceAbnormalService projectDeviceAbnormalService,
                                     RedisTemplate<String, String> redisTemplate, DeviceExcelEnum deviceExcelEnum,
                                     String batchId) {
        this.projectDeviceInfoService = projectDeviceInfoService;
        this.projectDeviceLoadLogDetailService = projectDeviceLoadLogDetailService;
        this.projectDeviceAbnormalService = projectDeviceAbnormalService;
        this.deviceExcelEnum = deviceExcelEnum;
        this.redisTemplate = redisTemplate;
        this.batchId = batchId;
        List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.list(new LambdaQueryWrapper<>(ProjectDeviceInfo.class).select(ProjectDeviceInfo::getDeviceName));
        if (CollUtil.isNotEmpty(deviceInfoList)) {
            redisTemplate.opsForSet().add(REDIS_KEY_PRE + batchId, deviceInfoList.stream().map(ProjectDeviceInfo::getDeviceName).toArray(String[]::new));
            redisTemplate.expire(REDIS_KEY_PRE + batchId, 1, TimeUnit.HOURS);
        }

    }

    public ProjectDeviceInfoListener(ProjectDeviceInfoService projectDeviceInfoService,
                                     ProjectDeviceLoadLogDetailService projectDeviceLoadLogDetailService,
                                     ProjectDeviceAbnormalService projectDeviceAbnormalService,
                                     RedisTemplate<String, String> redisTemplate, DeviceExcelEnum deviceExcelEnum,
                                     String batchId, boolean isCover){
        this(projectDeviceInfoService, projectDeviceLoadLogDetailService, projectDeviceAbnormalService, redisTemplate, deviceExcelEnum, batchId);
        this.isCover = isCover;
    }

    /**
     * 单行数据解析调用方法
     *
     * @param t               导入的实体
     * @param analysisContext 分析上下文内容
     */
    @Override
    public void invoke(Object t, AnalysisContext analysisContext) {

        // 这里更新当前行在数据库中导入状态为导入中
        this.updateLoadLogDetailStatus(analysisContext.readRowHolder().getRowIndex(), LoadStatusConstants.IMPORTING);
        RowInvokeResult result = null;
        if(!isCover){
            result = ExcelInvokeServiceFactory.getInstance(deviceExcelEnum.getCode()).invoke(batchId, t, analysisContext, "1".equals(deviceExcelEnum.getType()));
        }else {
            result = ExcelInvokeServiceFactory.getInstance(deviceExcelEnum.getCode()).invoke(batchId, t, analysisContext, "1".equals(deviceExcelEnum.getType()), isCover);
        }
        if (result.isFailed()) {
            saveErrorMap(analysisContext.readRowHolder().getRowIndex(), t, result.getFailedResult());
        } else {
            ProjectDeviceInfoVo deviceInfoVo = result.getDeviceInfoVo();
            deviceInfoVo.setDeviceType(deviceExcelEnum.getCode());
            if(deviceInfoVo.getDeviceType().equals(DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE)){
                if(StrUtil.isBlank(deviceInfoVo.getDeviceCode())){
                    saveErrorMap(analysisContext.readRowHolder().getRowIndex(), t, "请填写正确的设备编号");
                    return;
                }
            }
            try {
                deviceInfoVo.setAccessMethod(DeviceAccessMethodEnum.MANUAL.code);
                if(isCover){
                    List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.list(new LambdaQueryWrapper<>(ProjectDeviceInfo.class).eq(ProjectDeviceInfo::getDeviceName, deviceInfoVo.getDeviceName()));
                    if(CollUtil.isNotEmpty(deviceInfoList)){
                        saveErrorMap(analysisContext.readRowHolder().getRowIndex(), t, "设备名已存在");
                        return;
                    }
                    deviceInfoVo.setStatus(DeviceInfoConstant.UNACTIVATED_STATUS);
                    projectDeviceInfoService.saveDeviceVo(deviceInfoVo);
                }else {
                    Boolean nameExist = redisTemplate.opsForSet().isMember(REDIS_KEY_PRE + batchId, deviceInfoVo.getDeviceName());
                    if (nameExist != null && nameExist) {
                        saveErrorMap(analysisContext.readRowHolder().getRowIndex(), t, "设备名已存在");
                        return;
                    }
                    deviceInfoVo.setStatus(DeviceInfoConstant.UNACTIVATED_STATUS);
                    projectDeviceInfoService.saveDeviceVo(deviceInfoVo);
                }

                DeviceAbnormalHandleInfo handleInfo = new DeviceAbnormalHandleInfo();

                handleInfo.setThirdpartyCode(deviceInfoVo.getThirdpartyCode());
                handleInfo.setDStatus(deviceInfoVo.getStatus());
                handleInfo.setDeviceDesc(deviceInfoVo.getDeviceName());
                handleInfo.setDeviceId(deviceInfoVo.getDeviceId());
                handleInfo.setProjectId(ProjectContextHolder.getProjectId());
                handleInfo.setSn(deviceInfoVo.getSn());
                handleInfo.addParam(DeviceRegParamEnum.DEVICE_NO, deviceInfoVo.getDeviceCode());
                handleInfo.addParam(DeviceRegParamEnum.IPV4, deviceInfoVo.getIpv4());
                handleInfo.addParam(DeviceRegParamEnum.MAC, deviceInfoVo.getMac());
                projectDeviceAbnormalService.checkAbnormal(handleInfo);

                redisTemplate.opsForSet().add(REDIS_KEY_PRE + batchId, deviceInfoVo.getDeviceName());
                this.updateLoadLogDetailStatus(analysisContext.readRowHolder().getRowIndex(), LoadStatusConstants.IMPORT_SUCCESS);

            } catch (Exception exception) {
                exception.printStackTrace();
                saveErrorMap(analysisContext.readRowHolder().getRowIndex(), t, exception.getMessage().length() > 100 ? "外部原因导致设备导入失败" : exception.getMessage());
            }
        }
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) {
        log.error("[Excel导入] 第{}行解析时异常：", context.readRowHolder().getRowIndex());
        exception.printStackTrace();
        Integer row;
        String errorMessage;
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            row = excelDataConvertException.getRowIndex();
            errorMessage = "第" + excelDataConvertException.getColumnIndex() + "列数据解析异常";
        } else {
            errorMessage = "外部原因导致设备导入失败。";
            row = context.readRowHolder().getRowIndex();
        }
        Object object = context.readRowHolder().getCurrentRowAnalysisResult();
        if (ObjectUtil.isNotEmpty(object)) {
            saveErrorMap(row, object, errorMessage);
        }
        exception.printStackTrace();
    }

    private void saveErrorMessageMap(Integer row, String message) {
        this.errorNum++;
        this.updateLoadLogDetailStatus(row, LoadStatusConstants.IMPORT_FAILED, message, "");
    }

    /**
     * <p>
     * 用于后面生成导入失败的设备Excel（这里存储导入失败的数据）
     * </p>
     *
     * @author: xull
     */
    private void saveErrorMap(Integer row, Object object, String message) {
        if (errorMap.get(row) == null) {
            errorMap.put(row, object);
        } else {
            errorMap.replace(row, object);
        }
        saveErrorMessageMap(row, message);
    }

    /**
     * 读取完毕后回调方法
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        setErrorException();
    }

    /**
     * 保存错误文件信息
     */
    private void setErrorException() {

        if (this.errorNum > 0) {
            // 导入失败的列表，设置过期时间为1小时，用于生成导入失败的Excel文件
            redisTemplate.opsForValue().set(batchId, JSONUtil.toJsonStr(new ArrayList<>(errorMap.values())), 1, TimeUnit.HOURS);
        }
        // 删除redis缓存
        BaseExcelRowInvokeService service = ExcelInvokeServiceFactory.getInstance(deviceExcelEnum.getCode());
        service.clearCache();
        redisTemplate.delete(service.getRedisKeys(batchId));
        redisTemplate.delete(REDIS_KEY_PRE + batchId);
    }

    /**
     * <p>
     * 更新设备导入日志明细表中，当前行的导入状态
     * </p>
     *
     * @param row    当前行号
     * @param status 所要变更的状态 LoadStatusConstants
     * @author: 王良俊
     */
    private void updateLoadLogDetailStatus(Integer row, String status) {
        projectDeviceLoadLogDetailService.update(new LambdaUpdateWrapper<ProjectDeviceLoadLogDetail>()
                .eq(ProjectDeviceLoadLogDetail::getBatchId, this.batchId)
                .eq(ProjectDeviceLoadLogDetail::getRowNo, row)
                .set(ProjectDeviceLoadLogDetail::getLoadStatus, status));
    }

    /**
     * <p>
     * 更新设备导入日志明细表中，当前行的导入状态
     * </p>
     *
     * @param row          当前行号
     * @param status       所要变更的状态 LoadStatusConstants
     * @param errorCode    错误代码（如果有的话）
     * @param errorMessage 失败原因（如果有的话）
     * @author: 王良俊
     */
    private void updateLoadLogDetailStatus(Integer row, String status, String errorMessage, String errorCode) {
        projectDeviceLoadLogDetailService.update(new LambdaUpdateWrapper<ProjectDeviceLoadLogDetail>()
                .eq(ProjectDeviceLoadLogDetail::getBatchId, this.batchId)
                .eq(ProjectDeviceLoadLogDetail::getRowNo, row)
                .set(ProjectDeviceLoadLogDetail::getLoadStatus, status)
                .set(StrUtil.isNotEmpty(errorMessage), ProjectDeviceLoadLogDetail::getErrorMsg, errorMessage)
                .set(StrUtil.isNotEmpty(errorCode), ProjectDeviceLoadLogDetail::getErrorCode, errorCode));
    }
}
