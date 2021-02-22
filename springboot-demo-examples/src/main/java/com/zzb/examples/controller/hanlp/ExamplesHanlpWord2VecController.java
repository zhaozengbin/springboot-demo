package com.zzb.examples.controller.hanlp;

import com.zzb.hanlp.controller.DemoHanlpRecommendController;
import com.zzb.hanlp.controller.DemoHanlpWord2VecController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "词向量样例", tags = "词向量样例")
@RequestMapping("/examples/hanlp/word2vec")
@RestController
public class ExamplesHanlpWord2VecController extends DemoHanlpWord2VecController {
}
