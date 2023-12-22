FROM basic/uniface:v15

MAINTAINER  aurinerd@aurine.cn

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /cloudx

WORKDIR /cloudx

EXPOSE 4010

ADD ./target/cloudx-face-service.jar ./

CMD java -Xms256m -Xmx256m -Djava.security.egd=file:/dev/./urandom -jar cloudx-face-service.jar
