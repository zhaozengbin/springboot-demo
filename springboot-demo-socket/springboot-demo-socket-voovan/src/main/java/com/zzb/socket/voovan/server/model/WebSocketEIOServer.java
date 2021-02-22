package com.zzb.socket.voovan.server.model;

import cn.hutool.core.util.ObjectUtil;
import org.voovan.http.extend.engineio.Config;
import org.voovan.http.extend.engineio.EIODispatcher;
import org.voovan.http.extend.engineio.EIOHandler;
import org.voovan.http.extend.socketio.SIODispatcher;
import org.voovan.http.extend.socketio.SIOHandler;
import org.voovan.http.server.WebServer;
import org.voovan.http.server.context.WebServerConfig;
import org.voovan.http.websocket.exception.WebSocketFilterException;
import org.voovan.network.exception.SendMessageException;
import org.voovan.tools.json.JSON;
import org.voovan.tools.log.Logger;

public class WebSocketEIOServer {
    /**
     * 方法：start
     * 描述：TODO
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param host         : 监听地址
     * @param port         : 监听端口
     * @param readTimeOut  : 空闲事件触发时间, 单位: 秒
     * @param sendTimeout  :  发超时时间, 单位: 毫秒
     * @param idleInterval :  超时时间, 单位: 毫秒
     * @param routePath    :  路由地址
     * @return : boolean
     * @date: 2021年01月26日 11:54 上午
     */
    public static boolean start(String host, int port, int readTimeOut, int sendTimeout, int idleInterval, String routePath) {
        try {
            WebServerConfig webServerConfig = new WebServerConfig();
            if (ObjectUtil.isNotEmpty(host)) {
                webServerConfig.setHost(host);
            }
            if (port > 0) {
                webServerConfig.setPort(port);
            }
            if (readTimeOut > 0) {
                webServerConfig.setReadTimeout(readTimeOut);
            }
            if (sendTimeout > 0) {
                webServerConfig.setSendTimeout(sendTimeout);
            }
            if (idleInterval > 0) {
                webServerConfig.setSessionTimeout(idleInterval);
            }

            //构造客户端类实例
            WebServer webSocketServer = WebServer.newInstance(webServerConfig);

            //engine.io 测试用例
            webSocketServer.socket(routePath, new EIODispatcher(new Config())
                    .on("connection", new EIOHandler() {
                        @Override
                        public String execute(String msg) {
                            try {
                                this.send("connection");
                            } catch (SendMessageException e) {
                                e.printStackTrace();
                            } catch (WebSocketFilterException e) {
                                e.printStackTrace();
                            }
                            Logger.simple("connected");
                            return "server connected";
                        }
                    }).on("close", new EIOHandler() {
                        @Override
                        public String execute(String msg) {
                            Logger.simple("closed");
                            return null;
                        }
                    }).on("message", new EIOHandler() {
                        @Override
                        public String execute(String msg) {
                            Logger.simple("message: " + msg);
                            return "server " + msg;
                        }
                    }).on("ping", new EIOHandler() {
                        @Override
                        public String execute(String msg) {
                            Logger.simple("ping");
                            return null;
                        }
                    }).on("pong", new EIOHandler() {
                        @Override
                        public String execute(String msg) {
                            Logger.simple("pong");
                            return null;
                        }
                    }));
            //启动服务类
            webSocketServer.serve();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
