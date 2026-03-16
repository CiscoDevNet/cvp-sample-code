# Nginx High Availability for Cloud Connect Cache Service

## Overview

This project provides a **production-ready Nginx configuration** deployed using **Podman** to deliver **high availability and active-active load balancing** for the Cloud Connect **Cache Service** REST API:

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

The **Cache Service container** exposing the `/cache-service/api/v1/licenseUsage` API is deployed on **two Cloud Connect machines** that are **replicated** (publisher → subscriber):

* **PRIMARY Cloud Connect** (publisher) — VXMLServer publishes to this node; it syncs data to the backup cloud connect.
* **SUBSCRIBER Cloud Connect** (backup) — receives full replication then incremental replication from the publisher; holds the same state.

Customers do **not** call Cloud Connect machines directly. They access the API through an **Nginx load balancer**, which distributes traffic to **both** nodes:

### Routing Behavior (Active-Active)

* **Load-balance traffic equally** across PRIMARY and SUBSCRIBER when both are healthy.
* Both nodes are **active**; there is no passive standby. If one node goes down, the other is **already in rotation**—no failover window or client-visible errors.
* When a failed node recovers, it is automatically added back into the pool.
* **No external health-check scripts, cron jobs, or sidecars.**

Nginx enforces this using:

* Passive health checks (`max_fails` / `fail_timeout`)
* Equal upstream peers (no `backup` directive—both nodes receive traffic)
* Explicit timeout handling and `proxy_next_upstream` for per-request retry

---

## Architecture Summary

```
VXMLServer  ──publishes──►  PRIMARY (Publisher)  ──replication──►  SUBSCRIBER
                                    │                                    │
                                    └──────────────┬─────────────────────┘
                                                   │
Client / User / Customer  ──────────────────────►  Nginx (load balancer)
                                                   │
                                        ┌──────────┴──────────┐
                                        ▼                     ▼
                                PRIMARY :8445          SUBSCRIBER :8445
                                (active)               (active)
```

* Both nodes receive traffic when healthy (load-balanced).
* If PRIMARY goes down, Nginx does not "fail over"—SUBSCRIBER is already serving; no loss window.
* If publisher goes down, VXMLServer fails over to SUBSCRIBER and continues publishing.
* Full replication on first subscriber registration / after long disconnect; incremental replication thereafter. Both nodes have the same state; design is **active-active**.

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

Replace the placeholders with real IPs (both nodes are active; health checks apply to both):

```nginx
server <PRIMARY_CC_IP>:8445 max_fails=5 fail_timeout=30s;
server <BACKUP_CC_IP>:8445 max_fails=5 fail_timeout=30s;
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

* Remove any existing `nginx-ha-cc` container (if present), then start a new one
* Start Nginx using the official `nginx:stable` image
* Mount configuration files as read-only
* Expose port `80`
* Enable restart on reboot

Re-running the script replaces the current container with a new one (e.g. after config changes).

Verify:

```bash
podman ps
```

---

## Operational Behavior

* Nginx **load-balances** traffic across PRIMARY and SUBSCRIBER (both active).
* If one node goes down, the other is already in rotation—**no failover window**; clients can still see 200 OK.
* When a failed node recovers, it is automatically added back into the pool.
* Connection reuse prevents TLS churn and latency spikes.
* No external monitoring scripts required.
* Replication (publisher → subscriber) keeps both nodes in sync; VXMLServer can fail over to the subscriber if the publisher goes down.

---

## Validation & Load Testing

This configuration has been validated with:

* Sustained Vegeta load (up to 100 RPS for 30 minutes)
* Active-active load balancing across PRIMARY and SUBSCRIBER
* Controlled node failure and recovery with no client-visible failover window
* JWT certificate synchronization across Cloud Connect nodes

Observed steady-state latency:

* **~600–700 ms average**
* No failover window: when one node goes down, the other is already serving; clients see 200 OK.

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

* **Active-active** load balancing with **replication** (publisher → subscriber) so both nodes have the same state.
* **No failover window**: both nodes are always in rotation; if one goes down, the other continues serving without a 5–10+ second detection/failover delay.
* Simplicity, predictability, and operational clarity.

It avoids unnecessary complexity while delivering **reliable high availability** for a mission-critical licensing API.
