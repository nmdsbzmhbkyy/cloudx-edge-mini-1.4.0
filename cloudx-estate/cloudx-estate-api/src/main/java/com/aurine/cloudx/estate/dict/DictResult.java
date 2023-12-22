package com.aurine.cloudx.estate.dict;

/**
 * @ClassName: DictResult
 * @author: 王良俊 <>
 * @date: 2020年08月20日 上午09:57:26
 * @Copyright:
 */
public class DictResult {

    /**
     * 是否成功找到字典项
     */
    private boolean success = false;

    /**
     * 字典对应的code
     */
    private String code;

    /**
     * 字典对应的标签
     */
    private String label;

    /**
     * <p>
     * 返回是否成功找到字典项
     * </p>
     *
     * @return 字典查询结果
     */
    public boolean isSuccess() {
        return success;
    }

    public static DictResult success(String codeValue) {
        DictResult dictResult = new DictResult();
        dictResult.code = codeValue;
        dictResult.success = true;
        return dictResult;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
