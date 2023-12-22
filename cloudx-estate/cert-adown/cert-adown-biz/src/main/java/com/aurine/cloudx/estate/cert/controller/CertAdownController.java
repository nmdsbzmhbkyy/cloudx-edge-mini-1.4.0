
package com.aurine.cloudx.estate.cert.controller;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.cert.constant.ADownStateEnum;
import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;
import com.aurine.cloudx.estate.cert.service.CertAdownQueueService;
import com.aurine.cloudx.estate.cert.service.CertAdwonService;
import com.aurine.cloudx.estate.cert.vo.CertAdownRequestVO;
import com.aurine.cloudx.estate.cert.vo.R;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


/**
 * @description: 下发请求
 * @author: wangwei
 * @date: 2021/12/14 15:05
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/adown")
@Api(value = "cert-adown-service", tags = "凭证下发服务")
@Slf4j
public class CertAdownController {
    //@Resource
    //private ReceiveService receiveService;

    //@Resource
    //private AdownService adownServiceMultiplexingImpl;

    @Autowired
    private CertAdwonService certAdwonService;
    @Autowired
    private CertAdownQueueService certAdownQueueService;


    /**
     * 接收下发请求
     *
     * @param certAdownRequestVO
     * @return
     */
    @PostMapping
    public R request(@Valid @RequestBody CertAdownRequestVO certAdownRequestVO) {
        log.info("收到下发请求：{}", certAdownRequestVO);
        if (!checkSize(certAdownRequestVO.getBody())) {
            return R.failed("下发请求过大");
        }
        certAdownRequestVO.setRequestId(UUID.randomUUID().toString().replace("-", ""));
        SysCertAdownRequest request = BeanUtil.copyProperties(certAdownRequestVO, SysCertAdownRequest.class);
        certAdownQueueService.add(request);
        return R.ok();
    }


    /**
     * 接收下发请求
     *
     * @param certAdownRequestVOList
     * @return
     */
    @PostMapping("/list")
    public R requestList(@Valid @RequestBody List<CertAdownRequestVO> certAdownRequestVOList) {
        log.info("收到下发请求：{}条", certAdownRequestVOList.size());
        for (CertAdownRequestVO certAdownRequestVO : certAdownRequestVOList) {
            certAdownRequestVO.setRequestId(UUID.randomUUID().toString().replace("-", ""));
            SysCertAdownRequest request = BeanUtil.copyProperties(certAdownRequestVO, SysCertAdownRequest.class);
            certAdownQueueService.add(request);
        }
        return R.ok();
    }


    /**
     * 下载结果变更
     *
     * @param certAdownRequestVO
     * @return
     */
    @PutMapping
    public R update(@RequestBody CertAdownRequestVO certAdownRequestVO) {
        log.info("收到变更请求：{}", certAdownRequestVO);
        SysCertAdownRequest request = BeanUtil.copyProperties(certAdownRequestVO, SysCertAdownRequest.class);
        certAdownQueueService.update(request);
        if (!(ADownStateEnum.SUCCESS.getCode().equals(certAdownRequestVO.getState())
                || ADownStateEnum.FAIL.getCode().equals(certAdownRequestVO.getState()))) {
            return R.failed("请求状态类型错误");
        }
        //返回通讯成功结果
        return R.ok();
    }


    /**
     * 校验请求体容量
     *
     * @param jsonObject
     * @return
     */
    private boolean checkSize(JSONObject jsonObject) {
        //获取json对象大小（utf-8下序列化后体积）
        //校验数据体积是否超标
        return true;
    }

    private SysCertAdownRequest toPo(CertAdownRequestVO certAdownRequestVO) {
        return BeanUtil.copyProperties(certAdownRequestVO, SysCertAdownRequest.class);
    }


}
