FROM pig4cloud/java:8-jre
MAINTAINER  www.aurine.cn
ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN mkdir -p /cloudx
WORKDIR /cloudx
EXPOSE 4003
ADD ./target/cloudx-estate-app.jar ./
CMD java -Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom   -jar cloudx-estate-app.jar
