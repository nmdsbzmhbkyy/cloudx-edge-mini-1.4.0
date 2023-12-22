package com.aurine.cloudx.estate.thirdparty.module.parking.factory;

import com.aurine.cloudx.estate.constant.enums.ParkingAPICompanyEnum;
import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.service.ProjectParkingInfoService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.config.SfirmConstant;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 工厂生成器,根据车场选择工厂实例
 *
 * @ClassName: ParkingFactoryProducer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-29 9:01
 * @Copyright:
 */
@Component
public class ParkingFactoryProducer implements ApplicationContextAware {

    private static ProjectParkingInfoService projectParkingInfoService;

    public static AbstractParkingFactory getFactory(String parkingId) {

        //根据车场ID,获取车场对象
        ProjectParkingInfo parkingInfo = projectParkingInfoService.getById(parkingId);

        VersionEnum versionEnum = VersionEnum.V1;

        if (parkingInfo.getCompany().equalsIgnoreCase(ParkingAPICompanyEnum.SFIRM.value)) {
            versionEnum = VersionEnum.getByNumber(SfirmConstant.version);
            return ParkingSfirmFactory.getFactory(parkingInfo, versionEnum);
        } else if (parkingInfo.getCompany().equalsIgnoreCase(ParkingAPICompanyEnum.FUJICA.value)) {
            versionEnum = VersionEnum.getByNumber(SfirmConstant.version);
            return ParkingFujicaFactory.getFactory(parkingInfo, versionEnum);
        } else if(parkingInfo.getCompany().equalsIgnoreCase(ParkingAPICompanyEnum.REFORMER.value)){
            versionEnum = VersionEnum.getByNumber(SfirmConstant.version);
            return ParkingReformerFactory.getFactory(parkingInfo, versionEnum);
        }else {
            throw new RuntimeException(parkingInfo.getParkName() + "尚未对接车场系统");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        projectParkingInfoService = applicationContext.getBean(ProjectParkingInfoService.class);
    }
}
