
# Failover Behavior

- PRIMARY receives all traffic when healthy
- On failure, traffic switches to BACKUP
- When PRIMARY recovers, traffic automatically switches back
- JWT validation works due to shared certs across CC nodes
