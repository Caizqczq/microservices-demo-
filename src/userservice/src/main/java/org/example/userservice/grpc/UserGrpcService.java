package org.example.userservice.grpc;

import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.userservice.model.dto.LoginDTO;
import org.example.userservice.model.dto.LoginResult;
import org.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {
    @Autowired
    private UserService userService;

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(request.getUsername());
        loginDTO.setPassword(request.getPassword());

        LoginResult result = userService.login(loginDTO);

        LoginResponse response = LoginResponse.newBuilder()
                .setCode(result.getCode())
                .setMessage(result.getMessage())
                .setToken(result.getToken())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


}