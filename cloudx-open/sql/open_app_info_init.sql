-- 给开放平台应用表添加初始化数据
-- 注：应用名称（appName）和应用描述（appDesc）根据实际情况填写，可不写

LOCK TABLES `aurine`.`open_app_info` WRITE;
/*!40000 ALTER TABLE `aurine`.`open_app_info` DISABLE KEYS */;
REPLACE INTO `aurine`.`open_app_info`(`appUUID`, `appId`, `appName`, `appDesc`, `appType`) VALUES 
('94c87175b87f3443f8c84ec11a638081', 1, '', '', '1'),
('94c87175b87f3443f8c84ec11a638082', 2, '', '', '1'),
('94c87175b87f3443f8c84ec11a638083', 3, '', '', '1'),
('94c87175b87f3443f8c84ec11a638084', 4, '', '', '1');
/*!40000 ALTER TABLE `aurine`.`open_app_info` ENABLE KEYS */;
UNLOCK TABLES;