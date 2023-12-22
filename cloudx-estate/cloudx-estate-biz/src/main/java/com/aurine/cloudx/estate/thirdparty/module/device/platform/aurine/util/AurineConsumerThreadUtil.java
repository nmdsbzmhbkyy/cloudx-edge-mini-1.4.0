package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.util;

import com.aurine.cloudx.estate.config.kafka.KafkaConsumerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @ClassName: AurineConsumerThreadUtil
 * @author: 王良俊 <>
 * @date: 2020年11月19日 上午11:03:38
 * @Copyright:
 */
//@Component
@Slf4j
@Deprecated
public class AurineConsumerThreadUtil {
    private static HashMap<String, Thread> threadMap = new HashMap<>();

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    KafkaConsumerConfig kafkaConsumerConfig;

//    /**
//     * 创建线程
//     * <p>
//     * 根据系统配置，动态创建消费者线程
//     *
//     * @param sn
//     * @param baseAurineCertSendConsumer
//     */
//    public String initConsumerThread(String sn, AurineConfigDTO aurineConfigDTO, BaseAurineCertSendConsumer baseAurineCertSendConsumer) {
//        /**
//         * 根据配置，使用散列或设备SN作为主题
//         * @author: 王伟
//         * @since： 2020-12-01 9:02
//         */
//
//        String topic = "";
//        if (aurineConfigDTO.getHashTopic()) {
//            topic = AurineTopicUtil.getHashTopic(sn, aurineConfigDTO);
//        } else {
//            topic = AurineTopicUtil.getSnTopic(sn);
//        }
//
//        if (threadMap.get(topic) != null) {
//            log.info("[冠林中台] 动态创建消费者已存在 主题：{}", topic);
//            return topic;
//        }
//
//        final String finalTopic = topic;
//
//        //创建动态主题
//
//        //消费者启动标识
//        RedisUtil.set("INIT_" + finalTopic, "1", 10);
//
//        log.info("冠林中台 动态创建第 {} 个消费者 主题：{}", threadMap.size(), topic);
//        Thread thread = threadPoolTaskExecutor.createThread(() -> {
//
//            Map<String, Object> map = kafkaConsumerConfig.consumerConfigs();
//            map.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
//            KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(map);
//            List<String> topics = new ArrayList<>();
//            topics.add(finalTopic);
//            kafkaConsumer.subscribe(topics);
//            List<PartitionInfo> partitionInfos = null;
//            List<TopicPartition> topicPartitionList = null;
//            long index;
//            long sum;
//
//            RedisUtil.del("INIT_" + finalTopic);//启动结束标识
//
//            do {
//                index = 0;
//                sum = 0;
//                ConsumerRecords<String, String> poll = kafkaConsumer.poll(Duration.ofSeconds(2));
//                if (poll.count() != 0) {
//                    // 这里是用来计算当前主题下有多少消息
//                    partitionInfos = kafkaConsumer.partitionsFor(finalTopic);
//                    topicPartitionList = partitionInfos.stream().map(partitionInfo -> new TopicPartition(partitionInfo.topic(), partitionInfo.partition())).collect(Collectors.toList());
//
//                    for (TopicPartition topicPartition : topicPartitionList) {
//                        sum += kafkaConsumer.endOffsets(topicPartitionList).get(topicPartition) - kafkaConsumer.beginningOffsets(topicPartitionList).get(topicPartition);
//                    }
//
//                    // 开始对消息进行处理
//                    for (ConsumerRecord<String, String> stringStringConsumerRecord : poll) {
//                        index++;
//                        long offset = stringStringConsumerRecord.offset();
//                        log.info("[冠林中台] 通行凭证指令下发 消费者 本次拉取：{}/{}，偏移量：{}/{}, 剩余:{}", index, poll.count(), offset, sum - 1, sum - offset - 1);
////                        baseAurineCertSendConsumer.readActiveQueue(stringStringConsumerRecord.value(), finalTopic);
//                    }
//                    kafkaConsumer.commitSync();
//                }
//            }
//            while (true);
//        });
//
//        threadPoolTaskExecutor.execute(thread);
//        //等待线程执行，消费者创建后，下一步
//        while (RedisUtil.hasKey("INIT_" + topic)) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        threadMap.put(topic, thread);
//
//        return topic;
//    }

}
