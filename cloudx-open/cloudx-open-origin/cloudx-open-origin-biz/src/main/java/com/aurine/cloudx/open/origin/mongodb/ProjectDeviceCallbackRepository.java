package com.aurine.cloudx.open.origin.mongodb;

import com.aurine.cloudx.open.origin.entity.IotEventCallback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * <p>
 *  设备回调的Repository接口
 *  tip: 这里面方法名不能随意更改
 * </p>
 * @author : 王良俊
 * @date : 2021-07-14 10:16:37
 */
public interface ProjectDeviceCallbackRepository extends MongoRepository<IotEventCallback, String> {


    /**
    * <p>
    * 分页查询设备回调的日志记录
    * </p>
    *
    * @param deviceId 设备ID
    * @param pageable 分页信息对象
    * @return 分页数据
    * @author: 王良俊
    */
    Page<IotEventCallback> queryIotEventCallbackByDeviceIdOrderBySeqDesc(String deviceId, Pageable pageable);


    /**
    * <p>
    * 根据设备ID获取到这台设备的所有事件，根据上报时间排序
    * </p>
    *
    * @param deviceId 设备ID
    * @return 设备的所有事件列表
    * @author: 王良俊
    */
    List<IotEventCallback> findAllByDeviceIdOrderByEventTimeDesc(String deviceId);

    /**
    * <p>
    * 根据设备ID删除该设备所有事件回调记录
    * </p>
    *
    * @param deviceId 设备ID
    * @author: 王良俊
    */
    void removeAllByDeviceId(String deviceId);

}
