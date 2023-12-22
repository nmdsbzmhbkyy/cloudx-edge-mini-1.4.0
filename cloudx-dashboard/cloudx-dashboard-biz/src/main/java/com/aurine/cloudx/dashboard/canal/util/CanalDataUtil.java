package com.aurine.cloudx.dashboard.canal.util;

import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-24
 * @Copyright:
 */
public class CanalDataUtil {


    /**
     * 获取Canal行列信息
     *
     * @param canalEntry
     * @return
     */
    public static RowChange getRowChange(Entry canalEntry) {
        RowChange rowChage = null;
        try {
            rowChage = RowChange.parseFrom(canalEntry.getStoreValue());
        } catch (Exception e) {
            throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + canalEntry.toString(), e);
        }

        return rowChage;
    }


}
