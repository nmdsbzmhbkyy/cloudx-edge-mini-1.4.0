package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class AppPage<T> extends Page<T> {
    public AppPage(Long current, Long size) {
        super();
        if (size != null) {
            this.setSize(size);
        }
        if (current != null) {
            this.setCurrent(current);
        }
    }
}
