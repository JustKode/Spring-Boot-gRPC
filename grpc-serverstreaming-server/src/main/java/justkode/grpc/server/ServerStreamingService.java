package justkode.grpc.server;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.justkode.examples.lib.Message;
import org.justkode.examples.lib.Number;
import org.justkode.examples.lib.ServerStreamingGrpc;

@Slf4j
@GrpcService
public class ServerStreamingService extends ServerStreamingGrpc.ServerStreamingImplBase {
    @Override
    public void getServerResponse(Number request, StreamObserver<Message> responseObserver) {
        for (int i = 1; i <= 5; i++) {
            responseObserver.onNext(Message.newBuilder().setMessage("message #" + i).build());
        }
        log.info(String.format("Server processing gRPC server-streaming %d.", request.getValue()));
        responseObserver.onCompleted();
    }
}
