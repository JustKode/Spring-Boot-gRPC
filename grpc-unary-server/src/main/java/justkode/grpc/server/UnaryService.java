package justkode.grpc.server;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.justkode.examples.lib.MyNumber;
import org.justkode.examples.lib.MyServiceGrpc;

@GrpcService
public class UnaryService extends MyServiceGrpc.MyServiceImplBase {

    @Override
    public void myFunction(MyNumber request, StreamObserver<MyNumber> responseObserver) {
        MyNumber myNumber = MyNumber.newBuilder()
                .setValue(request.getValue() * request.getValue())
                .build();

        responseObserver.onNext(myNumber);
        responseObserver.onCompleted();
    }
}
