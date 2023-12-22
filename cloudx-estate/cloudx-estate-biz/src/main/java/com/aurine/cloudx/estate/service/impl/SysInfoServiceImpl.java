package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.RoleEnum;
import com.aurine.cloudx.estate.entity.SystemInfo;
import com.aurine.cloudx.estate.service.SysDeptUserService;
import com.aurine.cloudx.estate.service.SysInfoService;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.estate.util.SerialNumberUtil;
import com.aurine.cloudx.estate.vo.SysUserVo;
import com.aurine.edge.common.LicenseInfoVO;
import com.aurine.edge.licenseclient.EdgeLicenseManager;
import com.aurine.edge.licenseclient.LicenseFileUtil;
import com.codingapi.tx.annotation.TxTransaction;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.feign.RemoteRoleService;
import com.pig4cloud.pigx.admin.api.vo.SysRoleVO;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


/**
 * @author Administrator
 */
@Service
@Slf4j
@RefreshScope
public class SysInfoServiceImpl implements SysInfoService {
    @Resource
    DataSource dataSource;
    @Resource
    RemoteRoleService remoteRoleService;
    @Resource
    SerialNumberUtil serialNumberUtil;
    @Value("${licenseFilePath:C:\\license1\\}")
    String licenseFilePath;
    @Value("${sqlPath:sql\\}")
    String sqlPath;
    @Value("${server.local-project-id}")
    private Integer projectId;
    @Resource
    SysDeptUserService sysDeptUserService;
    private ExecutorService pool = Executors.newFixedThreadPool(1);

    public boolean verify(SystemInfo systemInfo, LicenseInfoVO licenseInfoVO) {
        boolean snVerify = systemInfo.getSn().equalsIgnoreCase(licenseInfoVO.getEdgeGatewaySn());
        boolean cpuIdVerify = systemInfo.getCpuId().equalsIgnoreCase(licenseInfoVO.getFirmwareInfo().getCpuSerial()) || StringUtil.isEmpty(licenseInfoVO.getFirmwareInfo().getCpuSerial());
        // 登记的
        List<String> macAddress = licenseInfoVO.getFirmwareInfo().getMacAddress().stream().map(item->{
            if(item.indexOf(":")!=-1) {
                item.replace(":","");
            }
            if(item.indexOf("-")!=-1) {
                item.replace("-","");
            }
            return item;
        }).collect(Collectors.toList());
        // 本地的
        List<String> strings = ListUtil.toList(systemInfo.getMac().split(",")).stream().map(item->{
            if(item.indexOf(":")!=-1) {
                item.replace(":","");
            }
            if(item.indexOf("-")!=-1) {
                item.replace("-","");
            }
            return item;
        }).collect(Collectors.toList());
        boolean macVerify = false;
        if(ObjectUtil.isNull(macAddress) || macAddress.size() == 0 ) {
            macVerify = true;
        }else {
            for (String macAddressString : strings) {
                if (macAddress.contains(macAddressString)) {
                    macVerify = true;
                    break;
                }
            }
        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime endDate = LocalDateTime.parse(licenseInfoVO.getEndDate() + " 23:59:59", df);

        if (!(macVerify || cpuIdVerify)) {
            throw new RuntimeException("mac地址或者cpu序列不匹配");
        }

        if (!snVerify) {
            throw new RuntimeException("sn不匹配");
        }

        if (!LocalDateTime.now().isBefore(endDate)) {
            throw new RuntimeException("授权文件已过期");
        }

        return (macVerify || cpuIdVerify) && snVerify && LocalDateTime.now().isBefore(endDate);
    }

    @SneakyThrows
    @Override

    public R verifiedAll() {
        String systemInfoCache = (String) RedisUtil.get("systemInfo");
        if(systemInfoCache!=null) {

        }
        Map<String, String> allSn = serialNumberUtil.getAllSn();

        SystemInfo systemInfo = JSONObject.parseObject(JSON.toJSONString(allSn), SystemInfo.class);

        LicenseFileUtil.setFilePath(licenseFilePath);
        EdgeLicenseManager edgeLicenseManager = EdgeLicenseManager.of(null, (a) -> {
            return verify(systemInfo, a);
        }, true);
        if (edgeLicenseManager.isError()) {
            log.error(edgeLicenseManager.getErrorMessage());
            this.deleteFiles();
            return R.failed(edgeLicenseManager.getErrorMessage());

        } else {
            try {
                LicenseInfoVO licenseInfoVO = edgeLicenseManager.getContent();
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                systemInfo.setStartDate(LocalDateTime.parse(licenseInfoVO.getStartDate() + " 00:00:00", df));
                systemInfo.setEndDate(LocalDateTime.parse(licenseInfoVO.getEndDate() + " 23:59:59", df));
                systemInfo.setVersionCode(licenseInfoVO.getVersionInfo().getVersionCode());
                String versionCode = (String) RedisUtil.get("versionCode");
                // 版本号为空就说明没有执行过脚本需要执行脚本,或者与授权文件内版本不一致需要重新执行脚本
                if (ObjectUtil.isNull(versionCode) || !versionCode.equals(systemInfo.getVersionCode())) {
                    pool.submit(() -> {
                        boolean role = this.createRole(projectId);
                        // 20230712 菜单初始化由install脚本执行，不需执行这个sql脚本
                        //boolean b = this.doExecuteSql(systemInfo.getVersionCode());
                        //if(role && b) {
                        if(role) {
                            RedisUtil.set("versionCode", systemInfo.getVersionCode());
                        }
                    });
                }
                RedisUtil.set("systemInfo", JSONObject.toJSONString(systemInfo), 60 * 60 * 24);
                log.info("校验成功systemInfo:{}", systemInfo);

            } catch (Exception var3) {
                var3.printStackTrace();
                return R.failed(var3.getMessage());
            }


        }
        return R.ok(JSONObject.toJSONString(systemInfo));
    }

    public boolean createRole(Integer deptId) {
        List<RoleEnum> enumList = RoleEnum.getEnumList();
        List<SysRoleVO> sysRoles = new ArrayList<>();
        enumList.forEach(item -> {
            SysRoleVO sysRole = new SysRoleVO();
            BeanUtils.copyProperties(item, sysRole);
            sysRole.setDeptId(deptId);
            sysRoles.add(sysRole);
        });
        R<Boolean> flag = remoteRoleService.innerSave(sysRoles, SecurityConstants.FROM_IN);
        if (null != flag && flag.getData()) {
            enumList.forEach(item -> {
                SysUserVo sysUserVo = new SysUserVo();
                sysUserVo.setPhone(item.getPhone());
                sysUserVo.setDeptId(deptId);
                sysUserVo.setDeptTypeId("3");
                sysUserVo.setTrueName(item.getRoleName());
                sysUserVo.setRoleId(item.getRoleId());
                sysUserVo.setUsername(item.getPhone());
                sysDeptUserService.addUserAndRoleInner(sysUserVo);
            });
        }
        return true;
    }

    public boolean doExecuteSql(String versionCode) {

        //通过数据源获取数据库链接
        Connection connection = DataSourceUtils.getConnection(dataSource);
        //创建脚本执行器
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        //创建字符输出流，用于记录SQL执行日志
        StringWriter writer = new StringWriter();
        PrintWriter print = new PrintWriter(writer);
        //设置执行器日志输出
        scriptRunner.setLogWriter(print);
        //设置执行器错误日志输出
        scriptRunner.setErrorLogWriter(print);
        //设置读取文件格式
        Resources.setCharset(StandardCharsets.UTF_8);


        Reader reader = null;
        ClassPathResource classPathResource = new ClassPathResource(sqlPath + versionCode + ".sql");

//        File file = ResourceUtils.getFile("classpath:" + sqlPath + "Lite.sql");
//            File file = new File(sqlPath + versionCode + ".sql");
        //获取资源文件的字符输入流
        try {
            if (classPathResource != null) {
                reader = new InputStreamReader(classPathResource.getStream(), "UTF-8");
            } else {
                throw new RuntimeException("sql文件不存在");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //执行SQL脚本
        scriptRunner.runScript(reader);
        //关闭文件输入流
        try {
            reader.close();
        } catch (IOException e) {
            log.info(e.getMessage(), e);
        }

        //输出SQL执行日志
        log.info(writer.toString());
        //关闭输入流
        scriptRunner.closeConnection();
        log.info("初始化菜单成功");
        // 标记已执行过sql脚本
        return true;
    }
//
//    public static void main(String[] args) throws Exception {
//        ClassPathResource classPathResource = new ClassPathResource("sql\\Lite.sql");
//        classPathResource.getStream()
//        File file = ResourceUtils.getFile("classpath:sql\\Lite.sql");
//        System.out.println(file.getAbsolutePath());
//    }

    @Override
    public R uploadFile(MultipartFile multipartFile) {
        LicenseFileUtil.setFilePath(licenseFilePath);
        Integer fileSize = 2;
        String licenseName = "license.lic";
        String keystoreName = "publicKey.keystore";
        if (multipartFile == null) {
            R.failed("文件不能为空");
        }

        //获取文件的原名称 getOriginalFilename
        String originalFilename = multipartFile.getOriginalFilename();
        if (!originalFilename.contains(".zip")) {
            R.failed("请上传zip文件");
        }
        //获取时间戳和文件的扩展名，拼接成一个全新的文件名； 用时间戳来命名是为了避免文件名冲突
        //定义文件存放路径
        String filePath = LicenseFileUtil.getFilePath();
        //新建一个目录（文件夹）
        File dest = new File(filePath + originalFilename);
        boolean isDirectory = dest.getParentFile().isDirectory();
        if (isDirectory) {
            FileUtil.del(dest.getParentFile().getAbsolutePath());
        }
        //判断filePath目录是否存在，如不存在，就新建一个
        if (!dest.getParentFile().canExecute()) {
            dest.getParentFile().mkdirs(); //新建一个目录
        }
        try {
            //文件输出
            multipartFile.transferTo(dest);
            File unzip = ZipUtil.unzip(dest, dest.getParentFile(), Charset.forName("GBK"));
            List<String> strings = ListUtil.toList(unzip.list());
            if (!strings.contains(licenseName) || !strings.contains(keystoreName)) {
                this.deleteFiles();
                return R.failed("文件校验不通过");
            }

        } catch (Exception e) {
            e.printStackTrace();
            //拷贝失败要有提示
            return R.failed("出现异常，请联系管理员");
        } finally {
            if (dest.exists()) {
                dest.delete();
            }
        }
        return verifiedAll();
    }

    void deleteFiles() throws IOException {
        File file = new File(licenseFilePath);
        if (file.exists() && file.isDirectory()) {
            Path path = Paths.get(licenseFilePath);
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                        // 先去遍历删除文件
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            Files.delete(file);
                            return FileVisitResult.CONTINUE;
                        }

                        // 再去遍历删除目录
                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                            Files.delete(dir);
                            return FileVisitResult.CONTINUE;
                        }
                    }
            );
        }
    }
}
