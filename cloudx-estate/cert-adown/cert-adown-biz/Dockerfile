FROM 10.110.18.200:9222/basic/jre:1.8
MAINTAINER  www.aurine.cn
ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN mkdir -p /cloudx
WORKDIR /cloudx
EXPOSE 6060
ADD ./target/cert-adown-biz.jar ./
CMD java -Xms128m -Xmx512m -Djava.security.egd=file:/dev/./urandom  -Xdebug -Xrunjdwp:transport=dt_socket,suspend=n,server=y,address=5555 -jar cert-adown-biz.jar
