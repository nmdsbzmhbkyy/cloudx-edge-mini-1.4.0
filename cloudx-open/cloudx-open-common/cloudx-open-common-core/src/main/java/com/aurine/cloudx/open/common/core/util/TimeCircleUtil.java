package com.aurine.cloudx.open.common.core.util;

import cn.hutool.core.collection.CollUtil;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: TimeCircleUtil
 * @author: 王良俊 <>
 * @date:  2020年10月23日 下午04:44:54
 * @Copyright:
*/
public class TimeCircleUtil {

    private static List<String> circleList = new ArrayList<>();
    private static int circle = 0;
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * <p>
     *  根据传入的circle进行判断是否要重新生成列表
     * </p>
     *
     * @param circle 这里要传入的是当前循环周期
    */
    public TimeCircleUtil(int circle) {
        if (TimeCircleUtil.circle != circle) {
            TimeCircleUtil.circle = circle;
            initCircleList();
        }
    }

    private void initCircleList() {
        // 清空原有周期列表
        circleList.clear();
        LocalTime time = LocalTime.parse("00:00", timeFormatter);
        String preNum = time.toString().replace(":","");
        String currentNum = "";
        for (int i = 1; i <= 1440; i++) {
            if (i%circle==0){
                currentNum = time.plus(i, ChronoUnit.MINUTES).toString().replace(":","");
                circleList.add(preNum + "-" + time.plus(i, ChronoUnit.MINUTES).toString().replace(":",""));
                preNum = currentNum;
            }
        }
    }

    /**
     * <p>
     *  用来判断任务是否已经错过了本时段的定时任务
     * </p>
     *
     * @param time 要进行判断的时间段
     * @return 是否已经错过当前所处的那个时间段
    */
    public boolean checkHasBeenMissed(LocalTime time) {
        // circleList存放的数据格式是 1200-1220代表12:00-12:20这个时间段
        String nowNum = LocalTime.now().format(timeFormatter).toString().replace(":", "");
        String checkNum = time.format(timeFormatter).toString().replace(":", "");
        List<String> resultList = circleList.stream().filter(item -> {
            String[] numArr = item.split("-");
            String beginNum = numArr[0];
            String endNum = numArr[1];
            return Integer.parseInt(beginNum) < Integer.parseInt(nowNum)
                    && Integer.parseInt(endNum) > Integer.parseInt(nowNum)
                    && Integer.parseInt(beginNum) < Integer.parseInt(checkNum)
                    && Integer.parseInt(endNum) > Integer.parseInt(checkNum);
        }).collect(Collectors.toList());
        // 如果列表为空说明没有错过某个循环周期
        return CollUtil.isNotEmpty(resultList);
    }

}
