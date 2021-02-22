package com.zzb.examples.controller.hanlp;

import com.zzb.hanlp.controller.DemoHanlpInfoAcquisitionController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "信息提取样例", tags = "信息提取样例")
@RequestMapping("/examples/hanlp/infoAcquisition")
@RestController
public class ExamplesHanlpInfoAcquisitionController extends DemoHanlpInfoAcquisitionController {
}
