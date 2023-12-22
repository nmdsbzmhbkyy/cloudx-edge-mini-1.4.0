

package com.aurine.cloudx.open.origin.service;


import com.aurine.cloudx.open.origin.entity.ProjectParkingPlace;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 车位
 *
 * @author 王伟
 * @date 2020-05-08 10:24:42
 */
public interface ProjectParkingPlaceService extends IService<ProjectParkingPlace> {

    List<ProjectParkingPlace> getByPersonId(String personId);

}
