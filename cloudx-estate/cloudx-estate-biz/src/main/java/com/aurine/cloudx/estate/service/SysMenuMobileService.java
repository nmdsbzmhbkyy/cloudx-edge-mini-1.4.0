package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.SysMenuMobile;
import com.aurine.cloudx.estate.vo.SysMenuMobileTreeVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 移动端菜单权限表(SysMenuMobile)表服务接口
 *
 * @author makejava
 * @since 2021-02-07 10:23:23
 */
public interface SysMenuMobileService extends IService<SysMenuMobile> {

    List<SysMenuMobileTreeVo> tree(String type);
}
