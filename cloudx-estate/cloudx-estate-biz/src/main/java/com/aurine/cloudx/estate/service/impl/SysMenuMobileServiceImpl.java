package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.SysMenuMobile;
import com.aurine.cloudx.estate.mapper.SysMenuMobileMapper;
import com.aurine.cloudx.estate.service.SysMenuMobileService;
import com.aurine.cloudx.estate.vo.SysMenuMobileTreeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.vo.TreeUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 移动端菜单权限表(SysMenuMobile)表服务实现类
 *
 * @author makejava
 * @since 2021-02-07 10:23:24
 */
@Service
public class SysMenuMobileServiceImpl extends ServiceImpl<SysMenuMobileMapper, SysMenuMobile> implements SysMenuMobileService {

    @Override
    public List<SysMenuMobileTreeVo> tree(String type) {
        List<SysMenuMobile> list = list(new QueryWrapper<SysMenuMobile>().lambda()
                .eq(SysMenuMobile::getType, type));
        List<SysMenuMobileTreeVo> treeNodes = new ArrayList<>();
        for (SysMenuMobile sysMenuMobile : list) {
            SysMenuMobileTreeVo treeNode = new SysMenuMobileTreeVo();
            treeNode.setId(sysMenuMobile.getMenuId());
            treeNode.setParentId(sysMenuMobile.getParentId());
            treeNode.setSort(sysMenuMobile.getSort());
            treeNode.setMenuType(sysMenuMobile.getMenuType());
            treeNode.setPermission(sysMenuMobile.getPermission());
            treeNode.setName(sysMenuMobile.getName());
            treeNodes.add(treeNode);
        }
        return TreeUtil.build(treeNodes,-1);
    }

}
