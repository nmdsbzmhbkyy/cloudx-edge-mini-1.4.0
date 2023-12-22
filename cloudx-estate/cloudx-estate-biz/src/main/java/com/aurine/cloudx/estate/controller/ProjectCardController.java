

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PassRightTokenStateEnum;
import com.aurine.cloudx.estate.entity.ProjectCard;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectCardService;
import com.aurine.cloudx.estate.vo.ProjectCardVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 记录项目卡资源信息，供辖区内已开放通行权限的卡识别设备下载
 *
 * @author 王良俊
 * @date 2020-05-22 11:21:18
 */
@RestController
@RequestMapping("/projectCard")
@Api(value = "projectCard", tags = "记录项目卡资源信息，供辖区内已开放通行权限的卡识别设备下载管理")
public class ProjectCardController {
    private static final String CARD_STATE_NORMAL = "1";
    private static final Object CARD_STATE_LOSS = "2";
//    @Resource
//    private ProjectCardService projectCardService;

    @Resource
    private AbstractProjectCardService abstractProjectCardService;


    /**
     * 分页查询
     *
     * @param page        分页对象
     * @param projectCard 记录项目卡资源信息，供辖区内已开放通行权限的卡识别设备下载
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R fetchList(Page page, ProjectCard projectCard) {
        return R.ok(abstractProjectCardService.page(page, Wrappers.query(projectCard)));
    }


    /**
     * 通过id查询记录项目卡资源信息，供辖区内已开放通行权限的卡识别设备下载
     *
     * @param cardId 卡片id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{cardId}")
    public R getById(@PathVariable("cardId") String cardId) {
        return R.ok(abstractProjectCardService.getById(cardId));
    }

    /**
     * 根据人员id查询所有卡片
     *
     * @param personId 人员id
     * @return
     */
    @ApiOperation(value = "根据人员id查询所有卡片", notes = "根据人员id查询所有卡片")
    @GetMapping("/list/{personId}")
    public R listProjectCardByPersonId(@PathVariable("personId") String personId) {
        return R.ok(abstractProjectCardService.list(new QueryWrapper<ProjectCard>().lambda().eq(ProjectCard::getPersonId, personId)));
    }


    /**
     * 新增记录项目卡资源信息，供辖区内已开放通行权限的卡识别设备下载
     *
     * @param projectCard 记录项目卡资源信息，供辖区内已开放通行权限的卡识别设备下载
     * @return R
     */
    @ApiOperation(value = "新增记录项目卡资源信息", notes = "新增记录项目卡资源信息")
    @SysLog("新增卡片资源信息")
    @PostMapping
    public R save(@RequestBody ProjectCard projectCard) {
        //设置状态为使用中
        projectCard.setStatus(PassRightTokenStateEnum.USED.code);
        //设置卡片状态为正常
        projectCard.setCardStatus(CARD_STATE_NORMAL);
//        /**
//         * 调用WR20接口，执行添加卡片
//         * @since 2020-08-07
//         * @author: 王伟
//         */
//        WR20Factory.getFactoryInstance().getRightService(ProjectContextHolder.getProjectId()).addCard(ProjectContextHolder.getProjectId(), projectCard);

        return R.ok(abstractProjectCardService.saveCard(projectCard));
    }
    /**
     * 检查卡号是否已存在
     *
     * @param cardNo
     * @return R
     */
    @ApiOperation(value = "新增记录项目卡资源信息", notes = "新增记录项目卡资源信息")
    @SysLog("新增卡片资源信息")
    @GetMapping("/checkCardIsExist/{cardNo}")
    public R checkCardIsExist(@PathVariable String cardNo) {
        ProjectCard byCardNo = abstractProjectCardService.getByCardNo(cardNo, ProjectContextHolder.getProjectId());
        if (byCardNo != null && StringUtil.isNotEmpty(byCardNo.getPersonId())) {
            return R.failed("当前卡号已被其他用户使用");
        } else {
            return R.ok();
        }
    }



    /**
     * 修改记录项目卡资源信息，供辖区内已开放通行权限的卡识别设备下载
     *
     * @param projectCard 记录项目卡资源信息，供辖区内已开放通行权限的卡识别设备下载
     * @return R
     */
    @ApiOperation(value = "通过cardId更新", notes = "通过cardId更新")
    @SysLog("修改卡片信息")
    @PutMapping
    public R updateById(@RequestBody ProjectCard projectCard) {
        return R.ok(abstractProjectCardService.updateCard(projectCard));
    }

    /**
     * 通过卡片id删除
     *
     * @param cardId 卡片id
     * @return R
     */
    @ApiOperation(value = "通过卡片id删除", notes = "通过卡片id删除")
    @SysLog("通过seq序列删除某张通行卡片（设置为未使用)")
    @DeleteMapping("/{cardId}")
    public R removeById(@PathVariable String cardId) {
//        ;
//        ProjectCard card = projectCardService.getById(cardId);
//
//        /**
//         * 调用WR20接口，删除卡
//         * @since 2020-08-07
//         * @author: 王伟
//         */
//        WR20Factory.getFactoryInstance().getRightService(ProjectContextHolder.getProjectId()).delCard(ProjectContextHolder.getProjectId(), card);
//
//
//        card.setCardId(cardId);
//        card.setStatus(PassRightTokenStateEnum.UNUSE.code);
//        card.setPersonType("");
//        card.setPersonId("");
//        //如果是实时删除数据库中的卡片与人员的关系则要删除其与设备的关p
//        webProjectRightDeviceService.removeCertmedia(cardId);
//        return R.ok(projectCardService.updateById(card));
        return R.ok(abstractProjectCardService.delCard(cardId));
    }

    /**
     * 根据卡号查询归属人
     *
     * @param cardNo
     * @return
     */
    @ApiOperation(value = "根据卡号查询归属人", notes = "根据卡号查询归属人")
    @SysLog("根据卡号查询归属人")
    @GetMapping("getCardAttribution/{cardNo}")
    public R getCardAttribution(@PathVariable("cardNo") String cardNo) {
        return R.ok(abstractProjectCardService.getCardAttribution(cardNo));
    }

    /**
     * 分页查询门禁卡
     *
     * @param projectCardVo
     * @return
     */
    @ApiOperation(value = "分页查询门禁卡", notes = "分页查询门禁卡")
    @SysLog("分页查询门禁卡")
    @GetMapping("/cardPage")
    public R cardPage(Page page,ProjectCardVo projectCardVo) {
        return R.ok(abstractProjectCardService.cardPage(page,projectCardVo));
    }

    /**
     * 挂失卡
     *
     * @return
     */
    @ApiOperation(value = "挂失卡", notes = "挂失卡")
    @SysLog("挂失卡")
    @PostMapping("/loseCard/{cardNo}")
    public R loseCard(@PathVariable("cardNo") String cardNo) {
        return abstractProjectCardService.loseCard(cardNo);
    }

    /**
     *  解挂卡
     *
     * @return
     */
    @ApiOperation(value = "解挂卡", notes = "解挂卡")
    @SysLog("解挂卡")
    @PostMapping("/obtainCard/{cardNo}/{readCardNo}/{phone}")
    public R obtainCard(@PathVariable("cardNo") String cardNo,@PathVariable("readCardNo") String readCardNo,@PathVariable("phone") String phone) {
        return abstractProjectCardService.obtainCard(cardNo,readCardNo,phone);
    }


    /**
     *  注销卡
     *
     * @return
     */
    @ApiOperation(value = "注销卡", notes = "注销卡")
    @SysLog("注销卡")
    @PostMapping("/removeCard/{cardNo}")
    public R removeCard(@PathVariable("cardNo") String cardNo) {
        return abstractProjectCardService.removeCard(cardNo);
    }

    /**
     *  换卡
     *
     * @return
     */
    @ApiOperation(value = "换卡", notes = "换卡")
    @SysLog("换卡")
    @PostMapping("/changeCard/{oldCardNo}/{newCardNo}")
    public R changeCard(@RequestBody ProjectCardVo projectCardVo,@PathVariable("oldCardNo") String oldCardNo,@PathVariable("newCardNo") String newCardNo) {
        return abstractProjectCardService.changeCard(projectCardVo,oldCardNo,newCardNo);
    }

}
