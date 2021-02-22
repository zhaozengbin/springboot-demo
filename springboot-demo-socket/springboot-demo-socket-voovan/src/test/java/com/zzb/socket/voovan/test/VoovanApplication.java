package com.zzb.socket.voovan.test;

import com.zzb.socket.voovan.server.ServerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()
public class VoovanApplication {
    public static void main(String[] args) {
        Thread eioSocket = new ServerAdapter("localhost", 19861, 100, 100, 100, ServerAdapter.ESocketType.WEB_SOCKET_EIO, "/engine.io");
        Thread sioSocket = new ServerAdapter("localhost", 19862, 100, 100, 100, ServerAdapter.ESocketType.WEB_SOCKET_SIO, "/socket.io");
        eioSocket.start();
        sioSocket.start();
        SpringApplication.run(VoovanApplication.class, args);
    }
}

