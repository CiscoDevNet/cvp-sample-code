#!/bin/bash
# Stop and remove the container (idempotent: no error if container is missing or already stopped)
podman rm -f nginx-ha-cc 2>/dev/null || true
