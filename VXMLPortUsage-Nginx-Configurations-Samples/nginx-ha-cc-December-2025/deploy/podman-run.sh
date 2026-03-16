
#!/bin/bash
podman run -d \
  --name nginx-ha-cc \
  --restart unless-stopped \
  -p 80:80 \
  -v /opt/nginx-ha-cc-December-2025/conf/nginx.conf:/etc/nginx/nginx.conf:ro \
  -v /opt/nginx-ha-cc-December-2025/conf/conf.d:/etc/nginx/conf.d:ro \
  docker.io/library/nginx:stable
