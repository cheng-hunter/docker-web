#Version 1.6.5
FROM jdk8:V1.0
MAINTAINER hunter <chengpanggang@126.com>
RUN echo '@edge http://nl.alpinelinux.org/alpine/edge/main' >> /etc/apk/repositories && \
    echo '@community http://nl.alpinelinux.org/alpine/edge/community' >> /etc/apk/repositories && \
    apk update && \
    apk upgrade && \
    apk add ca-certificates openjdk8-jre-base@community && \
    apk del ca-certificates && \
    rm -rf /tmp/* /var/cache/apk/*