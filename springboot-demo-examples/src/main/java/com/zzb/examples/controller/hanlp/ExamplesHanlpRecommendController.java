package com.zzb.examples.controller.hanlp;

import com.zzb.hanlp.controller.DemoHanlpInfoConvertController;
import com.zzb.hanlp.controller.DemoHanlpRecommendController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "文本推荐样例", tags = "文本推荐样例")
@RequestMapping("/examples/hanlp/recommend")
@RestController
public class ExamplesHanlpRecommendController extends DemoHanlpRecommendController {
}
