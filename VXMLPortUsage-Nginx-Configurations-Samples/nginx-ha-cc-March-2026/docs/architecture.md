# Architecture

Nginx fronts Cloud Connect using an **active-active** upstream model with **replication** between nodes.

## Replication model

- **Publisher (PRIMARY Cloud Connect)** syncs all data to the **Subscriber (BACKUP Cloud Connect)**.
- **VXMLServer** publishes to the Primary Cloud Connect node.
- **Clients** (single or multiple) connect only to the **Nginx load balancer** and do not need to know which Cloud Connect node they hit.
- Nginx **load-balances equally** across PRIMARY and SUBSCRIBER (round-robin by default).
- Both nodes are **active** and hold the **same state** at any point in time:
    - **Full replication** when the subscriber first registers with the publisher, or after a long disconnect.
    - **Incremental replication** thereafter.

## Traffic flow

```
VXMLServer  ──publishes──►  PRIMARY (Publisher)  ──syncs──►  SUBSCRIBER (Backup)
                                    │                              │
                                    └──────────┬───────────────────┘
                                               │
Clients  ──────────────────────────────────►  Nginx (load balancer)
                                               │
                                    ┌──────────┴───────────────────┐
                                    ▼                              ▼
                            PRIMARY :8445                  SUBSCRIBER :8445
                            (active)                       (active)
```

- If **PRIMARY** goes down, Nginx does not need to "fail over" to the subscriber—the subscriber is already in the pool and continues serving. There is **no failover window or loss**.
- If the **publisher** goes down, VXMLServer fails over to the subscriber and continues publishing.
- Clients can see **200 OK** even during a node failure because the other node is already serving traffic.
