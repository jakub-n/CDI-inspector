#!/bin/bash
MODULE_DIR=/home/watt/opt/wildfly/wildfly-8.0.0.Final/modules/cz/muni/fi/cdii
mkdir -p ${MODULE_DIR}
rm -rf ${MODULE_DIR}/main
cp -r $(dirname ${BASH_SOURCE[0]})/target/module/cz/muni/fi/cdii/main \
  ${MODULE_DIR}
