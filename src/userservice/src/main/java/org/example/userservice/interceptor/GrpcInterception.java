package org.example.userservice.interceptor;

import io.grpc.*;
import org.example.userservice.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GrpcInterception implements ServerInterceptor {
    @Autowired
    private JwtUtils jwtUtils;

    private static final ServerCall.Listener NOOP_LISTENER = new ServerCall.Listener() {};

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        // 获取方法全名
        String methodName = call.getMethodDescriptor().getFullMethodName();

        // 登录接口不需要验证token
        if (methodName.contains("Login")) {
            return next.startCall(call, headers);
        }

        // 从metadata中获取token
        String token = headers.get(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER));

        if (token == null || token.isEmpty()) {
            call.close(Status.UNAUTHENTICATED.withDescription("Token is missing"), headers);
            return NOOP_LISTENER;
        }

        // 验证token
        if (jwtUtils.validateToken(token) == null) {
            call.close(Status.UNAUTHENTICATED.withDescription("Invalid token"), headers);
            return NOOP_LISTENER;
        }

        return next.startCall(call, headers);
    }
}
