package justkode.grpc.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GrpcClientController {
    private final GrpcClientService grpcClientService;

    @GetMapping("/client_stream")
    public Integer clientStreamController() {
        return grpcClientService.clientStreamingFunction();
    }
}
