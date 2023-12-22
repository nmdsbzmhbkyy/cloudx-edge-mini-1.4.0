package com.aurine.cloudx.estate.util;

import cn.hutool.core.io.FileUtil;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.util.*;


@Component
@Slf4j
@Data
@RefreshScope
public class SerialNumberUtil {
	@Value("${snPath:C:\\machineSN}")
	String snPath ;

	/**
	 * 获取主板序列号
	 * @return
	 */
	public static String getMotherboardSN() {
		String result = "";
		try {
			File file = File.createTempFile("realhowto", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);
 
			String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
					+ "Set colItems = objWMIService.ExecQuery _ \n"
					+ "   (\"Select * from Win32_BaseBoard\") \n"
					+ "For Each objItem in colItems \n"
					+ "    Wscript.Echo objItem.SerialNumber \n"
					+ "    exit for  ' do the first cpu only! \n" + "Next \n";
 
			fw.write(vbs);
			fw.close();
			String path = file.getPath().replace("%20", " ");
			Process p = Runtime.getRuntime().exec(
					"cscript //NoLogo " + path);
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.trim();
	}
 
	/**
	 * 获取硬盘序列号(该方法获取的是 盘符的逻辑序列号,并不是硬盘本身的序列号)
	 * 硬盘序列号还在研究中
	 * @param drive 盘符
	 * @return
	 */
	public static String getHardDiskSN(String drive) {
		String result = "";
		try {
			File file = File.createTempFile("realhowto", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);
 
			String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
					+ "Set colDrives = objFSO.Drives\n"
					+ "Set objDrive = colDrives.item(\""
					+ drive
					+ "\")\n"
					+ "Wscript.Echo objDrive.SerialNumber"; // see note
			fw.write(vbs);
			fw.close();
			String path = file.getPath().replace("%20", " ");
			Process p = Runtime.getRuntime().exec(
					"cscript //NoLogo " + path);
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.trim();
	}
 
	/**
	 * 获取CPU序列号
	 * @return
	 */
	public static String getCPUSerial() {
		String result = "";
		try {
			File file = File.createTempFile("tmp", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);
			String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
					+ "Set colItems = objWMIService.ExecQuery _ \n"
					+ "   (\"Select * from Win32_Processor\") \n"
					+ "For Each objItem in colItems \n"
					+ "    Wscript.Echo objItem.ProcessorId \n"
					+ "    exit for  ' do the first cpu only! \n" + "Next \n";
 
			// + "    exit for  \r\n" + "Next";
			fw.write(vbs);
			fw.close();
			String path = file.getPath().replace("%20", " ");
			Process p = Runtime.getRuntime().exec(
					"cscript //NoLogo " + path);
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
			file.delete();
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		if (result.trim().length() < 1 || result == null) {
			result = "无CPU_ID被读取";
		}
		return result.trim();
	}
	
	private static List<String> getLocalHostLANAddress()	throws UnknownHostException, SocketException {
		List<String> ips = new ArrayList<String>();
		Enumeration<NetworkInterface> interfs = NetworkInterface.getNetworkInterfaces();
		while (interfs.hasMoreElements()) {
			NetworkInterface interf = interfs.nextElement();
			Enumeration<InetAddress> addres = interf.getInetAddresses();
			while (addres.hasMoreElements()) {
				InetAddress in = addres.nextElement();
				if (in instanceof Inet4Address) {
					System.out.println("v4:" + in.getHostAddress());
					if(!"127.0.0.1".equals(in.getHostAddress())){
						ips.add(in.getHostAddress());
					}
				}
			}
		}
		return ips;
	}
	
	/**
	 * MAC
	 * 通过jdk自带的方法,先获取本机所有的ip,然后通过NetworkInterface获取mac地址
	 * @return
	 */
	public static String getMac() {
		try {
			String resultStr = "";
			List<String> ls = getLocalHostLANAddress();

			for(String str : ls){
				InetAddress ia = InetAddress.getByName(str);// 获取本地IP对象
				// 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
				byte[] mac = NetworkInterface.getByInetAddress(ia)
						.getHardwareAddress();
				// 下面代码是把mac地址拼装成String
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < mac.length; i++) {
					if (i != 0) {
						sb.append("-");
					}
					// mac[i] & 0xFF 是为了把byte转化为正整数
					String s = Integer.toHexString(mac[i] & 0xFF);
					sb.append(s.length() == 1 ? 0 + s : s);
				}
				// 把字符串所有小写字母改为大写成为正规的mac地址并返回
				resultStr += sb.toString().toUpperCase()+",";
			}
			int i = resultStr.lastIndexOf(",");
			resultStr = resultStr.substring(0, i );
			return resultStr;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/***************************linux*********************************/
 
	public static String executeLinuxCmd(String cmd)  {
		try {
			System.out.println("got cmd job : " + cmd);
			Runtime run = Runtime.getRuntime();
			Process process;
			process = run.exec(cmd);
			InputStream in = process.getInputStream();
			BufferedReader bs = new BufferedReader(new InputStreamReader(in));
			StringBuffer out = new StringBuffer();
			byte[] b = new byte[8192];
			for (int n; (n = in.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
			
			in.close();
			process.destroy();
			return out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String executeLinuxCmd(String[] cmd)  {
		try {
			System.out.println("got cmd job : " + JSON.toJSONString(cmd));
			Runtime run = Runtime.getRuntime();
			Process process;
			process = run.exec(cmd);
			InputStream in = process.getInputStream();
			BufferedReader bs = new BufferedReader(new InputStreamReader(in));
			StringBuffer out = new StringBuffer();
			byte[] b = new byte[8192];
			for (int n; (n = in.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}

			in.close();
			process.destroy();
			return out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



	/**
	 * 
	 * @param cmd 命令语句
	 * @param record 要查看的字段
	 * @param symbol 分隔符
	 * @return
	 */ 
	public  String getSerialNumber(String cmd ,String record,String symbol) {
		String[] cmds = { "/bin/sh", "-c", cmd };
		String execResult = executeLinuxCmd(cmds);
		String[] infos = execResult.split("\n");

		for(String info : infos) {
			info = info.trim();
			if(info.indexOf(record) != -1) {
				info.replace(" ", "");
				String[] sn = info.split(symbol);
				return sn[1];
			}
		}
		
		return null;
	}



	public  String getCentosMac(String cmd ,String record,String symbol) {
		String[] cmds = { "/bin/sh", "-c", cmd };
		String execResult = executeLinuxCmd(cmds);
		String[] infos = execResult.split("\n");
		StringBuffer stringBuffer =new StringBuffer();
		for(String info : infos) {
			info = info.trim();
			if(info.indexOf(record) != -1) {
				info.replace(" ", "");
				String[] sn = info.split(symbol);
				stringBuffer.append(sn[1]+",");

			}
		}
		String macs = stringBuffer.toString();
		int i = macs.lastIndexOf(",");
		macs = macs.substring(0, i);
		return macs;
	}

	public  String getSn(String filePath) {
		File file = new File(filePath);
		if(!file.exists()) {
			return null;
		}
		BufferedInputStream inputStream = FileUtil.getInputStream(file);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String sn = null;
		try {
			sn = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sn;
	}
	
	/**
	 * 获取CPUID、硬盘序列号、MAC地址、主板序列号
	 * @return
	 */
	public  Map<String, String> getAllSn(){
		String os = System.getProperty("os.name");
		os = os.toUpperCase();
		System.out.println(os);
		
		Map<String, String> snVo = new HashMap<String, String>();
		if("LINUX".equals(os)) {
			System.out.println("=============>for linux");
			String cpuid = 				getSerialNumber("dmidecode -t processor | grep 'ID'", "ID",":");
			System.out.println("cpuid : "+ cpuid.toUpperCase().replace(" ", ""));
//			String mainboardNumber = 	getSerialNumber("dmidecode |grep 'Serial Number'", "Serial Number",":");
//			System.out.println("mainboardNumber : "+ mainboardNumber);
//			String diskNumber = 		getSerialNumber("fdisk -l", "Disk identifier",":");
//			System.out.println("diskNumber : "+ diskNumber);
			String mac = 				getCentosMac("ifconfig | grep enp | head -1 | awk '{print $1}' | xargs ifconfig -a", "ether"," ");
			System.out.println("mac:"+mac.toUpperCase().replace(" ", ""));
			snVo.put("cpuid",cpuid.toUpperCase().replace(" ", ""));
//			snVo.put("diskid",diskNumber.toUpperCase().replace(" ", ""));
			snVo.put("mac",mac.toUpperCase().replace(" ", ""));
//			snVo.put("mainboard",mainboardNumber.toUpperCase().replace(" ", ""));
		}else {
			System.out.println("=============>for windows");
			String cpuid = SerialNumberUtil.getCPUSerial();
//			String mainboard = SerialNumberUtil.getMotherboardSN();
//			String disk = SerialNumberUtil.getHardDiskSN("c");
			String mac = SerialNumberUtil.getMac();
			
			System.out.println("CPU  SN:" + cpuid);
//			System.out.println("主板  SN:" + mainboard);
//			System.out.println("C盘   SN:" + disk);
			System.out.println("MAC  SN:" + mac);


			
			snVo.put("cpuid",cpuid.toUpperCase().replace(" ", ""));
//			snVo.put("diskid",disk.toUpperCase().replace(" ", ""));
			snVo.put("mac",mac.toUpperCase().replace(" ", ""));
//			snVo.put("mainboard",mainboard.toUpperCase().replace(" ", ""));

		}
		System.out.println("SN:" + Optional.ofNullable(getSn(snPath)).orElse("").toUpperCase().replace(" ", ""));
		snVo.put("sn",Optional.ofNullable(getSn(snPath)).orElse("").toUpperCase().replace(" ", ""));
		return snVo;
	}
//	public static boolean verify(SystemInfo systemInfo,LicenseInfoVO licenseInfoVO) {
//		boolean snVerify = systemInfo.getSn().equals(licenseInfoVO.getEdgeGatewaySn());
//		boolean cpuIdVerify = systemInfo.getCpuId().equals(licenseInfoVO.getFirmwareInfo().getCpuSerial());
//		// 登记的
//		List<String> macAddress = licenseInfoVO.getFirmwareInfo().getMacAddress();
//		// 本地的
//		List<String> strings = ListUtil.toList(systemInfo.getMac().split(","));
//		boolean macVerify = false;
//		for (String macAddressString :strings) {
//			 if (macAddress.contains(macAddressString)) {
//				 macVerify = true;
//				 break;
//			 }
//		}
//
//		return (snVerify || snVerify) && macVerify;
//	}
	/**
	 * linux
	 * cpuid : dmidecode -t processor | grep 'ID'
	 * mainboard : dmidecode |grep 'Serial Number'
	 * disk : fdisk -l
	 * mac : ifconfig -a
	 * @param
	 */
//	public static void  verifiedAll() {
//		Map<String, String> allSn = getAllSn();
//
//		SystemInfo systemInfo = JSONObject.parseObject(JSON.toJSONString(allSn), SystemInfo.class);
//		System.out.println(systemInfo.toString());
////		LicenseFileUtil.setFilePath("C:\\license\\");
//		EdgeLicenseManager edgeLicenseManager = EdgeLicenseManager.of(null, (a) -> {
//			return verify(systemInfo,a);
//		}, false);
//		if (edgeLicenseManager.isError()) {
//			System.out.println("错误码为:" + edgeLicenseManager.getErrorCode());
//			System.out.println("错误信息为:" + edgeLicenseManager.getErrorMessage());
//		} else {
//			try {
//				LicenseInfoVO licenseInfoVO = edgeLicenseManager.getContent();
//				DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//				systemInfo.setStartDate(LocalDateTime.parse(licenseInfoVO.getStartDate()+" 00:00:00",df));
//				systemInfo.setEndDate(LocalDateTime.parse(licenseInfoVO.getEndDate()+" 23:59:59",df));
//				RedisUtil.set("systemInfo",JSONObject.toJSONString(systemInfo),1);
//
//				System.out.println(licenseInfoVO);
//
//			} catch (Exception var3) {
//				var3.printStackTrace();
//			}
//		}
//	}
//	public static boolean  uploadFile(MultipartFile multipartFile) {
//		LicenseFileUtil.setFilePath("C:\\license\\");
//		Integer fileSize  = 2;
//		String licenseName = "license.lic";
//		String keystoreName = "publicKey.keystore";
//		if (multipartFile == null){
//			return true;
//		}
//
//		//获取文件的原名称 getOriginalFilename
//		String originalFilename = multipartFile.getOriginalFilename();
//		if(!originalFilename.contains(".zip")) {
//			return false;
//		}
//		//获取时间戳和文件的扩展名，拼接成一个全新的文件名； 用时间戳来命名是为了避免文件名冲突
//		String fileName = "license.zip";
//		//定义文件存放路径
//		String filePath = LicenseFileUtil.getFilePath();
//		//新建一个目录（文件夹）
//		File dest = new File(filePath+originalFilename);
//		//判断filePath目录是否存在，如不存在，就新建一个
//		if (!dest.getParentFile().canExecute()){
//			dest.getParentFile().mkdirs(); //新建一个目录
//		}
//		try
//			{
//			//文件输出
//			multipartFile.transferTo(dest);
//			File unzip = ZipUtil.unzip(dest, Charset.forName("GBK"));
//			List <String> strings = ListUtil.toList(unzip.list());
//			if(strings.size() != fileSize || !strings.contains(licenseName) || !strings.contains(keystoreName) ) {
//				return false;
//			}
//			verifiedAll();
//		}
//
//		catch ( Exception e) {
//			e.printStackTrace();
//			//拷贝失败要有提示
//			return false;
//		}finally {
//			if(dest.exists()) {
//				dest.delete();
//			}
//		}
//		return true;
//	}
//	public static void main(String[] args) {
////		Map<String, String> allSn = getAllSn();
////
////		SystemInfo systemInfo = JSONObject.parseObject(JSON.toJSONString(allSn), SystemInfo.class);
////		System.out.println(systemInfo.toString());
////		LicenseFileUtil.setFilePath("C:\\license\\");
////		EdgeLicenseManager edgeLicenseManager = EdgeLicenseManager.of(null, (a) -> {
////			return verify(systemInfo,a);
////		}, false);
////		if (edgeLicenseManager.isError()) {
////			System.out.println("错误码为:" + edgeLicenseManager.getErrorCode());
////			System.out.println("错误信息为:" + edgeLicenseManager.getErrorMessage());
////		} else {
////			try {
////				LicenseInfoVO licenseInfoVO = edgeLicenseManager.getContent();
////				DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
////				systemInfo.setStartDate(LocalDateTime.parse(licenseInfoVO.getStartDate()+" 00:00:00",df));
////				systemInfo.setEndDate(LocalDateTime.parse(licenseInfoVO.getEndDate()+" 23:59:59",df));
////				System.out.println(licenseInfoVO);
////
////			} catch (Exception var3) {
////				var3.printStackTrace();
////			}
////		}
//		boolean flag = true;
//		File dest = new File("C:\\license\\license.zip");
//		File dest2 = new File("C:\\license");
//		File unzip = ZipUtil.unzip(dest,dest2);
//
//		List <String> strings = ListUtil.toList(unzip.list());
//		if(strings.size() !=2 || !strings.contains("license.lic") || !strings.contains("publicKey.keystore") ) {
//			flag = false;
//		}
//		System.out.println(flag);
////		if(dest.exists()) {
////			dest.delete();
////		}
//
//	}
//	@Resource
//	DataSource dataSource;
//	public boolean doExecuteSql(String[] filePaths) {
//		//通过数据源获取数据库链接
//		Connection connection = DataSourceUtils.getConnection(dataSource);
//		//创建脚本执行器
//		ScriptRunner scriptRunner = new ScriptRunner(connection);
//		//创建字符输出流，用于记录SQL执行日志
//		StringWriter writer = new StringWriter();
//		PrintWriter print = new PrintWriter(writer);
//		//设置执行器日志输出
//		scriptRunner.setLogWriter(print);
//		//设置执行器错误日志输出
//		scriptRunner.setErrorLogWriter(print);
//		//设置读取文件格式
//		Resources.setCharset(StandardCharsets.UTF_8);
//
//		for (String path : filePaths) {
//			Reader reader = null;
//			try {
//				File file = new File(path);
//				//获取资源文件的字符输入流
//				if(file.exists()) {
//					reader = new FileReader(file);
//				}
//			} catch (IOException e) {
//				//文件流获取失败，关闭链接
//				log.error(e.getMessage(), e);
//				scriptRunner.closeConnection();
//				return false;
//			}
//			//执行SQL脚本
//			scriptRunner.runScript(reader);
//			//关闭文件输入流
//			try {
//				reader.close();
//			} catch (IOException e) {
//				log.error(e.getMessage(), e);
//			}
//		}
//		//输出SQL执行日志
//		log.debug(writer.toString());
//		//关闭输入流
//		scriptRunner.closeConnection();
//
//		return true;
//	}

}