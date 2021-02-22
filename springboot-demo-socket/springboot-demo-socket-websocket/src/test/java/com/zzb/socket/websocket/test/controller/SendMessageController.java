package com.zzb.socket.websocket.test.controller;

import com.zzb.socket.websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendMessageController {

    @Autowired
    private WebSocketServer webSocketServer;

    @RequestMapping("/sendInfo")
    public String sendInfo(@RequestParam String msg) {
        try {
            webSocketServer.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return "信息发送异常!";
        }
        return "发送成功~";
    }
}
