//package com.aurine.cloudx.estate.util;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class SchedulerTask {
//
//  /**
//   * @Scheduled(fixedRate = 6000) ：上一次开始执行时间点之后6秒再执行
//   * @Scheduled(fixedDelay = 6000) ：上一次执行完毕时间点之后6秒再执行
//   * @Scheduled(initialDelay=1000, fixedRate=6000) ：第一次延迟1秒后执行，之后按fixedRate的规则每6秒执行一次
//   * @Scheduled(cron=""):详见cron表达式http://www.pppet.net/
//   */
//
//  @Scheduled(cron="*/6 * * * * ?")
//  private void scheduled3(){
//    log.info("使用cron执行定时任务");
//  }
//}