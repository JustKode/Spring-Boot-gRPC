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
    private BidirectionalGrpc.BidirectionalStub bidirectionalStub;

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
}
