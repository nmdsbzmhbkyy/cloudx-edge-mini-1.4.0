package com.aurine.cloudx.dashboard.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.dashboard.entity.BaseDashboardEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25
 * @Copyright:
 */
public abstract class AbstractDashboardService implements DashboardService {

    protected static final String HOUR = "HOUR";
    protected static final String DAY = "DAY";
    protected static final String MONTH = "MONTH";

    /**
     * 获取列表的首个对象，如果列表为空，则返回null
     *
     * @param list
     * @param <T>
     * @return
     */
    protected <T> T getFistOne(List<T> list) {
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据项目id，查询该项目的数据
     *
     * @param mapper    数据库mapper
     * @param t         实体类型
     * @param projectId 项目id
     * @param <T>
     * @return
     */
    protected <T extends BaseDashboardEntity> T getOneByProjectId(BaseMapper mapper, Class<T> t, String projectId) {
//        List<T> list = mapper.selectList(new QueryWrapper<T>().lambda().eq(T::getProjectId, projectId));
        List<T> list = mapper.selectList(new QueryWrapper<T>().eq("projectId", projectId));

        return getFistOne(list);
    }

    /**
     * 根据项目数组，查询该项目的数据
     *
     * @param mapper         数据库mapper
     * @param t              实体类型
     * @param projectIdArray 项目id
     * @param <T>
     * @return
     */
    protected <T extends BaseDashboardEntity> List<T> getListByProjectId(BaseMapper mapper, Class<T> t, String[] projectIdArray) {
        if (ArrayUtil.isEmpty(projectIdArray)) return null;
        return mapper.selectList(new QueryWrapper<T>().in("projectId", projectIdArray));
    }

    /**
     * 根据项目ID、楼栋ID、楼层数，查找该楼栋的数据，楼层数可以为空
     *
     * @param mapper        数据库mapper
     * @param t             实体类型
     * @param projectId     项目id
     * @param buildingId    楼栋ID
     * @param storey        楼层数
     * @param <T>
     * @return
     */
    protected <T extends BaseDashboardEntity> List<T> getListByProjectIdAndBuildingId(BaseMapper mapper, Class<T> t, String projectId, String buildingId, Integer storey) {
        if (StringUtil.isEmpty(projectId) || StringUtil.isEmpty(buildingId)) return null;
        if (storey == null || storey <= 0) {
            return mapper.selectList(
                    new QueryWrapper<T>()
                            .eq("projectId", projectId)
                            .eq("buildingId", buildingId));
        }
        return mapper.selectList(
                new QueryWrapper<T>()
                        .eq("projectId", projectId)
                        .eq("buildingId", buildingId)
                        .eq("storey", storey));
    }


    /**
     * 初始化列表
     *
     * @param length
     * @return
     */
    protected Long[] initArray(int length) {

        Long[] array = new Long[length];

        for (int i = 0; i < length; i++) {
            array[i] = 0L;
        }
        return array;
    }
    /**
     * 初始化列表
     *
     * @param length
     * @return
     */
    protected Double[] initDoubleArray(int length) {

        Double[] array = new Double[length];

        for (int i = 0; i < length; i++) {
            array[i] = 0D;
        }
        return array;
    }

    /**
     * 初始化Long类型的二维数组
     *
     * @param oneLength     外层数组长度
     * @param twoLength     内层数组长度
     * @return
     */
    protected Long[][] initTwoDimensionalArray(int oneLength, int twoLength) {
        Long[][] array = new Long[oneLength][twoLength];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = 0L;
            }
        }
        return array;
    }

    /**
     * 初始化 X轴数据
     *
     * @param length
     * @param type      HOUR\DAY\MONTH
     * @param orderDesc 是否倒叙排列
     * @param offset    偏移量 -1为提早一个单位时间
     * @return
     */
    protected String[] initAxis(int length, String type, boolean orderDesc, int offset) {

        String[] resultArray = new String[length];

        switch (type) {
            case HOUR:
                for (int i = 0; i < length; i++) {
                    resultArray[orderDesc ? i : length - 1 - i] = String.valueOf(DateUtil.hour(DateUtils.addHours(new Date(), -i + offset), true));
                }
                break;
            case DAY:
                for (int i = 0; i < length; i++) {
                    resultArray[orderDesc ? i : length - 1 - i] = String.valueOf(DateUtil.dayOfMonth(DateUtils.addDays(new Date(), -i + offset)));
                }
                break;
            case MONTH:
                for (int i = 0; i < length; i++) {
                    resultArray[orderDesc ? i : length - 1 - i] = String.valueOf(DateUtil.month(DateUtils.addMonths(new Date(), -i + offset)) + 1);
                }
                break;
            default:
                break;
        }


        DateUtil.dayOfMonth(DateUtils.addDays(new Date(), -1));
        return resultArray;
    }


    /**
     * 初始化 X轴数据
     *
     * @param length
     * @param type   HOUR\DAY\MONTH
     * @return
     */
    protected String[] initAxis(int length, String type) {
        return initAxis(length, type, false, 0);
    }



}
