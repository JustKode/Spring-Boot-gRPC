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

    @GrpcClient("FSSN")
    private BidirectionalGrpc.BidirectionalStub bidirectionalStub;

    @GrpcClient("FSSN")
    private ServerStreamingGrpc.ServerStreamingBlockingStub serverStreamingStub;

    @GrpcClient("FSSN")
    private ClientStreamingGrpc.ClientStreamingStub clientStreamingStub;

    public Integer myServiceFunction(Integer number) {
        try {
            return myServiceStub.myFunction(MyNumber.newBuilder().setValue(number).build()).getValue();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 내부 에러가 발생했습니다.");
        }
    }

    public List<String> bidirectionalFunction(List<String> messages) {
        List<String> results = new ArrayList<>();
        final CountDownLatch finishLatch = new CountDownLatch(1);
        // 서버에 보내는 콜백
        StreamObserver<Message> streamObserver = new StreamObserver<>() {
            @Override
            public void onNext(Message value) {
                results.add(value.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                log.error(t.getMessage());
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        };

        StreamObserver<Message> requestObserver = bidirectionalStub.getServerResponse(streamObserver);
        try {
            for (String message: messages) {
                requestObserver.onNext(Message.newBuilder().setMessage(message).build());
            }
            requestObserver.onCompleted();
            finishLatch.await(1, TimeUnit.MINUTES);
        } catch (Exception e) {
            requestObserver.onError(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 발생");
        }
        return results;
    }

    public List<String> serverStreamingFunction(Integer number) {
        try {
            Iterator<Message> serverResponse = serverStreamingStub.getServerResponse(Number.newBuilder().setValue(number).build());
            List<String> results = new ArrayList<>();
            for (Message m = serverResponse.next(); serverResponse.hasNext(); m = serverResponse.next()) {
                results.add(m.getMessage());
            }
            return results;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 발생");
        }
    }

    public Integer clientStreamingFunction(List<String> strings) {
        // 서버에 보내는 콜백
        final Integer[] result = new Integer[1];
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<Number> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(Number value) {
                result[0] = value.getValue();
            }

            @Override
            public void onError(Throwable t) {
                finishLatch.countDown();
                log.error(t.getMessage());
            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        };

        StreamObserver<Message> requestObserver = clientStreamingStub.getServerResponse(responseObserver);

        try {
            for (String string: strings) {
                requestObserver.onNext(Message.newBuilder().setMessage(string).build());
            }
            requestObserver.onCompleted();
            finishLatch.await(1, TimeUnit.MINUTES);
        } catch (Exception e) {
            requestObserver.onError(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 발생");
        }
        return result[0];
    }
}
