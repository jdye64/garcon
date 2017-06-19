FROM apache/nifi:1.2.0
MAINTAINER Jeremy Dyer <jeremy.dyer@apache.org>

ADD nifi-conf /opt/nifi/nifi-1.2.0/conf/.

USER root

RUN apt-get update && apt-get install -y vim

ADD scripts/start.sh /start.sh
CMD /start.sh