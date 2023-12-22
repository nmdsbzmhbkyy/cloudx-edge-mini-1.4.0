/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.aurine.cloudx.open.origin.vo;

import lombok.Data;

/**
 * 第三方系统配置
 *
 * @author 王伟
 * @date 2020-12-15 13:39:38
 */
@Data
public class ProjectDockModuleConfWR20Vo extends ProjectDockModuleConfBaseVo {

    /**
     * WR20网关sn
     */
    private String sn;

    /**
     * WR20对接版本
     */
    private String version;

    /**
     * WR20第三方code
     */
    private String thirdCode;

}
