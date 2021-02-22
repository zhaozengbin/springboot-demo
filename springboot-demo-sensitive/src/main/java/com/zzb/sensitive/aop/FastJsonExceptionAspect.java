package com.zzb.sensitive.aop;


import com.zzb.sensitive.exception.FastJsonCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 类名称：FastJsonExceptionAspect
 * 类描述：脱敏方法异常处理
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 11:45 上午
 * 修改备注：TODO
 */
@Slf4j
@Order(-1)
@RestControllerAdvice
public class FastJsonExceptionAspect {


    @ExceptionHandler(value = {FastJsonCustomException.class})
    public ResponseEntity<Object> exception(FastJsonCustomException e) {
        if (e != null) {
            return new ResponseEntity<>(e.getRt(), HttpStatus.OK);
        }
        return new ResponseEntity<>(Boolean.FALSE, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
