package org.example.userservice.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.userservice.model.dto.LoginDTO;
import org.example.userservice.model.dto.LoginResult;
import org.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(UserGrpcService.class);

    @Autowired
    private UserService userService;

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        try {

            if (request.getUsername().isEmpty() || request.getPassword().isEmpty()) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("Username and password cannot be empty")
                                .asRuntimeException()
                );
                return;
            }

            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername(request.getUsername());
            loginDTO.setPassword(request.getPassword());

            LoginResult result = userService.login(loginDTO);

            switch (result.getCode()) {
                case 200: // 成功
                    LoginResponse response = LoginResponse.newBuilder()
                            .setCode(result.getCode())
                            .setMessage(result.getMessage())
                            .setToken(result.getToken())
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                    break;
                case 401: // 用户名或密码为空
                    responseObserver.onError(
                            Status.INVALID_ARGUMENT
                                    .withDescription(result.getMessage())
                                    .asRuntimeException()
                    );
                    break;
                case 402: // 用户不存在
                    responseObserver.onError(
                            Status.NOT_FOUND
                                    .withDescription(result.getMessage())
                                    .asRuntimeException()
                    );
                    break;
                case 403: // 密码错误
                    responseObserver.onError(
                            Status.PERMISSION_DENIED
                                    .withDescription(result.getMessage())
                                    .asRuntimeException()
                    );
                    break;
                case 500: // 服务器错误
                    responseObserver.onError(
                            Status.INTERNAL
                                    .withDescription(result.getMessage())
                                    .asRuntimeException()
                    );
                    break;
                default:
                    responseObserver.onError(
                            Status.UNKNOWN
                                    .withDescription("Unknown error: " + result.getMessage())
                                    .asRuntimeException()
                    );
            }
        } catch (Exception e) {
            log.error("Login failed", e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error: " + e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }
}