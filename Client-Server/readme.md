GET http://192.168.1.2:999/chat?token=TN11EN HTTP/1.1
User-Agent: Composer
Host: 192.168.1.2:999


POST http://192.168.1.2:999/chat HTTP/1.1
Host: 192.168.1.2:999
User-Agent: Composer
Content-Length: 65

{"message":{"id":"2", "username":"User 2", "text":"What's up ?"}}


DELETE http://192.168.1.2:999/chat?id=2 HTTP/1.1
DELETE /chat HTTP/1.1: 
Host: 192.168.1.2:999
User-Agent: Composer


PUT http://192.168.1.2:999/chat HTTP/1.1
Host: 192.168.1.2:999
User-Agent: Composer
Content-Length: 44

{"message":{"id":"1", "text":"What's up ?"}}