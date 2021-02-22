package com.zzb.sensitive.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zzb.core.aop.ann.CallTimeAnn;
import com.zzb.core.controller.BaseDemoController;
import com.zzb.sensitive.entity.SensitiveEntity;
import com.zzb.sensitive.service.SensitiveService;
import com.zzb.swagger.controller.vo.BaseSwaggerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "脱敏加密接口样例", tags = "脱敏加密接口样例")
@RestController
public class DemoSensitiveController extends BaseDemoController {
    @Autowired
    private SensitiveService sensitiveService;

    @ApiOperationSupport(order = 1)
    @ApiOperation(
            value = "加密脱敏",
            notes = "加密脱敏样例。<br />" +
                    "说明:<br />" +
                    "name：中文默认脱敏规则 <br />" +
                    "idCard：身份证默认脱敏规则 <br />" +
                    "idCard2：身份证正则脱敏规则 <br />" +
                    "phone：手机默认脱敏规则 <br />" +
                    "ext：电话默认脱敏规则 <br />" +
                    "address：地址默认脱敏规则 <br />" +
                    "address2：地址设置为空脱敏规则 <br />" +
                    "bankCard：源字段默认加密规则 <br />" +
                    "bankCardAES：AES算法加密规则 <br />" +
                    "bankCardDES：DES算法加密规则 <br />" +
                    "bankCardSM4：SM4(国标)算法加密规则 <br />",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", dataType = "String", defaultValue = "张三"),
            @ApiImplicitParam(name = "idCard", value = "身份证", dataType = "String", defaultValue = "430524202012120832"),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "String", defaultValue = "1234567890"),
            @ApiImplicitParam(name = "ext", value = "电话", dataType = "String", defaultValue = "0739-8888888"),
            @ApiImplicitParam(name = "address", value = "地址", dataType = "String", defaultValue = "湖南省长沙市高新区岳麓大道芯城科技园"),
            @ApiImplicitParam(name = "bankCard", value = "银行卡", dataType = "String", defaultValue = "622260000027736298837"),
    })
    @PostMapping("/index")
    @ResponseBody
    @CallTimeAnn
    public BaseSwaggerVo<SensitiveEntity> index(String name, String idCard, String phone, String ext, String address, String bankCard) {
        SensitiveEntity sensitiveEntity = new SensitiveEntity(name, idCard, idCard, phone, ext, address, address, bankCard);
        sensitiveEntity = sensitiveService.testEncryption(sensitiveEntity);
        return BaseSwaggerVo.success(sensitiveEntity);
    }
}
