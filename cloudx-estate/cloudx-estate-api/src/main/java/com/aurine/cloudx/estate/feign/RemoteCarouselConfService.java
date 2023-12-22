package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.entity.ProjectCarouselConf;
import com.aurine.cloudx.estate.vo.ProjectCarouselConfQuery;
import com.aurine.cloudx.estate.vo.ProjectCarouselConfVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 * @ClassName: RemoteCarouselConfService
 * @author: 王良俊 <>
 * @date:  2021年01月13日 上午11:02:41
 * @Copyright:
*/
@FeignClient(contextId = "remoteCarouselConfService", value = "cloudx-estate-biz")
public interface RemoteCarouselConfService {
    /**
     * <p>
     *  根据资讯ID获取资讯数据
     * </p>
     *
     * @param infoId 资讯ID
    */
    @GetMapping({"/projectCarouselConf/{infoId}"})
    R<ProjectCarouselConf> selectOne(@PathVariable("infoId") String infoId);

    /**
     * <p>
     * 分页查询资讯信息
     * </p>
     *
     * @param infoId 资讯ID
     */
    @GetMapping({"/projectCarouselConf/page"})
    R<Page<ProjectCarouselConfVo>> carouselConfPage(@SpringQueryMap Map<String,Object> map);
    /**
     * <p>
     *  新增资讯
     * </p>
     *
     * @param projectCarouselConf 资讯po对象
    */
    @PostMapping({"/projectCarouselConf"})
    R<Boolean> insert(@RequestBody ProjectCarouselConf projectCarouselConf);

    /**
     * <p>
     *  修改资讯
     * </p>
     *
     * @param projectCarouselConf 资讯对象
    */
    @PutMapping({"/projectCarouselConf"})
    R<Boolean> update(@RequestBody ProjectCarouselConf projectCarouselConf);

    /**
     * <p>
     *  根据资讯ID删除资讯
     * </p>
     *
     * @param infoId 资讯ID
    */
    @DeleteMapping({"/projectCarouselConf/remove/{infoId}"})
    R<Boolean> delete(@PathVariable("infoId") String infoId);

}
