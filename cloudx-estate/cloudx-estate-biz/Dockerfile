FROM node.dev:9222/basic/jre:8
MAINTAINER  www.aurine.cn
ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN mkdir -p /cloudx
WORKDIR /cloudx
EXPOSE 4001
ADD ./target/cloudx-estate-biz.jar ./
ADD liblinkcp.so /lib64
CMD java -Xms128m -Xmx1024m -Djava.security.egd=file:/dev/./urandom   -jar cloudx-estate-biz.jar
