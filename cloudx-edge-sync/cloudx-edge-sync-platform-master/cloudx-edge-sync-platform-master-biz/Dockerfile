FROM basic/jre:1.8
MAINTAINER  aurinerd@aurine.cn
ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN mkdir -p /cloudx
WORKDIR /cloudx
EXPOSE 5902
ADD ./target/cloudx-edge-sync-platform-master-biz.jar ./
ADD ./target/classes/MqttParams.yml ./
CMD java -Xms128m -Xmx512m  -jar cloudx-edge-sync-platform-master-biz.jar --server.port=5902
