package com.zzb.sensitive.undo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 被观察者
 */
@Component
public class UndoObservedUtils {

    private static UndoObserved INSTANCE;

    /**
     * 方法：getInstance
     * 描述：获取被观察者实例
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : com.zzb.sensitive.undo.UndoObserved
     * @date: 2020年12月15日 1:20 下午
     */
    public static UndoObserved getInstance() {
        if (INSTANCE == null) {
            throw new IllegalArgumentException("UndoObservedUtils is Undefined");
        }
        return INSTANCE;
    }

    @Autowired
    public void setObserved(UndoObserved observed) {
        INSTANCE = observed;
    }
}
