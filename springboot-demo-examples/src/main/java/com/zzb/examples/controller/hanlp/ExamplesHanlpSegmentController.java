package com.zzb.examples.controller.hanlp;

import com.zzb.hanlp.controller.DemoHanlpSegmentController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "分词样例", tags = "分词样例")
@RequestMapping("/examples/hanlp/segment")
@RestController
public class ExamplesHanlpSegmentController extends DemoHanlpSegmentController {
}
