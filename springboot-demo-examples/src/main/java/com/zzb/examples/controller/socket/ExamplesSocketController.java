package com.zzb.examples.controller.socket;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "Socket样例", tags = "Socket样例")
@RequestMapping("/examples/socket/")
@RestController
public class ExamplesSocketController {

    @ApiOperation(value = "Socket样例页面", httpMethod = "POST",
            notes = "模拟聊天")
    @ApiIgnore
    @GetMapping(value = "chat")
    public ModelAndView chat() {
        ModelAndView modelAndView = new ModelAndView("socket/chat");
        return modelAndView;
    }
    @GetMapping(value = "index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("socket/index");
        return modelAndView;
    }

}
