package justkode.grpc.server;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.justkode.examples.lib.BidirectionalGrpc;
import org.justkode.examples.lib.Message;

@Slf4j
@GrpcService
public class BidirectionalStreamingService extends BidirectionalGrpc.BidirectionalImplBase {
    @Override
    public StreamObserver<Message> getServerResponse(StreamObserver<Message> responseObserver) {
        log.info("Server processing gRPC bidirectional streaming.");

        return new StreamObserver<>() {
            @Override
            public void onNext(Message value) {
                responseObserver.onNext(value);
            }

            @Override
            public void onError(Throwable t) {
                log.error(t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
