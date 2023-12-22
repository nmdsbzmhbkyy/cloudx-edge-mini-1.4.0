
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.config.SzztConfigurationProperties;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.SzztMapper;
import com.aurine.cloudx.estate.service.SzztService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报警事件处理
 *
 * @author 黄阳光
 * @date 2020-06-04 08:31:21
 */
@Service
@Slf4j
public class SzztServiceImpl implements SzztService {

    @Resource
    private SzztMapper szztMapper;

    @Resource
    private SzztConfigurationProperties config;

    private final static String URL_SAVE = "http://120.40.102.247:9200/eUrbanMIS/release/datamgrapi/save/";

    private final static String DELETE_SAVE = "http://120.40.102.247:9200/eUrbanMIS/release/datamgrapi/delete/";

    @Override
    public Szzt6193 findTable6193(String projectId) {
        // 填充默认参数
        Szzt6193 projectInfo = szztMapper.findTable6193(projectId);
        projectInfo.setHUMAN_ID(config.getHUMAN_ID());
        projectInfo.setVILLAGENO(config.getHUMAN_ID() + projectInfo.getVILLAGENO());

        Map<String, Szzt6193> projectConfig = config.getTable_6193();

        Szzt6193 c = projectConfig.get(projectId);
        Szzt6193 dc = projectConfig.get("default");

        if (config != null) {
            try {
                projectInfo.setValue(c);
                projectInfo.setValue(dc);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return projectInfo;
    }

    @Override
    public List<Szzt6194> findTable6194(String projectId) {
        // 填充默认参数
        List<Szzt6194> list = szztMapper.findTable6194(projectId);

        Map<String, Szzt6194> projectConfig = config.getTable_6194();

        Szzt6194 c = projectConfig.get(projectId);
        Szzt6194 dc = projectConfig.get("default");

        for (Szzt6194 projectInfo : list) {
            projectInfo.setHUMAN_ID(config.getHUMAN_ID());
            projectInfo.setBUILDINGNO(config.getHUMAN_ID() + projectInfo.getBUILDINGNO());
            projectInfo.setVILLAGENO(config.getHUMAN_ID() + projectInfo.getVILLAGENO());

            if (config != null) {
                try {
                    if (c != null) {
                        projectInfo.setValue(c);
                    }
                    if (dc != null) {
                        projectInfo.setValue(dc);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    @Override
    public List<Szzt6195> findTable6195(String projectId) {
        // 填充默认参数
        List<Szzt6195> list = szztMapper.findTable6195(projectId);

        Map<String, Szzt6195> projectConfig = config.getTable_6195();

        Szzt6195 c = projectConfig.get(projectId);
        Szzt6195 dc = projectConfig.get("default");

        Map<String, Integer> flag = new HashMap<>();

        for (Szzt6195 projectInfo : list) {
            projectInfo.setHUMAN_ID(config.getHUMAN_ID());
            projectInfo.setUNITNO(config.getHUMAN_ID() + projectInfo.getUNITNO());
            projectInfo.setBUILDINGNO(config.getHUMAN_ID() + projectInfo.getBUILDINGNO());

            // 设置单元编号
            if (flag.containsKey(projectInfo.getBUILDINGNO())) {
                Integer num = flag.get(projectInfo.getBUILDINGNO()) + 1;
                projectInfo.setUNITNUM(num);

                flag.put(projectInfo.getBUILDINGNO(), num);
            } else {
                flag.put(projectInfo.getBUILDINGNO(), 1);
                projectInfo.setUNITNUM(1);
            }

            if (config != null) {
                try {
                    if (c != null) {
                        projectInfo.setValue(c);
                    }
                    if (dc != null) {
                        projectInfo.setValue(dc);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    @Override
    public List<Szzt6196> findTable6196(String projectId) {
        // 填充默认参数
        List<Szzt6196> list = szztMapper.findTable6196(projectId);

        Map<String, Szzt6196> projectConfig = config.getTable_6196();

        Szzt6196 c = projectConfig.get(projectId);
        Szzt6196 dc = projectConfig.get("default");

        for (Szzt6196 projectInfo : list) {
            projectInfo.setHUMAN_ID(config.getHUMAN_ID());
            projectInfo.setHOUSENO(config.getHUMAN_ID() + projectInfo.getHOUSENO());
            projectInfo.setUNITNO(config.getHUMAN_ID() + projectInfo.getUNITNO());

            if (StringUtil.isNotEmpty(projectInfo.getIDENTITYID())) {
                projectInfo.setAGE(evaluateAge(projectInfo.getIDENTITYID()));
            }

            if (config != null) {
                try {
                    if (c != null) {
                        projectInfo.setValue(c);
                    }
                    if (dc != null) {
                        projectInfo.setValue(dc);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    @Override
    public List<Szzt6197> findTable6197(String projectId) {
        // 填充默认参数
        List<Szzt6197> list = szztMapper.findTable6197(projectId);

        Map<String, Szzt6197> projectConfig = config.getTable_6197();

        Szzt6197 c = projectConfig.get(projectId);
        Szzt6197 dc = projectConfig.get("default");

        for (Szzt6197 projectInfo : list) {
            projectInfo.setHUMAN_ID(config.getHUMAN_ID());
            projectInfo.setHOUSENO(config.getHUMAN_ID() + projectInfo.getHOUSENO());
            projectInfo.setRELNATION("0" + projectInfo.getRELNATION());

            if (StringUtil.isNotEmpty(projectInfo.getIDENTITYID())) {
                projectInfo.setAGE(evaluateAge(projectInfo.getIDENTITYID()));
            }

            if (config != null) {
                try {
                    if (c != null) {
                        projectInfo.setValue(c);
                    }
                    if (dc != null) {
                        projectInfo.setValue(dc);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    @Override
    public List<Szzt6198> findTable6198(String projectId) {
        // 填充默认参数
        List<Szzt6198> list = szztMapper.findTable6198(projectId);

        Map<String, Szzt6198> projectConfig = config.getTable_6198();

        Szzt6198 c = projectConfig.get(projectId);
        Szzt6198 dc = projectConfig.get("default");

        for (Szzt6198 projectInfo : list) {
            projectInfo.setHUMAN_ID(config.getHUMAN_ID());

            StringBuffer stringBuffer = new StringBuffer(projectInfo.getCARNO());
            projectInfo.setCARNO(stringBuffer.insert(2, "·").toString());

            if (config != null) {
                try {
                    if (c != null) {
                        projectInfo.setValue(c);
                    }
                    if (dc != null) {
                        projectInfo.setValue(dc);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    @Override
    public List<Szzt6199> findTable6199(String projectId) {
        // 填充默认参数
        List<Szzt6199> list = szztMapper.findTable6199(projectId);

        Map<String, Szzt6199> projectConfig = config.getTable_6199();

        Szzt6199 c = projectConfig.get(projectId);
        Szzt6199 dc = projectConfig.get("default");

        for (Szzt6199 projectInfo : list) {
            projectInfo.setHUMAN_ID(config.getHUMAN_ID());
            projectInfo.setHOUSENO(config.getHUMAN_ID() + projectInfo.getHOUSENO());

            if (config != null) {
                try {
                    if (c != null) {
                        projectInfo.setValue(c);
                    }
                    if (dc != null) {
                        projectInfo.setValue(dc);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    @Override
    public String pushData(String tableId, String projectId) {
        Map<String, Object> req = new HashMap();

        if ("6193".equals(tableId)) {
            Szzt6193 table = findTable6193(projectId);

            req = BeanUtil.beanToMap(table, false, true);

            String result = HttpUtil.createPost(URL_SAVE + tableId).form(req).execute().body();
            log.info(result);

            return result;
        } else if ("6194".equals(tableId)) {
            List<Szzt6194> table = findTable6194(projectId);
            StringBuffer results = new StringBuffer();

            for (Szzt6194 data : table) {
                req = BeanUtil.beanToMap(data, false, true);
                String result = HttpUtil.createPost(URL_SAVE + tableId).form(req).execute().body();

                log.info(result);
                results.append(result);
            }

            return results.toString();
        } else if ("6195".equals(tableId)) {
            List<Szzt6195> table = findTable6195(projectId);
            StringBuffer results = new StringBuffer();

            for (Szzt6195 data : table) {
                req = BeanUtil.beanToMap(data, false, true);
                String result = HttpUtil.createPost(URL_SAVE + tableId).form(req).execute().body();

                log.info(result);
                results.append(result);
            }

            return results.toString();
        } else if ("6196".equals(tableId)) {
            List<Szzt6196> table = findTable6196(projectId);
            StringBuffer results = new StringBuffer();

            for (Szzt6196 data : table) {
                req = BeanUtil.beanToMap(data, false, true);
                String result = HttpUtil.createPost(URL_SAVE + tableId).form(req).execute().body();

                log.info(result);
                results.append(result);
            }

            return results.toString();
        } else if ("6197".equals(tableId)) {
            List<Szzt6197> table = findTable6197(projectId);
            StringBuffer results = new StringBuffer();

            for (Szzt6197 data : table) {
                req = BeanUtil.beanToMap(data, false, true);
                String result = HttpUtil.createPost(URL_SAVE + tableId).form(req).execute().body();

                log.info(result);
                results.append(result);
            }

            return results.toString();
        } else if ("6198".equals(tableId)) {
            List<Szzt6198> table = findTable6198(projectId);
            StringBuffer results = new StringBuffer();

            for (Szzt6198 data : table) {
                req = BeanUtil.beanToMap(data, false, true);
                String result = HttpUtil.createPost(URL_SAVE + tableId).form(req).execute().body();

                log.info(result);
                results.append(result);
            }

            return results.toString();
        } else if ("6199".equals(tableId)) {
            List<Szzt6199> table = findTable6199(projectId);
            StringBuffer results = new StringBuffer();

            for (Szzt6199 data : table) {
                req = BeanUtil.beanToMap(data, false, true);
                String result = HttpUtil.createPost(URL_SAVE + tableId).form(req).execute().body();

                log.info(result);
                results.append(result);
            }

            return results.toString();
        }

        return null;
    }

    /**
     * 从身份证计算年龄
     * @param sfzjh
     * @return
     */
    private int evaluateAge(String sfzjh) {
        if (sfzjh.length() != 18) {
            return 0;
        }

        int age = 0;

        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH)+1;
        int dayNow = cal.get(Calendar.DATE);

        int year = Integer.valueOf(sfzjh.substring(6, 10));
        int month = Integer.valueOf(sfzjh.substring(10,12));
        int day = Integer.valueOf(sfzjh.substring(12,14));

        if ((month < monthNow) || (month == monthNow && day<= dayNow) ){
            age = yearNow - year;
        } else {
            age = yearNow - year - 1;
        }

        return age;
    }
}
