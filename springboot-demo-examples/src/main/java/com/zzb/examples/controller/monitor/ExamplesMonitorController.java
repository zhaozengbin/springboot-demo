package com.zzb.examples.controller.monitor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/examples/monitor")
@RestController
public class ExamplesMonitorController {
    @GetMapping("/test/exception")
    public void testException(int exceptionType) {
        switch (exceptionType) {
            case 1: {
                throw new NullPointerException();
            }
            case 2: {
                throw new ArithmeticException();
            }
        }
    }
}
