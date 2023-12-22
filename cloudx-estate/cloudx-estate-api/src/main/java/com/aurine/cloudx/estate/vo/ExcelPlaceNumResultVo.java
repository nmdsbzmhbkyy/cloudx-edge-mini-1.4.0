package com.aurine.cloudx.estate.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;


/**
 * @ClassName: ExcelPlaceNumResultVo
 * @author: 王良俊 <>
 * @date: 2020年09月04日 上午11:18:50
 * @Copyright:
 */
@Data
public class ExcelPlaceNumResultVo {

    /**
     * 这里用于判断是否可以继续进行导入操作
     */
    private boolean continueAble = true;

    /**
     * 存放消息数据
     */
    private HashMap<String, String> describe;
}
