package com.zzb.swagger.controller.vo;

import com.zzb.core.controller.vo.BaseDemoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "基础返回类", description = "基础返回类")
public class BaseSwaggerVo<T extends Object> extends BaseDemoVo {
    @ApiModelProperty(notes = "返回状态吗", example = "200")
    private int code;

    @ApiModelProperty(notes = "返回请求信息", example = "")
    private String msg;

    @ApiModelProperty(notes = "返回数据信息", example = "")
    private T data;

    public BaseSwaggerVo() {
    }

    public BaseSwaggerVo(int code, String msg, Object data) {
        super(code, msg, data);
    }

    public BaseSwaggerVo(int code, String msg) {
        super(code, msg);
    }

    public static BaseSwaggerVo success(String msg, Object data) {
        return new BaseSwaggerVo(200, msg, data);
    }

    public static BaseSwaggerVo success(Object data) {
        return new BaseSwaggerVo(200, "请求成功", data);
    }

    public static BaseSwaggerVo fail(String msg) {
        return new BaseSwaggerVo(500, msg);
    }

    public static BaseSwaggerVo fail(int code, String msg) {
        return new BaseSwaggerVo(code, msg);
    }
}
