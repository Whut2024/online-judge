FROM centos

ADD ./jdk-21_linux-aarch64_bin.tar.gz /jdk21

VOLUME /code

WORKDIR /

ENV JAVA_HOME=/jdk21/jdk-21.0.5

ENV PATH=$PATH:$JAVA_HOME/bin