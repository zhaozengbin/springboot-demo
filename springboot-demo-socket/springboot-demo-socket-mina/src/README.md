Mina集成方式

# application.yml

```yaml
socket:
  mima:
    port: 8090
    idel_time_out: 180
    session_key: account
    time_out_key: time_out
    time_out_num: 3
    authen: 1
    time_check: 2
    heartbeat: 3
    version: V1.0
```