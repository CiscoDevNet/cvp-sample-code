# Nginx High Availability for Cloud Connect Cache Service

## Overview

This project provides a **production-ready Nginx configuration** deployed using **Podman** to deliver **high availability, deterministic failover, and automatic failback** for the Cloud Connect **Cache Service** REST API:

```
/cache-service/api/v1/licenseUsage
```

This API is **continuously consumed by customers** and is critical for license enforcement and capacity monitoring. It provides:

* Total available self-service ports on the system
* Ports currently in use
* Total entitled self-service ports (per customer license)

Additionally, the API **aggregates license usage across multiple CVP instances** (CVP clusters or nodes) owned by a customer and correlates that usage with **active license entitlements**.

Because this endpoint is queried continuously and must remain accurate and available at all times, it requires a **highly resilient routing layer**.

---

## 🎯 Why This Project Exists

The **Cache Service container** exposing the `/cache-service/api/v1/licenseUsage` API is deployed on **two Cloud Connect machines**:

* **PRIMARY Cloud Connect**
* **BACKUP Cloud Connect**

Customers do **not** call Cloud Connect machines directly.
Instead, they access the API through an **Nginx endpoint**, which must enforce strict routing rules:

### Required Routing Behavior

* Route **all traffic to PRIMARY** when it is healthy
* Automatically **fail over to BACKUP** when PRIMARY becomes unavailable
* Automatically **fail back to PRIMARY** when it recovers
* Always keep **PRIMARY as the highest-priority node**
* **No external health-check scripts, cron jobs, or sidecars**

Nginx was introduced to enforce this behavior **reliably, efficiently, and deterministically** using:

* Passive health checks
* Upstream prioritization (`backup` directive)
* Connection reuse (`keepalive`)
* Explicit timeout handling

---

## Architecture Summary

```
Client / User / Customer
        |
        v
     Nginx (Virtual IP)
        |
        +--> PRIMARY Cloud Connect (preferred)
        |
        +--> BACKUP Cloud Connect (only if PRIMARY is down)
```

* PRIMARY always receives traffic when healthy
* BACKUP is used only when PRIMARY is marked down
* Failback to PRIMARY happens automatically
* No manual intervention required

---

## Project Structure

```
nginx-ha-cc-December-2025/
├── README.md
├── VERSION
│
├── conf/
│   ├── nginx.conf
│   └── conf.d/
│       └── upstream.conf
│
├── deploy/
│   ├── podman-run.sh
│   ├── podman-stop.sh
│   └── podman-logs.sh
│
├── validate/
│   └── nginx-test.sh
│
└── docs/
    ├── architecture.md
    └── failover-behavior.md
```

---

## Configuration Notes (IMPORTANT)

### Cloud Connect IP Addresses

This repository **does not contain hard-coded IP addresses**.

Before deployment, you **must edit**:

```
conf/conf.d/upstream.conf
```

Replace the placeholders with real IPs:

```nginx
server <PRIMARY_CC_IP>:8445;
server <BACKUP_CC_IP>:8445 backup;
```

---

## Packaging the Project (Windows / macOS)

From the project root:

```bash
tar -czf nginx-ha-cc-December-2025.tar.gz nginx-ha-cc-December-2025/
```

If you want a Linux-clean tar with no macOS junk, do this on macOS before creating the tar.

Exclude macOS junk while creating tar

```bash
tar -czvf nginx-ha-cc-December-2025.tar.gz \
  --exclude='.DS_Store' \
  --exclude='._*' \
  --exclude='__MACOSX' \
  nginx-ha-cc-December-2025/
```

---

## Copy the Tar File to AlmaLinux

From your local machine:

```bash
scp nginx-ha-cc-December-2025.tar.gz user@<ALMALINUX_HOST>:/opt/
```

---

## Extract on AlmaLinux

```bash
cd /opt
tar -xzf nginx-ha-cc-December-2025.tar.gz
cd nginx-ha-cc-December-2025
```

---

## Validate Configuration Before Running

Ensure IP placeholders are replaced, then run:

```bash
./validate/nginx-test.sh
```

This will:

* Verify PRIMARY and BACKUP IPs are set
* Run `nginx -t` inside the container

---

## Deploy Nginx Using Podman

Run the container using the provided script:

```bash
./deploy/podman-run.sh
```

This will:

* Start Nginx using the official `nginx:stable` image
* Mount configuration files as read-only
* Expose port `80`
* Enable restart on reboot

Verify:

```bash
podman ps
```

---

## Operational Behavior

* Nginx always prefers PRIMARY Cloud Connect
* On failure, traffic shifts to BACKUP
* On recovery, traffic automatically returns to PRIMARY
* Connection reuse prevents TLS churn and latency spikes
* No external monitoring scripts required

---

## Validation & Load Testing

This configuration has been validated with:

* Sustained Vegeta load (up to 100 RPS for 30 minutes)
* Controlled PRIMARY failure and recovery
* End-to-end failover and failback verification
* JWT certificate synchronization across Cloud Connect nodes

Observed steady-state latency:

* **~600–700 ms average**
* Rare spikes during failover windows only

---

## When to Modify This Setup

You may extend this setup if:

* You want upstream-level logging for audits
* You introduce more than two Cloud Connect nodes
* You add readiness gating at the application level

Otherwise, this configuration is **production-ready**.

---

## Final Notes

This project intentionally favors:

* Simplicity
* Predictability
* Operational clarity

It avoids unnecessary complexity while delivering **reliable high availability** for a mission-critical licensing API.
