package com.zzb.examples.controller.hanlp;

import com.zzb.hanlp.controller.DemoHanlpDependencyParserController;
import com.zzb.hanlp.controller.DemoHanlpSegmentController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "句法依存样例", tags = "句法依存样例")
@RequestMapping("/examples/hanlp/dependencyParser")
@RestController
public class ExamplesHanlpDependencyParserController extends DemoHanlpDependencyParserController {
}
