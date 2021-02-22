$(document).ready(function () {
    var msg = "";
    if (typeof (WebSocket) == "undefined") {
        msg = "很遗憾：您的浏览器不支持WebSocket";
    } else {
        msg = "恭喜您：您的浏览器支持WebSocket";
    }
    alert(msg);
})

var sd_socket = {
    url: '',
    socket: undefined,
    on_open: function (fun) {
        var that = this;
        if (this.socket && (this.socket.readyState == WebSocket.OPEN)) {
            return;
        }
        this.socket = new WebSocket(that.url == undefined ? alert("连接失败:" + that.url) : that.url);
        //连接打开事件
        this.socket.onopen = function (evt) {
            console.log("Socket 已打开");
            this.socket.send("连接成功!");
            if (fun) {
                fun();
            }
        };
    },
    on_message: function (fun) {
        this.socket.onmessage = function (event) {
            let that = this;
            let data;
            if (typeof event.data === String) {
                console.log("Received data string");
            }

            if (event.data instanceof ArrayBuffer) {
                console.log("Received arraybuffer");
            }
            console.log("Received Message: " + event.data);
            if (fun) {
                fun(event.data);
            }
            that.socket.close();
        };
    },
    on_close: function (fun) {
        this.socket.onclose = function (event) {
            console.log("Connection closed.");
            if (fun) {
                fun(event.data);
            }
        };
    },
    on_send: function (message) {
        this.socket.addEventListener('open', function () {
            this.send(message)
        });
    }
}