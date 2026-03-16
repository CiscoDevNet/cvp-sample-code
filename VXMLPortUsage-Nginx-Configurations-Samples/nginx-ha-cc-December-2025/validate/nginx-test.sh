#!/bin/bash
set -e

if grep -q "<PRIMARY_CC_IP>" conf/conf.d/upstream.conf; then
  echo "ERROR: PRIMARY_CC_IP is not set in upstream.conf"
  exit 1
fi

if grep -q "<BACKUP_CC_IP>" conf/conf.d/upstream.conf; then
  echo "ERROR: BACKUP_CC_IP is not set in upstream.conf"
  exit 1
fi

podman exec nginx-ha-cc nginx -t
