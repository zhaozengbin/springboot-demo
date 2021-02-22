package com.zzb.examples.controller.sensitive;

import com.zzb.sensitive.controller.DemoSensitiveController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "加密脱敏", tags = "加密脱敏")
@RequestMapping("/examples/sensitive")
@RestController
public class ExamplesSensitiveController extends DemoSensitiveController {

}
