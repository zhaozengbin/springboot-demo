package com.zzb.examples.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.zzb.examples.service.IExceptionService;
import com.zzb.monitor.core.enums.EException;
import org.springframework.stereotype.Service;

@Service
public class ExceptionServiceImpl implements IExceptionService {
    @Override
    public void getException(int... index) throws Throwable {
        if (ArrayUtil.isNotEmpty(index)) {
            throw new Exception(EException.getExceptionForIndex(index[0]).getException().newInstance());
        } else {
            throw new Exception(EException.random().getException().newInstance());
        }
    }
}
