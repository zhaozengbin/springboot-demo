package com.zzb.monitor.core.enums;

import lombok.Getter;

import java.security.SecureRandom;
import java.sql.SQLTimeoutException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum EException {
    NullPointerException(NullPointerException.class),
    SQLException(java.sql.SQLException.class),
    IndexOutOfBoundsException(IndexOutOfBoundsException.class),
    NumberFormatException(NumberFormatException.class),
    FileNotFoundException(java.io.FileNotFoundException.class),
    IOException(java.io.IOException.class),
    ClassCastException(ClassCastException.class),
    ArrayStoreException(ArrayStoreException.class),
    IllegalArgumentException(IllegalArgumentException.class),
    ArithmeticException(ArithmeticException.class),
    NegativeArraySizeException(NegativeArraySizeException.class),
    NoSuchMethodException(NoSuchMethodException.class),
    SecurityException(SecurityException.class),
    UnsupportedOperationException(UnsupportedOperationException.class),
    RuntimeException(RuntimeException.class),
    SQLTimeoutException(SQLTimeoutException.class);

    private Class<? extends Throwable> exception;

    EException(Class<? extends Throwable> exception) {
        this.exception = exception;
    }

    /**
     * 方法：random
     * 描述：获取随机异常
     * 作者：赵增斌 E-mail:zhaozengbin@duia.com
     *
     * @return : com.zzb.monitor.core.enums.EException
     * @date: 2021/2/23 10:34 上午
     */
    public static EException random() {

        SecureRandom random = new SecureRandom();
        List<EException> eExceptionList = random
                .ints(1, EException.values().length)
                .mapToObj(i -> Arrays.asList(EException.values()).get(i)).
                        collect(Collectors.toList());
        return eExceptionList.get(0);
    }

    /**
     * 方法：getExceptionForIndex
     * 描述：指定下标获取异常
     * 作者：赵增斌 E-mail:zhaozengbin@duia.com
     *
     * @param index :
     * @return : com.zzb.monitor.core.enums.EException
     * @date: 2021/2/23 10:35 上午
     */
    public static EException getExceptionForIndex(int index) {
        return Arrays.asList(EException.values()).get(index - 1);
    }
}
