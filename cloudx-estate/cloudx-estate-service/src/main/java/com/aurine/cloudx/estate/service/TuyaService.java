

package com.aurine.cloudx.estate.service;

import com.tuya.api.client.user.Models.SyncUserVO;

/**
 *
 *
 * @author 黄阳光
 * @date 2020-06-04 08:31:21
 */
public interface TuyaService {
   String syncUser(SyncUserVO user);

   String syncUser(SyncUserVO user, String schema);
}
