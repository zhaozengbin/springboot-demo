package com.zzb.examples.controller.hanlp;

import com.zzb.hanlp.controller.DemoHanlpInfoAcquisitionController;
import com.zzb.hanlp.controller.DemoHanlpInfoConvertController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "文本转换样例", tags = "文本转换样例")
@RequestMapping("/examples/hanlp/infoConvert")
@RestController
public class ExamplesHanlpInfoConvertController extends DemoHanlpInfoConvertController {
}
