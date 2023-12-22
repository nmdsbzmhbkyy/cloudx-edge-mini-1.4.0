package com.aurine.cloudx.estate.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ZipUtil;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.edge.licenseclient.LicenseFileUtil;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/** 系统信息
 * @author Administrator
 */
public interface SysInfoService {

    /**
     *  校验授权文件
     */
    public R verifiedAll() ;

    /**
     * 上传授权文件
     * @param multipartFile
     * @return
     */
    public  R  uploadFile(MultipartFile multipartFile) ;



}
