package com.zzb.examples.controller.monitor;

import com.zzb.examples.service.IExceptionService;
import com.zzb.mysql.utils.JDBCUtils;
import com.zzb.redis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/examples/monitor")
@RestController
public class ExamplesMonitorController {
    @Autowired
    private IExceptionService exceptionService;



    @GetMapping("/test/exception")
    public void testException(int index) throws Throwable {
        JDBCUtils.getConnection().prepareStatement("SELECT 1 FROM");
        exceptionService.getException(index);
    }
}
