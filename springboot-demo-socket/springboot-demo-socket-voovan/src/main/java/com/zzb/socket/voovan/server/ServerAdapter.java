package com.zzb.socket.voovan.server;


import com.zzb.socket.voovan.server.model.TcpSocketServer;
import com.zzb.socket.voovan.server.model.UdpSocketServer;
import com.zzb.socket.voovan.server.model.WebSocketEIOServer;
import com.zzb.socket.voovan.server.model.WebSocketSIOServer;

public class ServerAdapter extends Thread {
    private String host;
    private int port;
    private int readTimeOut;
    private int sendTimeout;
    private int idleInterval;
    private ESocketType eSocketType;
    private String routePath;

    public ServerAdapter(String host, int port, int readTimeOut, int sendTimeout, int idleInterval, ESocketType eSocketType, String routePath) {
        this.host = host;
        this.port = port;
        this.readTimeOut = readTimeOut;
        this.sendTimeout = sendTimeout;
        this.idleInterval = idleInterval;
        this.eSocketType = eSocketType;
        this.routePath = routePath;
    }

    @Override
    public void run() {
        execute();
    }

    private void execute() {
        switch (eSocketType) {
            case NIO:
                TcpSocketServer.start(host, port, readTimeOut, sendTimeout, idleInterval);
                break;
            case UDP:
                UdpSocketServer.start(host, port, readTimeOut, sendTimeout, idleInterval);
                break;
            case WEB_SOCKET_SIO:
                WebSocketSIOServer.start(host, port, readTimeOut, sendTimeout, idleInterval, routePath);
                break;
            case WEB_SOCKET_EIO:
                WebSocketEIOServer.start(host, port, readTimeOut, sendTimeout, idleInterval, routePath);
                break;
            default:
                break;
        }
    }


    public enum ESocketType {
        NIO, UDP, WEB_SOCKET_EIO, WEB_SOCKET_SIO;
    }
}
