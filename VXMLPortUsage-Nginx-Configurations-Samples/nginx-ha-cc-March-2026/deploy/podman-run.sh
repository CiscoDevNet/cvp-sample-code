#!/bin/bash
set -e

# Remove existing container if present (stop and remove), then run new one
podman rm -f nginx-ha-cc 2>/dev/null || true

podman run -d \
  --name nginx-ha-cc \
  --restart unless-stopped \
  -p 80:80 \
  -v /opt/nginx-ha-cc-March-2026/conf/nginx.conf:/etc/nginx/nginx.conf:ro \
  -v /opt/nginx-ha-cc-March-2026/conf/conf.d:/etc/nginx/conf.d:ro \
  docker.io/library/nginx:stable
