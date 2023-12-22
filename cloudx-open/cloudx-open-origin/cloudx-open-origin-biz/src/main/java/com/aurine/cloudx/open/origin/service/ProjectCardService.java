package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.common.entity.vo.CardInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectCard;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 记录项目卡资源信息，供辖区内已开放通行权限的卡识别设备下载
 *
 * @author 王良俊
 * @date 2020-05-22 11:21:18
 */
public interface ProjectCardService extends IService<ProjectCard> {
    /**
     * 保存卡片
     *
     * @param card
     * @return
     * @author: 王伟
     */
    boolean saveCard(ProjectCard card);

    /**
     * 保存或更新卡片，如果已卡片被占用，则更新为最新的人员
     *
     * @param card
     * @return
     * @author: 王伟
     */
    ProjectCard saveOrUpdateCard(ProjectCard card);

    /**
     * 修改卡片
     *
     * @param card
     * @return
     * @author: 王伟
     */
    boolean updateCard(ProjectCard card);

    /**
     * 根据卡号获取当前项目、租户下的卡片实体
     *
     * @param cardId
     * @return
     */
    ProjectCard getByCardId(String cardId);

    /**
     * <p>
     * 根据用户id获取该用户的所有卡片列表
     * </p>
     *
     * @param personId 人员id
     * @return 卡片对象列表
     * @author: 王良俊
     */
    List<ProjectCard> listByPersonId(String personId);

    /**
     * <p>
     * 根据用户id列表获取该列表用户的所有卡片
     * </p>
     *
     * @param personIdList 人员id列表
     * @return 卡片对象列表
     * @author: 王良俊
     */
    List<ProjectCard> listByPersonIdList(List<String> personIdList);

    /**
     * <p>
     *
     * </p>
     *
     * @param personId 人员id
     * @return 处理结果
     * @throws
     * @author: 王良俊
     */
    boolean updateCardBatch(String personId, List<String> cardIdList);

    /**
     * <p>
     * 根据用户id和卡片更新卡片所属(这里的卡片已经要是确认没有用户使用)
     * </p>
     *
     * @param personId 人员id
     * @return
     * @throws
     * @author: 王良俊
     */
    boolean updateCardById(String personId, String cardIdList);


    /**
     * 根据第三方id和项目编号，获取卡片
     *
     * @param code
     * @param projectId
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    ProjectCard getByCode(String code, int projectId);

    /**
     * 根据卡号和项目编号，获取卡片
     *
     * @param cardNo
     * @param projectId
     * @return
     * @author: 王伟
     * @since 2020-11-30
     */
    ProjectCard getByCardNo(String cardNo, int projectId);

    /**
     * <p>
     * 解绑该人员的全部卡片所属关系
     * </p>
     *
     * @param personId 人员id
     * @return 处理结果
     * @author: 王良俊
     */
    boolean removeByPersonId(String personId);

    /**
     * 删除卡
     *
     * @param cardId
     * @return
     */
    boolean delCard(String cardId);

    /**
     * <p>
     * 根据传入的卡片列表和访客id判断哪些卡片被删除或添加并进行对应操作
     * </p>
     *
     * @param cardList  卡片对象列表
     * @param visitorId 访客id
     * @author: 王良俊
     */
    void operateCard(List<ProjectCard> cardList, String visitorId);

    /**
     * 分页查询
     *
     * @param page
     * @param vo
     * @return
     */
    Page<CardInfoVo> page(Page page, CardInfoVo vo);

}
