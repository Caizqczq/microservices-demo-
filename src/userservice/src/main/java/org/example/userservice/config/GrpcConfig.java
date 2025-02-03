package org.example.userservice.config;

import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.example.userservice.interceptor.GrpcInterception;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @GrpcGlobalServerInterceptor
    public GrpcInterception authInterceptor() {
        return new GrpcInterception();
    }
}