package justkode.grpc.client;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.justkode.examples.lib.*;
import org.justkode.examples.lib.Number;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class GrpcClientService {

    @GrpcClient("FSSN")
    private MyServiceGrpc.MyServiceBlockingStub myServiceStub;

    public Integer myServiceFunction() {
        try {
            int result = myServiceStub.myFunction(MyNumber.newBuilder().setValue(4).build()).getValue();
            log.info("gRPC result: " + result);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 내부 에러가 발생했습니다.");
        }
    }
}
