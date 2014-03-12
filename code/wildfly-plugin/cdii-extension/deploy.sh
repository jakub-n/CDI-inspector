#!/bin/bash
mkdir -p /home/watt/opt/wildfly/wildfly-8.0.0.Final/modules/cz/muni/fi
cp -r $(dirname ${BASH_SOURCE[0]})/target/module/cz/muni/fi/cdii \
  /home/watt/opt/wildfly/wildfly-8.0.0.Final/modules/cz/muni/fi/
