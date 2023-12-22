package com.aurine.cloudx.common.data.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
public class PageParam implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("页号")
    @Min(1)
    private int pageNo = 1;
    @ApiModelProperty("页面大小")
    @Min(1)
    private int pageSize = 10;
    @ApiModelProperty("排序字段")
    private String sort;
    @ApiModelProperty("排序方式 asc/desc")
    private String order;
    @ApiModelProperty("是否查询总数")
    private boolean searchCount = true;

    public String getSort() {
        if (!StringUtils.hasLength(sort)) {
            return null;
        }
        return sort;
    }
}
