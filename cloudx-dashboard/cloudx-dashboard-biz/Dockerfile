FROM 121.36.19.192:40080/public/maven:3-jdk-8

MAINTAINER  icloud.aurine.cn

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /cloudx

WORKDIR /cloudx

EXPOSE 4021

ADD ./target/cloudx-dashboard-biz.jar ./

CMD sleep 120;java -Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -jar cloudx-dashboard-biz.jar
