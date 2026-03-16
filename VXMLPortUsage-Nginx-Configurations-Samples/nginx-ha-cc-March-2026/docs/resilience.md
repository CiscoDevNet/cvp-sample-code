# Resilience (Active-Active)

## No failover window

With **active-active** load balancing, both PRIMARY (publisher) and SUBSCRIBER (backup) are always in the Nginx upstream pool. Traffic is distributed to both nodes.

- When **one node goes down**, Nginx simply sends all traffic to the remaining healthy node. The other node is **already connected and serving**—there is no 5–10 second (or longer) detection and failover window.
- **Clients see 200 OK** even during a node failure, because the surviving node continues to serve requests without a separate "failover" step.

## Behavior summary

- **PRIMARY and SUBSCRIBER** both receive traffic when healthy (load-balanced).
- **If PRIMARY goes down:** Nginx routes all traffic to SUBSCRIBER; no explicit failover, no loss window.
- **If SUBSCRIBER goes down:** Nginx routes all traffic to PRIMARY.
- **If PRIMARY (publisher) goes down:** VXMLServer fails over to the SUBSCRIBER and continues publishing; replication continues from the new publisher.
- **When a failed node recovers:** It is automatically added back into the pool (per `max_fails` / `fail_timeout`); traffic is again distributed across both nodes.
- **JWT validation** works because certs are shared across Cloud Connect nodes.

## Why this is more resilient

- **Earlier (active-passive):** Failover required detecting the failure and switching to the backup → 5–10+ second window, possible errors to clients.
- **Now (active-active + replication):** Both nodes have the same state; both are already in rotation. One node down → traffic goes to the other with **no failover window** and no client-visible error.
