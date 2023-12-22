FROM basic/jre:1.8
MAINTAINER  aurinerd@aurine.cn
ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN mkdir -p /cloudx
WORKDIR /cloudx
EXPOSE 5001
ADD ./target/cloudx-open-biz.jar ./
CMD java -Xms128m -Xmx512m -Djava.security.egd=file:/dev/./urandom  -Xdebug -Xrunjdwp:transport=dt_socket,suspend=n,server=y,address=5556 -jar cloudx-open-biz.jar
