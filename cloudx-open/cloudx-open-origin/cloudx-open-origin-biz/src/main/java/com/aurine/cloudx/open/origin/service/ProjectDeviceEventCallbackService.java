package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.IotEventCallback;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 物联网设备事件回调服务类
 * </p>
 * @author : 王良俊
 * @date : 2021-07-20 10:26:10
 */
public interface ProjectDeviceEventCallbackService {

    /**
    * <p>
    * 分页查询日志记录（默认排序）
    * </p>
    *
    * @param deviceId 设备ID
    * @param page 当前页码
    * @param size 每页大小
    * @return 分页数据
    * @author: 王良俊
    */
    Page<IotEventCallback> page(String deviceId, int page, int size);

    /**
     * <p>
     * 分页查询日志记录（指定属性和排序方向）
     * </p>
     *
     * @param deviceId 设备ID
     * @param page 当前页码
     * @param size 每页大小
     * @param direction 正序或是逆序
     * @param properties 所要排序的字段名
     * @return 分页数据
     * @author: 王良俊
     */
    Page<IotEventCallback> page(String deviceId, int page, int size, Direction direction, String... properties);

    /**
    * <p>
    * 分页查询（根据某个字段排序通过对象的@SortByDesc注解来决定）
    * </p>
    *
    * @param deviceId 设备ID
    * @param page 当前页码
    * @param size 当前页面大小
    * @param clazz 必须为BaseCallback的某个子类的class
    * @return 分页数据
    * @author: 王良俊
    */
    Page<IotEventCallback> page(String deviceId, int page, int size, Class clazz);

    /**
     * <p>
     * 保存设备回调日志到MongoDB中（设备ID不能为空）
     * </p>
     *
     * @param iotEventCallback 设备回调对象
     * @return 带有主键ID数据的回调对象
     * @author: 王良俊
     */
    IotEventCallback save(IotEventCallback iotEventCallback);

    /**
     * <p>
     * 批量保存设备回调日志到MongoDB中（设备ID不能为空）
     * </p>
     *
     * @param iotEventCallback 设备回调对象
     * @return 带有主键ID数据的回调对象
     * @author: 王良俊
     */
    List<IotEventCallback> saveBatch(Collection<IotEventCallback> iotEventCallback);

    /**
    * <p>
    * 根据设备回调日志ID删除回调记录
    * </p>
    *
    * @param callbackId 回调ID
    * @author: 王良俊
    */
    void removeCallbackById(String callbackId);

    /**
    * <p>
    * 根据设备ID删除这台设备的所有回调
    * </p>
    *
    * @param deviceId 设备ID
    * @author: 王良俊
    */
    @Async
    void removeByDeviceId(String deviceId);

    /**
    * <p>
    * 导出设备事件回调日志
    * </p>
    *
    * @param deviceId 设备ID
    * @param response
    * @author: 王良俊
    */
    void exportExcel(String deviceId, String deviceName, HttpServletResponse response);

    @Data
    @AllArgsConstructor
    class FieldPriority {
        private int priority;
        private String fieldName;

    }

}
