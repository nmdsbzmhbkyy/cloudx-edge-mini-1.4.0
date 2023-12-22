package com.aurine.cloudx.estate.cert.remote;

import com.aurine.cloudx.estate.cert.vo.CertAdownRequestVO;
import com.aurine.cloudx.estate.cert.vo.R;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * @author zouyu
 */

@FeignClient(contextId = "remoteCertAdownService", value = "cloudx-estate-biz")
public interface RemoteCertAdownService {

    @PostMapping("/adown")
    R request(@Valid @RequestBody CertAdownRequestVO certAdownRequestVO, @RequestHeader(SecurityConstants.FROM) String var1);

    @PutMapping("/adown")
    R update(@Valid @RequestBody CertAdownRequestVO certAdownRequestVO,@RequestHeader(SecurityConstants.FROM) String var1);

    @PostMapping("/adown/list")
    R requestList(@Valid @RequestBody List<CertAdownRequestVO> certAdownRequestVOList,@RequestHeader(SecurityConstants.FROM) String var1);
}
