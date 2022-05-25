package justkode.grpc.server;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.justkode.examples.lib.ClientStreamingGrpc;
import org.justkode.examples.lib.Message;
import org.justkode.examples.lib.Number;

@Slf4j
@GrpcService
public class ClientStreamingService extends ClientStreamingGrpc.ClientStreamingImplBase {
    @Override
    public StreamObserver<Message> getServerResponse(StreamObserver<Number> responseObserver) {
        log.info("Server processing gRPC client-streaming.");

        return new StreamObserver<>() {
            private int count = 0;

            @Override
            public void onNext(Message value) {
                count++;
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(Number.newBuilder().setValue(count).build());
                responseObserver.onCompleted();
            }
        };
    }
}
