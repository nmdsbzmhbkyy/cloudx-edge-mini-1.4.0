FROM pig4cloud/java:8-jre
MAINTAINER  www.aurine.cn
ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN mkdir -p /cloudx
WORKDIR /cloudx
EXPOSE 5901
ADD ./target/cloudx-edge-sync-platform-biz.jar ./
ADD ./target/classes/MqttParams.yml ./
CMD java -Xms1024m -Xmx2048m  -jar cloudx-edge-sync-platform-biz.jar --server.port=5901
