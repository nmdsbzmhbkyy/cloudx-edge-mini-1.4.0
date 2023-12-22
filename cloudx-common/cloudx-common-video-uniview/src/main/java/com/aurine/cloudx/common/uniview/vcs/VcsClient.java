package com.aurine.cloudx.common.uniview.vcs;

import com.aurine.cloudx.common.uniview.model.VcsRequest;
import com.aurine.cloudx.common.uniview.model.VcsResponse;

/**
 * 宇视API客户端
 */
public interface VcsClient {
    <T extends VcsResponse> T getVcsResponse(VcsRequest<T> request) throws Exception;
}
