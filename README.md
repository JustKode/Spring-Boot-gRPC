## Spring Boot gRPC
Spring Boot gRPC Example Repository for 2022-1 FSSN Lecture

### How to Use
각 client 혹은 server 별로 build 하는 방법은 다음과 같습니다. 원하는 서비스가 있는 폴더에서 다음 명령어를 실행해 주면 됩니다.

```
$ cd grpc-bidirectional-streaming-client
$ ./gradlew build -x test
```

그러면 `해당폴더/build/libs` 위치에 `jar` 파일이 생성 됩니다.

```
$ cd build/libs
$ ls
```

```
bidirectional_streaming_client-0.0.1-SNAPSHOT-plain.jar bidirectional_streaming_client-0.0.1-SNAPSHOT.jar
```

해당 폴더 위에서 다음 명령어를 실행 해 주세요. 

```
java -jar [해당 서비스 파일명].jar
```

함께 실행 되어야 하는 서비스는 다음과 같습니다.
- Bidirectional streaming
  - grpc-bidirectional-streaming-client
  - grpc-bidirectional-streaming-server
- Client streaming
  - grpc-clientstreaming-client
  - grpc-clientstreaming-server
- Server streaming
  - grpc-serverstreaming-client
  - grpc-serverstreaming-server
- Unary gRPC
  - grpc-unary-client
  - grpc-unary-server

#### 실제 API 호출 예시
- Bidirectional streaming

```
curl localhost:8080/bidirectional
["message #1","message #2","message #3","message #4","message #5"]
```

- Client streaming

```
curl localhost:8080/client_stream
5
```

- Server streaming

```
curl localhost:8080/server_stream
["message #1","message #2","message #3","message #4","message #5"]
```
- Unary gRPC

```
curl localhost:8080/unary
16
```