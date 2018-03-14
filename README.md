# jersey-apache-connection
demonstrate connection pool problem with jersey 2.22.2+ and apache connection pool. Going back to Jersey 2.22.1 eliminates this problem.

See issue https://github.com/jersey/jersey/issues/3532

issue jersey/jersey#3532

## Example

This code will correctly utilize the connection pool:

```
String response = client.target(url)
                        .request()
                        .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE), new GenericType<String>(){});
```

From the logs:

```
DEBUG o.a.h.impl.execchain.MainClientExec - Connection can be kept alive indefinitely
DEBUG o.a.h.i.c.PoolingHttpClientConnectionManager - Connection [id: 0][route: {}->http://whatever:80] can be kept alive indefinitely
DEBUG o.a.h.i.c.PoolingHttpClientConnectionManager - Connection released: [id: 0][route: {}->http://whatever:80][total kept alive: 1; route allocated: 1 of 10; total allocated: 1 of 5]
(repeat 50 times...)
```

This code will create a new connection each time and won't keep-alive or otherwise share connections ...

```
// SomeResponse is a java class that holds json data such as
// {"success":false,"result_code":"ERROR","result_message":"Deserialization error while parsing input."}
SomeResponse response = client.target(url)
                              .request()
                              .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE), new GenericType<SomeResponse>(){});
```

In this case, the logs show:

```
DEBUG o.a.h.impl.execchain.MainClientExec - Connection can be kept alive indefinitely
DEBUG o.a.h.i.c.DefaultManagedHttpClientConnection - http-outgoing-49: Close connection
DEBUG o.a.h.impl.execchain.MainClientExec - Connection discarded
DEBUG o.a.h.i.c.PoolingHttpClientConnectionManager - Connection released: [id: 49][route: {}->http://whatever:80][total kept alive: 0; route allocated: 0 of 10; total allocated: 0 of 5]
(note that this is our 50th connection, id=49)
```  
  
  
