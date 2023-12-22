package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.aurine.cloudx.estate.annotation.OrderByDesc;
import com.aurine.cloudx.estate.entity.IotSeqEntity;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.mongodb.ProjectDeviceCallbackRepository;
import com.aurine.cloudx.estate.entity.IotEventCallback;
import com.aurine.cloudx.estate.service.ProjectDeviceEventCallbackService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoProxyService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备回调日志服务实现类
 * </p>
 *
 * @author : 王良俊
 * @date : 2021-07-15 11:06:46
 */
@Service
@Slf4j
public class ProjectDeviceEventCallbackServiceImpl implements ProjectDeviceEventCallbackService {

    @Resource
    ProjectDeviceCallbackRepository repository;
    @Resource
    ProjectDeviceInfoProxyService projectDeviceInfoProxyService;

    @Resource
    MongoTemplate mongoTemplate;

    @Resource
    RedisTemplate<String, String> redisTemplateAurine;

    private final static Map<Class, List<Order>> orderMap = new HashMap<>();


    @Override
    public Page<IotEventCallback> page(String deviceId, int page, int size) {
        return repository.queryIotEventCallbackByDeviceIdOrderBySeqDesc(deviceId, PageRequest.of(page - 1, size));
    }

    @Override
    public Page<IotEventCallback> page(String deviceId, int page, int size, Sort.Direction direction, String... properties) {
        return repository.queryIotEventCallbackByDeviceIdOrderBySeqDesc(deviceId, PageRequest.of(page - 1, size, Sort.by(direction, properties)));
    }

    @Override
    public Page<IotEventCallback> page(String deviceId, int page, int size, Class clazz) {
        List<Order> orderList = orderMap.get(clazz);
        if (orderList == null) {
            orderList = Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> field.getAnnotation(OrderByDesc.class) != null)
                    .map(field -> {
                        OrderByDesc orderByDesc = field.getAnnotation(OrderByDesc.class);
                        Field fieldNameAnnotation = field.getAnnotation(Field.class);
                        String fieldName = "";
                        if (fieldNameAnnotation == null) {
                            fieldName = field.getName();
                        } else if (StrUtil.isNotEmpty(fieldNameAnnotation.value())) {
                            fieldName = fieldNameAnnotation.value();
                        } else if (StrUtil.isNotEmpty(fieldNameAnnotation.name())) {
                            fieldName = fieldNameAnnotation.name();
                        }
                        return new FieldPriority(orderByDesc.priority(), fieldName);
                    })
                    .sorted(Comparator.comparingInt(FieldPriority::getPriority))
                    .map(fieldPriority -> Order.desc(fieldPriority.getFieldName()))
                    .collect(Collectors.toList());
            orderMap.put(clazz, orderList);
        }

        return CollUtil.isNotEmpty(orderList) ?
                repository.queryIotEventCallbackByDeviceIdOrderBySeqDesc(deviceId, PageRequest.of(page - 1, size, Sort.by(orderList))) :
                repository.queryIotEventCallbackByDeviceIdOrderBySeqDesc(deviceId, PageRequest.of(page - 1, size));
    }

    @Override
    public IotEventCallback save(IotEventCallback iotEventCallback) {
        ProjectDeviceInfo deviceInfo = projectDeviceInfoProxyService.getVoById(iotEventCallback.getDeviceId());
        initSeqData(deviceInfo.getProjectId(), deviceInfo.getDeviceType());
        IotSeqEntity seqEntity = mongoTemplate.findAndModify(
                Query.query(Criteria.where("projectId").is(deviceInfo.getProjectId()).and("deviceType").is(deviceInfo.getDeviceType())),
                new Update().inc("seq", 1),
                IotSeqEntity.class
        );

        iotEventCallback.setSeq(seqEntity.getSeq());
        if (StrUtil.isEmpty(iotEventCallback.getDeviceId())) {
            log.info("[物联设备-MongoDB] 设备ID为空无法保存日志到MongoDB中");
            return iotEventCallback;
        }
        return repository.save(iotEventCallback);
    }

    @Override
    public List<IotEventCallback> saveBatch(Collection<IotEventCallback> callbackList) {
        Set<IotEventCallback> collect = callbackList.stream().filter(item -> StrUtil.isEmpty(item.getDeviceId()))
                .collect(Collectors.toSet());
        callbackList.removeAll(collect);
        if (CollUtil.isEmpty(callbackList)) {
            log.warn("[物联设备-MongoDB] 这些设备回调日志缺少设备ID：{}", callbackList.toString());
        }
        Iterator<IotEventCallback> iterator = callbackList.iterator();
        IotEventCallback iotEventCallback = iterator.next();

        String deviceId = iotEventCallback.getDeviceId();
        ProjectDeviceInfo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);

        initSeqData(deviceInfo.getProjectId(), deviceInfo.getDeviceType());
        IotSeqEntity seqEntity = mongoTemplate.findAndModify(
                Query.query(Criteria.where("projectId").is(deviceInfo.getProjectId()).and("deviceType").is(deviceInfo.getDeviceType())),
                new Update().inc("seq", callbackList.size()),
                IotSeqEntity.class
        );
        AtomicInteger seq = new AtomicInteger(seqEntity.getSeq());
        callbackList.forEach(callback -> {
            callback.setSeq(seq.getAndIncrement());
        });

        return repository.saveAll(callbackList);
    }

    /**
     * <p>初始化保存设备事件日志所需要的序列seq数据</p>
     *
     * @param projectId 项目ID
     * @param deviceType 设备类型ID
     */
    private void initSeqData(Integer projectId, String deviceType) {
        String key = String.format("IotEventSeq_%s_%s", projectId.toString(), deviceType);
        Query query = Query.query(Criteria.where("projectId").is(projectId).and("deviceType").is(deviceType));
        boolean exists = mongoTemplate.exists(query, IotSeqEntity.class);
        if (!exists) {
            log.info("[物联设备-MongoDB] 准备保存序列数据到序列集合中，projectId：{} deviceType：{}", projectId, deviceType);
            Boolean aBoolean = redisTemplateAurine.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
            if (aBoolean != null) {
                if (!aBoolean) {
                    log.info("[物联设备-MongoDB] 序列数据可能已生成");
                    initSeqData(projectId, deviceType);
                } else if (!mongoTemplate.exists(query, IotSeqEntity.class)){
                    log.info("[物联设备-MongoDB] 获取到锁准备新增序列数据");
                    mongoTemplate.save(new IotSeqEntity(projectId, deviceType, 1));
                    mongoTemplate.indexOps(IotSeqEntity.class).ensureIndex(new Index("projectId", Sort.Direction.ASC).on("deviceType", Sort.Direction.ASC).unique());
                    mongoTemplate.indexOps(IotEventCallback.class).ensureIndex(new Index("seq", Sort.Direction.DESC).unique());
                }
            } else {
                this.initSeqData(projectId, deviceType);
            }
        } else {
            log.info("[物联设备-MongoDB] 无需创建");
        }
    }

    public IotEventCallback updateById(IotEventCallback iotEventCallback) {
        if (StrUtil.isEmpty(iotEventCallback.getCallbackId())) {
            log.error(" {}", iotEventCallback.toString());
            throw new RuntimeException("[物联设备-MongoDB] 回调日志ID为空无法更新设备日志 " + iotEventCallback.toString());
        }
        return repository.save(iotEventCallback);
    }

    public IotEventCallback updateBatchById(IotEventCallback iotEventCallback) {

        if (StrUtil.isEmpty(iotEventCallback.getCallbackId())) {
            log.error(" {}", iotEventCallback.toString());
            throw new RuntimeException("[物联设备-MongoDB] 回调日志ID为空无法更新设备日志 " + iotEventCallback.toString());
        }
        return repository.save(iotEventCallback);
    }

    @Override
    public void removeCallbackById(String callbackId) {
        repository.deleteById(callbackId);
    }

    @Override
    public void removeByDeviceId(String deviceId) {
        repository.removeAllByDeviceId(deviceId);
    }

    @SneakyThrows
    @Override
    public void exportExcel(String deviceId, String deviceName, HttpServletResponse response) {
        List<IotEventCallback> eventCallbackList = repository.findAllByDeviceIdOrderByEventTimeDesc(deviceId);
        if (CollUtil.isNotEmpty(eventCallbackList)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(deviceName + "-设备报表.xlsx", "UTF-8"));

            Set<String> excludeColumnSet = new HashSet<>();
            excludeColumnSet.add("callbackId");
            excludeColumnSet.add("deviceId");
            excludeColumnSet.add("objectMapper");

            EasyExcel.write(response.getOutputStream(), IotEventCallback.class).excludeColumnFiledNames(excludeColumnSet)
                    .sheet("报表").doWrite(eventCallbackList);
        }
    }
}
