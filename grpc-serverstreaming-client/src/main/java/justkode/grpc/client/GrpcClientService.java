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
    private ServerStreamingGrpc.ServerStreamingBlockingStub serverStreamingStub;

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
}
